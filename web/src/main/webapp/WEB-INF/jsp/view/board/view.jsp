<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">공지사항</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 공지사항</div>
        </div>

        <div class="box_noticeV">
            <div class="noticeVTitleWrap">
                <div class="noticeVTitle">
                    ${noticeInfo.notice.title}
                </div>
                <div class="noticeVDate">
                    <javatime:format value="${noticeInfo.notice.created}" pattern="yyyy.MM.dd"/>
                </div>
            </div>
            <div class="noticeVContents">
                ${fn:replace(noticeInfo.notice.contents, crcn, '<br/>')}
            </div>
            <div class="listPN">
                <dl>
                    <dt class="prev m_hidden"><div>이전글</div></dt>
                    <c:choose>
                        <c:when test="${not empty noticeInfo.before}"><dd><a href="javascript:void(0)" onclick="$(location).attr('href','${pageContext.request.contextPath}/board/announce/${noticeInfo.before.noticeSeq}')"><div>${noticeInfo.before.title}</div></a></dd></c:when>
                        <c:otherwise><dd><div>이전글이 없습니다.</div></dd></c:otherwise>
                    </c:choose>
                </dl>
                <dl>
                    <dt class="next m_hidden"><div>다음글</div></dt>
                    <c:choose>
                        <c:when test="${not empty noticeInfo.after}"><dd><a href="javascript:void(0)" onclick="$(location).attr('href','${pageContext.request.contextPath}/board/announce/${noticeInfo.after.noticeSeq}')"><div>${noticeInfo.after.title}</div></a></dd></c:when>
                        <c:otherwise><dd><div>다음글이 없습니다.</div></dd></c:otherwise>
                    </c:choose>
                </dl>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/board/announce">목록</a></div>
            </div>
        </div>

    </div>
</div>