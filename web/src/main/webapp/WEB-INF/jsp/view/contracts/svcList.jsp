<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">사용자관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 사용자관리</div>
        </div>

        <div class="list_tit">
            <span class="btn_tit"><button type="button">${contractInfo.serialNumber}</button></span>
            <div class="area_tit">
                <strong class="txt_tit">${contractInfo.name}</strong><span class="btn_grey"><button type="button" onclick="editName()">수정</button></span>
            </div>
            <div class="area_tit_ip" style="display: none"><!-- 저장 -->
                <strong class="txt_tit"><input type="text" name="contractName" id="contractName" value="${contractInfo.name}" maxlength="8"></strong><span class="btn_grey"><button type="button" onclick="saveName()">저장</button></span>
            </div>
        </div>

        <div class="box_listtop">
            <div class="area_txt">
                <ul class="clfix">
                    <li>계약기간 : <span><javatime:format value="${contractInfo.startDate}" pattern="yyyy.MM.dd"/>~<javatime:format value="${contractInfo.endDate}" pattern="yyyy.MM.dd"/></span></li>
                    <li>회선수 : <span><fmt:formatNumber value="${contractInfo.circuitCount}" pattern="#,###.##"/>회선 (허브연결상태 : <em class="red"><fmt:formatNumber value="${contractInfo.hubConnectCount}" pattern="#,###.##"/></em>/<fmt:formatNumber value="${contractInfo.circuitCount}" pattern="#,###.##"/>)</span></li>
                    <li>데이터허용량 : <span><fmt:formatNumber value="${contractInfo.viewCapacity}" pattern="#,###.##"/>MB</span></li>
                    <li>상태 : <span>${contractInfo.contractState}</span></li>
                </ul>
                <span class="btn_garw2"><button type="button">계약설명</button></span>
                <div class="supervisor">
                    ${contractInfo.description}
                </div>
            </div>

            <!-- pop -->
            <section class="area_pop">
                <div>
                    <div class="tit">
                        <ul>
                            <li class="on">
                                <button type="button" data="created">최신순</button>
                            </li>
                            <li>
                                <button type="button" data="name">계약명순</button>
                            </li>
                        </ul>
                        <div class="box_searchtop">
                            <select name="contractsColumn" id="contractsColumn">
                                <option value="all">전체</option>
                                <option value="serialNumber">Biz계약번호</option>
                                <option value="name">계약명</option>
                                <option value="managersName">담당자</option>
                            </select>
                            <div class="sech_inp">
                                <input type="text" name="searchWord" id="searchWord">
                                <span for="searchWord" class="btn_search"><a href="javascript:void(0)" onclick="contractListJson('created')">검색</a></span>
                            </div>
                        </div>
                    </div>

                    <div class="list" id="contractList">
                        <ul class="clfix">
                        </ul>
                    </div>

                    <c:set var="recentContracts" value="${sessionScope.recentContracts}" />
                    <div class="btm">
                        <strong>최근 본 계약건</strong>
                        <ul>
                            <c:forEach items="${recentContracts}" var="recent" varStatus="status">
                            <li><a href="${pageContext.request.contextPath}/contracts/${recent.serialNumber}">${recent.serialNumber} <span>${recent.name}</span></a></li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </section>
            <!-- //pop -->
        </div>

        <!-- search -->
        <form:form action="${pageContext.request.contextPath}${pageContext.request.pathInfo}" method="GET" commandName="page" id="pageform">
        <form:hidden path="size"/>
        <input type="hidden" name="serialNumber" value="${contractInfo.serialNumber}"/>
        <input type="hidden" name="page" value="${page.number}"/>
        <div class="box_searchtop">
            <select name="statusCode" id="statusCode">
                <option value="">전체</option>
                <option value="Y" <c:if test="${param.statusCode eq 'Y'}">selected="selected"</c:if>>사용</option>
                <option value="N" <c:if test="${param.statusCode eq 'N'}">selected="selected"</c:if>>정지</option>
                <option value="X" <c:if test="${param.statusCode eq 'X'}">selected="selected"</c:if>>미등록</option>
            </select>
            <select name="searchKey" id="searchKey">
                <option value="svcContSerial" <c:if test="${param.searchKey eq 'svcContSerial'}">selected="selected"</c:if>>서비스 계약번호</option>
                <option value="userName" <c:if test="${param.searchKey eq 'userName'}">selected="selected"</c:if>>사용자명</option>
                <option value="userCtn" <c:if test="${param.searchKey eq 'userCtn'}">selected="selected"</c:if>>휴대폰 번호</option>
                <option value="iccId" <c:if test="${param.searchKey eq 'iccId'}">selected="selected"</c:if>>유심일련번호</option>
                <option value="imei" <c:if test="${param.searchKey eq 'imei'}">selected="selected"</c:if>>단말일련번호</option>
            </select>
            <div class="sech_inp">
                <input type="text" name="searchText" id="searchText" title="검색" value="${param.searchText}">
                <span for="searchText" class="btn_search"><a href="javascript:void(0)" onclick="$('#pageform').submit()">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_tbllist">
            <table summary="서비스 계약번호,상세주소,사용자명,전화번호,인증유무,상태,가려진정보표시">
                <caption>사용자관리 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden"><input type="checkbox" id="allCheck" title="전체체크"></th>
                    <th scope="col" class="m_list">서비스 계약번호</th>
                    <th scope="col" class="m_list">상세주소</th>
                    <th scope="col" class="m_list">사용자명</th>
                    <th scope="col" class="m_hidden">휴대폰번호</th>
                    <th scope="col" class="m_hidden">사용자 인증</th>
                    <th scope="col" class="m_hidden">상태</th>
                    <th scope="col">가려진 정보 표시</th>
                </tr>
                </thead>
                <tbody id="originalSvcContracts">
                <c:forEach items="${svcContracts}" var="svc" varStatus="status">
                    <tr<c:if test="${status.last}"> class="last"</c:if> id="tr${svc.svcContSerial}">
                        <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="${svc.svcContSerial}"></td>
                        <td onclick="detail(${svc.svcContSerial})">${svc.svcContSerial}</td>
                        <td onclick="detail(${svc.svcContSerial})">${svc.devicePlace}</td>
                        <td onclick="detail(${svc.svcContSerial})">${svc.userName}</td>
                        <td class="m_hidden" onclick="detail(${svc.svcContSerial})">${svc.userCtn}</td>
                        <td class="m_hidden" onclick="detail(${svc.svcContSerial})">
                            <c:choose>
                                <c:when test="${svc.isCertify}">인증완료</c:when>
                                <c:otherwise>미인증</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="m_hidden" onclick="detail(${svc.svcContSerial})">
                            <c:choose>
                                <c:when test="${svc.isUserMapped == true}">사용</c:when>
                                <c:otherwise>미사용</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn_view">
                                <button type="button" onclick="showDialog('${svc.svcContSerial}', '${contractInfo.serialNumber}')">
                                    표시
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty svcContracts}">
            <!-- 데이터 없을경우 -->
            <div class="nondata bdr2p">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>
            </c:if>
            <div class="btn_tbllist"><a href="javascript:void(0)" class="btn_excel" id="excelDown">엑셀 다운로드</a></div>
            <div class="paging">
                ${pageHtml}
            </div>
        </div>
        </form:form>

    </div>
