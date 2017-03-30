package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by cecil on 2017. 1. 13..
 */
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
@MappedSuperclass
public abstract class BaseCreatedByModifiedByEntity extends BaseCreatedModifiedEntity implements Serializable {

    private static final long serialVersionUID = -6655984657036359376L;

    @CreatedBy
    @Column(name = "created_by", length = 12, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", length = 12, nullable = false)
    private String modifiedBy;
}
