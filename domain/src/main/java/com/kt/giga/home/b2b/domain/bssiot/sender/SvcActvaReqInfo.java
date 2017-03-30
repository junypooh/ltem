package com.kt.giga.home.b2b.domain.bssiot.sender;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "svcActvaReqInfo", propOrder = {"svccontno", "conttypecd", "setltypecd"})
@Data
public class SvcActvaReqInfo {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 11)
    @XmlElement(name = "SVC_CONT_NO", required = true)
    protected String svccontno;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 2)
    @XmlElement(name = "CONT_TYPE_CD",  required = true)
    protected String conttypecd;

    @XmlElement(name = "SETL_TYPE_CD")
    protected String setltypecd;

}
