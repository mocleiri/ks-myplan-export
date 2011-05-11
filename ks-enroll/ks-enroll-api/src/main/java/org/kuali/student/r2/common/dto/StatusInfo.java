/*
 * Copyright 2010 The Kuali Foundation Licensed under the
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
 */

package org.kuali.student.r2.common.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.r2.common.infc.Status;
import org.w3c.dom.Element;

/**
 * Information about the state of an object
 * 
 * @author nwright
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusInfo", propOrder = {"success", "message", "_futureElements"})
public class StatusInfo implements Status, Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	private Boolean success;
	
	@XmlElement
	private String message;
	
    @XmlAnyElement
    private List<Element> _futureElements;	

    public static StatusInfo newInstance() {
        return new StatusInfo();
    }
	
    public static StatusInfo getInstance(Status status) {
        return new StatusInfo(status);
    }
	
	private StatusInfo() {
		success = true;
		message = "";
		_futureElements = null;
	}
	
	private StatusInfo(Status builder) {
		this.success = new Boolean(builder.isSuccess().booleanValue());
		this.message = builder.getMessage();
		this._futureElements = null;
	}

    @Override
	public Boolean isSuccess(){
		return success;
	}

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
	public String getMessage() {
		return message;
	}

    public void setMessage(String message) {
        this.message = message;
    }
}
