package com.kt.giga.home.b2b.web.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.kt.giga.home.b2b.cache.CacheBannerEdit;
import com.kt.giga.home.b2b.common.constant.CodeConstants;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.BannerInfo;
import com.kt.giga.home.b2b.entity.Banner;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.BannerService;
import com.kt.giga.home.b2b.web.services.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by DaDa on 2017-03-20.
 */
@RequestMapping("/banner")
@Controller
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * 서비스관리 > 배너 리스트
     * @param model
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "")
    public String list(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        Map<String, Object> map = bannerService.getBannerList(pageable);

        List<BannerInfo> bannerInfoList = (List<BannerInfo>) map.get("bannerInfoList");
        Page<Banner> page = (Page<Banner>) map.get("page");

        model.addAttribute("bannerList", bannerInfoList);

        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "/banner/list";
    }


    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "pageAjax")
    @ResponseBody
    public Map<String, Object> listAjax(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        Map<String, Object> rtnMap = new HashMap<>();

        Map<String, Object> map = bannerService.getBannerList(pageable);
        List<BannerInfo> bannerInfoList = (List<BannerInfo>) map.get("bannerInfoList");
        Page<Banner> page = (Page<Banner>) map.get("page");


        rtnMap.put("bannerList", bannerInfoList);
        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }



    /**
     * 서비스관리 > 배너 등록 View
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/edit")
    public void edit() {}


    /**
     * 서비스관리 > 배너 등록
     * @param bannerInfo
     * @param bindingResult
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/save")
    public String bannerSave(@Valid @ModelAttribute BannerInfo bannerInfo, BindingResult bindingResult) {

        if (bannerInfo.getCurrent() && bannerInfo.getFileSeq() == null) {
            bindingResult.addError(new FieldError("bannerInfo","fileSeq", "이미지를 등록해주세요."));
        }

        if (bannerInfo.getCurrent() && bannerInfo.getOrder() == null) {
            bindingResult.addError(new FieldError("bannerInfo","order", "순서를 선택해주세요."));
        }

        if (bindingResult.hasErrors()) {
            return "banner/edit";
        }

        bannerService.saveBanner(bannerInfo);

        return "redirect:/banner";
    }

    /**
     * 배너 삭제
     * @param bannerSeqs
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/delete")
    @ResponseBody
    public void bannerDelete(Integer[] bannerSeqs) {
        bannerService.deleteBanner(bannerSeqs);
    }

    /**
     * 배너 상세
     * @param model
     * @param bannerSeq
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/{bannerSeq}")
    public String bannerDetail(Model model, @PathVariable("bannerSeq") Integer bannerSeq) {
        BannerInfo bannerInfo = bannerService.getBannerDetail(bannerSeq);
        String url = "error/404";
        if (bannerInfo != null) {
            model.addAttribute("bannerInfo", bannerService.getBannerDetail(bannerSeq));
            url = "banner/detail";
        }

        return url;
    }

    /**
     * 배너 상세 > 수정페이지 이동
     * @param bannerSeq
     * @param password
     * @param model
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/{bannerSeq}")
    public String getUnmaskedBannerDetail(@PathVariable("bannerSeq") int bannerSeq, String password, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        model.addAttribute("bannerInfo", bannerService.getBannerDetailUnMask(bannerSeq));

        if (StringUtils.isNotBlank(password) && commonService.doesPasswordMatch(password, currentManager.getPassword())) {
            // 가려진 정보 해제 여부 cache 등록 (5분 expired)
            IMap<Object, Object> map = hazelcastInstance.getMap(CodeConstants.BANNER_CACHE_MAP_NAME);
            map.put(currentManager.getMgrSeq(), new CacheBannerEdit(currentManager.getMgrSeq(), bannerSeq), 5, TimeUnit.MINUTES);

            return "banner/modify";
        } else {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("doesNotPasswordMatched", true);

            return "banner/detail";

        }
    }

    /**
     * 배너 수정 > 저장
     * @param bannerInfo
     * @param bindingResult
     * @param bannerSeq
     * @param currentManager
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/{bannerSeq}/save")
    public String bannerModify(@Valid @ModelAttribute BannerInfo bannerInfo, BindingResult bindingResult,
                               @PathVariable("bannerSeq") int bannerSeq,
                               @AuthenticationPrincipal B2bManager currentManager, Model model) {

        IMap<Object, Object> map               = hazelcastInstance.getMap(CodeConstants.BANNER_CACHE_MAP_NAME);
        CacheBannerEdit    cacheBannerEdit = (CacheBannerEdit) map.get(currentManager.getMgrSeq());
        CacheBannerEdit bannerEdit = new CacheBannerEdit(currentManager.getMgrSeq(), bannerSeq);

        if (!bannerEdit.equals(cacheBannerEdit)) {
            throw new AccessDeniedException("잘못된 경로로 접근하였습니다.");
       }

        if (bannerInfo.getCurrent() && bannerInfo.getFileSeq() == null) {
            bindingResult.addError(new FieldError("bannerInfo","fileSeq", "이미지를 등록해주세요."));
        }

        if(bannerInfo.getCurrent() && bannerInfo.getOrder() == null) {
            bindingResult.addError(new FieldError("bannerInfo","order", "순서를 선택해주세요."));
        }

       if (bindingResult.hasErrors()) {
           model.addAttribute("bannerInfo", bannerInfo);
           return "banner/modify";
       }

       bannerService.saveBanner(bannerInfo);

       return "redirect:/banner/{bannerSeq}";
    }




}
