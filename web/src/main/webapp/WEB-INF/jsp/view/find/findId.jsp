<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="container">
    <div class="wrap_member">
        <div>
            <section class="box_login">
                <div class="h2_web">
                    <h2>아이디찾기</h2>
                    <p>휴대폰 번호를 인증해주세요.</p>
                </div>
                <form:form action="${pageContext.request.contextPath}/find/findId" method="POST" commandName="findAccountInfo" id="frm">
                    <input type="hidden" name="mgrSeq">
                <div class="area_inp">
                    <ul>
                        <li>
                            <div class="inp_mem" id="divName">
                                <form:input path="name" placeholder="이름" />
                            </div>
                            <form:errors path="name" cssClass="txt_des" />
                            <%--<span class="txt_des">이름을 입력해주세요.</span>--%>
                        </li>
                        <li>
                            <div class="inp_mem numchk2"><form:input path="telephone" title="휴대폰 번호"
                                                                     placeholder="휴대폰 번호"
                                                                     onkeyup="autoHypenPhone(this)" maxlength="14"/></div>
                            <div class="btn_chk">
                                <div class="btn_send"><a href="javascript:void(0)" onclick="sendAuthentication(this)"
                                                         id="btnSend">인증</a></div>
                                <div class="btn_resend" style="display: none"><a href="javascript:void(0)"
                                                                                 onclick="sendAuthentication(this)"
                                                                                 id="btnReSend">재전송</a></div>
                            </div>
                            <form:errors path="telephone" cssClass="txt_des" />
                            <%--<span class="txt_des">휴대폰 번호를 입력해주세요.</span>--%>
                        </li>
                        <li>
                            <div class="inp_mem numchk2">
                                <form:input path="authenticationNumber" title="인증번호" placeholder="인증번호"
                                            cssClass="inp_numchk"/>
                                <span class="timechk" id="countdown"></span>
                            </div>
                            <div class="btn_phok"><a href="javascript:void(0)" onclick="confirm()">확인</a></div>
                            <form:errors path="authenticationNumber" cssClass="txt_des" />
                                <%--<span class="txt_des">인증번호를 입력해주세요.</span>--%>
                        </li>
                    </ul>
                    <p class="txt_nor">인증번호가 오지 않은 경우 인증번호를 다시 눌러 새로운 인증번호를 입력해주세요.</p>
                </div>
                <a href="javascript:void(0)" onclick="$('#frm').submit();" class="btn_login">확인</a>
                    <%--<p class="colred mt10">입력한 정보와 일치하는 아이디가 없습니다. 다시 확인해주시기 바랍니다.</p>--%>
                </form:form>
            </section>
        </div>
    </div>
</div>

<script type="text/javascript">

    var matchAuth = false;
    var interval;

    $(document).ready(function(){

        <c:if test="${not empty error and error}">
        $(".btn_login").after('<p class="colred mt10">입력한 정보와 일치하는 아이디가 없습니다. 다시 확인해주시기 바랍니다.</p>');
        </c:if>

        $("#frm").submit(function(){

            if(!validate()) {
                return false;
            }

            if (!$("input[name='authenticationNumber']").val()) {
                $(".btn_phok").after('<span class="txt_des">인증번호를 입력해주세요.</span>');
                return false;
            }

            if (!matchAuth) {
                alert('휴대폰 인증을 해주세요.');
                return false;
            }

        });

    });

    /**
     * form 데이터 조합 및 validate 체크
     */
    function validate() {

        $(".txt_des").remove();
        $(".colred").remove();
        if(!$("input[name='name']").val()) {
            $("#divName").after('<span class="txt_des">이름을 입력해주세요.</span>');
            return false;
        }

        if(!$("input[name='telephone']").val()) {
            $(".btn_chk").after('<span class="txt_des">휴대폰 번호를 입력해주세요.</span>');
            return false;
        }

        return true;
    }

    /**
     * 인증번호 발송
     */
    function sendAuthentication(obj) {

        if(!validate()) {
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/find/sendAuthentication",
            type: "POST",
            data : $('#frm').serialize(),
            success: function(rtnData) {

                console.log(rtnData);

                if(rtnData.errorCd == 0) {
                    $(".btn_send").hide();
                    $(".btn_resend").show();
                    $("#btnReSend").attr('onclick', 'sendAuthentication(this)');

                    if('btnReSend' == $(obj).attr('id')) {
                        $(".btn_chk").after('<span class="txt_des">인증번호가 전송 되었습니다.</span>');
                    }
                    interval = countdown( "countdown", 180, interval );

                } else if(rtnData.errorCd == -2) {
                    $(".btn_login").after('<p class="colred mt10">' + rtnData.msg + '</p>');
                } else if(rtnData.errorCd == -3) {
                    $(".btn_login").after('<p class="colred mt10">' + rtnData.msg + '</p>');
                } else {
                    alert(rtnData.msg);
                }
            },
            error: function (e) {
                alert('인증번호 발송에 실패하였습니다.');
            }
        });
    }

    /**
     * 인증 번호 확인
     */
    function confirm() {

        if(!validate()) {
            return false;
        }

        if(!$("input[name='authenticationNumber']").val()) {
            $(".btn_phok").after('<span class="txt_des">인증번호를 입력해주세요.</span>');
            return false;
        }

        if($('#countdown').text() == '00:00') {
            $(".btn_phok").after('<span class="txt_des">인증시간이 초과되었습니다. 인증번호를 다시 전송해주세요.</span>');
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/find/doesAuthNumberMatch",
            type: "POST",
            data : $('#frm').serialize(),
            success: function(rtnData) {

                if(rtnData.errorCd == 0) {
                    matchAuth = true;
                    clearInterval(interval);
                    $('#countdown').text('');
                    $("input[name='mgrSeq']").val(rtnData.mgrSeq);
                }

                $(".btn_phok").after('<span class="txt_des">' + rtnData.msg + '</span>');
            },
            error: function (e) {
                alert('인증번호 발송에 실패하였습니다.');
            }
        });
    }
</script>