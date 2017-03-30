package com.kt.giga.home.b2b.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by junypooh on 2017-03-02.
 * <pre>
 * com.kt.giga.home.b2b.cache.CompanyEdit
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-02 오후 3:56
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"mgrSeq", "code"})
public class CompanyEdit implements Serializable {

    private static final long serialVersionUID = 7362314293140023890L;

    /** 관리자 sequence */
    private Long mgrSeq;

    private String code;

    private Long svcContSerial;

    public CompanyEdit() {}

    public CompanyEdit(Long mgrSeq, String code) {
        this.mgrSeq = mgrSeq;
        this.code = code;
    }
}
