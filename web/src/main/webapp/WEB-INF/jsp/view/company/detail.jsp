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
            <h2 class="h2_f">계정관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 계정관리</div>
        </div>

        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/accounts">계정</a></li>
                    <li class="on"><a href="${pageContext.request.contextPath}/company">기업</a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 정보</h3>
        </div>

        <form:form action="${pageContext.request.contextPath}/company/${companyInfo.code}" method="POST" commandName="companyInfo" id="frm">
            <input type="hidden" name="password" id="passwordform" />
            <input type="hidden" name="reason" id="reason" />
        <div class="box_list2col">
            <div class="area_inp">
                <dl>
                    <dt class="bg_line"><div>기업코드</div></dt>
                    <dd><div>${companyInfo.code}</div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>기업명</div></dt>
                    <dd><div>${companyInfo.name}</div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>로고</div></dt>
                    <dd><div class="logo_comp"><button type="button" onclick="showLogo(${companyInfo.fileSeq})">로고 확인</button></div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>대표번호</div></dt>
                    <dd><div>${companyInfo.telephone}&nbsp;</div></dd>
                </dl>
                <dl>
                    <dt class="bg_line"><div>담당자</div></dt>
                    <dd><div>${companyInfo.manager.name}<c:if test="${not empty companyInfo.manager.username}">(${companyInfo.manager.username})</c:if>&nbsp;</div></dd>
                </dl>
                <%--<dl>
                    <dt class="bg_line"><div>상태</div></dt>
                    <dd><div>사용(?)</div></dd>
                </dl>--%>
                <dl>
                    <dt class="bg_line"><div>변경일</div></dt>
                    <dd><div><javatime:format value="${companyInfo.modified}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                </dl>
            </div>
        </div>
        </form:form>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="cancel()">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="showDialog()">수정</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<!-- layer pop -->
<div class="wrap_layerpop" id="layerLogo" style="display: none;">
    <!-- 로고 -->
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>로고</h2>
            <div class="btn_close">
                <button type="button">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <div class="view_logo"><img src="" id="logoImg"></div>
            <div class="view_logo_non"><span>로고이미지</span></div><!-- 로고 없을경우 -->
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerLogo').hide(); layerPopDimmOff()">확인</a></div>
        </div>
    </div>
    <!-- //로고 -->
</div>
<!-- //layer pop -->

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
            <div class="inp_pop"><input id="txtReason" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" style="display: none" id="reasonNotMatched">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff()">해제</a></div>
        </div>
    </div>
    <!-- //가려진 정보 해제 -->
</div>
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function () {

        <c:if test="${not empty doesNotPasswordMatched and doesNotPasswordMatched}">
        layerOpen('layerPasswordConfirm');
        $('#passwordNotMatched').show();
        </c:if>

        /**
         * 저장
         */
        $("#frm").submit(function(){

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

            $('#passwordform').val($('#password').val());
            $('#reason').val($('#txtReason').val());
        });

    });

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
        layerPopDimmOff();
        layerErrorTextHide();
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
     * 로고 미리보기
     * @param fileSeq
     */
    function showLogo(fileSeq) {
        if (fileSeq != undefined) {
            $('#logoImg').attr('src', '${pageContext.request.contextPath}/fileDown?type=LOGO&fileSeq=' + fileSeq);
            $('.view_logo').show();
            $('.view_logo_non').hide();
        } else {
            $('.view_logo').hide();
            $('.view_logo_non').show();
        }

        layerOpen('layerLogo');
    }

    /**
     * 취소 클릭
     */
    function cancel() {
        $(location).attr('href', '${pageContext.request.contextPath}/company');
    }

</script>