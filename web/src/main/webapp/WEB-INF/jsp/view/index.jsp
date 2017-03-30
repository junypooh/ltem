<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="companyCd" property="principal.companyCd"/>
    <security:authentication var="companyName" property="principal.companyName"/>
    <security:authentication var="authorityCd" property="principal.authorityCd"/>
    <security:authentication var="authorityName" property="principal.authorityName"/>
    <security:authentication var="name" property="principal.name"/>
</security:authorize>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.css" />
<div id="container">
    <div id="content">
        <div class="main_top">
            <div class="left">
                <span>${companyName} / ${authorityName}</span>
                <strong>${name}<span>님 환영합니다</span></strong>
            </div>
            <c:if test="${not empty companyInfo.fileSeq}">
            <div class="right"><span><img src="${pageContext.request.contextPath}/fileDown?type=LOGO&fileSeq=${companyInfo.fileSeq}" alt="${companyName} 로고"></span></div>
            </c:if>
        </div>

        <div class="wrap_content">

            <!-- 왼쪽 아이콘 리스트 -->
            <section class="box_data">
                <ul>
                    <li class="type1">
                        <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg1">
                            <em>${totContractCount}</em>
                            <strong>계약 총 건수</strong>
                        </div></a>
                    </li>
                    <c:choose>
                        <c:when test="${authorityCd eq 10}">
                            <!-- 슈퍼마스터// -->
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/company"><div class="bg bg8">
                                    <em>${registCompanyCount.NULL}/<span>${registCompanyCount.ALL}</span></em>
                                    <strong>기업 등록 현황</strong>
                                </div></a>
                            </li>
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/accounts"><div class="bg bg9">
                                    <em>${registAccountCount.ALL}</em>
                                    <strong>계정 발급 현황</strong>
                                </div></a>
                            </li>
                            <!-- //슈퍼마스터 -->
                        </c:when>
                        <c:when test="${authorityCd eq 20}">
                            <!-- 마스터// -->
                            <li class="type2">
                                <a href="${pageContext.request.contextPath}/accounts"><div class="bg">
                                    <div class="dv1 bg2">
                                        <em>${registAccountCount.SUPERVISOR}</em>
                                    </div>
                                    <div class="dv2 bg3">
                                        <em>${registAccountCount.STAFF}</em>
                                    </div>
                                    <strong>계정 발급 현황</strong>
                                </div></a>
                            </li>
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg4">
                                    <em>${notInChargeContractCount.NULL}/<span>${notInChargeContractCount.ALL}</span></em>
                                    <strong>담당자 미등록 계약 건</strong>
                                </div></a>
                            </li>
                            <!-- //마스터 -->
                        </c:when>
                        <c:when test="${authorityCd eq 30}">
                            <!-- 수퍼바이저// -->
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/accounts"><div class="bg bg3">
                                    <em>${registAccountCount.STAFF}</em>
                                    <strong>계정 발급 현황</strong>
                                </div></a>
                            </li>
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg4">
                                    <em>${notInChargeContractCount.NULL}/<span>${notInChargeContractCount.ALL}</span></em>
                                    <strong>담당자 미등록 계약 건</strong>
                                </div></a>
                            </li>
                            <!-- //수퍼바이저 -->
                        </c:when>
                        <c:when test="${authorityCd eq 40}">
                            <!-- 스탭// -->
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg3">
                                    <em>${contrantInChargeStaffCount}</em>
                                    <strong>계약 담당자 현황</strong>
                                </div></a>
                            </li>
                            <li class="type1">
                                <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg4">
                                    <em>${notInChargeContractCount.NULL}/<span>${notInChargeContractCount.ALL}</span></em>
                                    <strong>담당자 미등록 계약 건</strong>
                                </div></a>
                            </li>
                            <!-- //스탭 -->
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                    </c:choose>
                    <li class="type1">
                        <a href="${pageContext.request.contextPath}/contracts"><div class="bg bg5">
                            <em>${userAddrRegistCount.NULL}/<span>${userAddrRegistCount.ALL}</span></em>
                            <strong>사용자 주소 미등록 건</strong>
                        </div></a>
                    </li>
                    <li class="type1">
                        <div class="bg bg6">
                            <em>${hubCount}</em>
                            <strong>현재 허브 사용자</strong>
                        </div>
                    </li>
                    <li class="type1">
                        <div class="bg bg7">
                            <em>${hubStatusCount.ON}/<span>${hubStatusCount.ALL}</span></em>
                            <strong>허브 연결 상태</strong>
                        </div>
                    </li>
                </ul>
            </section>
            <!-- //왼쪽 아이콘 리스트 -->

            <!-- 오른쪽 게시물 리스트, 슬라이드 -->
            <div class="wrap_mrg">

                <div class="list_num">
                    <div>
                        <h3><a href="${pageContext.request.contextPath}/statistics">허용량 초과 계약  <em class="colred">${fn:length(avgCapacityOverInfo)}</em> 건</a></h3>
                        <!-- table list -->
                        <div class="box_tbllist">
                            <table summary="Biz계약번호, 데이터허용량, 전월데이터 평균사용량">
                                <caption>허용량 초과 계약 리스트</caption>
                                <thead>
                                <tr>
                                    <th scope="col" class="m_list">Biz계약번호</th>
                                    <c:if test="${authorityCd eq 10}"><th scope="col" class="m_hidden">회사명</th></c:if>
                                    <th scope="col" class="m_list">데이터허용량</th>
                                    <th scope="col" class="m_list">전월데이터<br>사용량</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${avgCapacityOverInfo}" var="capacityList">
                                    <tr>
                                        <td>${capacityList[0]}</td>
                                        <c:if test="${authorityCd eq 10}"><td class="m_hidden">${capacityList[6]}</td></c:if>
                                        <td>${capacityList[4]}MB</td>
                                        <td class="colred">${capacityList[5]}MB</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <c:if test="${empty avgCapacityOverInfo}">
                            <!-- 데이터 없을경우 -->
                            <div class="nondata bdr2p">허용량 초과 계약이 없습니다.</div>
                            </c:if>
                        </div>
                        <!-- table list -->
                    </div>
                </div>

                <div class="box_btm">
                    <div class="list_ntc">
                        <div>
                            <h3>공지사항</h3>
                            <ul>
                            <c:forEach var="notice" items="${notices}">
                                <li>
                                    <span><a href="javascript:void(0)" onclick="$(location).attr('href','${pageContext.request.contextPath}/board/announce/${notice.noticeSeq}')">${notice.title}</a></span>
                                    <span class="date"><javatime:format value="${notice.created}" pattern="yyyy/MM/dd"/></span>
                                </li>
                            </c:forEach>
                            <c:if test="${empty notices}">
                                <li class="datanon">
                                    <span>공지사항이 없습니다.</span>
                                </li>
                            </c:if>
                            </ul>
                        </div>
                    </div>

                    <div class="area_slide">
                        <div class="swiper-container">
                            <div class="swiper-wrapper">
                                <c:forEach items="${banners}" var="banner">
                                    <div class="swiper-slide"><a href="${banner.link}" <c:if test="${banner.newWindow}">target="_blank"</c:if>><img src="${pageContext.request.contextPath}/fileDown?type=SUBBANNER&fileSeq=${banner.fileSeq}" alt=""></a></div>
                                </c:forEach>
                                <c:if test="${empty banners}">
                                    <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_main_sample.jpg" alt=""></a></div>
                                    <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_main_sample.jpg" alt=""></a></div>
                                    <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_main_sample.jpg" alt=""></a></div>
                                </c:if>
                            </div>
                            <!-- Add Pagination -->
                            <div class="swiper-pagination"></div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- //오른쪽 게시물 리스트, 슬라이드 -->

        </div>
    </div>
</div>

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