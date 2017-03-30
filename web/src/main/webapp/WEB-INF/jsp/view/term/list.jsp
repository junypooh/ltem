<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">서비스관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 서비스관리</div>
        </div>

        <div class="box_tab tab3">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/notice">공지사항</a></li>
                    <li><a href="${pageContext.request.contextPath}/banner">배너</a></li>
                    <li class="ln2 on"><a href="${pageContext.request.contextPath}/term">서비스 <span>이용약관</span></a></li>
                </ul>
            </div>
        </div>

        <!-- table list -->
        <div class="box_tbllist">
            <table summary="분류,제목,버전,노출여부,작성자,작성일">
                <caption>서비스이용약관 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden per4"><input type="checkbox" name="allCheck" title="전체체크"></th>
                    <th scope="col" class="m_list">분류</th>
                    <th scope="col" class="m_list">제목</th>
                    <th scope="col" class="m_hidden">버전</th>
                    <th scope="col" class="m_hidden">노출여부</th>
                    <th scope="col" class="m_hidden">작성자</th>
                    <th scope="col" class="m_hidden">작성일</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${termList}" var="termList" varStatus="status">
                    <tr <c:if test="${status.last}">class="last"</c:if>>
                        <td class="m_hidden"><input type="checkbox" name="chkbox" value="${termList.termSeq}" title="체크"></td>
                        <td onclick="goDetail(${termList.termSeq})">${termList.categoryName}</td>
                        <td onclick="goDetail(${termList.termSeq})">${termList.title}</td>
                        <td class="m_hidden" onclick="goDetail(${termList.termSeq})">${termList.version}</td>
                        <td class="m_hidden" onclick="goDetail(${termList.termSeq})">${termList.isActiveName}</td>
                        <td class="m_hidden" onclick="goDetail(${termList.termSeq})">${termList.createdByMask}</td>
                        <td class="m_hidden" onclick="goDetail(${termList.termSeq})"><javatime:format value="${termList.created}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty termList}">
            <!-- 데이터 없을경우 -->
            <div class="nondata bdr2p">등록된 게시물이 없습니다.</div>
            </c:if>

            <div class="btn_btmlist btn_lhid">
                <div class="btn_left btn_white"><button type="button" onclick="deleteBtn()">삭제</button></div>
                <div class="btn_right">
                    <div class="btn_blk"><a href="${pageContext.request.contextPath}/term/edit">등록</a></div>
                </div>
            </div>
            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>
<!-- //content -->


<!-- Layer -->
<!--  하단 버튼한개-->
<div class="wrap_layerpop" id="failLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>안내</h2>
            <div class="btn_close">
                <button type="button" onclick="closeLayer()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10"></span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
        </div>
    </div>
</div>
<!-- // 하단 버튼한개 -->

<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="deleteLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>게시물 삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">해당 게시물을 삭제하시겠습니까?</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="fn_delete()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->
<!-- // Layer -->

<%-- Template ================================================================================ --%>
<script type="text/html" id="termListTemplate">
    {{each(i) termList}}
        <tr {{if termList.length == i+1}}class="last"{{/if}}>
            <td class="m_hidden"><input type="checkbox" name="chkbox" value="\${termSeq}" title="체크"></td>
            <td onclick="goDetail(\${termSeq})">\${categoryName}</td>
            <td onclick="goDetail(\${termSeq})">\${title}</td>
            <td class="m_hidden" onclick="goDetail(\${termSeq})">\${version}</td>
            <td class="m_hidden" onclick="goDetail(\${termSeq})">\${isActiveName}</td>
            <td class="m_hidden" onclick="goDetail(\${termSeq})">\${createdByMask}</td>
            <td class="m_hidden" onclick="goDetail(\${termSeq})">\${createdToString}</td>
        </tr>
    {{/each}}
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function() {
        checkBoxControll();
    });

    /** 체크박스 전체 선택/해제 */
    function checkBoxControll() {
        $('input[name="allCheck"]').click(function () {
            if ( $('input[name="allCheck"]').is(':checked')) {
                $(':checkbox[name="chkbox"]').prop('checked', true);
            } else {
                $(':checkbox[name="chkbox"]').prop('checked', false);
            }
        });

        $('input[name="chkbox"]').click(function () {
            var chkboxCnt = $('input[name="chkbox"]').length;
            var chkCnt = 0;
            $.each($('input[name="chkbox"]'), function () {
                if($(this).is(':checked') == true) {
                    chkCnt++;
                }
            });

            if(chkboxCnt == chkCnt) {
                $('input[name="allCheck"]').prop('checked', true);
            }else {
                $('input[name="allCheck"]').prop('checked', false);
            }

        });
    }

    /**
     * 페이징
     * @param page
     */
    function goPage(page) {
        if(page == null) {
            page = 0;
        }
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/term/pageAjax",
            type: "GET",
            data : {page:page},
            async : false,
            success: function(result) {
                $('tbody').empty();
                $('#termListTemplate').tmpl(result).appendTo('tbody');
                $('.paging').empty().append(result.pageHtml);

                $('input[name="allCheck"]').prop('checked', false);
                checkBoxControll();
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /**
     * 상세 페이지로 이동
     */
    function goDetail(termSeq) {
        $(location).attr('href', '${pageContext.request.contextPath}/term/' + termSeq);
    }

    /**
     * 삭제버튼 클릭
     */
    function deleteBtn() {
        var termSeqs = new Array();

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function () {
            termSeqs.push($(this).val());
            checkCnt++;
        });

        if (checkCnt == 0) {
            $('#failLayer .txt_top').html('삭제할 게시물을 선택해주세요.');
            layerOpen('failLayer');
        } else {
            layerOpen('deleteLayer');
        }

    }


    /** 약관 삭제 */
    function fn_delete() {
        var termSeqs = new Array();
        $(':checkbox[name="chkbox"]:checked').each(function () {
            termSeqs.push($(this).val());
        });

        console.log(termSeqs);

        $.ajax({
            url: "${pageContext.request.contextPath}/term/delete",
            type: "POST",
            data : {'termSeqs':termSeqs},
            async : false,
            success: function(result) {
                $(location).attr('href', '${pageContext.request.contextPath}/term');
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }


</script>