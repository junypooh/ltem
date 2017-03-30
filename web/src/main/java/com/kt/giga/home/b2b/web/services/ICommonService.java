package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by junypooh on 2017-02-08.
 * <pre>
 * com.kt.giga.home.b2b.web.services.ICommonService
 *
 * 공통 Service
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-08 오후 4:55
 */
public interface ICommonService {

    /**
     * 현재 비밀번호 확인
     * @param rawPassWord
     * @param encoded
     * @return
     */
    boolean doesPasswordMatch(String rawPassWord, String encoded);

    /**
     * 세션 타임 아웃 정보 조회
     * @return
     */
    Long getSessionTimeOut();

    /**
     * 파일 업로드 처리
     * @param multipartFile
     * @param appendPath
     * @return
     */
    FileInfo uploadFile(MultipartFile multipartFile, String appendPath, String type) throws Exception;

    /**
     * 파일 업로드 처리
     * @param multipartFile
     * @param appendPath
     * @return
     */
    FileInfo uploadFile(MultipartFile multipartFile, String appendPath, int thumbnailWidth, int thumbnailHeight) throws Exception;

    /**
     * 파일 다운로드 처리
     * @param fileSeq
     * @param origin
     * @param request
     * @param response
     * @throws Exception
     */
    void downloadFile(Long fileSeq, boolean origin, HttpServletRequest request, HttpServletResponse response, String type) throws Exception;

    /**
     * 파일 다운로드 처리
     * @param fileSeq
     * @param origin
     * @param request
     * @param response
     * @throws Exception
     */
    void downloadFile(Long fileSeq, boolean origin, HttpServletRequest request, HttpServletResponse response, int thumbnailWidth, int thumbnailHeight) throws Exception;
}
