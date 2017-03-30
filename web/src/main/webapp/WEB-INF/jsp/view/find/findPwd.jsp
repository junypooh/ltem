<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="container">
    <div class="wrap_member">
        <div>
            <section class="box_login">
                <div class="h2_web">
                    <h2>비밀번호 찾기</h2>
                    <p>비밀번호를 찾고자 하는 아이디를 입력해주세요.</p>
                </div>
                <form:form action="${pageContext.request.contextPath}/find/findPwd" method="POST" commandName="findAccountInfo" id="frm">
                <div class="area_inp">
                    <ul>
                        <li>
                            <div class="inp_mem"><form:input path="id" title="아이디" placeholder="아이디"
                                                             value="${findAccountInfo.id}"/></div>
                            <%--<span class="txt_des">아이디를 입력해주세요.</span>--%>
                            <form:errors path="id" cssClass="txt_des" />
                        </li>
                    </ul>
                    <p class="txt_nor txtpd align-c">아이디를 모르시는 경우 관리자(KT@kt.com) 으로 메일을 주시면 확인 후 연락 드리겠습니다.</p>
                </div>
                </form:form>
                <a href="javascript:void(0)" onclick="$('#frm').submit();" class="btn_login">확인</a>
            </section>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function(){

        $("input[name='id']").focus();

        $("#frm").submit(function(){

            if(!$("input[name='id']").val()) {
                $(".inp_mem").after('<span class="txt_des">아이디를 입력해주세요.</span>');
                return false;
            }

        });
    });
</script>