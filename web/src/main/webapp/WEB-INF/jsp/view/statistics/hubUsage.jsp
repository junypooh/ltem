<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" var="year" pattern="yyyy"/>
<fmt:formatDate value="${now}" var="month" pattern="MM"/>

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
                    <li><a href="${pageContext.request.contextPath}/statistics/total/${contractInfo.serialNumber}/${year}">전체 통계</a></li>
                    <li class="on"><a href="javascript:void(0)">허브별 통계</a></li>
                </ul>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/statistics/hub/${contractInfo.serialNumber}" method="GET" id="form">
        <input type="hidden" name="page" id="page" />
        <p class="tit_p">${year}년 ${month}월 당월 기준</p>
        <div class="box_ullist">
            <ul id="ulList">
                <li class="tit_list">
                    <ul>
                        <li class="m_hidden"><input type="checkbox" id="allCheck" title="체크"></li>
                        <li>서비스 계약번호</li>
                        <li>상세주소</li>
                        <li class="m_hidden">이름</li>
                        <li class="m_hidden">평균 사용량(MB)</li>
                        <li>전월 사용량(MB)</li>
                        <li>가려진 정보 표시</li>
                    </ul>
                </li>
            <c:forEach items="${statisticsByHubInfos}" var="info" varStatus="status">
                <li<c:if test="${status.last}"> class="last"</c:if>>
                    <ul id="ul${info.serialNumber}">
                        <li class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="${info.serialNumber}"></li>
                        <li onclick="viewChart(${status.index})">${info.serialNumber}</li>
                        <li onclick="viewChart(${status.index})">${info.devicePlace}&nbsp;</li>
                        <li onclick="viewChart(${status.index})" class="m_hidden">${info.userName}&nbsp;</li>
                        <li onclick="viewChart(${status.index})" class="m_hidden"><fmt:formatNumber value="${info.viewAvgUsage}" pattern="#,###.##"/></li>
                        <li onclick="viewChart(${status.index})" class="red"><fmt:formatNumber value="${info.viewUsage}" pattern="#,###.##"/></li>
                        <li><div class="btn_view"><button type="button" onclick="showDialog('${info.serialNumber}', ${status.index})">표시</button></div></li>
                    </ul>
                    <div class="box_chart">
                        <div id="chart${status.index}"></div>
                    </div>
                </li>
            </c:forEach>
            </ul>
            <c:if test="${empty statisticsByHubInfos}">
                <div class="nondata">데이터가 없습니다.</div>
            </c:if>
        </div>
        </form>

        <div class="btn_tbllist"><a href="javascript:void(0)" onclick="excelDownLoad()" class="btn_excel">엑셀 다운로드</a></div>
        <div class="paging">
            ${pageHtml}
        </div>

    </div>
</div>

<!-- layer pop -->
<div class="wrap_layerpop" id="layerPasswordConfirm" style="display: none;">
    <!-- 가려진 정보 해제 -->
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>가려진 정보 해제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeDialog()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <input type="hidden" id="svcContSerial" >
            <input type="hidden" id="liIndex" >
            <span class="txt_top mb10">가려진 정보 해제를 위해 비밀번호와 사유를 입력해주세요.</span>
            <div class="inp_pop mb10"><input type="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <p class="colred mb10" style="display: none" id="passwordNotMatched1">비밀번호를 입력해주세요.</p>
            <div class="inp_pop"><input name="txtReason" id="txtReason" maxlength="30" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" style="display: none" id="reasonNotMatched">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="removeMask()">해제</a></div>
        </div>
    </div>
    <!-- //가려진 정보 해제 -->
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
<script type="text/html" id="maskSvcListTemplate">
    <li class="tit_list">
        <ul>
            <li class="m_hidden"><input type="checkbox" id="allCheck" title="체크"></li>
            <li>서비스 계약번호</li>
            <li>상세주소</li>
            <li class="m_hidden">이름</li>
            <li class="m_hidden">평균 사용량(MB)</li>
            <li>전월 사용량(MB)</li>
            <li>가려진 정보 표시</li>
        </ul>
    </li>
    {{each(i) statisticsByHubInfos}}
    <li>
        <ul id="ul\${serialNumber}">
            <li class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${serialNumber}"></li>
            <li onclick="viewChart(\${i})">\${serialNumber}</li>
            <li onclick="viewChart(\${i})">\${devicePlace}&nbsp;</li>
            <li onclick="viewChart(\${i})" class="m_hidden">\${userName}&nbsp;</li>
            <li onclick="viewChart(\${i})" class="m_hidden">\${toCurrency(viewAvgUsage)}</li>
            <li onclick="viewChart(\${i})" class="red">\${toCurrency(viewUsage)}</li>
            <li><div class="btn_view"><button type="button" onclick="showDialog('\${serialNumber}', \${i})">표시</button></div></li>
        </ul>
        <div class="box_chart">
            <div id="chart\${i}"></div>
        </div>
    </li>
    {{/each}}
</script>
<%-- Template ================================================================================ --%>

