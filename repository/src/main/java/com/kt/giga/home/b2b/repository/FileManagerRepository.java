package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.FileManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by junypooh on 2017-03-03.
 * <pre>
 * com.kt.giga.home.b2b.repository.FileManagerRepository
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-03 오후 3:22
 */
public interface FileManagerRepository extends JpaRepository<FileManager, Long>, JpaSpecificationExecutor {

    FileManager findOneByFileSeq(Long fileSeq);
}
