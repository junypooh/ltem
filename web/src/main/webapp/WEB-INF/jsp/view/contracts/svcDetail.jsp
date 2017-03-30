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
            <span class="btn_tit"><button type="button">${svcContractInfo.bizContSerial}</button></span>
            <div class="area_tit">
                <strong class="txt_tit">${svcContractInfo.currentBizContract.name}</strong><span class="btn_grey"><button type="button" onclick="editName()">수정</button></span>
            </div>
            <div class="area_tit_ip" style="display: none"><!-- 저장 -->
                <strong class="txt_tit"><input type="text" name="contractName" id="contractName" value="${svcContractInfo.currentBizContract.name}" maxlength="8"></strong><span class="btn_grey"><button type="button" onclick="saveName()">저장</button></span>
            </div>
        </div>

        <div class="box_listtop">
            <div class="area_txt">
                <ul class="clfix">
                    <li>계약기간 : <span><javatime:format value="${svcContractInfo.currentBizContract.startDate}" pattern="yyyy.MM.dd"/>~<javatime:format value="${svcContractInfo.currentBizContract.endDate}" pattern="yyyy.MM.dd"/></span></li>
                    <li>회선수 : <span><fmt:formatNumber value="${svcContractInfo.currentBizContract.circuitCount}" pattern="#,###.##"/>회선</span></li>
                    <li>회선수 : <span><fmt:formatNumber value="${svcContractInfo.currentBizContract.circuitCount}" pattern="#,###.##"/>회선 (허브연결상태 : <em class="red"><fmt:formatNumber value="${svcContractInfo.currentBizContract.hubConnectCount}" pattern="#,###.##"/></em>/<fmt:formatNumber value="${svcContractInfo.currentBizContract.circuitCount}" pattern="#,###.##"/>)</span></li>
                    <li>데이터허용량 : <span><fmt:formatNumber value="${svcContractInfo.currentBizContract.viewCapacity}" pattern="#,###.##"/>MB</span></li>
                    <li>상태 : <span>${svcContractInfo.currentBizContract.contractState}</span></li>
                </ul>
                <span class="btn_garw2"><button type="button">계약설명</button></span>
                <div class="supervisor">
                    ${svcContractInfo.currentBizContract.description}
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

        <div class="area_h3">
            <h3 class="h3_nor">단말정보</h3>
        </div>

        <div class="box_list2col">
            <div class="area_inp">
                <dl>
                    <dt class="bg_line"><div>서비스 계약번호</div></dt>
                    <dd><div>${svcContractInfo.svcContSerial}</div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>유심일련번호</div></dt>
                    <dd><div>${svcContractInfo.iccId}</div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>단말일련번호</div></dt>
                    <dd><div>${svcContractInfo.imei}</div></dd>
                </dl>
            </div>
        </div>

        <div class="area_h3 mt10">
            <h3 class="h3_nor">사용자 정보</h3>
        </div>

        <div class="box_list2col">
            <form:form action="${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}" method="POST" commandName="svcContractInfo" id="frm">
            <input type="hidden" name="password" id="passwordform" />
            <input type="hidden" name="reason"  id="reason" />
            <div class="area_inp">
                <dl>
                    <dt class="bg_line"><div>상세주소</div></dt>
                    <dd><div>${svcContractInfo.devicePlace}&nbsp;</div></dd> <!-- 빈값일 경우 화면 깨짐 발생하여 강제로 &nbsp; 추가 -->
                </dl>
                <dl>
                    <dt class="bg_line"><div>사용자명</div></dt>
                    <dd><div>${svcContractInfo.userName}&nbsp;</div></dd> <!-- 빈값일 경우 화면 깨짐 발생하여 강제로 &nbsp; 추가 -->
                </dl>
                <dl>
                    <dt class="bg_line"><div>휴대폰 번호</div></dt>
                    <dd><div>${svcContractInfo.userCtn}&nbsp;<c:if test="${svcContractInfo.isUserMapped and not svcContractInfo.isCertify}"><span class="btn_blk btn_conf"><a href="javascript:void(0)" onclick="resend()">인증번호 재발송</a></span></c:if></div></dd> <!-- 빈값일 경우 화면 깨짐 발생하여 강제로 &nbsp; 추가 -->
                </dl>
                <dl>
                    <dt class="bg_line"><div>사용자 인증</div></dt>
                    <dd>
                        <div>
                        <c:choose>
                            <c:when test="${svcContractInfo.isCertify}">인증완료</c:when>
                            <c:otherwise>미인증</c:otherwise>
                        </c:choose>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden"><div>상태</div></dt>
                    <dd>
                        <div>
                        <c:choose>
                            <c:when test="${not svcContractInfo.isSuspended}">사용</c:when>
                            <c:otherwise>일시정지</c:otherwise>
                        </c:choose>
                        </div>
                    </dd>
                </dl>
            </div>

            </form:form>
        </div>

        <div class="btn_btmlist">
            <c:if test="${svcContractInfo.deviceStatusCd == 30 and svcContractInfo.isUserMapped == true}">
            <div class="btn_left btn_white"><button type="button" onclick="deleteAccountLayer()">사용자 해제</button></div>
            </c:if>
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="cancel()">취소</a></div>
                <c:if test="${svcContractInfo.deviceStatusCd >= 20 and svcContractInfo.deviceStatusCd < 40}"><div class="btn_blk"><a href="javascript:void(0)" onclick="showDialog()">수정</a></div></c:if>
            </div>
        </div>

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
            <span class="txt_top mb10">정보수정을 위해서는 가려진 정보를 해제해야 수정이 가능합니다.<br>비밀번호와 사유를 입력해주세요. </span>
            <div class="inp_pop mb10"><input type="password" name="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <p class="colred mb10" style="display: none" id="passwordNotMatched1">비밀번호를 입력해주세요.</p>
            <div class="inp_pop"><input id="txtReason" maxlength="30" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" style="display: none" id="reasonNotMatched">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="unmask()">해제</a></div>
        </div>
    </div>
    <!-- //가려진 정보 해제 -->
