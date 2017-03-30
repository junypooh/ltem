package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by junypooh on 2017-03-13.
 * <pre>
 * com.kt.giga.home.b2b.entity.SpotDevBas
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-13 오후 4:36
 */
@IdClass(SpotDevBas.SpotDevBasPk.class)
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"spotDevSeq", "svcTgtSeq"})
@ToString(callSuper = true)
@Table(name = "spot_dev_bas")
public class SpotDevBas implements Serializable {

    private static final long serialVersionUID = -3498640698594381273L;

    @Id
    @Column(name = "spot_dev_seq", updatable = false, nullable = false)
    private Long spotDevSeq;

    @Id
    @Column(name = "svc_tgt_seq", updatable = false, nullable = false)
    private Long svcTgtSeq;

    @Column(name = "rmark")
    private String remark;

    @Data
    public static class SpotDevBasPk implements Serializable {

        private static final long serialVersionUID = 5451898415555537112L;

        private Long spotDevSeq;

        private Long svcTgtSeq;

        public SpotDevBasPk(){

        }

        public SpotDevBasPk(Long spotDevSeq, Long svcTgtSeq) {
            this.spotDevSeq = spotDevSeq;
            this.svcTgtSeq = svcTgtSeq;

        }
    }
}
