<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">이력조회</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 이력조회</div>
        </div>

        <div class="box_listtop tm30">
            <div class="area_txt">
                <ul class="clfix">
                    <li>아이디 : <span>${managerHistory.managerMaskUserId}</span></li>
                    <li>권한 : <span>${managerHistory.authorityName}</span></li>
                    <li>총 다운로드 수 : <span><fmt:formatNumber value="${managerHistory.downloadCount}" pattern="#,###.##"/></span></li>
                    <li>총 조회건 수 : <span><fmt:formatNumber value="${managerHistory.maskedCount}" pattern="#,###.##"/></span></li>
                    <fmt:parseDate value="${managerHistory.startDt}" var="startDt" pattern="yyyyMM"/>
                    <fmt:parseDate value="${managerHistory.endDt}" var="endDt" pattern="yyyyMM"/>
                    <li>기간 : <span><fmt:formatDate value="${startDt}" pattern="yyyy.MM"/> ~ <fmt:formatDate value="${endDt}" pattern="yyyy.MM"/></span></li>
                </ul>
            </div>
        </div>

        <div class="box_tbllist">
            <table summary="계약번호,위치,이름,정보,작업내용,작업일시,가려진 정보 표시">
                <caption>이력조회 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden per4"><input type="checkbox" name="allCheck" title="전체체크"></th>
                    <th scope="col">계약번호</th>
                    <th scope="col" class="m_hidden">위치</th>
                    <th scope="col">이름</th>
                    <th scope="col" class="m_hidden">정보</th>
                    <th scope="col">작업내용</th>
                    <th scope="col" class="m_hidden">작업일시</th>
                    <th scope="col">가려진 정보 표시</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${historyList}" var="history" varStatus="status">
                    <tr <c:if test="${status.last}">class="last"</c:if> id="tr${history.historySeq}">
                        <td class="m_hidden"><input type="checkbox" name="chkbox" value="${history.historySeq}" title="체크"></td>
                        <td>${history.bizContSerial}</td>
                        <td class="m_hidden alignl">${history.menuPath}</td>
                        <td>${history.userName}</td>
                        <td class="m_hidden alignl">${history.targetInfo}</td>
                        <td class="alignl">${history.workContext}<p class="m_hidden">${history.reason}</p></td>
                        <td class="m_hidden">${history.workHistoryDate}</td>
                        <td><div class="btn_view"><button type="button" onclick="showDialog('${history.historySeq}','${page}')">표시</button></div></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty historyList}">
                <!-- 데이터 없을경우 -->
                <div class="nondata bdr2p">검색결과가 없습니다.</div>
            </c:if>

            <div class="btn_btmlist">
                <div class="btn_left btn_tbllist"><a href="javascript:void(0)" class="btn_excel" onclick="excelDownLoad()">엑셀 다운로드</a></div>
                <div class="btn_right">
                    <div class="btn_blk"><a href="${pageContext.request.contextPath}/history">목록</a></div>
                </div>
            </div>
            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<!-- layer pop -->
<!-- 가려진 정보 해제 -->
<div class="wrap_layerpop" id="layerPasswordConfirm" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>가려진 정보 해제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeDialog()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <input type="hidden" id="historySeq" >
            <input type="hidden" id="pageNumber" >
            <span class="txt_top mb10">가려진 정보 해제를 위해 비밀번호와 사유를 입력해주세요.</span>
            <div class="inp_pop mb10"><input type="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <p class="colred mb10" style="display: none" id="passwordNotMatched1">비밀번호를 입력해주세요.</p>
            <div class="inp_pop"><input name="txtReason" id="txtReason" maxlength="30" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" style="display: none" id="reasonNotMatched">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="removeMask()">해제</a></div>
        </div>
    </div>
</div>
<!-- //가려진 정보 해제 -->

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

<%-- Template ================================================================================ --%>
<script type="text/html" id="managerListTemplate">
    {{each(i) historyList}}
        <tr {{if historyList.length == i+1}}class="last"{{/if}} id="tr\${historySeq}">
            <td class="m_hidden"><input type="checkbox" name="chkbox" value="\${historySeq}" title="체크"></td>
            <td>\${bizContSerial}</td>
            <td class="m_hidden alignl">\${menuPath}</td>
            <td>\${userName}</td>
            <td class="m_hidden alignl">\${targetInfo}</td>
            <td class="alignl">\${workContext}<p class="m_hidden">\${reason}</p></td>
            <td class="m_hidden">\${workHistoryDate}</td>
            <td><div class="btn_view"><button type="button" onclick="showDialog('\${historySeq}','\${page}')">표시</button></div></td>
        </tr>
    {{/each}}
