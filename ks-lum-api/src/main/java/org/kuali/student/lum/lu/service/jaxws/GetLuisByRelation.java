
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.4
 * Wed Mar 11 10:08:47 PDT 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "getLuisByRelation", namespace = "http://student.kuali.org/wsdl/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLuisByRelation", namespace = "http://student.kuali.org/wsdl/lu", propOrder = {"relatedLuiId","luLuRelationTypeKey"})

public class GetLuisByRelation {

    @XmlElement(name = "relatedLuiId")
    private java.lang.String relatedLuiId;
    @XmlElement(name = "luLuRelationTypeKey")
    private java.lang.String luLuRelationTypeKey;

    public java.lang.String getRelatedLuiId() {
        return this.relatedLuiId;
    }

    public void setRelatedLuiId(java.lang.String newRelatedLuiId)  {
        this.relatedLuiId = newRelatedLuiId;
    }

    public java.lang.String getLuLuRelationTypeKey() {
        return this.luLuRelationTypeKey;
    }

    public void setLuLuRelationTypeKey(java.lang.String newLuLuRelationTypeKey)  {
        this.luLuRelationTypeKey = newLuLuRelationTypeKey;
    }

}

