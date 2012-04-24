package org.kuali.student.myplan.academicplan.service;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.student.common.util.UUIDHelper;

import org.kuali.student.core.atp.dto.AtpTypeInfo;
import org.kuali.student.core.atp.service.AtpService;
import org.kuali.student.lum.course.service.CourseService;

import org.kuali.student.lum.course.service.CourseServiceConstants;
import org.kuali.student.myplan.academicplan.dto.LearningPlanInfo;
import org.kuali.student.myplan.academicplan.dto.PlanItemInfo;
import org.kuali.student.myplan.academicplan.dto.PlanItemSetInfo;
import org.kuali.student.myplan.academicplan.dao.LearningPlanDao;
import org.kuali.student.myplan.academicplan.dao.LearningPlanTypeDao;
import org.kuali.student.myplan.academicplan.dao.PlanItemDao;
import org.kuali.student.myplan.academicplan.dao.PlanItemTypeDao;
import org.kuali.student.myplan.academicplan.model.*;

import org.kuali.student.r2.common.exceptions.*;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.dto.*;
import org.kuali.student.r2.common.infc.ValidationResult;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebParam;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Academic Plan Service Implementation.
 */
@Transactional(readOnly = true, noRollbackFor = {DoesNotExistException.class}, rollbackFor = {Throwable.class})
public class AcademicPlanServiceImpl implements AcademicPlanService {

    private LearningPlanDao learningPlanDao;
    private LearningPlanTypeDao learningPlanTypeDao;
    private PlanItemDao planItemDao;
    private PlanItemTypeDao planItemTypeDao;
    private CourseService courseService;
    private AtpService atpService;

    /**
     * This method provides a way to manually provide a CourseService implementation during testing.
     * @param courseService
     */
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    protected synchronized CourseService getCourseService() {
        if (this.courseService == null) {
            this.courseService = (CourseService) GlobalResourceLoader
                    .getService(new QName(CourseServiceConstants.COURSE_NAMESPACE, "CourseService"));
        }
        return this.courseService;
    }

    /**
     * Provides an instance of the AtpService client.
     */
    protected AtpService getAtpService() {
        if (atpService == null) {
            // TODO: Namespace should not be hard-coded.
            atpService = (AtpService) GlobalResourceLoader.getService(new QName("http://student.kuali.org/wsdl/atp", "AtpService"));
        }
        return this.atpService;
    }

    public PlanItemDao getPlanItemDao() {
        return planItemDao;
    }

    public void setPlanItemDao(PlanItemDao planItemDao) {
        this.planItemDao = planItemDao;
    }

    public PlanItemTypeDao getPlanItemTypeDao() {
        return planItemTypeDao;
    }

    public void setPlanItemTypeDao(PlanItemTypeDao planItemTypeDao) {
        this.planItemTypeDao = planItemTypeDao;
    }

    public LearningPlanDao getLearningPlanDao() {
        return learningPlanDao;
    }

    public void setLearningPlanDao(LearningPlanDao learningPlanDao) {
        this.learningPlanDao = learningPlanDao;
    }

    public LearningPlanTypeDao getLearningPlanTypeDao() {
        return learningPlanTypeDao;
    }

    public void setLearningPlanTypeDao(LearningPlanTypeDao learningPlanTypeDao) {
        this.learningPlanTypeDao = learningPlanTypeDao;
    }

