
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Feb 17 15:12:56 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "validateClu", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validateClu", namespace = "http://student.kuali.org/lum/lu", propOrder = {"validationType","cluInfo"})

public class ValidateClu {

    @XmlElement(name = "validationType")
    private java.lang.String validationType;
    @XmlElement(name = "cluInfo")
    private org.kuali.student.lum.lu.dto.CluInfo cluInfo;

    public java.lang.String getValidationType() {
        return this.validationType;
    }

    public void setValidationType(java.lang.String newValidationType)  {
        this.validationType = newValidationType;
    }

    public org.kuali.student.lum.lu.dto.CluInfo getCluInfo() {
        return this.cluInfo;
    }

    public void setCluInfo(org.kuali.student.lum.lu.dto.CluInfo newCluInfo)  {
        this.cluInfo = newCluInfo;
    }

}

