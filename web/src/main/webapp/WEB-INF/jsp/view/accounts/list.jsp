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
            <h2 class="h2_f">계정관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 계정관리</div>
        </div>
        <c:if test="${authorityCode eq '10'}">
        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li class="on"><a href="${pageContext.request.contextPath}/accounts">계정</a></li>
                    <li><a href="${pageContext.request.contextPath}/company">기업</a></li>
                </ul>
            </div>
        </c:if>
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
        <c:if test="${authorityCode eq '10'}">
        </div>
        </c:if>

        <div class="box_tbllist">
            <table summary="기업명,아이디,이름,휴대폰번호,권한,관리계약건,상태,변경일,가려진정보표시">
                <caption>계정관리 리스트</caption>
                <thead>
                <tr>
                    <th scope="col" class="m_hidden per4"><input type="checkbox" name="allCheck" title="전체체크"></th>
                    <th scope="col" class="m_hidden">NO</th>
                    <c:if test="${authorityCode eq '10'}"><th scope="col" class="m_hidden">기업명</th></c:if>
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
                            <td class="m_hidden per4"><input type="checkbox" name="chkbox" value="${manager.mgrSeq}" title="체크"></td>
                            <td onclick="detail(${manager.mgrSeq})" class="m_hidden no">${pageTotalCnt - status.index}</td>
                            <c:if test="${authorityCode eq '10'}"><td onclick="detail(${manager.mgrSeq})" class="m_hidden">${manager.companyName}</td></c:if>
                            <td onclick="detail(${manager.mgrSeq})">${manager.username}</td>
                            <td onclick="detail(${manager.mgrSeq})">${manager.name}</td>
                            <td class="m_hidden" onclick="detail(${manager.mgrSeq})">${manager.telephone}</td>
                            <td onclick="detail(${manager.mgrSeq})">${manager.authorityName}</td>
                            <td onclick="detail(${manager.mgrSeq})" class="m_hidden contractsName">${manager.contractsName}</td>
                            <td onclick="detail(${manager.mgrSeq})" class="m_hidden">${manager.mgrStatusName}</td>
                            <td onclick="detail(${manager.mgrSeq})" class="m_hidden"><javatime:format value="${manager.modified}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
                            <td><div class="btn_view"><button type="button" onclick="showDialog('${manager.mgrSeq}', '${pageNumber}')">표시</button></div></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty managerList}">
                <!-- 데이터 없을경우 -->
                <div class="nondata bdr2p">검색결과가 없습니다.<br>마스킹 처리된 항목은 정확히 입력하셔야 검색이 됩니다.</div>
            </c:if>

            <div class="btn_btmlist btn_lhid">
                <div class="btn_left btn_white"><button type="button" onclick="fn_delete()">삭제</button></div>
                <div class="btn_right">
                    <div class="btn_blk"><a href="javascript:void(0)" onclick="goEdit()">계정등록</a></div>
                </div>
            </div>

            <div class="paging">
                ${pageHtml}
            </div>
        </div>

    </div>
</div>
<!-- //content -->


<!-- Layer -->
<!--  하단 버튼한개-->
<div class="wrap_layerpop" id="failLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>계정 삭제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeLayer()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10"></span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="checkBoxRelieve()">확인</a></div>
        </div>
    </div>
</div>
<!-- // 하단 버튼한개 -->

<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="succLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>계정 삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="deleteAccount()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->

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
            <div class="inp_pop"><input name="reason" id="reason" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
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
            <td class="m_hidden per4"><input type="checkbox" name="chkbox" value="\${mgrSeq}" title="체크"></td>
            <td onclick="detail(\${mgrSeq})" class="m_hidden no">\${pageTotalCnt - (pageNumber * 30 + i)}</td>
            {{if authorityCode == '10'}}<td onclick="detail(\${mgrSeq})" class="m_hidden">\${companyName}</td>{{/if}}
            <td onclick="detail(\${mgrSeq})">\${username}</td>
            <td onclick="detail(\${mgrSeq})">\${name}</td>
            <td class="m_hidden" onclick="detail(\${mgrSeq})">\${telephone}</td>
            <td onclick="detail(\${mgrSeq})">\${authorityName}</td>
            <td onclick="detail(\${mgrSeq})" class="m_hidden contractsName">\${contractsName}</td>
            <td onclick="detail(\${mgrSeq})" class="m_hidden">\${mgrStatusName}</td>
            <td onclick="detail(\${mgrSeq})" class="m_hidden">\${modifiedToString}</td>
            <td><div class="btn_view"><button type="button" onclick="showDialog('\${mgrSeq}','\${pageNumber}')">표시</button></div></td>
        </tr>
    {{/each}}
</script>


