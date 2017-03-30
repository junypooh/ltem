<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="container">
    <div class="wrap_member">
        <div>
            <section class="box_login">
                <div class="h2_web">
                    <h2>비밀번호 변경</h2>
                    <p>변경할 새로운 비밀번호로 재설정 해주세요.</p>
                </div>
                <form:form action="${pageContext.request.contextPath}/find/pwdChangeProcess" method="POST"
                           commandName="findAccountInfo" id="frm">
                    <form:hidden path="id" />
                    <form:hidden path="mgrSeq" />
                    <form:hidden path="name"/>
                    <form:hidden path="telephone"/>
                    <div class="area_inp">
                        <ul>
                            <li>
                                <div class="inp_mem"><form:password path="newPasswd" title="새로운 비밀번호"
                                                                    placeholder="새로운 비밀번호"/></div>
                                <form:errors path="newPasswd" cssClass="txt_des"/>
                                <%--<span class="txt_des">비밀번호를 입력해주세요.</span>--%>
                            </li>
                            <li>
                                <div class="inp_mem"><form:password path="newPasswdConfirm" title="비밀번호 재확인"
                                                                    placeholder="비밀번호 재확인"/></div>
                                <form:errors path="newPasswdConfirm" cssClass="txt_des"/>
                                <%--<span class="txt_des">비밀번호가 일치하지 않습니다. 다시 입력해주세요</span>--%>
                            </li>
                        </ul>
                        <p class="txt_nor">영문, 숫자, 특수 문자 포함 6~12자리</p>
                    </div>
                    <a href="javascript:void(0)" onclick="$('#frm').submit();" class="btn_login">확인</a>
                </form:form>
            </section>
        </div>
    </div>
</div>