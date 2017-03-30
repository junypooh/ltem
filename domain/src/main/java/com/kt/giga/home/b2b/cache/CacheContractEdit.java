package com.kt.giga.home.b2b.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by junypooh on 2017-02-20.
 * <pre>
 * com.kt.giga.home.b2b.cache.CacheContractEdit
 *
 * 서비스 관리 상세 수정시 비밀번호 확인 여부로 사용될 cache 객체
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-20 오후 12:32
 */

@Data
@EqualsAndHashCode(callSuper = false, of = {"mgrSeq", "bizContSerial", "svcContSerial"})
public class CacheContractEdit implements Serializable {

    private static final long serialVersionUID = 8087196468209039264L;

    /** 관리자 sequence */
    private Long mgrSeq;

    private String bizContSerial;

    private Long svcContSerial;

    public CacheContractEdit() {}

    public CacheContractEdit(Long mgrSeq, String bizContSerial, Long svcContSerial) {
        this.mgrSeq = mgrSeq;
        this.bizContSerial = bizContSerial;
        this.svcContSerial = svcContSerial;
    }
}