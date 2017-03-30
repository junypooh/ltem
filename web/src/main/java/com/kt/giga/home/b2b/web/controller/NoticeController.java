package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.NoticeInfo;
import com.kt.giga.home.b2b.entity.Notice;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-10.
 */
@RequestMapping("/notice")
@Controller
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private PageUtil pageUtil;

    /**
     * 서비스관리 > 공지사항 등록 View
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/edit")
    public void edit() {}


    /**
     * 서비스관리 > 공지사항 리스트
     * @param model
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "")
    public String selectNoticeAllInfo(Model model,
                                               @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        // 서비스관리 공지 리스트 조회
        Map<String, Object> map = noticeService.selectNoticeAllInfo((short) 10, pageable);
        List<NoticeInfo> noticeInfoList = (List<NoticeInfo>) map.get("notices");
        Page<Notice> page        = (Page<Notice>) map.get("page");

        model.addAttribute("noticeList", noticeInfoList);

        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());


        return "/notice/list";

    }


    /**
     * 서비스관리 > 공지사항 페이징 리스트 Ajax
     * @param model
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "pageAjax")
    @ResponseBody
    public Map<String, Object> selectNoticeAllInfoAjax(Model model,
                                      @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {

        Map<String, Object> rtnMap = new HashMap<>();
        // 서비스관리 공지 리스트 조회
        Map<String, Object> map = noticeService.selectNoticeAllInfo((short) 10, pageable);
        List<NoticeInfo> noticeInfoList = (List<NoticeInfo>) map.get("notices");
        Page<Notice> page        = (Page<Notice>) map.get("page");

        rtnMap.put("noticeList", noticeInfoList);

        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;

    }

    /**
     * 공지사항 등록
     * @param noticeInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/save")
    public String noticeInfoSave(@Valid @ModelAttribute NoticeInfo noticeInfo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "notice/edit";
        }

        noticeService.insertNotice(noticeInfo);

        return "redirect:/notice";
    }

    /**
     * 서비스관리 > 공지사항 상세,수정
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/{noticeSeq}")
    public String detail(Model model, @PathVariable("noticeSeq") int noticeSeq) {
        model.addAttribute("noticeInfo", noticeService.selectNoticeOne(noticeSeq));
        return "notice/modify";
    }

    /**
     * 공지사항 등록
     * @param noticeInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/{noticeSeq}/save")
    public String noticeInfoUpdate(@Valid @ModelAttribute NoticeInfo noticeInfo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "notice/modify";
        }

        noticeService.insertNotice(noticeInfo);

        return "redirect:/notice";
    }


    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/delete")
    @ResponseBody
    public void noticeInfoDelete(Integer[] noticeSeqs) {
        noticeService.deleteNotice(noticeSeqs);
    }



}
