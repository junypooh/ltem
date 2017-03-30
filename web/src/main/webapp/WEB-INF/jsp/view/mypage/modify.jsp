<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="authorityCode" property="principal.authorityCd"/>
</security:authorize>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">마이페이지</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 마이페이지</div>
        </div>

        <div class="box_list2col">
            <form:form action="${pageContext.request.contextPath}/mypage/save" method="POST" commandName="myPageInfo" id="frm">
                <div class="area_inp">
                    <dl>
                        <form:hidden path="id"/>
                        <dt class="bg_line"><div>아이디</div></dt>
                        <dd><div>${myPageInfo.id}</div></dd>
                    </dl>
                    <dl class="">
                        <dt class="bg_line tit2"><div><label for="passwd">비밀번호 변경</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp"><form:password path="passwd" placeholder="현재 비밀번호"/></span>
                                <div class="txt_vi" id="passwdAlert">
                                    <form:errors path="passwd" cssClass="txt_des tx_mb15" />
                                    <%--<p class="txt_des tx_mb15">비밀번호를 입력해주세요.</p>--%>
                                </div>

                                <span class="inp_sp"><form:password path="newPasswd" placeholder="새로운 비밀번호"/></span>
                                <span class="file_des">영문, 숫자, 특수 문자 포함 6~12자리</span>
                                <div class="txt_vi" id="newPasswdAlert">
                                    <%--<p class="txt_des tx_mb15">비밀번호를 입력해주세요.</p>--%>
                                    <form:errors path="newPasswd" cssClass="txt_des tx_mb15" />
                                </div>
                                <span class="inp_sp"><form:password path="newPasswdConfirm" placeholder="새로운 비밀번호 확인"/></span>
                                <div class="txt_vi" id="newPasswdConfirmAlert">
                                    <%--<p class="txt_des">새로운 비밀번호를 입력해주세요.</p>--%>
                                    <form:errors path="newPasswdConfirm" cssClass="txt_des tx_mb15" />
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <form:hidden path="name"/>
                        <dt class="bg_line"><div>이름</div></dt>
                        <dd><div>${myPageInfo.name}</div></dd>
                    </dl>
                    <dl>
                        <form:hidden path="companyName"/>
                        <dt class="bg_line"><div>기업명</div></dt>
                        <dd><div>${myPageInfo.companyName}</div></dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="email">이메일</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <input type="hidden" id="email" name="email">
                                <input type="text" id="email1" name="email1" class="d_email" placeholder="${fn:substring(myPageInfo.email,  0, fn:indexOf(myPageInfo.email, "@"))}" value="${fn:substring(myPageInfo.email,  0, fn:indexOf(myPageInfo.email, "@"))}"> @
                                <input type="text" id="email2" name="email2" class="emailbak" placeholder="${fn:substring(myPageInfo.email, fn:indexOf(myPageInfo.email, "@")+1 , -1)}" value="${fn:substring(myPageInfo.email, fn:indexOf(myPageInfo.email, "@")+1 , -1)}">
                                <div class="txt_vi" id="emailAlert">
                                    <%--<p class="txt_des">이메일을 입력해주세요.</p>--%>
                                    <form:errors path="email" cssClass="txt_des tx_mb15" />
                                </div>
                            </div>
                        </dd>
                    </dl>

                    <dl class="m_area_h">
                        <dt class="bg_line"><div><label for="telephone">휴대폰번호</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp2"><span class="txt_num telephone">${myPageInfo.telephone}</span><span class="btn_blk btn_conf_aw"><button type="button" onclick="telephoneChange()">변경</button></span></span>
                                <div class="box_inphid">
                                    <div class="inp_txt3">
                                        <form:hidden path="telephone" />
                                        <span class="inp_sp"><input type="text" name="tempTelephone" id="tempTelephone" placeholder="휴대폰번호" onkeyup="autoHypenPhone(this)" maxlength="14"></span>
                                        <span class="btn_blk btn_conf btn_send"><a href="javascript:void(0)" onclick="sendAuthentication()">인증</a></span><span class="btn_blk btn_conf btn_resend" style="display: none"><a href="javascript:void(0)" onclick="sendAuthentication()" id="btnReSend">재전송</a></span>
                                        <div class="txt_vi" id="telephoneAlert">
                                            <form:errors path="telephone" cssClass="txt_des tx_mb15" />
                                            <%--<p class="txt_des tx_mb15">휴대폰번호를 입력해주세요.</p>--%>
                                        </div>
                                    </div>
                                    <div class="inp_txt3">
                                        <div class="inp_sp numchk2">
                                            <input type="text" name="authenticationNumber" id="authenticationNumber" class="inp_numchk" placeholder="인증번호">
                                            <span class="timechk" id="countdown"></span>
                                        </div>
                                        <span class="btn_blk btn_conf"><a href="javascript:void(0)" onclick="confirm()">확인</a></span>
                                        <div class="txt_vi" id="authenticationNumberAlert">
                                            <%--<p class="txt_des">인증번호를 입력해 주세요.</p>--%>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </dd>
                    </dl>
                </div>
            </form:form>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="history.back(-1)">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit()">저장</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<script type="text/javascript">

    var interval;

    $(document).ready(function () {

        // 저장
        $("#frm").submit(function(){
            $('.txt_des').remove();

            // 비밀번호 확인
            if (!($.trim($('#passwd').val()) == '' && $.trim($('#newPasswd').val()) == '' && $.trim($('#newPasswdConfirm').val()) == '')) {
                if(!($.trim($('#passwd').val()) != '' && $.trim($('#newPasswd').val()) != '' && $.trim($('#newPasswdConfirm').val()) != '')) {
                    if($.trim($('#passwd').val()) == '') {
                        $('#passwdAlert').append('<p class="txt_des tx_mb15">비밀번호를 입력해주세요.</p>');
                        return false;
                    }
                    if($.trim($('#newPasswd').val()) == '') {
                        $('#newPasswdAlert').append('<p class="txt_des tx_mb15">새로운 비밀번호를 입력해주세요.</p>');
                        return false;
                    }
                    if($.trim($('#newPasswdConfirm').val()) == '') {
                        $('#newPasswdConfirmAlert').append('<p class="txt_des tx_mb15">새로운 비밀번호 확인을 입력해주세요.</p>');
                        return false;
                    }
                } else {
                    var regType =/^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{6,20}$/;
                    if(!regType.test($.trim($('#newPasswd').val()))) {
                        $('#newPasswdAlert').append('<p class="txt_des tx_mb15">영문, 숫자, 특수 문자 포함 6~12자리로 설정해주세요.</p>');
                        return false;
                    }
                }
            }

            // 이메일 확인
            if($.trim($('#email1').val()) == '' || $.trim($('#email2').val()) == '') {
                $('#emailAlert').append('<p class="txt_des tx_mb15">이메일을 입력해주세요.</p>');
                return false;
            } else {
                $('#email').val($('#email1').val() + '@' + $('#email2').val());
            }


            // 인증번호 전송후 인증번호 입력안함
            if($('#countdown').text().length > 0 && $.trim($('#authenticationNumber').val()) == '') {
                $("#authenticationNumberAlert").after('<span class="txt_des">인증번호를 입력해주세요.</span>');
                return false;
            }

        });

    });

    /**
     * 휴대폰번호 변경 버튼 클릭
     */
    function telephoneChange() {
        $('.box_inphid').toggleClass('on');
    }

    /**
     * 인증번호 발송
     */
    function sendAuthentication() {
        $('.txt_des').remove();
        if($.trim($('#tempTelephone').val()) == '') {
            $('#telephoneAlert').append('<p class="txt_des tx_mb15">휴대폰번호를 입력해주세요.</p>');
            return false;
        }


        $.ajax({
            url: "${pageContext.request.contextPath}/mypage/sendAuthentication",
            type: "POST",
            data : $('#frm').serialize(),
            success: function(rtnData) {
                if(rtnData.errorCd == 0) {
                    $('.txt_des').remove();
                    $(".btn_send").hide();
                    $(".btn_resend").show();
                    $('#telephoneAlert').append('<p class="txt_des tx_mb15">인증번호가 전송 되었습니다.</p>');
                    $('#authenticationNumber').val('');
                    interval = countdown( "countdown", 180, interval );
                } else {
                    $('.txt_des').remove();
                    $('#telephoneAlert').append('<p class="txt_des tx_mb15">인증번호 발송에 실패하였습니다.</p>');
                }

            },
            error: function (e) {
                alert('인증번호 발송에 실패하였습니다.');
            }
        });

    }

    /**
     * 인증번호 확인
     */
    function confirm() {

        console.log($('#countdown').text().length);


        $('.txt_des').remove();
        if($.trim($('#authenticationNumber').val()) == '') {
            $('#authenticationNumberAlert').append('<p class="txt_des">인증번호를 입력해주세요.</p>');
            return false;
        }

        if($('#countdown').text() == '00:00') {
            $("#authenticationNumberAlert").after('<span class="txt_des">인증시간이 초과되었습니다. 인증번호를 다시 전송해주세요.</span>');
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/mypage/doesAuthNumberMatch",
            type: "POST",
            data : $('#frm').serialize(),
            success: function(rtnData) {
                $("#authenticationNumberAlert").after('<span class="txt_des">' + rtnData.msg + '</span>');

                if(rtnData.errorCd == 0) {
                    matchAuth = true;
                    clearInterval(interval);
                    $('#countdown').text('');

                    $('#telephone').val($('#tempTelephone').val());
                    $('.telephone').text($('#tempTelephone').val());
                    $('.box_inphid').removeClass('on');

                    // 휴대폰번호 변경 폼 초기화
                    $('.txt_des').remove();
                    $(".btn_send").show();
                    $(".btn_resend").hide();
                    $('#authenticationNumber').val('');
                    $('#tempTelephone').val('');

                }

            },
            error: function (e) {
                alert('인증번호 발송에 실패하였습니다.');
            }
        });



    }
</script>
