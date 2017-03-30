package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.domain.FileInfo;
import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.ICompanyService;
import com.kt.giga.home.b2b.web.services.ITermService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * com.kt.giga.home.b2b.web.controller
 *      HomeController
 *
 * 임시 컨트롤러
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2016-12-28 오후 4:08
 */
@Controller
@RequestMapping
@Slf4j
public class CommonController {

    @Autowired
    private ICommonService iCommonService;

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private ITermService iTermService;

    /**
     * 세션 연장 용 dummy
     * @return
     */
    @RequestMapping(value = "/refreshSession")
    @ResponseBody
    public Object refreshSession() {
        return "";
    }

    @GetMapping(value = {"/error/403", "/error/401", "/error/404", "/error/500", "/error/working"})
    public void errorPage() {
    }

    @GetMapping(value = {"/service/introduce", "/service/charge"})
    public void servicePage() {
    }

    /**
     * 파일 업로드
     * @param uploadFile
     * @param code
     * @return
     * @throws Exception
     */
    @PostMapping(value="/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam MultipartFile uploadFile, String code, String type) throws Exception {

        Assert.notNull(type, "Type must be not null.");

        FileInfo fileInfo = iCommonService.uploadFile(uploadFile, code, type);

        log.debug("###############################");
        log.debug("{}", fileInfo);
        log.debug("###############################");

        Map<String, Object> map = new HashMap<>();
        map.put("fileSeq", fileInfo.getFileSeq());
        map.put("oriFileName", fileInfo.getOriFileName());
        map.put("fileSize", fileInfo.getFileSize());
        map.put("fileExt", fileInfo.getFileExt());

        return map;
    }

    /**
     * 파일 다운로드
     * @param fileSeq
     * @param origin
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/fileDown")
    public void downloadFile(@RequestParam Long fileSeq, @RequestParam(required=false, defaultValue = "false") String origin, HttpServletRequest request, HttpServletResponse response, String type)
            throws Exception {

        iCommonService.downloadFile(fileSeq, "true".equals(origin) ? true: false ,  request, response, type);
    }

    /**
     * 파일 다운로드
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/companyLogo")
    public void companyLogo(@AuthenticationPrincipal B2bManager manager, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CompanyInfo companyInfo = iCompanyService.selectCompanyInfo(manager.getCompanyCd());

        iCommonService.downloadFile(companyInfo.getFileSeq(), false ,  request, response, "LOGO");
    }


    /**
     * footer 이용 약관 조회
     * @param categoryCd
     * @return
     */
    @GetMapping(value = "/agree/provision")
    @ResponseBody
    public TermInfo getIsActiveTrueAndCategor(int categoryCd) {
        return iTermService.getIsActiveTrueAndCategor((short) categoryCd);
    }


    /**
     * footer 개인정보처리방침 조회
     * @param categoryCd
     * @return
     */
    @GetMapping(value = "/agree/policy")
    @ResponseBody
    public Map<String, Object> getTermsCategor(Short categoryCd, Integer termSeq) {
        return iTermService.getTermsCategor(categoryCd, termSeq);
    }
}
