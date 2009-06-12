
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Tue Jun 02 13:05:55 PDT 2009
 * Generated source version: 2.2
 */

@XmlRootElement(name = "getNaturalLanguageForReqComponentInfo", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getNaturalLanguageForReqComponentInfo", namespace = "http://student.kuali.org/lum/lu", propOrder = {"reqComponent","nlUsageTypeKey","language"})

public class GetNaturalLanguageForReqComponentInfo {

    @XmlElement(name = "reqComponent")
    private org.kuali.student.lum.lu.dto.ReqComponentInfo reqComponent;
    @XmlElement(name = "nlUsageTypeKey")
    private java.lang.String nlUsageTypeKey;
    @XmlElement(name = "language")
    private java.lang.String language;

    public org.kuali.student.lum.lu.dto.ReqComponentInfo getReqComponent() {
        return this.reqComponent;
    }

    public void setReqComponent(org.kuali.student.lum.lu.dto.ReqComponentInfo newReqComponent)  {
        this.reqComponent = newReqComponent;
    }

    public java.lang.String getNlUsageTypeKey() {
        return this.nlUsageTypeKey;
    }

    public void setNlUsageTypeKey(java.lang.String newNlUsageTypeKey)  {
        this.nlUsageTypeKey = newNlUsageTypeKey;
    }

    public java.lang.String getLanguage() {
        return this.language;
    }

    public void setLanguage(java.lang.String newLanguage)  {
        this.language = newLanguage;
    }

}

