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

        <div class="list_tit">
            <span class="btn_tit"><button type="button">${contractInfo.serialNumber}</button></span>
            <div class="area_tit">
                <strong class="txt_tit">${contractInfo.name}</strong><span class="btn_grey"><button type="button" onclick="editName()">수정</button></span>
            </div>
            <div class="area_tit_ip" style="display: none"><!-- 저장 -->
                <strong class="txt_tit"><input type="text" name="contractName" id="contractName" value="${contractInfo.name}" maxlength="8"></strong><span class="btn_grey"><button type="button" onclick="saveName()">저장</button></span>
            </div>
        </div>

        <div class="box_listtop">
            <div class="area_txt">
                <ul>
                    <li>계약기간 : <span><javatime:format value="${contractInfo.startDate}" pattern="yyyy.MM.dd"/>~<javatime:format value="${contractInfo.endDate}" pattern="yyyy.MM.dd"/></span></li>
                    <li>회선수 : <span><fmt:formatNumber value="${contractInfo.circuitCount}" pattern="#,###.##"/>회선 (허브연결상태 : <em class="red"><fmt:formatNumber value="${contractInfo.hubConnectCount}" pattern="#,###.##"/></em>/<fmt:formatNumber value="${contractInfo.circuitCount}" pattern="#,###.##"/>)</span></li>
                    <li>데이터허용량 : <span><fmt:formatNumber value="${contractInfo.viewCapacity}" pattern="#,###.##"/>MB</span></li>
                    <li>상태 : <span>${contractInfo.contractState}</span></li>
                </ul>
            </div>

            <!-- pop -->
            <section class="area_pop">
                <div>
                    <div class="tit">
                        <ul>
                            <li class="on">
                                <button type="button" data="created">최신순</button>
                            </li>
                            <li>
                                <button type="button" data="name">계약명순</button>
                            </li>
                        </ul>
                        <div class="box_searchtop">
                            <select name="contractsColumn" id="contractsColumn">
                                <option value="all">전체</option>
                                <option value="serialNumber">Biz계약번호</option>
                                <option value="name">계약명</option>
                                <option value="managersName">담당자</option>
                            </select>
                            <div class="sech_inp">
                                <input type="text" name="searchWord" id="searchWord">
                                <span for="searchWord" class="btn_search"><a href="javascript:void(0)" onclick="contractListJson('created')">검색</a></span>
                            </div>
                        </div>
                    </div>

                    <div class="list" id="contractList">
                        <ul>
                        </ul>
                    </div>

                    <c:set var="recentContracts" value="${sessionScope.recentContracts}" />
                    <div class="btm">
                        <strong>최근 본 계약건</strong>
                        <ul>
                        <c:forEach items="${recentContracts}" var="recent" varStatus="status">
                            <li><a href="${pageContext.request.contextPath}/statistics/total/${recent.serialNumber}/${year}">${recent.serialNumber} <span>${recent.name}</span></a></li>
                        </c:forEach>
                        </ul>
                    </div>
                </div>
            </section>
            <!-- //pop -->
        </div>

        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li class="on"><a href="javascript:void(0)">전체 통계</a></li>
                    <li><a href="${pageContext.request.contextPath}/statistics/hub/${contractInfo.serialNumber}">허브별 통계</a></li>
                </ul>
            </div>
        </div>

        <p class="tit_p">
            <select name="" id="" onchange="$(location).attr('href', '${pageContext.request.contextPath}/statistics/total/${contractInfo.serialNumber}/' + this.value)">
            <c:forEach begin="${0}" end="${5}" var="item">
                <option value="${year-item}" ${(targetYear eq (year-item))?'selected':''}>${year-item}년</option>
            </c:forEach>
            </select>
            <span class="tit">당월 기준</span>
        </p>

        <c:if test="${not empty contractMonthUsages}">
        <div class="box_chart">
            <div class="chart"></div>
        </div>
        </c:if>

        <div class="box_tbllist">
            <table summary="월, 사용량 평균(MB), 전체 사용량(MB)">
                <caption>전체통계</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden"><input type="checkbox" id="allCheck" title="전체체크"></th>
                    <th scope="col">월</th>
                    <th scope="col">허용량(MB)</th>
                    <th scope="col">사용중인 회선수</th>
                    <th scope="col">허브 사용자</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${contractMonthUsages}" var="usage" varStatus="status">
                <tr<c:if test="${status.last}"> class="last"</c:if>>
                    <td class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="${usage.month}"></td>
                    <td>${usage.month}월</td>
                    <td>${usage.viewCapacity}</td>
                    <td><fmt:formatNumber value="${usage.activatedCount}" pattern="#,###.##"/></td>
                    <td><fmt:formatNumber value="${usage.mappedCount}" pattern="#,###.##"/></td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty contractMonthUsages}">
            <div class="nondata">데이터가 없습니다.</div>
            </c:if>

            <c:if test="${not empty contractMonthUsages}">
            <div class="btn_tbllist"><a href="javascript:void(0)" class="btn_excel" id="excelDown">엑셀 다운로드</a></div>
            </c:if>

        </div>

    </div>
</div>

