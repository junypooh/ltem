package com.kt.giga.home.b2b.domain.bssiot.receiver;

/**
 * Created by 민우 on 2017-01-06.
 */

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bizContInfoRstReqInfo", namespace = "", propOrder = {"cmpncd", "bizcd", "bizcontno", "cperdunitcd", "cperdval", "bizcontcircuitnum", "bizcontstdt", "maxcontfnsdt", "alwdqntunitcd", "alwdqnt", "bizcontdesc"})
@Data
public class BssIotBizContractDto {

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 3)
    @XmlElement(name = "CMPN_CD", required = true)
    protected String cmpncd;

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 3)
    @XmlElement(name = "BIZ_CD",  required = true)
    protected String bizcd;

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "BIZ_CONT_NO",  required = true)
    protected String bizcontno;

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 1)
    @XmlElement(name = "CPERD_UNIT_CD", required = true)
    protected String cperdunitcd;

    @NotNull(message = "empty")
    @Min(1)
    @Max(99999)
    @XmlElement(name = "CPERD_VAL")
    protected Integer cperdval;

    @NotNull(message = "empty")
    @Min(1)
    @Max(99999999)
    @XmlElement(name = "BIZ_CONT_CIRCUIT_NUM", required = true)
    protected Integer bizcontcircuitnum;


    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 14)
    @XmlElement(name = "BIZ_CONT_ST_DT", required = true)
    protected String bizcontstdt;

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 14)
    @XmlElement(name = "MAX_CONT_FNS_DT", required = true)
    protected String maxcontfnsdt;

    @Pattern(regexp = "G|M|P",message = "value")
    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 1)
    @XmlElement(name = "ALWD_QNT_UNIT_CD", required = true)
    protected String alwdqntunitcd;

    @NotNull(message = "empty")
    @Min(1)
    @Max(9999999999L)
    @Digits(integer = 10, fraction = 0)
    @XmlElement(name = "ALWD_QNT", required = true)
    protected Long alwdqnt;

    @XmlElement(name = "BIZ_CONT_DESC")
    protected String bizcontdesc;

}