<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">이력조회</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 이력조회</div>
        </div>

        <div class="box_tbllist tbltm30">
            <form action="${pageContext.request.contextPath}/history" method="GET" id="frm">
                <input type="hidden" name="page" id="page" />
                <table summary="기업명, 마스킹해제 수, 다운로드 수">
                    <caption>이력조회 리스트</caption>
                    <thead>
                    <tr>
                        <th scope="col" class="m_hidden per4"><input type="checkbox" name="allCheck" title="전체체크"></th>
                        <th scope="col">기업명</th>
                        <th scope="col">마스킹해제 수</th>
                        <th scope="col">다운로드 수</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${historyList}" var="history" varStatus="status">
                            <tr <c:if test="${status.last}">class="last"</c:if>>
                                <td class="m_hidden"><input type="checkbox" name="chkbox" value="${history[0]}" title="체크"></td>
                                <td onclick="$(location).attr('href', '${pageContext.request.contextPath}/history/${history[0]}')">${history[1]}</td>
                                <td onclick="$(location).attr('href', '${pageContext.request.contextPath}/history/${history[0]}')">${history[2]}</td>
                                <td onclick="$(location).attr('href', '${pageContext.request.contextPath}/history/${history[0]}')">${history[3]}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <c:if test="${empty historyList}">
                    <!-- 데이터 없을경우 -->
                    <div class="nondata bdr2p">검색결과가 없습니다.</div>
                </c:if>
            </form>

            <div class="btn_tbllist"><a href="javascript:void(0)" onclick="excelDownLoad()" class="btn_excel">엑셀 다운로드</a></div>
            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<!-- layer pop -->
<div class="wrap_layerpop" id="layerExcelConfirm" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>엑셀 다운로드</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerExcelConfirm').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">항목을 선택 후 다운로드 해주세요.</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerExcelConfirm').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
</div>
<!-- //layer pop -->

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
        $("input[name='page']").val(page);
        $("#frm").submit();
    }

    /**
     * 엑셀 다운로드
     */
    function excelDownLoad() {
        var url = "${pageContext.request.contextPath}/history/historyExcel";
        var inputs = "";
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='companyCds[" + idx + "]' value='" + $(this).val() + "'/>";
            checkCnt++;
        });

        var sForm = "<form action='" + url + "' method='POST'>" + inputs + "</form>";

        if (checkCnt == 0) {
            layerOpen('layerExcelConfirm');
            return;
        } else {
            $(sForm).appendTo("body").submit().remove();
        }
    }


</script>