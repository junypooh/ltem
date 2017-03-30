<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">계정관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 계정관리</div>
        </div>

        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/accounts">계정</a></li>
                    <li class="on"><a href="${pageContext.request.contextPath}/company">기업</a></li>
                </ul>
            </div>
            <!-- search -->
            <div class="box_searchtop">
                <select name="column" id="column">
                    <option value="">전체</option>
                    <option value="name">기업명</option>
                    <option value="code">기업코드</option>
                </select>
                <div class="sech_inp">
                    <input type="text" name="searchWord" id="searchWord" title="검색">
                    <span for="searchb" class="btn_search"><a href="javascript:void(0)" onclick="fn_search()">검색</a></span>
                </div>
            </div>
            <!-- //search -->
        </div>

        <div class="box_tbllist">
            <table summary="기업코드,기업명,로고,대표번호,상태,변경일">
                <caption>계정관리 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_list">기업코드</th>
                    <th scope="col" class="m_list">기업명</th>
                    <th scope="col" class="m_list">로고</th>
                    <th scope="col" class="m_hidden">대표번호</th>
                    <%--<th scope="col" class="m_list">상태</th>--%>
                    <th scope="col" class="m_hidden">변경일</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="company" varStatus="status">
                    <tr <c:if test="${status.last}">class="last"</c:if>>
                        <td onclick="detail('${company.code}')">${company.code}</td>
                        <td onclick="detail('${company.code}')">${company.name}</td>
                        <c:choose>
                            <c:when test="${not empty company.fileSeq}">
                                <td class="logo_comp"><button type="button" onclick="showLogo(${company.fileSeq})">로고 확인</button></td>
                            </c:when>
                            <c:otherwise>
                                <td>-</td>
                            </c:otherwise>
                        </c:choose>
                        <td class="m_hidden" onclick="detail('${company.code}')">${company.telephone}</td>
                        <%--<td onclick="detail('${company.code}')">사용(?)</td>--%>
                        <td class="m_hidden" onclick="detail('${company.code}')"><javatime:format value="${company.modified}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty list}">
            <!-- 데이터 없을경우 -->
            <div class="nondata bdr2p">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>
            </c:if>

            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<!-- layer pop -->
<div class="wrap_layerpop" id="layerLogo" style="display: none;">
    <!-- 로고 -->
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>로고</h2>
            <div class="btn_close">
                <button type="button">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <div class="view_logo"><img src="" id="logoImg"></div>
            <div class="view_logo_non"><span>로고이미지</span></div><!-- 로고 없을경우 -->
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerLogo').hide(); layerPopDimmOff()">확인</a></div>
        </div>
    </div>
    <!-- //로고 -->
</div>
<!-- //layer pop -->

<%-- Template ================================================================================ --%>
<script type="text/html" id="companyListTemplate">
    {{each(i) list}}
    <tr {{if list.length == i+1}}class="last"{{/if}}>
        <td onclick="detail('\${code}')">\${code}</td>
        <td onclick="detail('\${code}')">\${name}</td>
        <td class="logo_comp"><button type="button">로고 확인</button></td>
        <td class="m_hidden" onclick="detail('\${code}')">\${telephone}</td>
        <%--<td onclick="detail('\${code}')">사용(?)</td>--%>
        <td class="m_hidden" onclick="detail('\${code}')">\${modifiedToString}</td>
    </tr>
    {{/each}}
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">


    /**
     * 상세이동
     * @param code
     */
    function detail(code) {
        $(location).attr('href', '${pageContext.request.contextPath}/company/' + code);
    }

    /** 페이징 */
    function goPage(page) {
        fn_search(page);
    }

    /** 검색 */
    function fn_search(currPage) {
        if(currPage == null) {
            currPage = 0;
        }
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/company/listSearch",
            type: "GET",
            data : {status: $('#status').val(), column: $('#column').val(), searchWord: $('#searchWord').val(), page:currPage},
            success: function(result) {

                $('tbody').empty();
                $('#companyListTemplate').tmpl(result).appendTo('tbody');
                $('.paging').empty().append(result.pageHtml);

                $('.nondata').remove();
                if(result.list.length < 1) {
                    $('table').after('<div class="nondata bdr2p">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /**
     * 로고 미리보기
     * @param fileSeq
     */
    function showLogo(fileSeq) {

        if (fileSeq != undefined) {
            $('#logoImg').attr('src', '${pageContext.request.contextPath}/fileDown?type=LOGO&fileSeq=' + fileSeq);
            $('.view_logo').show();
            $('.view_logo_non').hide();
        } else {
            $('.view_logo').hide();
            $('.view_logo_non').show();
        }

        layerOpen('layerLogo');
    }

</script>