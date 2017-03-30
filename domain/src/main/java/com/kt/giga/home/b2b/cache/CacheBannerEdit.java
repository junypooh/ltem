package com.kt.giga.home.b2b.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by DaDa on 2017-03-21.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"bannerSeq"})
public class CacheBannerEdit implements Serializable {

    private static final long serialVersionUID = -604457383976236680L;

    private Long mgrSeq;

    private Integer bannerSeq;

    public CacheBannerEdit() {}

    public CacheBannerEdit(Long mgrSeq, Integer bannerSeq) {
        this.mgrSeq = mgrSeq;
        this.bannerSeq = bannerSeq;
    }


}
