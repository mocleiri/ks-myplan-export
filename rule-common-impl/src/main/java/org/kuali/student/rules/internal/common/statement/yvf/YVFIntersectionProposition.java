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
package org.kuali.student.rules.internal.common.statement.yvf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.kuali.student.rules.factfinder.dto.FactResultDTO;
import org.kuali.student.rules.internal.common.entity.ComparisonOperator;
import org.kuali.student.rules.internal.common.statement.exceptions.PropositionException;
import org.kuali.student.rules.internal.common.statement.propositions.IntersectionProposition;
import org.kuali.student.rules.internal.common.statement.propositions.Proposition;
import org.kuali.student.rules.internal.common.statement.report.PropositionReport;

public class YVFIntersectionProposition<E> implements Proposition {

	private IntersectionProposition<E> proposition;
	
	public YVFIntersectionProposition(String propositionName, ComparisonOperator comparisonOperator, Integer expectedValue, Object criteria, Object fact) {
		if (propositionName == null || propositionName.isEmpty()) {
			throw new PropositionException("Proposition name cannot be null");
		} else if (comparisonOperator == null) {
			throw new PropositionException("Comparison operator name cannot be null");
		} else if (expectedValue == null) {
			throw new PropositionException("Expected value name cannot be null");
		} else if (criteria == null) {
			throw new PropositionException("Criteria cannot be null");
		} else if (!(criteria instanceof FactResultDTO)) {
			throw new PropositionException("Criteria must be an instance of org.kuali.student.rules.factfinder.dto.FactResultDTO");
		} else if (fact == null) {
			throw new PropositionException("Fact cannot be null");
		} else if (!(fact instanceof FactResultDTO)) {
			throw new PropositionException("Fact must be an instance of org.kuali.student.rules.factfinder.dto.FactResultDTO");
		}

		FactResultDTO criteriaDTO = (FactResultDTO) criteria;
		FactResultDTO factDTO = (FactResultDTO) fact;

		Set<E> criteriaSet = getSet(criteriaDTO);
		Set<E> factSet = getSet(factDTO);

        this.proposition = new IntersectionProposition<E>( propositionName, 
        		comparisonOperator, expectedValue, criteriaSet, factSet); 
	}
	
	private Set<E> getSet(FactResultDTO criteria) {
		Set<E> set = new HashSet<E>();
		for( Map<String,Object> map : criteria.getResultList()) {
			for(Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().equals("column1")) {
					E value = (E) entry.getValue();
					set.add(value);
				}
			}
		}
		return set;
	}
	
	public IntersectionProposition<E> getProposition() {
		return this.proposition;
	}

	@Override
	public Boolean apply() {
		return proposition.apply();
	}
	
	@Override
	public String getPropositionName() {
		return this.proposition.getPropositionName();
	}

	@Override
	public PropositionReport getReport() {
		return this.proposition.getReport();
	}

	@Override
	public Boolean getResult() {
		return this.proposition.getResult();
	}
}
