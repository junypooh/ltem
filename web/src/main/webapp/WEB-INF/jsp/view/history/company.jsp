<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" var="year" pattern="yyyy"/>

<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">이력조회</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 이력조회</div>
        </div>

        <div class="box_listtop blt2">
            <div class="area_txt">
                <h3 class="h3_txt">${rtnInfo.companyName}</h3>
                <ul class="clfix">
                    <li>전체 마스킹 해제 수 : <span><fmt:formatNumber value="${rtnInfo.maskCount}" pattern="#,###.##"/></span></li>
                    <li>전체 다운로드 수 : <span><fmt:formatNumber value="${rtnInfo.downCount}" pattern="#,###.##"/></span></li>
                </ul>
            </div>
        </div>

        <form:form action="${pageContext.request.contextPath}${pageContext.request.pathInfo}" method="GET" id="form">
        <input type="hidden" name="page" value="${page}"/>
        <!-- search -->
        <div class="box_searchtop bsch2">
            <select name="startYear" id="startYear" class="wid_dt">
            <c:forEach begin="${0}" end="${5}" var="item">
                <option value="${year-item}" ${(startYear eq (year-item))?'selected':''}>${year-item}</option>
            </c:forEach>
            </select>
            <select name="startMonth" id="startMonth" class="wid_dt">
            <c:forEach begin="${1}" end="${12}" var="item">
                <c:choose>
                    <c:when test="${item lt 10}">
                        <fmt:parseNumber var="_startMonth" type="number" value="${startMonth}" />
                        <option value="0${item}" ${(_startMonth eq item)?'selected':''}>0${item}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${item}" ${(startMonth eq item)?'selected':''}>${item}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </select>
            <span class="txt_s">~</span>
            <select name="endYear" id="endYear" class="wid_dt">
            <c:forEach begin="${0}" end="${5}" var="item">
                <option value="${year-item}" ${(endYear eq (year-item))?'selected':''}>${year-item}</option>
            </c:forEach>
            </select>
            <select name="endMonth" id="endMonth" class="wid_dt">
            <c:forEach begin="${1}" end="${12}" var="item">
                <c:choose>
                    <c:when test="${item lt 10}">
                        <fmt:parseNumber var="_endMonth" type="number" value="${endMonth}" />
                        <option value="0${item}" ${(_endMonth eq item)?'selected':''}>0${item}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${item}" ${(endMonth eq item)?'selected':''}>${item}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </select>
            <div class="sech_inp">
                <span for="searchb" class="btn_search"><a href="javascript:void(0)" onclick="$('#form').submit()">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_tbllist">
            <table summary="아이디,이름, 권한, 마스킹해제 수, 다운로드수, 최종이력 일시, 가려진 정보 표시">
                <caption>이력조회 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden per4"><input type="checkbox" id="allCheck" title="전체체크"></th>
                    <th scope="col">아이디</th>
                    <th scope="col" class="m_hidden">이름</th>
                    <th scope="col" class="m_hidden">권한</th>
                    <th scope="col">마스킹해제 수</th>
                    <th scope="col">다운로드 수</th>
                    <th scope="col" class="m_hidden">최종이력 일시</th>
                    <th scope="col">가려진 정보 표시</th>
                </tr>
                </thead>
                <tbody id="tbody">
                <c:forEach items="${rtnInfo.historyList}" var="info" varStatus="status">
                <tr<c:if test="${status.last}"> class="last"</c:if> id="tr${info.managerUserId}">
                    <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="${info.managerUserId}"></td>
                    <td onclick="goDetail('${info.managerUserId}')">${info.managerMaskUserId}</td>
                    <td onclick="goDetail('${info.managerUserId}')" class="m_hidden">${info.managerName}</td>
                    <td onclick="goDetail('${info.managerUserId}')" class="m_hidden">${info.authorityName}</td>
                    <td onclick="goDetail('${info.managerUserId}')"><fmt:formatNumber value="${info.maskedCount}" pattern="#,###.##"/></td>
                    <td onclick="goDetail('${info.managerUserId}')"><fmt:formatNumber value="${info.downloadCount}" pattern="#,###.##"/></td>
                    <td onclick="goDetail('${info.managerUserId}')" class="m_hidden">${info.lastHistoryDate}</td>
                    <td><div class="btn_view"><button type="button" onclick="showDialog('${info.managerUserId}')">표시</button></div></td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty rtnInfo.historyList}">
            <!-- 데이터 없을경우 -->
            <div class="nondata bdr2p">검색결과가 없습니다.</div>
            </c:if>

            <div class="btn_btmlist">
                <div class="btn_left btn_tbllist"><a href="javascript:void(0)" class="btn_excel" id="excelDown">엑셀 다운로드</a></div>
                <security:authorize access="hasRole('ROLE_SUPER_MASTER')">
                <div class="btn_right">
                    <div class="btn_blk"><a href="${pageContext.request.contextPath}/history">기업 목록</a></div>
                </div>
                </security:authorize>
            </div>
            <div class="paging">
                ${pageHtml}
            </div>
        </div>
        </form:form>

    </div>
</div>

<!-- layer pop -->
<div class="wrap_layerpop" id="layerPasswordConfirm" style="display: none;">
    <!-- 가려진 정보 해제 -->
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>가려진 정보 해제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeDialog()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <input type="hidden" id="managerUserId" >
            <span class="txt_top mb10">가려진 정보 해제를 위해 비밀번호와 사유를 입력해주세요.</span>
            <div class="inp_pop mb10"><input type="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <p class="colred mb10" style="display: none" id="passwordNotMatched1">비밀번호를 입력해주세요.</p>
            <div class="inp_pop"><input name="txtReason" id="txtReason" maxlength="30" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" style="display: none" id="reasonNotMatched">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="removeMask()">해제</a></div>
        </div>
    </div>
    <!-- //가려진 정보 해제 -->
