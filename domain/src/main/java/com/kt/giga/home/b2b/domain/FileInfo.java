package com.kt.giga.home.b2b.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by junypooh on 2017-03-03.
 * <pre>
 * com.kt.giga.home.b2b.domain.FileInfo
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-03 오후 1:25
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"fileSeq"})
public class FileInfo {

    private Long fileSeq;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String fileExt;

    private String oriFileName;
}
