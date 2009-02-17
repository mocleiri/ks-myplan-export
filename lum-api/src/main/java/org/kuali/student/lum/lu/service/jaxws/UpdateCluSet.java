
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

@XmlRootElement(name = "updateCluSet", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCluSet", namespace = "http://student.kuali.org/lum/lu", propOrder = {"cluSetId","cluSetInfo"})

public class UpdateCluSet {

    @XmlElement(name = "cluSetId")
    private java.lang.String cluSetId;
    @XmlElement(name = "cluSetInfo")
    private org.kuali.student.lum.lu.dto.CluSetInfo cluSetInfo;

    public java.lang.String getCluSetId() {
        return this.cluSetId;
    }

    public void setCluSetId(java.lang.String newCluSetId)  {
        this.cluSetId = newCluSetId;
    }

    public org.kuali.student.lum.lu.dto.CluSetInfo getCluSetInfo() {
        return this.cluSetInfo;
    }

    public void setCluSetInfo(org.kuali.student.lum.lu.dto.CluSetInfo newCluSetInfo)  {
        this.cluSetInfo = newCluSetInfo;
    }

}

