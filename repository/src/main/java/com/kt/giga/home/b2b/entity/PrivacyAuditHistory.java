package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.converters.LocalDateTimeAttributeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by junypooh on 2017-03-21.
 * <pre>
 * com.kt.giga.home.b2b.entity.PrivacyAuditHistory
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-21 오후 4:01
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"historySeq"})
@Entity
@Table(name = "privacy_audit_history", schema = "ltem")
public class PrivacyAuditHistory implements Serializable {

    private static final long serialVersionUID = -9104304191998104119L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRIVACY_SEQ")
    @Column(name = "history_seq", updatable = false)
    @SequenceGenerator(name = "PRIVACY_SEQ", sequenceName = "ltem.seq_privacy_audit_history", allocationSize = 1, schema = "ltem")
    private Integer historySeq;

    @Column(name = "company_cd", nullable = false)
    private String companyCd;   // 회사코드

    @Column(name = "company_name", nullable = false)
    private String companyName;     // 회사이름

    @Column(name = "biz_cont_serial")
    private String bizContSerial;

    @Column(name = "manager_user_id", nullable = false)
    private String managerUserId;

    @Column(name = "manager_name", nullable = false)
    private String managerName;

    @Column(name = "authority_name", nullable = false)
    private String authorityName;

    @Column(name = "menu_path", nullable = false)
    private String menuPath;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "target_info")
    private String targetInfo;

    @Column(name = "is_unmasked")
    private boolean unmasked;

    @Column(name = "is_downloaded")
    private boolean downloaded;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "audited")
    private LocalDateTime audited;
}