</div>
<!-- //content -->

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
            <input type="hidden" id="svcContSerial" >
            <input type="hidden" id="serialNumber" >
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
<script type="text/html" id="maskSvcListTemplate">
    <tr id="tr\${svcContSerial}">
        <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${svcContSerial}"></td>
        <td onclick="detail(\${svcContSerial})">\${svcContSerial}</td>
        <td onclick="detail(\${svcContSerial})">\${devicePlace}</td>
        <td onclick="detail(\${svcContSerial})">\${userName}</td>
        <td class="m_hidden" onclick="detail(\${svcContSerial})">\${userCtn}</td>
        <td class="m_hidden" onclick="detail(\${svcContSerial})">
            {{if isCertify == true}}
            인증
            {{else}}
            미인증
            {{/if}}
        </td>
        <td class="m_hidden" onclick="detail(\${svcContSerial})">
            {{if isUserMapped == true}}
            사용
            {{else}}
            미사용
            {{/if}}
        </td>
        <td>
            <div class="btn_view">
                <button type="button" onclick="showDialog('\${svcContSerial}', '\${bizContSerial}')">표시</button>
            </div>
        </td>
    </tr>
</script>
<%-- Template ================================================================================ --%>

<%-- Template ================================================================================ --%>
<script type="text/html" id="unMaskSvcListTemplate">
    <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${svcContSerial}"></td>
    <td onclick="detail(\${svcContSerial})">\${svcContSerial}</td>
    <td onclick="detail(\${svcContSerial})">\${devicePlace}</td>
    <td onclick="detail(\${svcContSerial})">\${userName}</td>
    <td class="m_hidden" onclick="detail(\${svcContSerial})">\${userCtn}</td>
    <td class="m_hidden" onclick="detail(\${svcContSerial})">
        {{if isCertify == true}}
        인증
        {{else}}
        미인증
        {{/if}}
    </td>
    <td class="m_hidden" onclick="detail(\${svcContSerial})">
        {{if isUserMapped == true}}
        사용
        {{else}}
        미사용
        {{/if}}
    </td>
    <td></td>
</script>
<%-- Template ================================================================================ --%>

