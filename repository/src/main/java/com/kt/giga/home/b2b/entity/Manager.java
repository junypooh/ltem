package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.converters.Aes256CryptoConverter;
import com.kt.giga.home.b2b.converters.LocalDateTimeAttributeConverter;
import com.kt.giga.home.b2b.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by mac on 2017. 1. 11..
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"sequence"})
@ToString(callSuper = true, exclude = {"company", "bizContracts", "managerTerms"})
@Table(name = "manager", schema = "ltem")
public class Manager extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = 5108420707231299678L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANAGER_SEQ")
    @Column(name = "mgr_seq", updatable = false)
    @SequenceGenerator(name = "MANAGER_SEQ", sequenceName = "ltem.seq_manager", allocationSize = 1, schema = "ltem")
    private Long sequence;

    @Column(name = "authority_cd")
    private short role;

    public void setRole(Role role) {
        this.role = role.getAuthorityCd();
    }

    public Role getRole() {
        return Role.getRole(this.role);
    }

    public short getAuthorityCd() { return this.role; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cd", referencedColumnName = "company_cd")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mgr_biz_contract_rel",
            schema = "ltem",
            joinColumns = @JoinColumn(name = "mgr_seq"),
            inverseJoinColumns = @JoinColumn(name = "biz_cont_serial")
    )
    private Set<BizContract> bizContracts;


    @OneToMany(mappedBy = "manager")
    private Set<ManagerTerm> managerTerms;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "manager")
//    private Set<Notice> notices;

    @Column(name = "mgr_status_cd", nullable = false)
    private short status;

    public void setStatus(Status status) {
        this.status = status.getStatusCd();
    }

    public Status getStatus() {
        return Status.getStatus(this.status);
    }

    public short getMgrStatusCd() { return this.status; }

    @Column(name = "user_id", updatable = false)
    private String id;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "is_passwd_init")
    private boolean isPasswordInitialized;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "auth_number")
    private String authenticationNumber;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "auth_sent")
    private LocalDateTime authenticationNumberSent;

    @Column(name = "name")
    private String name;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "telephone")
    private String telephone;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "email")
    private String email;

    @Column(name = "dept")
    private String department;

    @Column(name = "rank")
    private String rank;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "passwd_modified")
    private LocalDateTime passwordModified;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "last_logged_in")
    private LocalDateTime lastLoggedIn;

    @Column(name = "passwd_fail_cnt")
    private short passwordFailedCount;

    @Column(name = "captcha_fail_cnt")
    private short captchaFailedCount;

    @Column(name = "is_locked")
    private boolean isLocked;

    @ToString
    public enum Status {

        /**
         * 승인 대기
         */
        WAITING((short) 10),
        /**
         * 반려
         */
        REJECTED((short) 20),
        /**
         * 정상
         */
        ACTIVATED((short) 30),
        /**
         * 정지
         */
        SUSPENDED((short) 40),
        /**
         * 삭제
         */
        DELETED((short) 50);

        private short statusCd;

        Status(short statusCd) {
            this.statusCd = statusCd;
        }

        public short getStatusCd() {
            return statusCd;
        }

        public static Status getStatus(short statusCd) {
            for (Status status : values()) {
                if (status.statusCd == statusCd)
                    return status;
            }

            throw new IllegalArgumentException("No matching status for status code [" + statusCd + "] found.");
        }

    }
}
