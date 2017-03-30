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

        <!-- search -->
        <div class="box_searchtop">
            <select id="searchKey" class="sel_col1">
                <option value="">전체</option>
                <option value="title">제목</option>
                <option value="content">내용</option>
            </select>
            <div class="sech_inp">
                <input type="text" id="searchText" title="검색">
                <span for="searchText" class="btn_search"><a href="javascript:void(0)" onclick="goPage(0)">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_tbllist">
            <table summary="번호, 제목, 작성일">
                <caption>공지사항 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_list per8">번호</th>
                    <th scope="col" class="m_list">제목</th>
                    <th scope="col" class="m_list per12">작성일</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${noticeList}" var="noticeList" varStatus="status">
                    <tr <c:if test="${status.last}">class="last"</c:if>>
                        <td onclick="goDetail('${noticeList.noticeSeq}')">${pageTotalCnt - status.index}</td>
                        <td class="alignl" onclick="goDetail('${noticeList.noticeSeq}')">${noticeList.title}</td>
                        <td onclick="goDetail('${noticeList.noticeSeq}')"><javatime:format value="${noticeList.created}" pattern="yyyy.MM.dd"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty noticeList}">
                <!-- 데이터 없을경우 -->
                <div class="nondata bdr2p">등록된 게시물이 없습니다.</div>
            </c:if>

            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>

<%-- Template ================================================================================ --%>
<script type="text/html" id="noticeListTemplate">
    {{each(i) noticeList}}
    <tr {{if noticeList.length == i+1}}class="last"{{/if}}>
        <td onclick="goDetail('\${noticeSeq}')">\${pageTotalCnt - (pageNumber * 30 + i)}</td>
        <td class="alignl" onclick="goDetail('\${noticeSeq}')">\${title}</td>
        <td onclick="goDetail('\${noticeSeq}')">\${createdDtToString}</td>
    </tr>
    {{/each}}
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function() {
        enterFunc($('#searchText'), goPage);
    });

    /**
     * 페이징
     * @param page
     */
    function goPage(page) {
        if(page == null) {
            page = 0;
        }
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/board/announce/pageAjax",
            type: "GET",
            data : {page:page, searchKey:$('#searchKey').val(), searchText:$('#searchText').val()},
            async : false,
            success: function(result) {
                $('tbody').empty();
                $('#noticeListTemplate').tmpl(result).appendTo('tbody');
                $('.paging').empty().append(result.pageHtml);

                if(result.noticeList.length == 0) {
                    $('table').after('<div class="nondata bdr2p">검색결과가 없습니다.</div>');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /**
     * 상세이동
     */
    function goDetail(noticeSeq) {
        $(location).attr('href', '${pageContext.request.contextPath}/board/announce/' + noticeSeq);
    }

</script>