<script type="text/html" id="accountTemplate">
    <td class="m_hidden per4"><input type="checkbox" name="chkbox" value="\${mgrSeq}" title="체크"></td>
    <td onclick="detail(\${mgrSeq})" class="m_hidden">\${no}</td>
    {{if authorityCode == '10'}}<td onclick="detail(\${mgrSeq})" class="m_hidden">\${companyName}</td>{{/if}}
    <td onclick="detail(\${mgrSeq})">\${username}</td>
    <td onclick="detail(\${mgrSeq})">\${name}</td>
    <td class="m_hidden" onclick="detail(\${mgrSeq})">\${telephone}</td>
    <td onclick="detail(\${mgrSeq})">\${authorityName}</td>
    <td onclick="detail(\${mgrSeq})" class="m_hidden contractsName">\${contractsName}</td>
    <td onclick="detail(\${mgrSeq})" class="m_hidden">\${mgrStatusName}</td>
    <td onclick="detail(\${mgrSeq})" class="m_hidden">\${modifiedToString}</td>
    <td><%--<div class="btn_view"><button type="button" onclick="showDialog('\${mgrSeq}','\${pageNumber}')">표시</button></div>--%></td>
</script>


<%-- Template ================================================================================ --%>

<script type="text/javascript">

    $(document).ready(function() {
        checkBoxControll();

        enterFunc($('#searchWord'), fn_search);
    });


    /** 체크박스 전체 선택/해제 */
    function checkBoxControll() {
        $('input[name="allCheck"]').click(function () {
            if ( $('input[name="allCheck"]').is(':checked')) {
                $(':checkbox[name="chkbox"]').prop('checked', true);
            } else {
                $(':checkbox[name="chkbox"]').prop('checked', false);
            }
        });

        $('input[name="chkbox"]').click(function () {
            var chkboxCnt = $('input[name="chkbox"]').length;
            var chkCnt = 0;
            $.each($('input[name="chkbox"]'), function () {
                if($(this).is(':checked') == true) {
                    chkCnt++;
                }
            });

            if(chkboxCnt == chkCnt) {
                $('input[name="allCheck"]').prop('checked', true);
            }else {
                $('input[name="allCheck"]').prop('checked', false);
            }

        });
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
            url: "${pageContext.request.contextPath}/accounts/listSearch",
            type: "GET",
            data : {status: $('#status').val(), column: $('#column').val(), searchWord: $('#searchWord').val(), page:currPage},
            async : false,
            success: function(result) {
                result.authorityCode = '${authorityCode}';
                $('tbody').empty();
                $('#accountListTemplate').tmpl(result).appendTo('tbody');
                $('.paging').empty().append(result.pageHtml);

                $('input[name="allCheck"]').prop('checked', false);
                checkBoxControll();

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

    /** 계정삭제버튼 클릭 */
    function fn_delete() {
        var mgrSeqs = new Array();
        var checkCnt = 0;
        var contCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function () {
            mgrSeqs.push($(this).val());
            var contractCnt = $(this).parent().siblings('.contractsName').text().length;
            contCnt += contractCnt;
            checkCnt++;
        });

        if(checkCnt == 0) {
            $('#failLayer .txt_top').html('삭제할 계정을 선택해주세요.');
            layerOpen('failLayer');
            return;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/deleteConfirm",
            type: "POST",
            data : {"mgrSeqs": mgrSeqs},
            success: function(result) {
                if(result.code == '00') {
                    if (contCnt > checkCnt) {
                        $('#succLayer .txt_top').html('해당 계정에 1개 이상의 계약을 관리하고 있습니다. 계정 삭제 시 관리되고 있는 계약은 모두 삭제 됩니다.');
                        layerOpen('succLayer');
                    } else {
                        $('#succLayer .txt_top').html('계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.');
                        layerOpen('succLayer');
                    }
                }else {
                    var msg = '';
                    if (result.code == '02') {
                        msg = 'Super-master계정은 삭제 불가능합니다.'
                    } else {
                        msg = 'Master계정은 삭제가 불가능 합니다.'
                    }
                    $('#failLayer .txt_top').html(msg);
                    layerOpen('failLayer');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }

    /** 체크박스 해제 */
    function checkBoxRelieve() {
        closeLayer();
        $('input[name="chkbox"]').prop('checked', false);
        $('input[name="allCheck"]').prop('checked', false);
    }

    /** 계정삭제 */
    function deleteAccount() {
        var mgrSeqs = new Array();
        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function () {
            mgrSeqs.push($(this).val());
            checkCnt++;
        });

        if(checkCnt == 0) {
            $('#failLayer .txt_top').html('삭제할 계정을 선택해주세요.');
            return;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/delete",
            type: "POST",
            data : {"mgrSeqs": mgrSeqs},
            success: function(result) {
                if(result.code == '00') {
                    closeLayer();
                    fn_search(0);
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
            url: "${pageContext.request.contextPath}/accounts/unmask",
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

    /** 상세 이동 */
    function detail(mgrSeq) {
        $(location).attr('href', '${pageContext.request.contextPath}/accounts/' + mgrSeq);

    }
    
    function goEdit() {
        $(location).attr('href', '${pageContext.request.contextPath}/accounts/edit');
    }

</script>