<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="authorityCode" property="principal.authorityCd"/>
</security:authorize>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">전체 계약 담당자</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 계정관리</div>
        </div>
        <!-- search -->
        <div class="box_searchtop">
            <select name="status" id="status">
                <option value="">전체</option>
                <option value="30">사용</option>
                <option value="40">정지</option>
            </select>
            <select name="column" id="column">
                <option value="id">아이디</option>
                <option value="name">이름</option>
                <option value="role">권한</option>
            </select>
            <div class="sech_inp">
                <input type="text" name="searchWord" id="searchWord" title="검색">
                <span for="searchb" class="btn_search"><a href="javascript:void(0)" onclick="fn_search()">검색</a></span>
            </div>
        </div>
        <!-- //search -->

        <div class="box_tbllist">
            <table summary="기업명,아이디,이름,휴대폰번호,권한,관리계약건,상태,변경일,가려진정보표시">
                <caption>계정관리 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden">NO</th>
                    <th scope="col" class="m_list">아이디</th>
                    <th scope="col" class="m_list">이름</th>
                    <th scope="col" class="m_hidden">휴대폰번호</th>
                    <th scope="col" class="m_list">권한</th>
                    <th scope="col" class="m_hidden">관리 계약건</th>
                    <th scope="col" class="m_hidden">상태</th>
                    <th scope="col" class="m_hidden">변경일</th>
                    <th scope="col">가려진 정보 표시</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach items="${managerList}" var="manager" varStatus="status">
                        <tr <c:if test="${status.last}">class="last"</c:if> id="${manager.mgrSeq}">
                            <td class="m_hidden no">${pageTotalCnt - status.index}</td>
                            <td>${manager.username}</td>
                            <td>${manager.name}</td>
                            <td class="m_hidden">${manager.telephone}</td>
                            <td>${manager.authorityName}</td>
                            <td class="m_hidden">${manager.contractsName}</td>
                            <td class="m_hidden">${manager.mgrStatusName}</td>
                            <td class="m_hidden"><javatime:format value="${manager.modified}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
                            <td><div class="btn_view"><button type="button" onclick="showDialog('${manager.mgrSeq}', '${pageNumber}')">표시</button></div></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty managerList}">
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

<!-- 가려진 정보 해제 -->
<div class="wrap_layerpop" id="layerPasswordConfirm" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>가려진 정보 해제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeDialog()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <input type="hidden" id="mgrSeq" >
            <input type="hidden" id="pageNumber" >
            <input type="hidden" id="no" >
            <span class="txt_top mb10">가려진 정보 해제를 위해 비밀번호와 사유를 입력해주세요.</span>
            <div class="inp_pop mb10"><input type="password" id="passWord" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <div class="inp_pop"><input name="reason" id="reason" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)"></div>
            <p class="colred mt10" id="reasonNotInput"style="display: none;">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="removeMask()">해제</a></div>
        </div>
    </div>
    <!-- //가려진 정보 해제 -->
</div>
<!-- //Layer -->

<%-- Template ================================================================================ --%>
<script type="text/html" id="accountListTemplate">
    {{each(i) managerList}}
        <tr {{if managerList.length == i+1}}class="last"{{/if}} id="\${mgrSeq}">
            <td class="m_hidden no">\${pageTotalCnt - (pageNumber * 30 + i)}</td>
            <td>\${username}</td>
            <td>\${name}</td>
            <td class="m_hidden">\${telephone}</td>
            <td>\${authorityName}</td>
            <td class="m_hidden">\${contractsName}</td>
            <td class="m_hidden">\${mgrStatusName}</td>
            <td class="m_hidden">\${modifiedToString}</td>
            <td><div class="btn_view"><button type="button" onclick="showDialog('\${mgrSeq}','\${pageNumber}')">표시</button></div></td>
        </tr>
    {{/each}}
</script>


<script type="text/html" id="accountTemplate">
    <td class="m_hidden">\${no}</td>
    <td>\${username}</td>
    <td>\${name}</td>
    <td class="m_hidden">\${telephone}</td>
    <td>\${authorityName}</td>
    <td class="m_hidden">\${contractsName}</td>
    <td class="m_hidden">\${mgrStatusName}</td>
    <td class="m_hidden">\${modifiedToString}</td>
    <td><%--<div class="btn_view"><button type="button" onclick="showDialog('\${mgrSeq}','\${pageNumber}')">표시</button></div>--%></td>
</script>


<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function() {
        enterFunc($('#searchWord'), fn_search);
    });

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
            url: "${pageContext.request.contextPath}/contracts/managers/search",
            type: "GET",
            data : {serialNumber: '${serialNumber}', status: $('#status').val(), column: $('#column').val(), searchWord: $('#searchWord').val(), page:currPage},
            async : false,
            success: function(result) {
                $('tbody').empty();
                $('#accountListTemplate').tmpl(result).appendTo('tbody');
                $('.paging').empty().append(result.pageHtml);

                $('input[name="allCheck"]').prop('checked', false);

                $('.nondata').remove();
                if(result.managerList.length < 1) {
                    $('table').after('<div class="nondata bdr2p">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

    /** 가려진 정보 해제 layer show */
    function showDialog(mgrSeq, pageNumber) {
        layerOpen('layerPasswordConfirm');
        $('#mgrSeq').val(mgrSeq);
        $('#pageNumber').val(pageNumber);
        $('#no').val($('#'+mgrSeq).find('.no').text());

        // 초기화
        $('#passWord').val('');
        $('#reason').val('');
        $('#passwordNotMatched').hide();
        $('#reasonNotInput').hide();


    }

    /** 가려진 정보 해제 layer close */
    function closeDialog() {
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
    }

    /** 가려진 정보 해제 프로세스 */
    function removeMask() {

        var pageNumber = $('#pageNumber').val();
        var mgrSeq = $('#mgrSeq').val();
        $('#passwordNotMatched').hide();
        $('#reasonNotInput').hide();

        if ($.trim($('#passWord').val()) == '') {
            $('#passwordNotMatched').html('비밀번호를 입력해주세요.');
            $('#passwordNotMatched').show();
            return false;
        }

        if ($.trim($('#reason').val()) == '') {
            $('#reasonNotInput').show();
            return false;
        }


        $.ajax({
            url: "${pageContext.request.contextPath}/contracts/allInCharge/unmask",
            type: "POST",
            data : {'password':$('#passWord').val(), 'mgrSeq':mgrSeq, 'reason':$('#reason').val()},
            async : false,
            success: function(result) {

                if (result.errorCd == 0) {
                    // 전체 리스트 갱신(마스킹 해제된 건이 있을 경우 마스킹 처리해줘야하니까)
                    fn_search(pageNumber);

                    result.manager.no = $('#no').val();
                    result.manager.authorityCode = '${authorityCode}';
                    $('#'+mgrSeq).empty();
                    $('#accountTemplate').tmpl(result.manager).appendTo('#'+mgrSeq);
                    closeDialog();
                } else if (result.errorCd == -1){
                    $('#passwordNotMatched').html('비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.');
                    $('#passwordNotMatched').show();
                } else {
                    alert(result.msg);
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

</script>