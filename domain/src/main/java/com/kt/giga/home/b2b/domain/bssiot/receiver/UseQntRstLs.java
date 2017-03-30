package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by 민우 on 2017-01-08.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "useQntRstLs",namespace = "", propOrder = {"svccontno", "chguse"})
@Data
public class UseQntRstLs {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 11)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "SVC_CONT_NO", required = true)
    //protected long svccontno;
    protected String svccontno;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "CHG_USE", required = true)
    //protected long chguse;
    protected String chguse;
}