</div>

<div class="wrap_layerpop" id="resendSuccLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>인증번호 발송</h2>
            <div class="btn_close"><button type="button" onclick="$('#resendSuccLayer').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">인증번호가 발송 되었습니다.<br/>[KT GiGA IoT 홈매니저] App을 다운로드 후 인증해주세요.</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#resendSuccLayer').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
</div>

<div class="wrap_layerpop" id="deleteAccountLayer" style="display: none;">
    <div class="layerpop pop1">
        <div class="box_contpop">
            <span class="txt_top txt_titnon">사용자 해제를 할 경우 입력한 정보와 사용자 인증이 모두 삭제 됩니다. 진행 하시겠습니까?</span></span>
            <div class="btn_col2">
                <div class="btn_wht_full fl btn_close2"><button type="button" onclick="$('#deleteAccountLayer').hide();layerPopDimmOff();">취소</button></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="deleteAccount()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- //layer pop -->

<%-- Template ================================================================================ --%>
<script type="text/html" id="contractTemplate">
    <li><a href="${pageContext.request.contextPath}/contracts/\${serialNumber}">\${serialNumber} <span>{{if name == null || name == ''}}-{{else}} \${name} {{/if}}</span></a></li>
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function () {

        <c:if test="${not empty doesNotPasswordMatched and doesNotPasswordMatched}">
        layerOpen('layerPasswordConfirm');
        $('#passwordNotMatched').show();
        </c:if>

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

        /**
         * 저장
         */
        $("#frm").submit(function(){

            layerErrorTextHide();

            var passWord = $('#password').val();
            var txtReason = $('#txtReason').val();

            if(passWord.length == 0){
                $('#passwordNotMatched1').show();
                return false;
            }

            if(txtReason.length == 0){
                $('#reasonNotMatched').show();
                return false;
            }

            $('#passwordform').val( passWord );
            $('#reason').val( txtReason );

        });

    });

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
                    $('#contractList').append('<ul class="clfix">');
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
                serialNumber: ${svcContractInfo.bizContSerial},
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

    /**
     * 비밀번호 입력 layer open
     * @param svcContSerial
     * @param serialNumber
     */
    function showDialog() {
        layerErrorTextHide();
        layerOpen('layerPasswordConfirm');
        $('#password').focus();
    }

    /**
     * 비밀번호 입력 layer close
     */
    function closeDialog() {
        $('#layerPasswordConfirm').hide();
        ;layerPopDimmOff();
        $('#password').val('');
        $('#txtReason').val('');
        layerErrorTextHide();
    }

    /**
     * 취소 클릭
     */
    function cancel() {
        $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}');
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
     * 사용자 해제
     */
    function deleteAccountLayer() {
        layerOpen('deleteAccountLayer');
    }
    function deleteAccount() {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}/unmap",
            type: "PUT",
            data: {},
            success: function (obj) {
                if(obj.errorCd == '00') {
                    $('#deleteAccountLayer').hide();
                    $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /**
     * 인증번호 재발송
     */
    function resend() {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}/send",
            type: "POST",
            data: {},
            success: function (obj) {
                if(obj.errorCd == '00') {
                    layerOpen('resendSuccLayer');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    function unmask() {

        layerErrorTextHide();

        var passWord = $('#password').val();
        var txtReason = $('#txtReason').val();

        if(passWord.length == 0){
            $('#passwordNotMatched1').show();
            return false;
        }

        if(txtReason.length == 0){
            $('#reasonNotMatched').show();
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}/unmask",
            contentType: "application/json; charset=UTF-8",
            type: "POST",
            dataType: "json",
            data: JSON.stringify({"password": passWord, "reason": txtReason}),
            success: function (result) {
                console.log(result);
                if (result.code == '00') {
                    location.reload(true);
                } else if (result.code == '-10') {
                    $('#passwordNotMatched').show();
//                    $('#failLayer .box_contpop .txt_top').html('비밀번호가 일치하지 않습니다.');
//                    $('#failLayer').show();
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

</script>