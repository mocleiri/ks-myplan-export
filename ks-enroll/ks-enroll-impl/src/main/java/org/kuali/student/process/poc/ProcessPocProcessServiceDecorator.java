/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.student.process.poc;

import java.util.Arrays;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.common.util.constants.ProcessServiceConstants;
import org.kuali.student.r2.core.process.dto.CheckInfo;
import org.kuali.student.r2.core.process.dto.InstructionInfo;
import org.kuali.student.r2.core.process.dto.ProcessInfo;
import org.kuali.student.r2.core.process.service.ProcessService;
import org.kuali.student.r2.core.process.service.ProcessServiceDecorator;

/**
 *
 * @author nwright
 */
public class ProcessPocProcessServiceDecorator extends ProcessServiceDecorator {

    public ProcessPocProcessServiceDecorator(ProcessService nextDecorator) {
        super();
        this.setNextDecorator(nextDecorator);
        initializeData();
    }

    private boolean isInitalized() {
        ContextInfo context = new ContextInfo();
        context.setPrincipalId("POC-Initializer");
        try {
            ProcessInfo info = this.getNextDecorator().getProcess(ProcessServiceConstants.PROCESS_KEY_BASIC_ELIGIBILITY, context);
        } catch (DoesNotExistException ex) {
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    private void initializeData() {
        if (isInitalized()) {
            return;
        }

        ContextInfo context = new ContextInfo();
        context.setPrincipalId("POC-Initializer");

        _createProcess(ProcessServiceConstants.PROCESS_KEY_BASIC_ELIGIBILITY, "Basic Eligibility", "The process of checking a student's basic eligibility to register for courses.", context);
        _createProcess(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "Eligibility for Term", "The process of checking a student's eligibility to register for a particular term.", context);
        _createProcess(ProcessServiceConstants.PROCESS_KEY_HOLDS_CLEARED, "Holds Cleared", "The process of checking a student's eligibility to register for a particular term.", context);
        _createProcess(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSE, "Eligible for Course", "The process of checking a student's eligibility to register for a particular course.", context);
        _createProcess(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSES, "Eligible for Courses", "The process of checking a student's eligibility and ability to register for a proposed set of courses.", context);
        _createProcess(ProcessServiceConstants.PROCESS_KEY_REGISTER_FOR_COURSES, "Register for Courses", "The process of checking a student's eligibility and actually register for a proposed set of courses.", context);

        _createCheck(ProcessServiceConstants.CHECK_KEY_IS_ALIVE, "kuali.check.type.rule.direct", "", "", "kuali.agenda.is.alive", "", "is alive", "Checks if student is actually alive", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_HAS_OVERDUE_LIBRARY_BOOK, "kuali.check.type.hold", "kuali.hold.issue.library.book.overdue", "", "", "", "has overdue library book", "Checks if student has an overdue library book", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_HAS_NOT_PAID_BILL_FROM_PRIOR_TERM, "kuali.check.type.hold", "kuali.hold.issue.financial.unpaid.tuition.prior.term", "", "", "", "has not paid bill from prior term", "Checks if student has an unpaid bill from a prior term", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_STUDENT_HAS_BASIC_ELIGIBILITY, "kuali.check.type.process", "", "", "", "kuali.process.registration.basic.eligibility", "student has basic eligibility", "Checks all the checks defined in the basic eligibility process", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_REGISTRATION_PERIOD_IS_OPEN, "kuali.check.type.milestone.startdate", "", "kuali.atp.milestone.RegistrationPeriod", "", "", "registration period is open", "Checks that the registration period is open", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_REGISTRATION_PERIOD_IS_NOT_CLOSED, "kuali.check.type.milestone.deadline", "", "kuali.atp.milestone.RegistrationPeriod", "", "", "registration period is not closed", "Checks that the registration period is not yet closed", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_REGISTRATION_HOLDS_CLEARED, "kuali.check.type.process", "", "", "", "kuali.process.registration.holds.cleared", "Registration Holds Cleared", "Checks that the checks in the registration holds process", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_IS_NOT_SUMMER_TERM, "kuali.check.type.rule.direct", "", "", "kuali.agenda.is.not.summer.term", "", "is not Summer Term", "Checks that this is not the summer term", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_ELIGIBILITY_FOR_TERM, "kuali.check.type.process", "", "", "", "kuali.process.registration.eligibility.for.term", "Eligibility for Term", "Checks all the checks that the student is eligible for the term", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_HAS_THE_NECESSARY_PREREQ, "kuali.check.type.rule.indirect", "", "", "", "", "Has the necessary pre-req", "Checks that the student has all the necessary pee-requisites to take the course", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_STUDENT_HAS_ELIGIBILITY_FOR_EACH_COURSE, "kuali.check.type.process", "", "", "", "kuali.process.registration.eligible.for.course", "student has eligibility for each course", "Checks all the checks that make sure the student is eligible for a particular course but does it for all the courses in the proposed set of courses", context);
        _createCheck(ProcessServiceConstants.CHECK_KEY_DOES_NOT_EXCEED_CREDIT_LIMIT, "kuali.check.type.value.max", "", "", "", "", "does not exceed credit limit", "Checks that the student has not exceeded her credit limit", context);

        _createInstruction(ProcessServiceConstants.PROCESS_KEY_BASIC_ELIGIBILITY, "kuali.population.everyone", "kuali.check.is.alive", "A key piece of data is wrong on your biographic record.  Please come to the Registrar's office to clear it up.", 1, "Error", "No", "No", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_HOLDS_CLEARED, "kuali.population.everyone", "kuali.check.has.overdue.library.book", "Please note: you have an overdue library book", 3, "Warning", "Yes", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_HOLDS_CLEARED, "kuali.population.everyone", "kuali.check.has.not.paid.bill.from.prior.term", "You have unpaid tuition charges from last term, please contact the bursars office to resolve this matter", 5, "Error", "Yes", "Yes", context);

        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "kuali.population.everyone", "kuali.check.student.has.basic.eligibility", "Something about you as a person or your relationship with this institution needs to be fixed", 1, "Error", "No", "No", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "kuali.population.everyone", "kuali.check.registration.period.is.open", "Registration period for this term has not yet begun", 3, "Error", "Yes", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "kuali.population.everyone", "kuali.check.registration.period.is.not.closed", "Registration period for this term is closed", 4, "Error", "Yes", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "kuali.population.everyone", "kuali.check.registration.holds.cleared", "You have one or more holds that need to be cleared", 5, "Error", "Yes", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBILITY_FOR_TERM, "kuali.population.summer.only.student", "kuali.check.is.not.summer.term", "Summer only students cannot register for fall, winter or spring terms", 9, "Error", "Yes", "Yes", context);

        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSE, "kuali.population.everyone", "kuali.check.eligibility.for.term", "", 1, "Error", "No", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSE, "kuali.population.everyone", "kuali.check.has.the.necessary.prereq", "", 2, "Error", "Yes", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSES, "kuali.population.everyone", "kuali.check.student.has.eligibility.for.each.course", "", 1, "Error", "No", "Yes", context);
        _createInstruction(ProcessServiceConstants.PROCESS_KEY_ELIGIBLE_FOR_COURSES, "kuali.population.everyone", "kuali.check.does.not.exceed.credit.limit", "You are exceeding your credit limit", 2, "Error", "", "Yes", context);
    }

