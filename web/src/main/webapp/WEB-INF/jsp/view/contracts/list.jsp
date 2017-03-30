<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="authorityCd" property="principal.authorityCd"/>
</security:authorize>


<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">사용자관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 사용자관리</div>
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
                <span for="searchb" class="btn_search"><a href="javascript:void(0)" onclick="fn_search()">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_list2col">
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
                                    <li>주소 미등록건 : <em class="red">${bizContract.devicePlaceIsNullCount}</em>/${bizContract.devicePlaceCount}</li>
                                    <li>데이터량 : <fmt:formatNumber value="${bizContract.viewCapacity}" pattern="#,###.##"/>MB</li>
                                    <li>
                                        <c:choose>
                                            <c:when test="${!empty bizContract.master}">
                                                <c:forEach items="${bizContract.master}" var="master">
                                                    <input type="hidden" name="mgrSeq" value="${master.mgrSeq}">
                                                    <em>담당자 : Master ${master.name}</em>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <em>담당자 : -</em>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${bizContract.contractState ne '계약해지'}">
                                            <span class="btn_grey2"><button type="button"
                                                                            onclick="goMovePage('allView', '${bizContract.serialNumber}')">전체보기</button></span>
                                            <span class="btn_blk"><button type="button"
                                                                          onclick="goMovePage('add', '${bizContract.serialNumber}', '${bizContract.companyCd}')">추가</button></span>
                                        </c:if>
                                    </li>
                                    <li class="hum"><em>계약설명</em> <span class="btn_allview"><button
                                            type="button">보기</button></span></li>
                                </ul>
                                <!-- 보기 -->
                                <div class="supervisor">
                                        ${bizContract.description}
                                </div>
                                <a href="${pageContext.request.contextPath}/contracts/${bizContract.serialNumber}" class="btn_pgmid">페이지 이동</a><!-- 0214 버튼 추가 -->
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
<!-- //content -->

<!-- Layer -->
<div class="layerpop pop2" style="display: none;">
    <div class="box_titpop">
        <h2>안내</h2>
        <div class="btn_close">
            <button type="button" onclick="closeLayer()">닫기</button>
        </div>
    </div>
    <div class="box_contpop">
        <span class="txt_top mb10">Staff 계정은 해당 기능 접근이 불가능합니다. </span>
        <div class="btn_blk_full"><a href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
    </div>
</div>
<!-- //Layer -->

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
                            <li>주소 미등록건 : <em class="red">\${devicePlaceIsNullCount}</em>/\${devicePlaceCount}</li>
                            <li>데이터량 : \${toCurrency(viewCapacity)}MB</li>
                            <li>
                                {{if master.length > 0}}
                                {{each master}}
                                <input type="hidden" name="mgrSeq" value="\${mgrSeq}">
                                <em>담당자 : Master \${name}</em>
                                {{/each}}
                                {{else}}
                                <em>담당자 : -</em>
                                {{/if}}
                                <span class="btn_grey2"><button type="button"
                                                                onclick="goMovePage('allView', '\${serialNumber}')">전체보기</button></span>
                                <span class="btn_blk"><button type="button"
                                                              onclick="goMovePage('add', '\${serialNumber}', '\${companyCd}')">추가</button></span>
                            </li>
                            <li class="hum"><em>계약설명</em> <span class="btn_allview"><button
                                    type="button">보기</button></span></li>
                        </ul>
                        <!-- 보기 -->
                        <div class="supervisor">
                            \${description}
                        </div>
                        <a href="${pageContext.request.contextPath}/contracts/\${serialNumber}" class="btn_pgmid">페이지 이동</a><!-- 0214 버튼 추가 -->
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

    $(document).ready(function() {

        enterFunc($('#searchWord'), fn_search);
    });

    /** 계약 검색 */
    function fn_search() {
        var contractsState = $('#contractsState option:selected').val();
        var contractsColumn = $('#contractsColumn option:selected').val();

        $('.nondata').remove();
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/listSearch",
            traditional: true,
            data : {'contractsState': contractsState, 'contractsColumn': contractsColumn, 'searchWord': $('#searchWord').val(), 'sortName': 'created'},
            success: function(result) {
                $('.box_list2col').empty();
                $('#contractListTemplate').tmpl(result).appendTo('.box_list2col');
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 계정관리 페이지로 이동 */
    function goMovePage(type, serialNumber, companyCd) {
        var authorityCd = '${authorityCd}';
        if (authorityCd == 40) {
            $('.layerpop').show();
        } else {
            if (type == 'allView') {
                $(location).attr('href', '${pageContext.request.contextPath}/contracts/' + serialNumber + '/managers');
            } else {
                $(location).attr('href', '${pageContext.request.contextPath}/contracts/' + companyCd +'/'+ serialNumber + '/managers');
            }
        }
    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.layerpop').hide();
    }

</script>