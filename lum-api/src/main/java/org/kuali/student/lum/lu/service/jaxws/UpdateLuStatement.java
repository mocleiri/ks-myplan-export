
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

@XmlRootElement(name = "updateLuStatement", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLuStatement", namespace = "http://student.kuali.org/lum/lu", propOrder = {"luStatementId","luStatementInfo"})

public class UpdateLuStatement {

    @XmlElement(name = "luStatementId")
    private java.lang.String luStatementId;
    @XmlElement(name = "luStatementInfo")
    private org.kuali.student.lum.lu.dto.LuStatementInfo luStatementInfo;

    public java.lang.String getLuStatementId() {
        return this.luStatementId;
    }

    public void setLuStatementId(java.lang.String newLuStatementId)  {
        this.luStatementId = newLuStatementId;
    }

    public org.kuali.student.lum.lu.dto.LuStatementInfo getLuStatementInfo() {
        return this.luStatementInfo;
    }

    public void setLuStatementInfo(org.kuali.student.lum.lu.dto.LuStatementInfo newLuStatementInfo)  {
        this.luStatementInfo = newLuStatementInfo;
    }

}

