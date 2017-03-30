package com.kt.giga.home.b2b.domain.bssiot.receiver;

/**
 * Created by 민우 on 2017-01-08.
 */

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contSttusChgRstLs",namespace = "", propOrder = {"svccontno", "trtsttuscd"})
@Data
public class ContSttusChgRstLs {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 11)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "SVC_CONT_NO", required = true)
    protected String svccontno;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 6)
    @XmlElement(name = "TRT_STTUS_CD")
    protected String trtsttuscd;

}
