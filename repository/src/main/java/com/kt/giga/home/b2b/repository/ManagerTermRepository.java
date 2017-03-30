package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.ManagerTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by DaDa on 2017-03-15.
 */
public interface ManagerTermRepository extends JpaRepository<ManagerTerm, ManagerTerm.ManagerTermPK> {

    /**
     * 해당 회원이 동의하지 않은 약관 count
     * @param termSeq
     * @param mgrSeq
     * @return
     */
    @Query("SELECT count(m) FROM ManagerTerm m WHERE m.term.sequence = :#{#termSeq} AND m.manager.sequence = :#{#mgrSeq}")
    int countByTermAndManager(@Param("termSeq") Integer termSeq, @Param("mgrSeq") Long mgrSeq);


    /**
     * 약관동의 저장
     * @param termSeq
     * @param mgrSeq
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ltem.terms_agreed VALUES(:#{#termSeq}, :#{#mgrSeq}, now(), null)", nativeQuery = true)
    void saveTermManager(@Param("termSeq") int termSeq, @Param("mgrSeq") long mgrSeq);

    /**
     * 약관 동의 삭제
     * @param mgrSeq 회원일련번호
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ManagerTerm t WHERE t.manager.sequence IN :#{#mgrSeq}")
    void deleteByManager(@Param("mgrSeq") List<Long> mgrSeq);

    /**
     * 약관 동의 삭제
     * @param termSeq 약관시퀀스
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ManagerTerm t WHERE t.term.sequence IN :#{#termSeq}")
    void deleteByTerm(@Param("termSeq") List<Integer> termSeq);


}
