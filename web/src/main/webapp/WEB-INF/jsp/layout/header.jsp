<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="username" property="principal.username"/>
    <security:authentication var="name" property="principal.name"/>
    <security:authentication var="companyName" property="principal.companyName"/>
</security:authorize>

<header id="header">
    <div class="topmenu">
        <c:choose>
            <c:when test="${empty username}">
                <div><a href="${pageContext.request.contextPath}/login/login">로그인</a></div>
            </c:when>
            <c:otherwise>
        <div><a href="javascript:void(0)" onclick="$('#logoutForm').submit()">로그아웃</a><a href="${pageContext.request.contextPath}/mypage">마이페이지</a><%--<a href="#" id="sessionCountDown">00:00</a>--%></div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="box_gnb">
        <h1><a href="${pageContext.request.contextPath}/">KT</a></h1>
        <h2 id="mobileMenuNm"></h2>
        <button type="button" id="menutoggle" class="btn_gnb">메뉴보기</button>
        <nav id="gnb">
            <div class="m_member">
                <c:if test="${not empty username}">
                <div>
                    <div class="left">
                        <span><img src="${pageContext.request.contextPath}/companyLogo" alt="${companyName} 로고"></span>
                        <strong>${name}<span>님</span></strong>
                    </div>
                    <div class="right"><a href="${pageContext.request.contextPath}/mypage">마이페이지</a></div>
                </div>
                </c:if>
            </div>
            <ul>
            <c:set var="currentUrl" value="${requestScope['javax.servlet.forward.request_uri']}" />
            <c:if test="${not empty username}">

                <c:set var="contractsUrl" value="${pageContext.request.contextPath}/contract" />
                <li<c:if test="${fn:contains(currentUrl, contractsUrl)}"> class="on"</c:if>><a href="${pageContext.request.contextPath}/contracts">사용자관리</a></li>

                <security:authorize access="hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER') or hasRole('ROLE_SUPERVISOR')">
                <c:set var="accountsUrl" value="${pageContext.request.contextPath}/accounts" />
                <c:set var="companyUrl" value="${pageContext.request.contextPath}/company" />
                <li<c:if test="${fn:contains(currentUrl, accountsUrl) or fn:contains(currentUrl, companyUrl)}"> class="on"</c:if>><a href="${pageContext.request.contextPath}/accounts">계정관리</a></li>
                </security:authorize>

                <c:set var="statisticsUrl" value="${pageContext.request.contextPath}/statistics" />
                <li<c:if test="${fn:contains(currentUrl, statisticsUrl)}"> class="on"</c:if>><a href="${pageContext.request.contextPath}/statistics">통계관리</a></li>

                <security:authorize access="hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')">
                <c:set var="historyUrl" value="${pageContext.request.contextPath}/history" />
                <li<c:if test="${fn:contains(currentUrl, historyUrl)}"> class="on"</c:if>><a href="${pageContext.request.contextPath}/history">이력관리</a></li>
                </security:authorize>

                <security:authorize access="hasRole('ROLE_SUPER_MASTER')">
                <c:set var="noticeUrl" value="${pageContext.request.contextPath}/notice" />
                <c:set var="termUrl" value="${pageContext.request.contextPath}/term" />
                <c:set var="bannerUrl" value="${pageContext.request.contextPath}/banner" />
                <li<c:if test="${fn:contains(currentUrl, noticeUrl) or fn:contains(currentUrl, termUrl) or fn:contains(currentUrl, bannerUrl)}"> class="on"</c:if>><a href="${pageContext.request.contextPath}/notice">서비스관리</a></li>
                </security:authorize>

            </c:if>
                <c:set var="serviceUrl" value="${pageContext.request.contextPath}/service" />
                <li class="bg_line ftcolor<c:if test="${fn:contains(currentUrl, serviceUrl)}"> on</c:if>"><a href="${pageContext.request.contextPath}/service/introduce">서비스 소개</a></li>

            <c:if test="${not empty username}">
                <c:set var="boardUrl" value="${pageContext.request.contextPath}/board" />
                <li class="ftcolor<c:if test="${fn:contains(currentUrl, boardUrl)}"> on</c:if>"><a href="${pageContext.request.contextPath}/board/announce">공지사항</a></li>
            </c:if>
            </ul>
            <c:choose>
                <c:when test="${not empty username}">
                    <a href="javascript:void(0)" onclick="$('#logoutForm').submit()" class="btn_logout">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login/login" class="btn_login">로그인</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<form id="logoutForm" method="POST" action="${pageContext.request.contextPath}/logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