    @Override
    public LearningPlanInfo getLearningPlan(@WebParam(name = "learningPlanId") String learningPlanId,
                                        @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {

        LearningPlanEntity lpe = learningPlanDao.find(learningPlanId);
        if (null == lpe) {
            throw new DoesNotExistException(learningPlanId);
        }

        LearningPlanInfo dto = lpe.toDto();
        return dto;
    }

    @Override
    public PlanItemInfo getPlanItem(@WebParam(name = "planItemId") String planItemId,
                                @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {

        PlanItemEntity planItem = planItemDao.find(planItemId);
        if (null == planItem) {
            throw new DoesNotExistException(String.format("Plan item with Id [%s] does not exist", planItemId));
        }

        return planItem.toDto();
    }

    @Override
    public List<PlanItemInfo> getPlanItemsInPlanByType(@WebParam(name = "learningPlanId") String learningPlanId,
                                                   @WebParam(name = "planItemTypeKey") String planItemTypeKey,
                                                   @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        List<PlanItemInfo> planItemInfos=new ArrayList<PlanItemInfo>();
        List<PlanItemEntity> planItemEntities=planItemDao.getLearningPlanItems(learningPlanId,planItemTypeKey);
        if(null == planItemEntities){
            throw new DoesNotExistException(String.format("Plan item with learning plan Id [%s] does not exist", learningPlanId));
        }
        else {
            for(PlanItemEntity planItemEntity:planItemEntities){
                planItemInfos.add(planItemEntity.toDto());
            }
        }
        return planItemInfos;
    }

    @Override
    public List<PlanItemInfo> getPlanItemsInPlan(@WebParam(name = "learningPlanId") String learningPlanId,
                                             @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {

        List<PlanItemInfo> dtos = new ArrayList<PlanItemInfo>();

        List<PlanItemEntity> planItems = planItemDao.getLearningPlanItems(learningPlanId);
        for (PlanItemEntity pie : planItems) {
            dtos.add(pie.toDto());
        }
        return dtos;
    }

    @Override
    public List<PlanItemInfo> getPlanItemsInPlanByAtp(@WebParam(name = "learningPlanId") String learningPlanId,
                                                  @WebParam(name = "atpKey") String atpKey,
                                                  @WebParam(name = "planItemTypeKey") String planItemTypeKey,
                                                  @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<PlanItemInfo> getPlanItemsInPlanByRefObjectIdByRefObjectType(@WebParam(name = "learningPlanId") String learningPlanId,
                                                                             @WebParam(name = "refObjectId") String refObjectId,
                                                                             @WebParam(name = "refObjectType") String refObjectType,
                                                                             @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {

        List<PlanItemEntity> planItemsList = planItemDao.getLearningPlanItemsByRefObjectId(learningPlanId, refObjectId, refObjectType);

        List<PlanItemInfo> planItemDtos = new ArrayList<PlanItemInfo>();
        for (PlanItemEntity pie : planItemsList) {
            planItemDtos.add(pie.toDto());
        }

        return planItemDtos;
    }

    @Override
    public PlanItemSetInfo getPlanItemSet(@WebParam(name = "planItemSetId") String planItemSetId,
                                      @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<PlanItemInfo> getPlanItemsInSet(@WebParam(name = "planItemSetId") String planItemSetId,
                                            @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<LearningPlanInfo> getLearningPlansForStudentByType(@WebParam(name = "studentId") String studentId,
                                                               @WebParam(name = "planTypeKey") String planTypeKey,
                                                               @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {

        List<LearningPlanEntity> lpeList = learningPlanDao.getLearningPlansByType(studentId, planTypeKey);

        List<LearningPlanInfo> learningPlanDtos = new ArrayList<LearningPlanInfo>();
        for (LearningPlanEntity lpe : lpeList) {
            learningPlanDtos.add(lpe.toDto());
        }

        return learningPlanDtos;
    }

    @Override
    @Transactional
    public LearningPlanInfo createLearningPlan(@WebParam(name = "learningPlan") LearningPlanInfo learningPlan,
                                               @WebParam(name = "context") ContextInfo context)
            throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {

        LearningPlanEntity lpe = new LearningPlanEntity();
        lpe.setId(UUIDHelper.genStringUUID());

        LearningPlanTypeEntity type = learningPlanTypeDao.find(learningPlan.getTypeKey());
        if (type == null) {
            throw new InvalidParameterException(String.format("Unknown type [%s].", learningPlan.getTypeKey()));
        }
        lpe.setLearningPlanType(type);

        lpe.setStudentId(learningPlan.getStudentId());
        lpe.setDescr(new LearningPlanRichTextEntity(learningPlan.getDescr()));

        LearningPlanEntity existing = learningPlanDao.find(lpe.getId());
        if (existing != null) {
            throw new AlreadyExistsException();
        }

        learningPlanDao.persist(lpe);

        return learningPlanDao.find(lpe.getId()).toDto();
    }

    @Override
    @Transactional
    public PlanItemInfo createPlanItem(@WebParam(name = "planItem") PlanItemInfo planItem,
                                       @WebParam(name = "context") ContextInfo context)
            throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {

        //  FIXME: For a given plan there should be only one planned course item per course id. So, do a lookup to see
        //  if a plan item exists if the type is "planned" and do an update of ATPid instead of creating a new plan item.


        PlanItemEntity pie = new PlanItemEntity();
        String planItemId = UUIDHelper.genStringUUID();
        pie.setId(planItemId);

        pie.setRefObjectId(planItem.getRefObjectId());
        pie.setRefObjectTypeKey(planItem.getRefObjectType());

        PlanItemTypeEntity planItemTypeEntity = planItemTypeDao.find(planItem.getTypeKey());
        if (planItemTypeEntity == null) {
            throw new InvalidParameterException(String.format("Unknown plan item type id [%s].", planItem.getTypeKey()));
        }
        pie.setLearningPlanItemType(planItemTypeEntity);

        //  Convert the List of plan periods to a Set.
        pie.setPlanPeriods(new HashSet<String>(planItem.getPlanPeriods()));

        //  Set attributes.
        pie.setAttributes(new ArrayList<PlanItemAttributeEntity>());
        if (planItem.getAttributes() != null) {
            for (Attribute att : planItem.getAttributes()) {
                PlanItemAttributeEntity attEntity = new PlanItemAttributeEntity(att);
                pie.getAttributes().add(attEntity);
            }
        }

        //  Create text entity.
        pie.setDescr(new PlanItemRichTextEntity(planItem.getDescr()));

        //  Set the learning plan.
        String planId = planItem.getLearningPlanId();
        if (planId == null) {
            throw new InvalidParameterException("Learning plan id was null.");
        }
        LearningPlanEntity plan = learningPlanDao.find(planItem.getLearningPlanId());
        if (plan == null) {
            throw new InvalidParameterException(String.format("Unknown learning plan id [%s]", planItem.getLearningPlanId()));
        }
        pie.setLearningPlan(plan);

        planItemDao.persist(pie);

        return planItemDao.find(planItemId).toDto();
    }

    @Override
    public PlanItemSetInfo createPlanItemSet(@WebParam(name = "planItemSet") PlanItemSetInfo planItemSet,
                                             @WebParam(name = "context") ContextInfo context)
            throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    @Transactional
    public LearningPlanInfo updateLearningPlan(@WebParam(name = "learningPlanId") String learningPlanId,
                                               @WebParam(name = "learningPlan") LearningPlanInfo learningPlan,
                                               @WebParam(name = "context") ContextInfo context)
            throws DataValidationErrorException, InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException, DoesNotExistException {

        LearningPlanEntity lpe = learningPlanDao.find(learningPlanId);
        if (lpe == null) {
            throw new DoesNotExistException(learningPlanId);
        }

        lpe.setStudentId(learningPlan.getStudentId());
        lpe.setDescr(new LearningPlanRichTextEntity(learningPlan.getDescr()));


        lpe.setAttributes(new ArrayList<LearningPlanAttributeEntity>());
        if (null != learningPlan.getAttributes()) {
            for (Attribute att : learningPlan.getAttributes()) {
                LearningPlanAttributeEntity attEntity = new LearningPlanAttributeEntity(att);
                lpe.getAttributes().add(attEntity);
            }
        }

        learningPlanDao.merge(lpe);
        return learningPlanDao.find(learningPlanId).toDto();
    }

    @Override
    @Transactional
    public PlanItemInfo updatePlanItem(@WebParam(name = "planItemId") String planItemId,
                                       @WebParam(name = "planItem") PlanItemInfo planItem,
                                       @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, DataValidationErrorException, InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {

        //  See if the plan item exists before trying to update it.
        PlanItemEntity planItemEntity = planItemDao.find(planItemId);
        if (planItemEntity == null) {
            throw new DoesNotExistException(planItemId);
        }

        planItemEntity.setRefObjectId(planItem.getRefObjectId());
        planItemEntity.setRefObjectTypeKey(planItem.getRefObjectType());

        //  Update the plan item type if it has changed.
        if ( ! planItemEntity.getLearningPlanItemType().getId().equals(planItem.getTypeKey())) {
            PlanItemTypeEntity planItemTypeEntity = planItemTypeDao.find(planItem.getTypeKey());
            if (planItemTypeEntity == null) {
                throw new InvalidParameterException(String.format("Unknown plan item type id [%s].", planItem.getTypeKey()));
            }
            planItemEntity.setLearningPlanItemType(planItemTypeEntity);
        }

        //  Update plan periods.
        if (planItem.getPlanPeriods() != null) {
            //  Convert from List to Set.
            planItemEntity.setPlanPeriods(new HashSet<String>(planItem.getPlanPeriods()));
        }

        //  Update attributes.
        if (planItem.getAttributes() != null) {
            List<PlanItemAttributeEntity> attributeEntities = new ArrayList<PlanItemAttributeEntity>();
            for (AttributeInfo att : planItem.getAttributes()) {
                attributeEntities.add(new PlanItemAttributeEntity(att));
            }
            planItemEntity.setAttributes(attributeEntities);
        }

        //  Update text entity.
        planItemEntity.setDescr(new PlanItemRichTextEntity(planItem.getDescr()));

        //  Update the learning plan.
        if ( ! planItemEntity.getLearningPlan().getId().equals(planItem.getLearningPlanId())) {
            String planId = planItem.getLearningPlanId();
            if (planId == null) {
                throw new InvalidParameterException("Learning plan id was null.");
            }
            LearningPlanEntity plan = learningPlanDao.find(planItem.getLearningPlanId());
            if (plan == null) {
                throw new InvalidParameterException(String.format("Unknown learning plan id [%s]", planItem.getLearningPlanId()));
            }
            planItemEntity.setLearningPlan(plan);
        }

        planItemDao.merge(planItemEntity);

        return planItemDao.find(planItemEntity.getId()).toDto();
    }

    @Override
    public PlanItemSetInfo updatePlanItemSet(@WebParam(name = "planItemSetId") String planItemSetId, @WebParam(name = "planItemSet") PlanItemSetInfo planItemSet, @WebParam(name = "context") ContextInfo context) throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public StatusInfo deleteLearningPlan(@WebParam(name = "learningPlanId") String learningPlanId,
                                         @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);

        LearningPlanEntity lpe = learningPlanDao.find(learningPlanId);
        if (lpe == null) {
            throw new DoesNotExistException(learningPlanId);
        }

        //  Delete plan items.
        List<PlanItemEntity> pies = planItemDao.getLearningPlanItems(learningPlanId);
        for (PlanItemEntity pie : pies) {
            planItemDao.remove(pie);
        }

        learningPlanDao.remove(lpe);

        return status;

    }

    @Override
    @Transactional
    public StatusInfo deletePlanItem(@WebParam(name = "planItemId") String planItemId,
                                     @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {

        StatusInfo status = new StatusInfo();
        status.setSuccess(true);

        PlanItemEntity pie = planItemDao.find(planItemId);
        if (pie == null) {
            throw new DoesNotExistException(String.format("Unknown plan item id [%s].", planItemId));
        }

        planItemDao.remove(pie);

        return status;
    }

    @Override
    public StatusInfo deletePlanItemSet(@WebParam(name = "planItemSetId") String planItemSetId,
                                        @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<ValidationResultInfo> validateLearningPlan(@WebParam(name = "validationType") String validationType,
                                                           @WebParam(name = "learningPlanInfo") LearningPlanInfo learningPlanInfo,
                                                           @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        return new ArrayList<ValidationResultInfo>();
    }

    @Override
    public List<ValidationResultInfo> validatePlanItem(@WebParam(name = "validationType") String validationType,
                                                       @WebParam(name = "planItemInfo") PlanItemInfo planItemInfo,
                                                       @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, AlreadyExistsException {

        List<ValidationResultInfo> validationResultInfos = new ArrayList<ValidationResultInfo>();

        /*
         *  Validate that the course exists.
         * TODO: Move this validation to the data dictionary.
         */
        try {
            getCourseService().getCourse(planItemInfo.getRefObjectId());
        } catch (org.kuali.student.common.exceptions.DoesNotExistException e) {
            validationResultInfos.add(makeValidationResultInfo(
                String.format("Could not find course with ID [%s].", planItemInfo.getRefObjectId()),
                "refObjectId", ValidationResult.ErrorLevel.ERROR));
        } catch (Exception e) {
            validationResultInfos.add(makeValidationResultInfo(e.getLocalizedMessage(),
                "refObjectId", ValidationResult.ErrorLevel.ERROR));
        }

        //  TODO: This validation should be implemented in the data dictionary when that possibility manifests.
        //  Make sure a plan period exists if type is planned course.
        if (planItemInfo.getTypeKey().equals(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE_PLANNED)
                || planItemInfo.getTypeKey().equals(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE_BACKUP)) {
            if (planItemInfo.getPlanPeriods() == null || planItemInfo.getPlanPeriods().size() == 0) {
                validationResultInfos.add(makeValidationResultInfo(
                    String.format("Plan Item Type was [%s], but no plan periods were defined.", planItemInfo.getTypeKey()),
                    "typeKey", ValidationResult.ErrorLevel.ERROR));
            } else {
                //  Make sure the plan periods are valid.
                /*

                FIXME: Uncomment after Friday demo.

                for (String atpId : planItemInfo.getPlanPeriods()) {
                    boolean valid = false;
                    try {
                        valid = isValidAtp(atpId);
                        if ( ! valid) {
                            validationResultInfos.add(makeValidationResultInfo(
                                String.format("ATP ID [%s] was not valid.", planItemInfo.getTypeKey()), "typeKey", ValidationResult.ErrorLevel.ERROR));
                        }
                    } catch (Exception e) {
                        validationResultInfos.add(makeValidationResultInfo(
                            "ATP ID lookup failed.", "typeKey", ValidationResult.ErrorLevel.ERROR));
                    }
                }*/
            }
        }

        /*
         * Check for duplicate list items:
         *    Make sure a saved courses item with this course id doesn't already exist in the plan.
         *    Make sure a planned course item with the same ATP id doesn't exist in the plan.
         *
         * Note: This validation is last to insure that all of the other validations are performed on "update" operations.
         * The duplicate check throw an AlreadyExistsException on updates.
         *
         * TODO: Maybe there is a better way to deal with validating udpates?
         *
         * TODO: Move these validations to the data dictionary.
         */
        checkPlanItemDuplicate(planItemInfo);

        return validationResultInfos;
    }

    @Override
    public List<ValidationResultInfo> validatePlanItemSet(@WebParam(name = "validationType") String validationType,
                                                          @WebParam(name = "planItemInfo") PlanItemSetInfo planItemSetInfo,
                                                          @WebParam(name = "context") ContextInfo context)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        return new ArrayList<ValidationResultInfo>();
    }

    /**
     * @throws AlreadyExistsException If the plan item is a duplicate.
     */
    private void checkPlanItemDuplicate(PlanItemInfo planItem) throws AlreadyExistsException {

        String planItemId = planItem.getLearningPlanId();
        String courseId = planItem.getRefObjectId();
        String planItemType = planItem.getTypeKey();

        /**
         * See if a duplicate item exits in the plan. If the type is wishlist then only the course id has to match to make
         * it a duplicate. If the type is planned course then the ATP must match as well.
         */
        List<PlanItemEntity> planItems = this.planItemDao.getLearningPlanItems(planItemId, planItemType);
        for (PlanItemEntity p : planItems) {
            if (p.getRefObjectId().equals(courseId)) {
                if (planItemType.equals(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE_PLANNED)||planItemType.equals(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE_BACKUP)) {
                    for (String atpId : planItem.getPlanPeriods()) {
                        if (p.getPlanPeriods().contains(atpId)) {
                            throw new AlreadyExistsException(String.format("A plan item for plan [%s], course id [%s], and term [%s] already exists.",
                                    p.getLearningPlan().getId(), courseId, atpId));
                        }
                    }
                } else {
                     throw new AlreadyExistsException(String.format("A plan item for plan [%s] and course id [%s] already exists.",
                        p.getLearningPlan().getId(), courseId));
                }
            }
        }
    }

    private ValidationResultInfo makeValidationResultInfo(String errorMessage, String element, ValidationResult.ErrorLevel errorLevel) {
        ValidationResultInfo vri = new ValidationResultInfo();
        vri.setError(errorMessage);
        vri.setElement(element);
        vri.setLevel(errorLevel);
        return vri;
    }

    private boolean isValidAtp(String atpId) {
        try {
            getAtpService().getAtp(atpId);
        } catch (org.kuali.student.common.exceptions.DoesNotExistException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Query to ATP service failed.", e);
        }
        return true;
    }
}
