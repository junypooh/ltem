package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.converters.Aes256CryptoConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by mac on 2017. 1. 11..
 * <p>
 * 관리자 상태
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(exclude = {"bizContracts"}, callSuper = true)
@Table(name = "company", schema = "ltem")
public class Company extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = 9115211579386923770L;

    @Id
    @Column(name = "company_cd", updatable = false, nullable = false)
    private String code;   // 회사코드

    @Column(name = "company_name")
    private String name;     // 회사이름

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mgr_seq", referencedColumnName = "mgr_seq")
    private Manager manager;

    @Column
    @Convert(converter = Aes256CryptoConverter.class)
    private String telephone;       // 대표 전화번호 (AES256)

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "company")
    private Set<BizContract> bizContracts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_seq", referencedColumnName = "file_seq")
    private FileManager fileManager;

    public static Long getFileSeq(FileManager fileManager) {
        if(fileManager == null) {
            return null;
        } else {
            return fileManager.getFileSeq();
        }
    }

}
