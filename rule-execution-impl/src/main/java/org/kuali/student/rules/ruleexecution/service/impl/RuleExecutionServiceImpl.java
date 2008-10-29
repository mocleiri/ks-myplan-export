/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.rules.ruleexecution.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.kuali.student.poc.common.ws.exceptions.DoesNotExistException;
import org.kuali.student.poc.common.ws.exceptions.InvalidParameterException;
import org.kuali.student.poc.common.ws.exceptions.MissingParameterException;
import org.kuali.student.poc.common.ws.exceptions.OperationFailedException;
import org.kuali.student.rules.factfinder.dto.FactResultDTO;
import org.kuali.student.rules.internal.common.utils.BusinessRuleUtil;
import org.kuali.student.rules.repository.dto.RuleSetDTO;
import org.kuali.student.rules.repository.util.ObjectUtil;
import org.kuali.student.rules.ruleexecution.dto.FactDTO;
import org.kuali.student.rules.ruleexecution.dto.ResultDTO;
import org.kuali.student.rules.ruleexecution.exceptions.RuleSetExecutionException;
import org.kuali.student.rules.ruleexecution.runtime.ExecutionResult;
import org.kuali.student.rules.ruleexecution.runtime.RuleSetExecutor;
import org.kuali.student.rules.ruleexecution.service.RuleExecutionService;
import org.kuali.student.rules.rulemanagement.dto.RuntimeAgendaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@WebService(endpointInterface = "org.kuali.student.rules.ruleexecution.service.RuleExecutionService", 
		serviceName = "RuleExecutionService", 
		portName = "RuleExecutionService", 
		targetNamespace = "http://student.kuali.org/wsdl/brms/RuleExecution")
@Transactional
public class RuleExecutionServiceImpl implements RuleExecutionService {
    /** SLF4J logging framework */
    final static Logger logger = LoggerFactory.getLogger(RuleExecutionServiceImpl.class);
    
    private RuleSetExecutor ruleSetExecutor;

    /**
     * Gets the rule execution engine.
     * 
     * @return Rule execution engine
     */
	public RuleSetExecutor getRuleSetExecutor() {
		return ruleSetExecutor;
	}

	/**
     * Sets the rule execution engine.
	 * 
	 * @param ruleSetExecutor Rule execution engine
	 */
	public void setRuleSetExecutor(RuleSetExecutor ruleSetExecutor) {
		this.ruleSetExecutor = ruleSetExecutor;
	}

    /**
     * Executes an <code>agenda</code> with <code>fact</code>.
     * 
     * @param agenda Agenda to execute
     * @param fact List of Facts for the <code>agenda</code>
     * @return Result of executing the <code>agenda</code>
     * @throws DoesNotExistException If rule set to be executed cannot be found
     * @throws InvalidParameterException If method parameters are invalid
     * @throws MissingParameterException If method parameters are missing
     * @throws OperationFailedException If the rule engine execution fails
     */
    public Object executeAgenda(RuntimeAgendaDTO agenda, Object fact) 
    	throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException 
    {
    	List<?> factList = (List<?>) fact;
    	
    	if (agenda == null) {
    		throw new MissingParameterException("Agenda is null");
    	} else if (fact == null) {
    		throw new MissingParameterException("Fact is null");
    	} else if (factList.isEmpty()) {
    		throw new InvalidParameterException("Fact list contains no elements");
    	}

    	/*try {
    		return this.ruleSetExecutor.execute(agenda, factList);
    	} catch(RuleSetExecutionException e) {
    		throw new OperationFailedException(e.getMessage());
    	}*/
    	throw new RuntimeException("Method not yet implemented");
    }

    /**
     * Executes an <code>agenda</code> with <code>fact</code> and a 
     * <code>ruleSet</code>.
     * 
     * @param ruleSet Rule set to execute
     * @param fact List of Facts for the <code>agenda</code>
     * @return Result of executing the <code>agenda</code>
     */
    public ResultDTO executeRuleSet(RuleSetDTO ruleSet, FactResultDTO fact)
		throws InvalidParameterException, MissingParameterException, OperationFailedException 
	{
    	if (ruleSet == null) {
    		throw new MissingParameterException("RuleSet is null");
    	} else if (fact == null) {
    		throw new MissingParameterException("Fact is null");
    	} else if (fact.getResultList() == null || fact.getResultList().isEmpty()) {
    		throw new MissingParameterException("Fact list is null");
    	}
    	
        List<Object> factList = new ArrayList<Object>();
        for(Map<String, Object> row : fact.getResultList()) {
        	Object obj = row.get("column1");
			// Convert to proper datatype
        	Object val = BusinessRuleUtil.convertToDataType(obj);
        	factList.add(val);
        }

        try {
    		ExecutionResult result =  this.ruleSetExecutor.execute(ruleSet, factList);
    		
    		ResultDTO dto = new ResultDTO();
    		for(Object obj : result.getResults()) {
    			dto.addResult(null, obj);
    		}
    		return dto;
    	} catch(RuleSetExecutionException e) {
    		throw new OperationFailedException("RuleSetExecutionException:" + e.getMessage()+"\n"+e.getCause());
    	}
//return null;
    }
}
