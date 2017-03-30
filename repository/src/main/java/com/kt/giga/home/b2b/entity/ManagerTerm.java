package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by mac on 2017. 1. 11..
 */
@Entity
@Data
@IdClass(ManagerTerm.ManagerTermPK.class)
@EqualsAndHashCode(exclude = {"term", "manager"})
@ToString(exclude = {"term", "manager"})
@Table(name = "terms_agreed", schema = "ltem")
public class ManagerTerm implements Serializable {

    private static final long serialVersionUID = 3191962520223325637L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_seq")
    private Term term;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgr_seq")
    private Manager manager;

    private LocalDateTime agreed;

    private LocalDateTime withdrawn;

    @Data
    public static class ManagerTermPK implements Serializable {

        private static final long serialVersionUID = -8087014752197256235L;
        private Term term;
        private Manager manager;

        public ManagerTermPK() {

        }

        public ManagerTermPK(Term term, Manager manager) {
            this.term = term;
            this.manager = manager;
        }

    }
}