    private void _createInstruction(String processKey,
            String populationKey,
            String checkKey,
            String message,
            int position,
            String isWarning,
            String continueOnFail,
            String canBeExempted,
            ContextInfo context) {

        InstructionInfo info = new InstructionInfo();
        info.setTypeKey(ProcessServiceConstants.INSTRUCTION_TYPE_KEY);
        info.setStateKey(ProcessServiceConstants.INSTRUCTION_ENABLED_STATE_KEY);
        info.setProcessKey(processKey);
        info.setAppliedPopulationKeys(Arrays.asList(populationKey));
        info.setCheckKey(checkKey);
        info.setMessage(new RichTextHelper().fromPlain(message));
        info.setPosition(position);
        info.setContinueOnFail(_asBoolean(continueOnFail));
        info.setIsWarning(_asWarning(isWarning));
        info.setIsExemptable(_asBoolean(canBeExempted));
        try {
            info = this.createInstruction(info.getProcessKey(), info.getCheckKey(), info, context);
        } catch (Exception ex) {
            throw new RuntimeException("error creating exemption request", ex);
        }

    }

    private Boolean _asBoolean(String str) {
        if ("Yes".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    private Boolean _asWarning(String str) {
        if ("Error".equalsIgnoreCase(str)) {
            return false;
        }
        return true;
    }

    private ProcessInfo _createProcess(String key, String name, String descr, ContextInfo context) {
        ProcessInfo info = new ProcessInfo();
        info.setKey(key);
        info.setTypeKey(ProcessServiceConstants.PROCESS_TYPE_KEY);
        info.setStateKey(ProcessServiceConstants.PROCESS_ENABLED_STATE_KEY);
        info.setName(name);
        info.setDescr(new RichTextHelper().fromPlain(descr));
        try {
            info = this.createProcess(info.getKey(), info, context);
        } catch (Exception ex) {
            throw new RuntimeException("error creating exemption request", ex);
        }
        return info;
    }

    private CheckInfo _createCheck(String key, String type, String issueKey, String milestoneTypeKey, String agendaId, String processKey, String name, String descr, ContextInfo context) {
        CheckInfo info = new CheckInfo();
        info.setKey(key);
        info.setTypeKey(type);
        info.setStateKey(ProcessServiceConstants.PROCESS_CHECK_STATE_ENABLED);
        info.setName(name);
        info.setIssueKey(_toNull(issueKey));
        info.setMilestoneTypeKey(_toNull(milestoneTypeKey));
        info.setAgendaId(_toNull(agendaId));
        info.setProcessKey(_toNull(processKey));
        info.setDescr(new RichTextHelper().fromPlain(descr));

        try {
            info = this.createCheck(info, context);
        } catch (Exception ex) {
            throw new RuntimeException("error creating exemption request", ex);
        }
        return info;
    }

    private String _toNull(String str) {
        if (str.isEmpty()) {
            return null;
        }
        return str;
    }
}
