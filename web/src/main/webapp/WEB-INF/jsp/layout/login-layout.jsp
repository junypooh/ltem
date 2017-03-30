<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--<meta name="viewport" content="width=device-width, initial-scale=1.0, target-densitydpi=medium-dpi">--%>
    <title>KT GIGA B2B 홈매니저</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/login.css" />
    <tiles:insertAttribute name="jsHeader" />
</head>
<body>

<div id="skip">
    <ul>
        <li><a href="#container">본문 바로가기</a></li>
        <li><a href="#gnb">주 메뉴 바로가기</a></li>
    </ul>
</div>

<!-- header -->
<header id="header">
    <div class="topmenu">
    </div>
    <div class="box_gnb">
        <h1><a href="${pageContext.request.contextPath}/">KT</a></h1>
        <h2>로그인</h2>
        <!-- gnb -->
        <button type="button" id="menutoggle" class="btn_gnb">메뉴보기</button>
        <nav id="gnb">
            <div class="m_member">
            </div>
            <ul>
                <li class="ftcolor"><a href="${pageContext.request.contextPath}/service/introduce">서비스 소개</a></li>
                <%--<li class="ftcolor"><a href="#">고객센터</a></li>--%>
            </ul>
            <a href="${pageContext.request.contextPath}/login/login" class="btn_login">로그인</a>
        </nav>
        <!-- //gnb -->
    </div>
</header>
<!-- //header -->

<!-- content -->
<tiles:insertAttribute name="body" />
<!-- //content -->

<!-- footer -->
<tiles:insertAttribute name="footer" />
<!-- footer -->

<!-- provision -->
<tiles:insertAttribute name="provision" />
<!-- //provision -->

<!-- Initialize Swiper -->
<script type="text/javascript">
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        nextButton: '.arrow-right',
        prevButton: '.arrow-left',
        autoplay: 2500,
        autoplayDisableOnInteraction: false
    });
</script>

</body>
</html>