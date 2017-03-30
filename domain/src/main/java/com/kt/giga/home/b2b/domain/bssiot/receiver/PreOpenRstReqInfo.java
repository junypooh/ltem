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
@XmlType(name = "preOpenRstReqInfo",namespace = "", propOrder = {"actdate", "bizcontno", "totcnt", "preOpenRstLs"})
@Data
public class PreOpenRstReqInfo {
    @NotNull
    @NotEmpty
    @Length(min = 1, max = 8)
    @XmlElement(name = "ACT_DATE", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    protected String actdate;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "BIZ_CONT_NO", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    //protected long bizcontno;
    protected String bizcontno;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "TOT_CNT", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    //protected long totcnt;
    protected String totcnt;

    @Valid
    @Size(max = 1000)
    @XmlElement(name = "preOpenRstLs", namespace = "http://www.kt.com/m2m/domain/svcResProvider")
    protected List<PreOpenRstLs> preOpenRstLs;

    public List<PreOpenRstLs> getPreOpenRstLs()
    {
        if (this.preOpenRstLs == null) {
            this.preOpenRstLs = new ArrayList();
        }
        return this.preOpenRstLs;
    }
}