<%-- Template ================================================================================ --%>
<script type="text/html" id="unMaskSvcListTemplate">
    <li class="m_hidden"><input type="checkbox" name="chkbox" title="체크" value="\${serialNumber}"></li>
    <li onclick="viewChart(\${index})">\${serialNumber}</li>
    <li onclick="viewChart(\${index})">\${devicePlace}&nbsp;</li>
    <li onclick="viewChart(\${index})" class="m_hidden">\${userName}&nbsp;</li>
    <li onclick="viewChart(\${index})" class="m_hidden">\${toCurrency(viewAvgUsage)}</li>
    <li onclick="viewChart(\${index})" class="red">\${toCurrency(viewUsage)}</li>
    <li></li>
</script>
<%-- Template ================================================================================ --%>

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

        // 챠트 그리기
        makeChart();

    });

    function viewChart(index) {

        if($('#chart' + index).parent('.box_chart').hasClass('on')) {
            $('.box_chart').each(function () {
                $(this).removeClass('on');
            });
        } else {
            $('#chart' + index).parent('.box_chart').addClass('on');
        }
    }

    function makeChart() {

        <c:forEach items="${statisticsByHubInfos}" var="info" varStatus="status">
        var chart${status.index} = c3.generate({
            bindto: '#chart${status.index}',
            data: {
                columns: [
                    ['data1', ${info.viewAvgUsage}, ${info.viewUsage}]
                ],
                bar: {
                    width: {
                        ratio: 0.2
                    }
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
                        text: '사용량(MB)',
                        position: 'outer-top'
                    }
                },
                x:{
                    type:'category',
                    categories:['평균 사용량','전월 사용량']
                }
            },
            tooltip: {
                show: false
            },
            legend:{
                show: false
            },
        });
        </c:forEach>
    }

    /**
     * 컨텐츠 테이블 tr class 조정
     * 가장 하단 tr 에 class="last" 추가
     */
    function ulLiClassSetting() {

        var liArray = $('#ulList > li');
        liArray.each(function(index, item) {
            $(this).removeClass('last');
            if(index == liArray.length - 1) {
                $(this).addClass('last');
            }
        });
    }

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

        var url = "${pageContext.request.contextPath}/statistics/hubUsageExcel";
        var inputs = "";
        var paramData = $.param({'bizContSerial':${contractInfo.serialNumber}}, true);
        $.each(paramData.split('&'), function () {
            var pair = this.split('=');
            inputs += "<input type='hidden' name='" + pair[0] + "' value='" + pair[1] + "' />";
        });
        inputs += "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'/>";

        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function (idx) {
            inputs += "<input type='hidden' name='serialNumbers[" + idx + "]' value='" + $(this).val() + "'/>";
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

    /**
     * 페이징
     * @param page
     */
    function goPage(page) {
        $("input[name='page']").val(page);
        $("#form").submit();
    }

    /**
     * 레이어 오류 메세지 숨김 처리.
     */
    function layerErrorTextHide() {
        $('#passwordNotMatched').hide();
        $('#passwordNotMatched1').hide();
        $('#reasonNotMatched').hide();
    }

    /**
     * 비밀번호 입력 layer open
     * @param svcContSerial
     * @param serialNumber
     */
    function showDialog(svcContSerial, index) {
        layerErrorTextHide();
        layerOpen('layerPasswordConfirm');
        $('#svcContSerial').val(svcContSerial);
        $('#liIndex').val(index);
    }

    /**
     * 비밀번호 입력 layer close
     */
    function closeDialog() {
        layerErrorTextHide();
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
        $('#svcContSerial').val('');
        $('#password').val('');
        $('#txtReason').val('');
    }

    /**
     * 가려진 정보 표시
     */
    function removeMask() {

        layerErrorTextHide();

        var password = $('#password').val();
        var txtReason = $('#txtReason').val();

        if(password.length == 0){
            $('#passwordNotMatched1').show();
            return false;
        }

        if(txtReason.length == 0){
            $('#reasonNotMatched').show();
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/statistics/hub/unmask/${contractInfo.serialNumber}/" + $('#svcContSerial').val(),
            type: "POST",
            data: {
                password: $('#password').val(),
                reason: $('#txtReason').val(),
                page: $("input[name='page']").val()
            },
            success: function (rtnData) {

                if (rtnData.errorCd == 0) {
                    console.log($('#liIndex').val());
                    rtnData.unmaskInfo.index = $('#liIndex').val();

                    // 전체 리스트 갱신(마스킹 해제된 건이 있을 경우 마스킹 처리해줘야하니까)
                    $('#ulList').empty();
                    $('#maskSvcListTemplate').tmpl(rtnData).appendTo('#ulList');
                    // 마스킹 해제 데이터로 변경
                    $('#ul' + $('#svcContSerial').val()).empty();
                    $('#unMaskSvcListTemplate').tmpl(rtnData.unmaskInfo).appendTo('#ul' + $('#svcContSerial').val());

                    closeDialog();
                    checkBoxControll();
                    ulLiClassSetting();
                    makeChart();
                } else if (rtnData.errorCd == -1) {
                    $('#passwordNotMatched').show();
                } else {
                    alert(rtnData.msg);
                }
            },
            error: function (request, status, error) {
                //TODO 로그인 만료, 권한 없음 구분하여 처리할 방법 강구
                if (request.status == 403) {
                    // Not Authenticated
                    alert('로그인해주세요.');
                } else {
                    console.log(error);
                    alert('실패하였습니다.');
                }
            }
        });

    }

</script>