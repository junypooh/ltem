<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="container">
    <div class="wrap_member">
        <div>
            <section class="box_login">
                <div class="h2_web">
                    <h2>아이디찾기</h2>
                    <p>입력하신 정보로 찾은 아이디입니다.<br>개인정보 보호를 위해 아이디 일부는 *로 표기됩니다</p>
                </div>
                <div class="id_view">
                    <span>${userId}</span>
                </div>
                <p class="txt_nor1 txtpd align-c">아이디를 모르시는 경우 관리자(KT@kt.com) 으로 메일을 주시면 확인 후 연락 드리겠습니다.</p>
                <div class="btn_list">
                    <div class="btn_left"><a href="${pageContext.request.contextPath}/find/findPwd">비밀번호 찾기</a></div><div class="btn_right"><a href="${pageContext.request.contextPath}/login/login">로그인하기</a></div>
                </div>
            </section>
        </div>
    </div>
</div>