</script>

<script type="text/html" id="managerTemplate">
    <td class="m_hidden"><input type="checkbox" name="chkbox" value="\${historySeq}" title="체크"></td>
    <td>\${bizContSerial}</td>
    <td class="m_hidden alignl">\${menuPath}</td>
    <td>\${userName}</td>
    <td class="m_hidden alignl">\${targetInfo}</td>
    <td class="alignl">\${workContext}<p class="m_hidden">\${reason}</p></td>
    <td class="m_hidden">\${workHistoryDate}</td>
    <td><%--<div class="btn_view"><button type="button" onclick="showDialog('\${historySeq}','\${page}')">표시</button></div>--%></td>
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
        console.log('페이징');
        if(page == null) {
            page = 0;
        }
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/history/manager/pageAjax",
            type: "GET",
            data : {managerUserId:'${managerHistory.managerUserId}', startDt:'${managerHistory.startDt}', endDt:'${managerHistory.endDt}', page:page},
            async : false,
            success: function(result) {

                $('tbody').empty();
                $('#managerListTemplate').tmpl(result).appendTo('tbody');
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
     * 엑셀 다운로드
     */
    function excelDownLoad() {
        var url = "${pageContext.request.contextPath}/history/managerExcel";
        var inputs = "";
        var paramData = $.param({
            startDt: '${managerHistory.startDt}',
            endDt: '${managerHistory.endDt}',
            managerUserId : '${managerHistory.managerUserId}'
        }, true);
        $.each(paramData.split('&'), function () {
            var pair = this.split('=');
            inputs += "<input type='hidden' name='" + pair[0] + "' value='" + pair[1] + "' />";
        });
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='historySeqs[" + idx + "]' value='" + $(this).val() + "'/>";
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

    /**
     * 비밀번호 입력 layer open
     */
    function showDialog(historySeq, pageNumber) {
        layerErrorTextHide();
        $('#historySeq').val(historySeq);
        $('#pageNumber').val(pageNumber);
        layerOpen('layerPasswordConfirm');
        $('#password').val('');
        $('#txtReason').val('');
    }

    /**
     * 비밀번호 입력 layer close
     */
    function closeDialog() {
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
    }

    /**
     * 레이어 오류 메세지 숨김 처리.
     */
    function layerErrorTextHide() {
        $('#passwordNotMatched').hide();
        $('#passwordNotMatched1').hide();
        $('#reasonNotMatched').hide();
    }

    /**
     * 가려진 정보 표시
     */
    function removeMask() {
        layerErrorTextHide();

        var password = $('#password').val();
        var txtReason = $('#txtReason').val();
        var pageNumber = $('#pageNumber').val();

        if(password.length == 0){
            $('#passwordNotMatched1').show();
            return false;
        }

        if(txtReason.length == 0){
            $('#reasonNotMatched').show();
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/history/manager/unmask",
            type: "POST",
            data: { historySeq: $('#historySeq').val(), password: $('#password').val(), reason: $('#txtReason').val() },
            async : false,
            success: function (rtnData) {
                if (rtnData.errorCd == 0) {

                    // 전체 리스트 갱신(마스킹 해제된 건이 있을 경우 마스킹 처리해줘야하니까)
                    goPage(pageNumber);


                    // 마스킹 해제 데이터로 변경
                    $('#tr' + rtnData.historyInfo.historySeq).empty();
                    $('#managerTemplate').tmpl(rtnData.historyInfo).appendTo('#tr' + rtnData.historyInfo.historySeq);

                    closeDialog();
                    checkBoxControll();
                } else if (rtnData.errorCd == -1) {
                    $('#passwordNotMatched').show();
                } else {
                    alert(rtnData.msg);
                }

            },
            error: function (request, status, error) {
                //TODO 로그인 만료, 권한 없음 구분하여 처리할 방법 강구
                if (request.status == 403) {
                    // Not Authenticated
                    alert('로그인해주세요.');
                } else {
                    console.log(error);
                    alert('실패하였습니다.');
                }
            }
        });

    }


</script>