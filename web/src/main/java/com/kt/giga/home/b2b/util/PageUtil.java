package com.kt.giga.home.b2b.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by junypooh on 2017-02-09.
 * <pre>
 * com.kt.giga.home.b2b.util.PageUtil
 *
 * 화면단 page 표시 util
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-09 오후 2:53
 */
@Component
@Data
@Slf4j
public class PageUtil {

    /** 페이징 건수 */
    private int viewPageCount;

    /** 전체 건수 */
    private long allCount;

    /** 마지막 페이지 */
    private int lastPage;

    /** 현재 페이지 */
    private int currentPage;

    /** 화면에 보여질 데이터 수 */
    private int contentsCount;

    /** 처음으로 */
    private String firstPageNavi;

    /** 마지막으로 */
    private String lastPageNavi;

    /** 화면에 표시된 페이지 중 선택 X */
    private String pageNaviOff;

    /** 화면에 표시된 페이지 중 선택 O */
    private String pageNaviOn;

    /** 이전페이지 */
    private String currentFirstPageNavi;

    /** 다음페이지 */
    private String currentLastPageNavi;

    private int defaultPageCount = 10;

    public String makePageHtml(Page<?> page) {
        return makePageHtml(page, defaultPageCount);
    }

    public String makePageHtml(Page<?> page, int pageCount) {

        setPageNaviParam();

        if(page.getTotalElements() == 0) {
            return "";
        }

        // 전체 건수
        this.allCount = page.getTotalElements();

        // 현재 페이지 (0부터 시작)
        this.currentPage = page.getNumber() + 1;

        // 화면에 보여질 데이터 수
        this.contentsCount = page.getSize();

        // 화면에 보여질 페이지 최대 건수
        this.viewPageCount = pageCount;

        // 마지막 페이지
        this.lastPage = (int)Math.ceil((double)allCount / (double)contentsCount);

        return makePageHtmlComponent(currentPage, viewPageCount, lastPage);
    }

    /**
     * Collection 객체를 이용한 페이징 util
     * @param list
     * @param pageCount 페이징 갯수
     * @param totCount 전체 건수
     * @param currPage 현재 페이지
     * @param viewSize 리스트 갯수
     * @return
     */
    public String makePageHtml(int pageCount, int totCount, int currPage, int viewSize) {

        setPageNaviParam();

        if(totCount == 0) {
            return "";
        }

        // 전체 건수
        this.allCount = totCount;

        // 현재 페이지 (0부터 시작)
        this.currentPage = currPage + 1;

        // 화면에 보여질 데이터 수
        this.contentsCount = viewSize;

        // 화면에 보여질 페이지 최대 건수
        this.viewPageCount = pageCount;

        // 마지막 페이지
        this.lastPage = (int)Math.ceil((double)allCount / (double)contentsCount);

        return makePageHtmlComponent(currentPage, viewPageCount, lastPage);
    }

    /**
     * 페이징 HTML make
     * @param currentPage
     * @param viewPageCount
     * @param lastPage
     * @return
     */
    private String makePageHtmlComponent(int currentPage, int viewPageCount, int lastPage) {


        String _html = "";

        // 처음으로 // 10개 페이지 이동으로 변경
        if ( currentPage - 10 > 0 )
            _html += String.format(getFirstPageNavi(), clickScript(currentPage - 10));
//            _html += String.format(getFirstPageNavi(), clickScript(1));
        else
            _html += String.format(getFirstPageNavi(), clickScript(1));

        // 이전페이지
        int prevLast = currentPage - 1;
        if ( prevLast >= 1 )
            _html += String.format(getCurrentFirstPageNavi(), clickScript(prevLast));
        else
            _html += String.format(getCurrentFirstPageNavi(), "");

        // PageList
        for ( int i = ((currentPage - 1) / viewPageCount) * viewPageCount + 1; i < ((currentPage - 1) / viewPageCount) * viewPageCount + 1 + viewPageCount && i <= lastPage; i++ ) {

            if ( i > lastPage ) break;


            if ( i == currentPage ) {
                _html += String.format(getPageNaviOn(), clickScript(i), i);
            } else {
                _html += String.format(getPageNaviOff(), clickScript(i), i);
            }

        }


        // 다음페이지
        int nextFirst = currentPage + 1;
        if ( nextFirst <= lastPage )
            _html += String.format(getCurrentLastPageNavi(), clickScript(nextFirst));
        else
            _html += String.format(getCurrentLastPageNavi(), "");

        // 마지막으로 // 10개 페이지 이동으로 변경
        if ( currentPage + 10 <= lastPage )
            _html += String.format(getLastPageNavi(), clickScript(currentPage + 10));
//            _html += String.format(getLastPageNavi(), clickScript(lastPage));
        else
            _html += String.format(getLastPageNavi(), clickScript(lastPage));


        return _html;

    }

    public void setPageNaviParam() {

        firstPageNavi = "<span class=\"btn_first\"><a href=\"javascript:void(0)\" %s>처음페이지</a></span>";
        currentFirstPageNavi = "<span class=\"btn_prev\"><a href=\"javascript:void(0)\" %s>이전</a></span>";
        currentLastPageNavi = "<span class=\"btn_next\"><a href=\"javascript:void(0)\" %s>이전</a></span>";
        lastPageNavi = "<span class=\"btn_last\"><a href=\"javascript:void(0)\" %s>마지막페이지</a></span>";
        pageNaviOff = "<a href=\"javascript:void(0)\" %s>%d</a>";
        pageNaviOn = "<a href=\"javascript:void(0)\" %s class=\"on\">%d</a>";

    }

    public String clickScript(int page) {
        return "onclick=\"goPage(" + Integer.toString(page - 1) + ")\"";
    }
}
