package com.kt.giga.home.b2b.domain.bssiot.receiver;

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

/**
 * Created by 민우 on 2017-01-08.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contSttusChgRstReqInfo",namespace = "", propOrder = {"bizcontno", "tgtbizcontno", "bizcontchgcd", "chgrsncd", "totcnt", "contSttusChgRstLs"})
@Data
public class ContSttusChgRstReqInfo {

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "BIZ_CONT_NO", required = true)
//    protected long bizcontno;
    protected String bizcontno;


//    @NotNull
//    @NotEmpty
//    @Length(min = 1, max = 10)
//    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "TGT_BIZ_CONT_NO")
//    protected long tgtbizcontno;
    protected String tgtbizcontno;

    @Pattern(regexp = "BCC|CAN",message = "value")
    @NotNull
    @NotEmpty
    @Length(min = 1, max = 3)
    @XmlElement(name = "BIZ_CONT_CHG_CD", required = true)
    protected String bizcontchgcd;

    @Pattern(regexp = "CR|OL",message = "value")
    @NotNull
    @NotEmpty
    @Length(min = 1, max = 4)
    @XmlElement(name = "CHG_RSN_CD", required = true)
    protected String chgrsncd;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 4)
    @Pattern(regexp = "^[0-9]*$")
    @XmlElement(name = "TOT_CNT")
    protected String totcnt;

    @Valid
    @Size(max = 1000)
    protected List<ContSttusChgRstLs> contSttusChgRstLs;

    public List<ContSttusChgRstLs> getContSttusChgRstLs()
    {
        if (this.contSttusChgRstLs == null) {
            this.contSttusChgRstLs = new ArrayList();
        }
        return this.contSttusChgRstLs;
    }
}