<div class="wrap_layerpop" id="layerExcelConfirm" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>엑셀 다운로드</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerExcelConfirm').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">항목을 선택 후 다운로드 해주세요.</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerExcelConfirm').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
</div>
<!-- //layer pop -->

<%-- Template ================================================================================ --%>
<script type="text/html" id="contractTemplate">
    <li><a href="${pageContext.request.contextPath}/statistics/total/\${serialNumber}/${year}">\${serialNumber} <span>{{if name == null || name == ''}}-{{else}} \${name} {{/if}}</span></a></li>
</script>
<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function () {

        /**
         * 체크박스 전체 선택/해제
         */
        checkBoxControll();

        /**
         * 엑셀 다운로드
         */
        $("#excelDown").click(function (e) {

            e.preventDefault();
            excelDownLoad();
        });

        /**
         * 상단 계약명 클릭(검색 영역 show)
         */
        $('.list_tit .btn_tit button').click(function () {
            if (!$(this).hasClass('on')) {

                $('.area_pop .tit ul li').each(function () {
                    if($(this).hasClass('on')) {
                        contractListJson($(this).children().attr('data'));
                    }
                });
            }
            $(this).toggleClass('on');
            $(".box_listtop .area_pop").toggleClass('on');
        });

        /**
         * 계약명순, 최신순 클릭
         */
        $('.area_pop .tit ul li > button').click(function(){
            $(this).parent('li').addClass('on');
            $(this).parent().siblings('li').removeClass('on');

            contractListJson($(this).attr('data'));
        });

        // char
        <c:if test="${not empty contractMonthUsages}">
        var chart = c3.generate({
            bindto: '.chart',
            data: {
                columns: [
                    ['data1'
                        <c:forEach items="${contractMonthUsages}" var="usage" varStatus="status">
                        , ${usage.viewCapacity}
                        </c:forEach>
                    ]
                ],
                bar: {
                    width: {
                        ratio: 0.2,
                    },
                },
                colors:{
                    data1:'#97d8d4'
                },
                axes: {
                    data1: 'y'
                },
                types: {
                    data1: 'bar' // ADD
                },
            },
            axis: {
                y: {
                    label: {
                        text: '허용량(MB)',
                        position: 'outer-top'
                    }
                },
                x:{
                    type:'category',
                    categories:[
                        <c:forEach items="${contractMonthUsages}" var="usage" varStatus="status">
                        <c:if test="${!status.first}">, </c:if>'${usage.month}월'
                        </c:forEach>
                    ]
                }
            },
            tooltip: {
                show: false
            },
            legend:{
                show: false
            },
        });
        </c:if>

    });

    /**
     * 체크박스 전체 선택/해제
     */
    function checkBoxControll() {

        $('#allCheck').click(function () {
            if ($('#allCheck').is(':checked')) {
                $(':checkbox[name="chkbox"]').prop('checked', true);
            } else {
                $(':checkbox[name="chkbox"]').prop('checked', false);
            }
        });
    }

    /**
     * 상단 검색 영역 계약 리스트 조회
     */
    function contractListJson(sortName) {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/list.json",
            data: {
                contractsColumn : $('#contractsColumn').val(),
                searchWord : $('#searchWord').val(),
                sortName : sortName
            },
            success: function (obj) {

                $('#contractList').empty();
                if(obj.length > 0) {
                    $('#contractList').append('<ul></ul>');
                    $('#contractTemplate').tmpl(obj).appendTo('#contractList > ul');
                } else {
                    $('#contractList').append('<div class="nondata">검색결과가 없습니다. </div>');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /**
     * 계약명 수정
     */
    function editName() {
        $('.area_tit').hide();
        $('.area_tit_ip').show();
    }

    /**
     * 계약명 저장
     */
    function saveName() {

        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/editContractName",
            type: "POST",
            data: {
                serialNumber: ${contractInfo.serialNumber},
                contractName: $('#contractName').val()
            },
            success: function (rtnData) {

                if (rtnData.errorCd == 0) {

                    $('.area_tit').show();
                    $('.area_tit_ip').hide();

                    $('.list_tit .area_tit .txt_tit').text($('#contractName').val());

                }
            },
            error: function (e) {
                console.log(e);
                alert('실패하였습니다.');
            }
        });
    }

    /**
     * 엑셀 다운로드
     */
    function excelDownLoad() {

        var url = "${pageContext.request.contextPath}/statistics/totalUsageExcel";
        var inputs = "";
        var paramData = $.param({'serialNumber':${contractInfo.serialNumber}, 'year':${targetYear}}, true);
        $.each(paramData.split('&'), function () {
            var pair = this.split('=');
            inputs += "<input type='hidden' name='" + pair[0] + "' value='" + pair[1] + "' />";
        });
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='months[" + idx + "]' value='" + $(this).val() + "'/>";
            checkCnt++;
        });

        var sForm = "<form action='" + url + "' method='POST'>" + inputs + "</form>";

        if (checkCnt == 0) {
            layerOpen('layerExcelConfirm');
            return;
        } else {
            $(sForm).appendTo("body").submit().remove();
        }

    }

</script>