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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-24.
 * <pre>
 * com.kt.giga.home.b2b.web.controller.BoardController
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-24 오전 11:29
 */
@RequestMapping("/board")
@Controller
public class BoardController {

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private PageUtil pageUtil;

    /**
     * 공지사항 리스트
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping(value = "/announce")
    public String selectNoticeAllInfo(Model model,
                                      @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        // 서비스관리 공지 리스트 조회
        Map<String, Object> map = noticeService.selectNoticeInfoIsActiveTrue(pageable);
        List<NoticeInfo> noticeInfoList = (List<NoticeInfo>) map.get("notices");
        Page<Notice> page        = (Page<Notice>) map.get("page");

        model.addAttribute("noticeList", noticeInfoList);

        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "/board/list";

    }

    /**
     * 공지사항 페이징 리스트 Ajax
     * @param pageable
     * @return
     */
    @GetMapping(value = "/announce/pageAjax")
    @ResponseBody
    public Map<String, Object> selectNoticeAllInfoAjax(String searchKey, String searchText
            , @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {

        Map<String, Object> rtnMap = new HashMap<>();
        // 서비스관리 공지 리스트 조회
        Map<String, Object> map = noticeService.selectNoticeListByCondition((short) 10, searchKey, searchText, pageable);
        List<NoticeInfo> noticeInfoList = (List<NoticeInfo>) map.get("notices");
        Page<Notice> page        = (Page<Notice>) map.get("page");

        rtnMap.put("noticeList", noticeInfoList);

        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;

    }

    /**
     * 공지사항 상세
     */
    @GetMapping(value = "/announce/{noticeSeq}")
    public String detail(Model model, @PathVariable("noticeSeq") int noticeSeq) {

        model.addAttribute("noticeInfo", noticeService.selectNoticeDetail(noticeSeq));
        model.addAttribute("crcn", "\r\n");

        return "/board/view";
    }
}
