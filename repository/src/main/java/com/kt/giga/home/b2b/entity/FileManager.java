package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by junypooh on 2017-03-03.
 * <pre>
 * com.kt.giga.home.b2b.entity.File
 *
 * 공통 파일 관리 테이블 Entity
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-03 오후 12:19
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"fileSeq"})
@Table(name = "file_info", schema = "ltem")
public class FileManager extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = -151119786282859172L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_INFO_SEQ")
    @Column(name = "file_seq", updatable = false, nullable = false)
    @SequenceGenerator(name = "FILE_INFO_SEQ", sequenceName = "ltem.seq_file_info", allocationSize = 1, schema = "ltem")
    private Long fileSeq;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "ori_file_name")
    private String oriFileName;

}