<%-- Template ================================================================================ --%>
<script type="text/html" id="contractTemplate">
    <li><a href="${pageContext.request.contextPath}/contracts/\${serialNumber}">\${serialNumber} <span>{{if name == null || name == ''}}-{{else}} \${name} {{/if}}</span></a></li>
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

        /**
         * 상단 계약명 클릭(검색 영역 show)
         */
        $('.list_tit .btn_tit button').click(function () {
            if (!$(this).hasClass('on')) {

                $('.area_pop .tit ul li').each(function () {
                    if($(this).hasClass('on')) {
                        contractListJson($(this).children().attr('data'));
                    }
                });
            }
            $(this).toggleClass('on');
            $(".box_listtop .area_pop").toggleClass('on');
        });

        /**
         * 계약명순, 최신순 클릭
         */
        $('.area_pop .tit ul li > button').click(function(){
            $(this).parent('li').addClass('on');
            $(this).parent().siblings('li').removeClass('on');

            contractListJson($(this).attr('data'));
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
     * 상단 검색 영역 계약 리스트 조회
     */
    function contractListJson(sortName) {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/list.json",
            data: {
                contractsColumn : $('#contractsColumn').val(),
                searchWord : $('#searchWord').val(),
                sortName : sortName
            },
            success: function (obj) {

                $('#contractList').empty();
                if(obj.length > 0) {
                    $('#contractList').append('<ul class="clfix"></ul>');
                    $('#contractTemplate').tmpl(obj).appendTo('#contractList > ul');
                } else {
                    $('#contractList').append('<div class="nondata">검색결과가 없습니다. </div>');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /**
     * 비밀번호 입력 layer open
     * @param svcContSerial
     * @param serialNumber
     */
    function showDialog(svcContSerial, serialNumber) {
        layerErrorTextHide();
        layerOpen('layerPasswordConfirm');
        $('#svcContSerial').val(svcContSerial);
        $('#serialNumber').val(serialNumber);
        $('#password').focus();
    }

    /**
     * 비밀번호 입력 layer close
     */
    function closeDialog() {
        layerErrorTextHide();
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
        $('#svcContSerial').val('');
        $('#serialNumber').val('');
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


    $(".box_contpop > p").hide();

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
            url: "${pageContext.request.contextPath}/contracts/unmask",
            type: "POST",
            data: {
                password: $('#password').val(),
                reason: $('#txtReason').val(),
                svcContSerial: $('#svcContSerial').val(),
                serialNumber: $('#serialNumber').val(),
                size: ${page.size},
                page: ${page.number}
            },
            success: function (rtnData) {
                console.log(rtnData);

                if (rtnData.errorCd == 0) {

                    // 전체 리스트 갱신(마스킹 해제된 건이 있을 경우 마스킹 처리해줘야하니까)
                    $('#originalSvcContracts').empty();
                    $('#maskSvcListTemplate').tmpl(rtnData.originalList).appendTo('#originalSvcContracts');
                    // 마스킹 해제 데이터로 변경
                    $('#tr' + rtnData.info.svcContSerial).empty();
                    $('#unMaskSvcListTemplate').tmpl(rtnData.info).appendTo('#tr' + rtnData.info.svcContSerial);

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
     * 페이징
     * @param page
     */
    function goPage(page) {
        $("input[name='page']").val(page);
        $("#pageform").submit();
    }

    /**
     * 엑셀 다운로드
     */
    function excelDownLoad() {

        var url = "${pageContext.request.contextPath}/contracts/svcListExcel";
        var inputs = "";
        var paramData = $.param({'serialNumber':${contractInfo.serialNumber}}, true);
        $.each(paramData.split('&'), function () {
            var pair = this.split('=');
            inputs += "<input type='hidden' name='" + pair[0] + "' value='" + pair[1] + "' />";
        });
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='svcContSerials[" + idx + "]' value='" + $(this).val() + "'/>";
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
     * 상세이동
     * @param serialNumber
     */
    function detail(svcContSerial) {
        $(location).attr('href', '${pageContext.request.contextPath}/contracts/${contractInfo.serialNumber}/' + svcContSerial);
    }

    /**
     * 계약명 수정
     */
    function editName() {
        $('.area_tit').hide();
        $('.area_tit_ip').show();
        $('#contractName').select();
    }

    /**
     * 계약명 저장
     */
    function saveName() {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/editContractName",
            type: "POST",
            data: {
                serialNumber: ${contractInfo.serialNumber},
                contractName: $('#contractName').val()
            },
            success: function (rtnData) {

                if (rtnData.errorCd == 0) {

                    $('.area_tit').show();
                    $('.area_tit_ip').hide();

                    $('.list_tit .area_tit .txt_tit').text($('#contractName').val());

                }
            },
            error: function (e) {
                console.log(e);
                alert('실패하였습니다.');
            }
        });
    }

</script>