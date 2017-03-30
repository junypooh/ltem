package com.kt.giga.home.b2b.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by DaDa on 2017-03-02.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"mgrSeq", "applyMgrSeq"})
public class CacheAccountEdit implements Serializable {

    private static final long serialVersionUID = -1020296687720302883L;

    /** 나의 관리자 sequence */
    private Long mgrSeq;

    /** 변경할 관리자 sequence */
    private Long applyMgrSeq;

    public CacheAccountEdit() {}

    public CacheAccountEdit(Long mgrSeq, Long applyMgrSeq) {
        this.mgrSeq = mgrSeq;
        this.applyMgrSeq = applyMgrSeq;
    }
}
