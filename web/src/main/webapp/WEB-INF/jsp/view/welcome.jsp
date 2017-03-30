<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authentication var="b2BManager" property="principal" />

<security:authorize access="isAuthenticated()">
    <security:authentication var="id" property="principal.username" />
    <security:authentication var="name" property="principal.name" />
</security:authorize>
${b2BManager.name} (${b2BManager.username}) 님! 안녕하세요.
<br/>
${message} | <a href="#" onclick="$('#logoutForm').submit()">Logout</a>
<br/><br/>
<a href="${pageContext.request.contextPath}/staff">staff</a>
<br/>
<a href="${pageContext.request.contextPath}/supervisor">supervisor</a>
<br/>
<a href="${pageContext.request.contextPath}/master">master</a>
<%--
<ul class="nav navbar-nav navbar-right">
    <!-- 회원 권한이 없을 때 -->
    <security:authorize ifNotGranted="ROLE_USER">
        <li><a href="/user/sign_up">회원가입</a></li>
        <li><a href="/user/login">로그인</a></li>
    </security:authorize>

    <!-- 회원 권한이 있을 때 -->
    <security:authorize ifAnyGranted="ROLE_USER">
        <li><a href="/user/logout">로그아웃</a></li>
    </security:authorize>

    <!-- 여러 권한 체크 -->
    <security:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
        <li><a href="/user/edit">정보수정</a></li>
    </security:authorize>
</ul>--%>