</div>

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
<script type="text/html" id="maskListTemplate">
    <tr id="tr\${managerUserId}">
        <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${managerUserId}"></td>
        <td onclick="goDetail('\${managerUserId}')">\${managerMaskUserId}</td>
        <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${managerName}</td>
        <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${authorityName}</td>
        <td onclick="goDetail('\${managerUserId}')">\${toCurrency(maskedCount)}</td>
        <td onclick="goDetail('\${managerUserId}')">\${toCurrency(downloadCount)}</td>
        <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${lastHistoryDate}</td>
        <td><div class="btn_view"><button type="button" onclick="showDialog('\${managerUserId}')">표시</button></div></td>
    </tr>
</script>
<%-- Template ================================================================================ --%>

<%-- Template ================================================================================ --%>
<script type="text/html" id="unMaskListTemplate">
    <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${managerUserId}"></td>
    <td onclick="goDetail('\${managerUserId}')">\${managerUserId}</td>
    <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${managerName}</td>
    <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${authorityName}</td>
    <td onclick="goDetail('\${managerUserId}')">\${toCurrency(maskedCount)}</td>
    <td onclick="goDetail('\${managerUserId}')">\${toCurrency(downloadCount)}</td>
    <td onclick="goDetail('\${managerUserId}')" class="m_hidden">\${lastHistoryDate}</td>
    <td></td>
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function () {

        /**
         * 체크박스 전체 선택/해제
         */
        checkBoxControll();

        /**
         * 엑셀 다운로드
         */
        $("#excelDown").click(function (e) {

            e.preventDefault();
            excelDownLoad();
        });

    });

    /**
     * 컨텐츠 테이블 tr class 조정
     * 가장 하단 tr 에 class="last" 추가
     */
    function tableTrClassSetting() {

        var trArray = $('.box_tbllist > table > tbody > tr');
        trArray.each(function(index, item) {
            $(this).removeClass('last');
            if(index == trArray.length - 1) {
                $(this).addClass('last');
            }
        });
    }

    /**
     * 체크박스 전체 선택/해제
     */
    function checkBoxControll() {

        $('#allCheck').click(function () {
            if ( $('#allCheck').is(':checked')) {
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
                $('#allCheck').prop('checked', true);
            }else {
                $('#allCheck').prop('checked', false);
            }

        });
    }

    /**
     * 페이징
     * @param page
     */
    function goPage(page) {
        $("input[name='page']").val(page);
        $("#form").submit();
    }

    /**
     * 비밀번호 입력 layer open
     * @param svcContSerial
     * @param serialNumber
     */
    function showDialog(managerUserId) {
        layerErrorTextHide();
        $('#managerUserId').val(managerUserId);
        layerOpen('layerPasswordConfirm');
    }

    /**
     * 비밀번호 입력 layer close
     */
    function closeDialog() {
        layerErrorTextHide();
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
        $('#password').val('');
        $('#txtReason').val('');
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

        if(password.length == 0){
            $('#passwordNotMatched1').show();
            return false;
        }

        if(txtReason.length == 0){
            $('#reasonNotMatched').show();
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/history/${companyCd}/unmask",
            type: "POST",
            data: {
                password: $('#password').val(),
                reason: $('#txtReason').val(),
                managerUserId: $('#managerUserId').val(),
                startYear: $('#startYear').val(),
                startMonth: $('#startMonth').val(),
                endYear: $('#endYear').val(),
                endMonth: $('#endMonth').val(),
                page: ${page}
            },
            success: function (rtnData) {
                console.log(rtnData);

                if (rtnData.errorCd == 0) {

                    // 전체 리스트 갱신(마스킹 해제된 건이 있을 경우 마스킹 처리해줘야하니까)
                    $('#tbody').empty();
                    $('#maskListTemplate').tmpl(rtnData.historyList).appendTo('#tbody');
                    // 마스킹 해제 데이터로 변경
                    $('#tr' + rtnData.historyInfo.managerUserId).empty();
                    $('#unMaskListTemplate').tmpl(rtnData.historyInfo).appendTo('#tr' + rtnData.historyInfo.managerUserId);

                    closeDialog();
                    checkBoxControll();
                    tableTrClassSetting();
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

    /**
     * 엑셀 다운로드
     */
    function excelDownLoad() {

        var url = "${pageContext.request.contextPath}/history/${companyCd}/excel";
        var inputs = "";
        var paramData = $.param({
            startYear: $('#startYear').val(),
            startMonth: $('#startMonth').val(),
            endYear: $('#endYear').val(),
            endMonth: $('#endMonth').val()
        }, true);
        $.each(paramData.split('&'), function () {
            var pair = this.split('=');
            inputs += "<input type='hidden' name='" + pair[0] + "' value='" + pair[1] + "' />";
        });
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='managerUserIds[" + idx + "]' value='" + $(this).val() + "'/>";
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
     * 상세 이동
     * @param managerUserId
     */
    function goDetail(managerUserId) {
        var params = $.param({
            managerUserId: managerUserId,
            startDt:$('#startYear').val()+$('#startMonth').val(),
            endDt:$('#endYear').val()+$('#endMonth').val()
        });

        $(location).attr('href', '${pageContext.request.contextPath}/history/manager?'+params);
    }

</script>
