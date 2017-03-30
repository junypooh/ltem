<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@commonService.getSessionTimeOut()" var="sessionTimeOut" />
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--<meta name="viewport" content="width=device-width, initial-scale=1.0, target-densitydpi=medium-dpi">--%>
    <title>KT GIGA B2B 홈매니저</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/c3.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/content.css" />
    <tiles:insertAttribute name="jsHeader" />
    <script type="text/javascript">
        var contextPath = "${pageContext.request.contextPath}";
        var sessionTimeOut = ${sessionTimeOut};
    </script>
</head>
<body>

<div id="skip">
    <ul>
        <li><a href="#container">본문 바로가기</a></li>
        <li><a href="#gnb">주 메뉴 바로가기</a></li>
    </ul>
</div>

    <!-- header -->
    <tiles:insertAttribute name="header" />
    <!-- //header -->

    <!-- content -->
    <tiles:insertAttribute name="body" />
    <!-- //content -->

    <!-- provision -->
    <tiles:insertAttribute name="provision" />
    <!-- //provision -->

    <!-- footer -->
    <tiles:insertAttribute name="footer" />
    <!-- footer -->

</body>
</html>