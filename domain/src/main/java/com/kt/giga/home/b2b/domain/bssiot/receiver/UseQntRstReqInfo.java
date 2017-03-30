package com.kt.giga.home.b2b.domain.bssiot.receiver;

/**
 * Created by 민우 on 2017-01-08.
 */

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "useQntRstReqInfo",namespace = "", propOrder = {"bizcontno", "bizalwdqnt", "bizalwdunitcd", "svcconttotalwdqnt", "totcnt", "useQntRstLs"})
@Data
public class UseQntRstReqInfo {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "BIZ_CONT_NO", required = true)
    //protected long bizcontno;
    protected String bizcontno;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "BIZ_ALWD_QNT", required = true)
    protected String bizalwdqnt;

    @Pattern(regexp = "P|p",message = "value")
    @NotNull
    @NotEmpty
    @Length(min = 1, max = 1)
    @XmlElement(name = "BIZ_ALWD_UNIT_CD")
    protected String bizalwdunitcd;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "SVC_CONT_TOT_ALWD_QNT")
    //protected long svcconttotalwdqnt;
    protected String svcconttotalwdqnt;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 4)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "TOT_CNT", required = true)
    //protected long totcnt;
    protected String totcnt;

    @Valid
    @Size(max = 1000)
    protected List<UseQntRstLs> useQntRstLs;

    public List<UseQntRstLs> getUseQntRstLs()
    {
        if (this.useQntRstLs == null) {
            this.useQntRstLs = new ArrayList();
        }
        return this.useQntRstLs;
    }

}
