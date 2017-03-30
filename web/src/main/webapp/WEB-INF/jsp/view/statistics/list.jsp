<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" var="year" pattern="yyyy"/>
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">통계관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 통계관리</div>
        </div>

        <!-- search -->
        <div class="box_searchtop">
            <select name="contractsState" id="contractsState">
                <option value="">전체</option>
                <option value="contractPrev">계약</option>
                <option value="contract">계약중</option>
                <option value="revocationOfContract">계약만료</option>
            </select>
            <select name="contractsColumn" id="contractsColumn">
                <option value="serialNumber">Biz계약번호</option>
                <option value="name">계약명</option>
                <option value="managersName">담당자</option>
            </select>
            <div class="sech_inp">
                <input type="text" name="searchWord" id="searchWord" title="검색">
                <span for="searchb" class="btn_search btn_grey"><a href="javascript:void(0)" onclick="fn_search()">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_list2col type2" id="listBody">
            <div class="area_sup">
                <c:forEach items="${bizContract}" var="bizContract">
                    <dl id="${bizContract.serialNumber}">
                        <dt>
                        <div>
                            <span class="ico_th">${bizContract.contractState}</span>
                            <strong>
                                <span>${bizContract.serialNumber}</span>
                                <c:choose>
                                    <c:when test="${bizContract.name ne null and bizContract.name != ''}">
                                        <span>${bizContract.name} </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>-</span>
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </div>
                        </dt>
                        <dd>
                            <div>
                                <ul>
                                    <li>계약기간 : <javatime:format value="${bizContract.startDate}" pattern="yyyy.MM.dd"/> ~ <javatime:format value="${bizContract.endDate}" pattern="yyyy.MM.dd"/></li>
                                    <li>회선수 : <fmt:formatNumber value="${bizContract.circuitCount}" pattern="#,###.##"/>
                                        (허브연결 상태 : <em class="red"><fmt:formatNumber value="${bizContract.hubConnectCount}" pattern="#,###.##"/></em>/<fmt:formatNumber value="${bizContract.circuitCount}" pattern="#,###.##"/>)
                                    </li>
                                    <li>데이터량 : <fmt:formatNumber value="${bizContract.viewCapacity}" pattern="#,###.##"/>MB</li>
                                    <c:choose>
                                        <c:when test="${not empty bizContract.recentContUsage}">
                                            <li>전월 데이터 사용량 : <fmt:formatNumber value="${bizContract.recentContUsage.viewCapacity}" pattern="#,###.##"/>MB</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>전월 데이터 사용량 : 0MB</li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                                <a href="${pageContext.request.contextPath}/statistics/total/${bizContract.serialNumber}/${year}" class="btn_pgmid">페이지 이동</a><!-- 0214 버튼 추가 -->
                            </div>
                        </dd>
                    </dl>
                </c:forEach>
            </div>
            <c:if test="${empty bizContract}">
            <div class="nondata">검색결과가 없습니다.</div>
            </c:if>
        </div>
    </div>
</div>

<%-- Template ================================================================================ --%>
<script type="text/html" id="contractListTemplate">
    {{if searchList.length > 0}}
    <div class="area_sup">
        {{each searchList}}
        <dl id="\${serialNumber}">
            <dt>
            <div>
                <span class="ico_th">\${contractState}</span>
                <strong><span>\${serialNumber}</span><span>\${name}</span></strong>
            </div>
            </dt>
            <dd>
                <div>
                    <ul>
                        <li>계약기간 : \${String(startDateToString).replaceAll('-','.')} ~ \${String(endDateToString).replaceAll('-','.')}</li>
                        <li>회선수 : \${toCurrency(circuitCount)} (허브연결 상태 : <em class="red">\${toCurrency(hubConnectCount)}</em>/\${toCurrency(circuitCount)})</li>
                        <li>데이터량 : \${toCurrency(viewCapacity)}MB</li>
                        {{if recentContUsage != null}}
                        <li>전월 데이터 사용량 : \${recentContUsage.viewCapacity}MB</li>
                        {{else}}
                        <li>전월 데이터 사용량 : 0MB</li>
                        {{/if}}
                    </ul>
                    <a href="${pageContext.request.contextPath}/statistics/total/\${serialNumber}/${year}">페이지 이동</a><!-- 0214 버튼 추가 -->
                </div>
            </dd>
        </dl>
        {{/each}}
    </div>
    {{else}}
    <!-- 데이터 없을경우 -->
    <div class="nondata">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>
    {{/if}}
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">
    /** 계약 검색 */
    function fn_search() {
        var contractsState = $('#contractsState option:selected').val();
        var contractsColumn = $('#contractsColumn option:selected').val();

        $('.nondata').remove();
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/statistics/listSearch",
            traditional: true,
            data : {'contractsState': contractsState, 'contractsColumn': contractsColumn, 'searchWord': $('#searchWord').val(), 'sortName': 'created'},
            success: function(result) {
                $('#listBody').empty();
                $('#contractListTemplate').tmpl(result).appendTo('#listBody');
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

</script>