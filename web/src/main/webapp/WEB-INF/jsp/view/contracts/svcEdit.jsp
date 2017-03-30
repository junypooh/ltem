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
                <strong class="txt_tit">${svcContractInfo.currentBizContract.name}</strong>
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
            <form:form action="${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}/save" method="POST" commandName="svcContractInfo" id="frm">
            <div class="area_inp">
                <dl class="m_area">
                    <dt class="m_hidden"><div><label for="devicePlace">상세주소</label></div></dt>
                    <dd>
                        <div class="inp_txt">
                            <form:input path="devicePlace" placeholder="상세주소" />
                            <%--<p class="red mt10">상세 주소를 입력해주세요.</p>--%>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden"><div><label for="userName">사용자명</label></div></dt>
                    <dd>
                        <div class="inp_txt">
                            <form:input path="userName" cssClass="inp_name" placeholder="사용자명" />
                            <%--<p class="red mt10">사용자명을 입력해주세요.</p>--%>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden"><div><label for="userCtn">휴대폰 번호</label></div></dt>
                    <dd>
                        <div class="inp_txt">
                            <c:choose>
                                <c:when test="${svcContractInfo.isUserMapped}">
                                    <form:input path="userCtn" disabled="true" cssClass="inp_phone" placeholder="휴대폰 번호" onkeyup="autoHypenPhone(this)" maxlength="14" /><%--<span class="btn_blk btn_conf"><a href="#">인증번호 발송</a></span>--%>
                                </c:when>
                                <c:otherwise>
                                    <form:input path="userCtn" cssClass="inp_phone" placeholder="휴대폰 번호" onkeyup="autoHypenPhone(this)" maxlength="14" /><%--<span class="btn_blk btn_conf"><a href="#">인증번호 발송</a></span>--%>
                                </c:otherwise>
                            </c:choose>
                            <%--<p class="red mt10">휴대폰 번호를 입력해주세요.</p>--%>
                        </div>
                    </dd>
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
                            <input type="radio" id="d_chkuse"
                                   <c:if test="${not svcContractInfo.isSuspended}">checked="checked"</c:if>
                                   name="status" value="Y"> <label for="d_chkuse">사용</label>
                            <input type="radio" id="d_chknot"
                                   <c:if test="${svcContractInfo.isSuspended}">checked="checked"</c:if>
                                   class="ml20" name="status" value="N"> <label for="d_chknot">일시정지</label>
                        </div>
                    </dd>
                </dl>
            </div>
            </form:form>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="showLayer()">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="save()">저장</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->
<!-- 취소버튼 클릭시 Layer  -->
<div class="wrap_layerpop" id="cancelLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>안내</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">취소할 경우 입력된 정보가 다 삭제 됩니다.</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="cancel()">확인</a></div>
            </div>
        </div>
    </div>
</div>

<!-- 하단 버튼한개-->
<div class="wrap_layerpop" id="failLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>실패</h2>
            <div class="btn_close">
                <button type="button" onclick="closeLayer()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10"></span>
            <div class="btn_blk_full"><a id="fail-confirm" href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
        </div>
    </div>
</div>


<%-- Template ================================================================================ --%>
<script type="text/html" id="contractTemplate">
    <li><a href="${pageContext.request.contextPath}/contracts/\${serialNumber}">\${serialNumber} <span>{{if name == null || name == ''}}-{{else}} \${name} {{/if}}</span></a></li>
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function () {

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

            $('.area_inp .red').hide();
            if (!$("input[name='devicePlace']").val() && $("input[name='userCtn']").val().length > 0) {
                $("input[name='devicePlace']").after('<p class="red mt10">상세 주소를 입력해주세요.</p>');
                return false;
            }

            if (!$("input[name='userName']").val() && $("input[name='userCtn']").val().length > 0) {
                $("input[name='userName']").after('<p class="red mt10">사용자명을 입력해주세요.</p>');
                return false;
            }

            /*
            if (!$("input[name='userCtn']").val()) {
                $("input[name='userCtn']").after('<p class="red mt10">휴대폰 번호를 입력해주세요.</p>');
                return false;
            }
             */

        });

        <c:if test="${not empty errMsg}">
        alert('${errMsg}');
        </c:if>
        <c:if test="${not empty svcContractInfo.userCtn}">
        $("input[name='userCtn']").attr("readonly", "readonly");
        </c:if>

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

    /** 저장 */
    function save() {
        closeLayer();

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}",
            contentType: "application/json; charset=UTF-8",
            type: "PUT",
            dataType: "json",
            data: JSON.stringify($('#frm').serializeJSON()),
            success: function (result) {
                console.log(result);
                if (result.code == '00') {
                    $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}');
                } else if (result.code == '-10') {
                    $('#failLayer .box_contpop .txt_top').html('현재 계약 상태를 변경할 수 없는 상태입니다.');
                    layerOpen('failLayer');
                    $('#fail-confirm').click(function(){
                        $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}');
                    });
                } else if (result.code == '-20') {
                    $('#failLayer .box_contpop .txt_top').html('서비스 연동에 실패하였습니다.<br/>잠시 후 다시 시도해주세요.');
                    layerOpen('failLayer');
                    $('#fail-confirm').click(function(){
                        $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}/${svcContractInfo.svcContSerial}');
                    });
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /**
     * 취소 클릭
     */
    function cancel() {
        $(location).attr('href', '${pageContext.request.contextPath}/contracts/${svcContractInfo.bizContSerial}');
    }

    /** 레이어팝업 열기 */
    function showLayer() {
        layerOpen('cancelLayer');
    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }

</script>