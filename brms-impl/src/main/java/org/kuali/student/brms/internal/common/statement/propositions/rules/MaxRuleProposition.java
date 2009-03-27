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
package org.kuali.student.brms.internal.common.statement.propositions.rules;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.student.brms.factfinder.dto.FactResultDTO;
import org.kuali.student.brms.factfinder.dto.FactStructureDTO;
import org.kuali.student.brms.internal.common.entity.ComparisonOperator;
import org.kuali.student.brms.internal.common.statement.MessageContextConstants;
import org.kuali.student.brms.internal.common.statement.exceptions.PropositionException;
import org.kuali.student.brms.internal.common.statement.propositions.MaxProposition;
import org.kuali.student.brms.internal.common.statement.propositions.PropositionType;
import org.kuali.student.brms.internal.common.statement.report.PropositionReport;
import org.kuali.student.brms.internal.common.utils.BusinessRuleUtil;
import org.kuali.student.brms.internal.common.utils.FactUtil;
import org.kuali.student.brms.rulemanagement.dto.RulePropositionDTO;
import org.kuali.student.brms.rulemanagement.dto.YieldValueFunctionDTO;

public class MaxRuleProposition<T extends Comparable<T>> extends AbstractRuleProposition<T> {

	public MaxRuleProposition(String id, String propositionName, 
			RulePropositionDTO ruleProposition, Map<String, ?> factMap) {
    	super(id, propositionName, PropositionType.MAX, ruleProposition);

		YieldValueFunctionDTO yvf = ruleProposition.getLeftHandSide().getYieldValueFunction();
		List<FactStructureDTO> factStructureList = yvf.getFactStructureList();
		FactStructureDTO fact = factStructureList.get(0);

		if (fact == null) {
			throw new PropositionException("Fact structure cannot be null");
		}

		Set<T> factSet = null;
		factDTO = null;

		if (fact.isStaticFact()) {
			String value = fact.getStaticValue();
			String dataType = fact.getStaticValueDataType();
			if (value == null || value.isEmpty() || dataType == null || dataType.isEmpty()) {
				throw new PropositionException("Static value and data type cannot be null or empty");
			}
			factSet = getSet(dataType, value);
			factDTO = createStaticFactResult(dataType, value);
		} else {
			if (factMap == null || factMap.isEmpty()) {
				throw new PropositionException("Fact map cannot be null or empty");
			}
	    	String factKey = FactUtil.createFactKey(fact);
			factDTO = (FactResultDTO) factMap.get(factKey);

			factColumn = fact.getResultColumnKeyTranslations().get(MessageContextConstants.PROPOSITION_MAX_COLUMN_KEY);
			if (factColumn == null || factColumn.trim().isEmpty()) {
				throw new PropositionException("Max column not found for key '"+
						MessageContextConstants.PROPOSITION_MAX_COLUMN_KEY+"'. Fact structure id: " + fact.getFactStructureId());
			}

			factSet = getSet(factDTO, factColumn);
			if (factSet == null || factSet.isEmpty()) {
				throw new PropositionException("Facts not found for column '"+
						factColumn+"'. Fact structure id: " + fact.getFactStructureId());
			}
		}

		ComparisonOperator comparisonOperator = ComparisonOperator.valueOf(ruleProposition.getComparisonOperatorTypeKey()); 
		@SuppressWarnings("unchecked")
		T expectedValue = (T) BusinessRuleUtil.convertToDataType(ruleProposition.getComparisonDataTypeKey(), ruleProposition.getRightHandSide().getExpectedValue());

		if(logger.isDebugEnabled()) {
			logger.debug("\n---------- YVFMaxProposition ----------"
					+ "\nFact static="+fact.isStaticFact()
					+ "\nFact key="+FactUtil.createFactKey(fact)
					+ "\nYield value function type="+yvf.getYieldValueFunctionType()
					+ "\nComparison operator="+comparisonOperator
					+ "\nExpected value="+expectedValue
					+ "\nFact set="+factSet
					+ "\n--------------------------------------------------");
		}

		super.proposition = new MaxProposition<T>(id, propositionName, 
        		comparisonOperator, expectedValue, factSet, ruleProposition); 
	}

    @Override
    public PropositionReport buildReport() {
        return buildDefaultReport(MessageContextConstants.PROPOSITION_MAX_SUCCESS_MESSAGE, MessageContextConstants.PROPOSITION_MAX_FAILURE_MESSAGE);
    }
}
