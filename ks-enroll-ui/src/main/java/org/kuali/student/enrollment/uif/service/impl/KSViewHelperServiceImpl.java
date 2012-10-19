/**
 * Copyright 2012 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.kuali.student.enrollment.uif.service.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.uif.service.KSViewHelperService;
import org.kuali.student.r2.common.class1.type.dto.TypeInfo;
import org.kuali.student.r2.common.class1.type.service.TypeService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.*;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.core.class1.state.dto.StateInfo;
import org.kuali.student.r2.core.class1.state.service.StateService;

/**
 * Base class for the KS View helper service.
 *
 * @author Kuali Student Team
 */
public class KSViewHelperServiceImpl extends ViewHelperServiceImpl implements KSViewHelperService {

    private final static Logger LOG = Logger.getLogger(KSViewHelperServiceImpl.class);

    public ContextInfo createContextInfo(){
        return ContextUtils.createDefaultContextInfo();
    }

    public StateInfo getStateInfo(String stateKey){
        StateService stateService = CourseOfferingResourceLoader.loadStateService();
        try {
            return stateService.getState(stateKey, createContextInfo());
        }catch (Exception e){
            throw convertServiceExceptionsToUI(e);
        }

    }

    public TypeInfo getTypeInfo(String typeKey){
        TypeService typeService = CourseOfferingResourceLoader.loadTypeService();
        try {
            return typeService.getType(typeKey, createContextInfo());
        }catch (Exception e){
            throw convertServiceExceptionsToUI(e);
        }
    }

    /**
     * This is a helper method which handles all the services exceptions. This can be the central point to handle all
     * the services exceptions. For now, we're converting all the exceptions to Runtime exceptions.
     *
     * Once we implement authz and when services fully implemented the decorators/exceptions, we need to
     * refactor to handle some of the exceptions with HandlerExceptionResolver (Note, underlying exception is passed to
     * the runtime exception as cause... So, this can be accessed at the exception resolver to determine the action)
     *
     * @param ex service exception to be handled
     * @return service exception wrapped in a RuntimeException instance
     */
    protected RuntimeException convertServiceExceptionsToUI(Exception ex){

        if (LOG.isEnabledFor(Level.ERROR)){
            LOG.error(ex);
        }
         //Commented for now... once service throws MPE/IPE(KSENROLL-3446), needs to rework.
        /*StringBuilder refinedErrorMessage = new StringBuilder();
        refinedErrorMessage.append(ex.getMessage());
        refinedErrorMessage.append("[Caused at ");
        refinedErrorMessage.append(ex.getStackTrace()[0].getClass().getSimpleName() + ".");
        refinedErrorMessage.append(ex.getStackTrace()[0].getMethodName() + "()");
        refinedErrorMessage.append(ex.getStackTrace()[0].getLineNumber() + "]");*/

        if (ex instanceof DoesNotExistException){
            return new RuntimeException("Does Not Exists - " + ex.getMessage(),ex);
        } else if (ex instanceof InvalidParameterException){
            return new RuntimeException("Invalid parameter - " + ex.getMessage(),ex);
        } else if (ex instanceof MissingParameterException){
            return new RuntimeException("Missing parameter - " + ex.getMessage(),ex);
        } else if (ex instanceof OperationFailedException){
            return new RuntimeException("Operation Failed - " + ex.getMessage(),ex);
        } else if (ex instanceof PermissionDeniedException){
            return new RuntimeException("Permission Denied - " + ex.getMessage(),ex);
        } else {
//            throw new IllegalArgumentException("KSViewHelperServiceImpl.convertServiceExceptionsToUI() doesn't support this exception " + ex.getClass().getSimpleName(),ex);
            return new RuntimeException(ex.getMessage(),ex);
        }
    }

}
