package com.kt.giga.home.b2b.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by cecil on 2017. 1. 13..
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseCreatedModifiedEntity {

    @Column(name = "created", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @CreatedDate
    private LocalDateTime created;

    @Column(name = "modified", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @LastModifiedDate
    private LocalDateTime modified;
}
