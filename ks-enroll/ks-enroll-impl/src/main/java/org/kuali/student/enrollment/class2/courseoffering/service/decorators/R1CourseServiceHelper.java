/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.student.enrollment.class2.courseoffering.service.decorators;

import org.apache.oro.text.regex.PatternMatcher;
import org.kuali.student.r2.common.versionmanagement.dto.VersionDisplayInfo;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.acal.service.AcademicCalendarService;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.service.CourseService;
import org.kuali.student.r2.lum.util.constants.CourseServiceConstants;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nwright
 */
public class R1CourseServiceHelper {

    private CourseService courseService;
    private AcademicCalendarService acalService;

    public AcademicCalendarService getAcalService() {
        return acalService;
    }

    public void setAcalService(AcademicCalendarService acalService) {
        this.acalService = acalService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public R1CourseServiceHelper() {
    }

    public R1CourseServiceHelper(CourseService courseService, AcademicCalendarService acalService) {
        this.courseService = courseService;
        this.acalService = acalService;
    }

    /**
     * Get the course 
     * @param courseId
     * @return
     * @throws DoesNotExistException
     * @throws OperationFailedException 
     */
    public CourseInfo getCourse(String courseId) throws DoesNotExistException, OperationFailedException {
        CourseInfo course = null;
        try {
            course = courseService.getCourse(courseId, null);
        } catch (org.kuali.student.r2.common.exceptions.DoesNotExistException e) {
            throw new DoesNotExistException("The course does not exist. course: " + courseId, e);
        } catch (Exception e) {
            throw new OperationFailedException("unxpected trying to get course " + courseId, e);
        }
        return course;
    }

    /**
     * Get the versions of the course that are valid for the specific term.
     * Most of the time there is just zero or one but some school's configuration may allow for multiple
     * @param courseId
     * @param targetTermId
     * @param context
     * @return
     * @throws DoesNotExistException
     * @throws OperationFailedException 
     */
    public List<CourseInfo> getCoursesForTerm(String courseId, String targetTermId, ContextInfo context)
            throws DoesNotExistException, OperationFailedException {
        List<CourseInfo> list = new ArrayList<CourseInfo>();
        CourseInfo sourceCourse = this.getCourse(courseId);
        String versionIndCourseId = sourceCourse.getVersionInfo().getVersionIndId();
        TermInfo targetTerm;
        try {
            targetTerm = acalService.getTerm(targetTermId, context);
        } catch (InvalidParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (MissingParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (PermissionDeniedException ex) {
            throw new OperationFailedException("unexpected", ex);
        }
        // TODO: Consider adding a shortcut by getting the current version of the course and comparing that first instead of 
        // all versions of the course
        List<VersionDisplayInfo> versions;
        try {
            versions = courseService.getVersions(CourseServiceConstants.COURSE_NAMESPACE_URI, versionIndCourseId, null);
        } catch (org.kuali.student.r2.common.exceptions.DoesNotExistException e) {
            // TODO: if no version exists for the target term should we return null instead?
            throw new DoesNotExistException("The course does not exist. course: " + versionIndCourseId, e);
        } catch (Exception e) {
            throw new OperationFailedException("unexpected trying to get course " + versionIndCourseId, e);
        }
        // TODO: consider sorting this in reverse order so the latest versions are checked first
        for (VersionDisplayInfo version : versions) {
            CourseInfo course = this.getCourse(version.getId());
            if (isCourseValidToBeOfferendInTerm(course, targetTerm, context)) {
                list.add(course);
            }
        }
        return list;
    }

    // TODO: Remove hack once data is valid (converts kuali.atp.term.xxx to 2008Fall, which is what is in Acal)
    private String _hackTerm(String termId) {
        if (termId.startsWith("kuali")) {
            Pattern p = Pattern.compile("\\d\\d\\d\\d");
            Matcher m = p.matcher(termId);
            if (m.find()) {
                String s = m.group();
                return s + "FALL";
            }
        }
        return termId;
    }

    private boolean isCourseValidToBeOfferendInTerm(CourseInfo course, TermInfo targetTerm, ContextInfo context)
            throws OperationFailedException {
        // TODO: check the status of the course to make sure it is active, superseded or retired but it cannot be draft or otherwise in-active
        // shortcut if the terms match
        if (targetTerm.getId().equals(course.getStartTerm())) {
            return true;
        }
        if (targetTerm.getId().equals(course.getEndTerm())) {
            return true;
        }
        // TODO: find out if the course's Effective and expiration dates can be used so I don't have to fetch all the terms to 
        // compare start/end dates
        TermInfo startTerm;
        String startTermStr = course.getStartTerm();
        startTermStr = _hackTerm(startTermStr); // TODO: Fix hack once term data is good
        try {
            startTerm = acalService.getTerm(startTermStr, context);
        } catch (DoesNotExistException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (InvalidParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (MissingParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (PermissionDeniedException ex) {
            throw new OperationFailedException("unexpected", ex);
        }
        if (targetTerm.getStartDate().before(startTerm.getStartDate())) {
            return false;
        }
        // if no end term the all done
        if (course.getEndTerm() == null || course.getEndTerm().indexOf("9999") != -1) { // TODO: Hack
            return true;
        }
        TermInfo endTerm;
        try {
            endTerm = acalService.getTerm(course.getEndTerm(), context);
        } catch (DoesNotExistException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (InvalidParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (MissingParameterException ex) {
            throw new OperationFailedException("unexpected", ex);
        } catch (PermissionDeniedException ex) {
            throw new OperationFailedException("unexpected", ex);
        }
        if (targetTerm.getStartDate().after(endTerm.getEndDate())) {
            return false;
        }
        return false;

    }
}
