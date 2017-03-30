<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--<meta name="viewport" content="width=device-width, initial-scale=1.0, target-densitydpi=medium-dpi"--%>>
    <title>KT GIGA B2B 홈매니저	</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/content.css" />
    <tiles:insertAttribute name="jsHeader" />
</head>
<body>

<!-- content -->
<tiles:insertAttribute name="body" />
<!-- //content -->

<!-- provision -->
<tiles:insertAttribute name="provision" />
<!-- //provision -->

</body>
</html>