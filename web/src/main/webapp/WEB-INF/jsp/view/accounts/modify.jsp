<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        </div>
        </c:if>

        <div class="area_h3 area_h3_nx">
            <%--
            <div class="box_btnlist">
                <div class="btn_grey">
                    <button type="button">가려진 정보 표시</button>
                </div>
            </div>
            --%>
            <h3 class="h3_nor">상세정보</h3>
        </div>

        <div class="box_list2col">
            <div class="area_inp">
                <form:form action="${pageContext.request.contextPath}/accounts/${accountInfo.mgrSeq}/save" method="POST" commandName="accountInfo" id="frm">
                    <dl>
                        <form:hidden path="companyCd"/>
                        <form:hidden path="companyName"/>
                        <dt class="bg_line"><div>기업명</div></dt>
                        <dd><div>${accountInfo.companyName}&nbsp;</div></dd>
                    </dl>

                    <dl>
                        <form:hidden path="userId"/>
                        <input type="hidden" name="tempId" value="${accountInfo.userId}">
                        <dt class="bg_line"><div>아이디</div></dt>
                        <dd><div>${accountInfo.userId}</div></dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="name">이름</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp"><form:input path="name" placeholder="${accountInfo.name}" maxlength="8"/></span>
                                <div class="txt_vi" id="nameAlert">
                                    <%--<p class="txt_des">이름을 입력해주세요.</p>--%>
                                    <form:errors path="name" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <form:hidden path="telephone"/>
                        <input type="hidden" name="tempTelephone" value="${accountInfo.telephone}">
                        <dt class="bg_line"><div>휴대폰번호</div></dt>
                        <dd><div>${accountInfo.telephone}</div></dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="email">이메일</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <input type="hidden" id="email" name="email">
                                <input type="text" id="email1" name="email1" class="d_email" placeholder="${fn:substring(accountInfo.email,  0, fn:indexOf(accountInfo.email, "@"))}" value="${fn:substring(accountInfo.email,  0, fn:indexOf(accountInfo.email, "@"))}"> @
                                <input type="text" id="email2" name="email2" class="emailbak" placeholder="${fn:substring(accountInfo.email, fn:indexOf(accountInfo.email, "@")+1 , -1)}" value="${fn:substring(accountInfo.email, fn:indexOf(accountInfo.email, "@")+1 , -1)}">
                                <div class="txt_vi" id="emailAlert">
                                    <form:errors path="email" cssClass="txt_des"/>
                                    <%--<p class="txt_des">이메일을 입력해주세요.</p>--%>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="authority">권한</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <c:if test="${accountInfo.authorityCd < 30}"><input type="hidden" name="authorityCd" value="${accountInfo.authorityCd}"></c:if>
                                <span class="inp_sp">
                                    <select name="authorityCd" id="authority" <c:if test="${accountInfo.authorityCd < 30}">disabled</c:if> onchange="authorityChange()">
                                        <option value="">선택하세요</option>
                                        <c:forEach items="${authorityCd}" var="authority">
                                            <option value="${authority.authorityCd}" <c:if test="${authority.authorityCd eq accountInfo.authorityCd}">selected</c:if>>${authority.authorityName}</option>
                                        </c:forEach>
                                    </select>
                                </span>
                                <div class="txt_vi" id="authorityCdAlert"><div class="txt_vi">
                                    <%--<p class="txt_des">권한을 선택해주세요.</p>--%>
                                    <form:errors path="authorityCd" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="manageContract">관리 계약건</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt4">
                                <span class="inp_sp">
                                    <select name="manageContract" id="manageContract" <c:if test="${accountInfo.authorityCd < 30}">disabled</c:if>>
                                        <option value="">계약명(Biz계약번호)</option>
                                        <c:forEach items="${bizContract}" var="bizContract">
                                            <option value="${bizContract.serialNumber}">${bizContract.name}(${bizContract.serialNumber})</option>
                                        </c:forEach>
                                    </select>
                                </span>
                                <span class="btn_blk btn_conf btn_add"><button type="button" onclick="bizContractAccountAdd()">추가</button></span>
                                <div class="txt_vi" id="serialNumberAlert">
                                    <form:errors path="serialNumber" cssClass="txt_des"/>
                                </div>

                                <!-- 리스트 -->
                                <div class="view_list <c:if test="${!empty contract}">on</c:if>">
                                    <ul>
                                        <c:forEach items="${contract}" var="contract" varStatus="status">
                                            <li <c:if test="${status.first}">class="list_first"</c:if>>
                                                <span>
                                                    <input type="hidden" name="serialNumber" value="${contract.serialNumber}">
                                                    <span class="first">${contract.name}(${contract.serialNumber}) </span>
                                                    <button type="button" class="btn_del" onclick="bizContractAccountDel(${contract.serialNumber})">삭제</button>
                                                </span>
                                            </li>
                                        </c:forEach>
                                        <%--<li class="list_first">
                                            <span>
                                                <span class="first">래미안 12차(1231231) </span><span>Master</span><button type="button" class="btn_del">삭제</button>
                                            </span>
                                        </li>--%>
                                    </ul>
                                </div>
                                <!-- //리스트 -->
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden"><div>상태</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="mgrStatusUse" checked="checked" name="mgrStatusCd" value="30"> <label for="mgrStatusUse">사용</label> <input type="radio" id="mgrStatusStop" class="ml20" name="mgrStatusCd" value="40"> <label for="mgrStatusStop">정지</label>
                                <div class="txt_vi" id="mgrStatusCdAlert">
                                    <form:errors path="mgrStatusCd" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>변경일시</div></dt>
                        <dd><div><javatime:format value="${accountInfo.modified}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>최종로그인일시</div></dt>
                        <dd><div><javatime:format value="${accountInfo.lastLoggedIn}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                    </dl>
                </form:form>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="fn_delete()">삭제</button></div>
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="accountCancel()">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff()">저장</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->


<!-- Layer -->
<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="deleteLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>계정 삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="accountDelete()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->

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
            <span class="txt_top mb10">Master계정은 1건씩 삭제 가능합니다. <br>다시 선택해 주세요. </span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
        </div>
    </div>
</div>
<!-- // 하단 버튼한개 -->

<!-- 취소버튼 클릭시 Layer  -->
<div class="wrap_layerpop" id="cancelLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>안내</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">변경내용 저장을 취소 하시겠습니까?</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="goList()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 취소버튼 클릭시 Layer -->
<!-- //Layer -->

<script type="text/javascript">
    
    $(document).ready(function(){
        if ('${accountInfo.authorityCd}' < 30 ) {
            $("input[name=mgrStatusCd]").attr("disabled",true);
            if ('${accountInfo.authorityCd}' == 20) $('#authority').append('<option selected>MASTER</option>');
            if ('${accountInfo.authorityCd}' == 10) $('#authority').append('<option selected>SUPER_MASTER</option>');
        }

        // 저장
        $("#frm").submit(function(){
            $('.txt_des').remove();
            // 이름 확인
            if ($.trim($('#name').val()) == '' || $.trim($('#name').val()) == null) {
                $('#nameAlert').append('<p class="txt_des">이름을 입력해주세요.</p>');
                return false;
            }

            // 이메일 확인
            if($.trim($('#email1').val()) == '' || $.trim($('#email2').val()) == '') {
                $('#emailAlert').append('<p class="txt_des">이메일을 입력해주세요.</p>');
                return false;
            } else {
                $('#email').val($('#email1').val() + '@' + $('#email2').val());
            }


            // 권한 선택
            if ($('#authority').val() == '') {
                $('#authorityCdAlert').append('<p class="txt_des">권한을 선택 해주세요.</p>');
                return false;
            }

            // 상태값
            if ($(':radio[name="mgrStatusCd"]:checked').val() != 40) {
                // 사용이라면
                // 관리계약건 (Supervisor , Staff 일경우)
                if ($('#authority').val() == 30 || $('#authority').val() == 40) {
                    if ($('.area_inp .view_list li').length < 1) {
                        $('#serialNumberAlert').append('<p class="txt_des">관리 계약건을 선택 해주세요.</p>');
                        return false;
                    }
                }

            } else {
                // 정지라면
                // Supervisor, Staff 일경우
                if ($('#authority').val() == 30 || $('#authority').val() == 40) {
                    // 관리 계약건이 있을경우
                    if ($('.area_inp .view_list li').length > 0) {
                        $('#mgrStatusCdAlert').append('<p class="txt_des">상태가 정지인 경우 관리 계약을 추가 할수 없습니다.</p>');
                        return false;
                    }
                }
            }

            // 상태 disabled 해제
            $("input[name=mgrStatusCd]").attr("disabled",false);

        });
        
    });

    /** 관리계약건 삭제 버튼 클릭 */
    function bizContractAccountDel(serialNumber) {
        $('.txt_des').remove();
        if ('${accountInfo.authorityCd}' < 30 ) {
            $('#serialNumberAlert').append('<p class="txt_des">해당 계정은 계약을 삭제 할 수 없습니다.</p>');
        } else {

            var obj = null;
            $.each($('input[name="serialNumber"]'), function () {
                if($(this).val() == serialNumber) {
                    obj = $(this).parent().parent();
                    $(this).remove();
                }
            });

            realTimeContractAddOrDel();

            obj.remove();


            var numchk = $('.area_inp .view_list li').length;
            if (numchk < 1) {
                $('.view_list').removeClass('on');
            }

        }

    }

    /** 관리계약건 추가버튼 클릭 */
    function bizContractAccountAdd() {

        $('.txt_des').remove();
        if ('${accountInfo.authorityCd}' < 30 ) {
            $('#serialNumberAlert').append('<p class="txt_des">해당 계정은 계약을 추가 할 수 없습니다.</p>');
        } else {
            var serialNumber = $('#manageContract').val();

            if (serialNumber == '') {
                alert('계약을 선택해주세요.');
                return;
            }

            var numchk = $('.area_inp .view_list li').length;
            var text = $("#manageContract option:selected").text();

            var option = "";
            if(numchk > 0) {
                option += '<li>';
            } else {
                option += '<li class="list_first">';
            }
            option += '<span>';
            option += '<input type="hidden" name="serialNumber" value="' + serialNumber + '">';
            option += '<span class="first">' + text + ') </span>';
            option += '<button type="button" class="btn_del" onclick="bizContractAccountDel('+serialNumber+')">삭제</button>';
            option += '</span>';
            option += '</li>';

            $('.view_list').addClass('on');
            $('.area_inp .view_list ul').append(option);

            realTimeContractAddOrDel();

        }
    }

    /** 관리계약건의 담당자 추가 및 삭제 일때 관리계약건 새로 조회 */
    function realTimeContractAddOrDel() {
        var serialNumbers = new Array();
        $.each($('input[name="serialNumber"]'), function () {
            serialNumbers.push($(this).val());
        });

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/realTimeBizContract",
            type: "GET",
            data : {'companyCd':$('#companyCd').val(), 'serialNumber':serialNumbers},
            async: false,
            success: function(result) {
                $('#manageContract').empty();
                var option = [];
                option.push('<option value="">계약명(Biz계약번호)</option>');
                for(var i = 0 ; i < result.length; i++) {
                    option.push('<option value="'+ result[i].serialNumber +'">' + result[i].name +'(' + result[i].serialNumber + ')</option>');
                }
                $('#manageContract').append(option);
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 권한 변경에 따른 관리 계약건 조회 */
    function authorityChange() {
        if($('#authority').val() != '') {

            var numchk = $('.area_inp .view_list li').length;
            if(numchk > 0) {
                var conf = confirm('관리중인 계약 건을 그대로 유지 하시겠습니까?');
                if(conf == false) {
                    $('.area_inp .view_list ul').empty();
                    $('.view_list').removeClass('on');
                }

            }

            // supervisor , staff 일 경우에만
            if ($('#authority').val() == '30' || $('#authority').val() == '40') {
                $("input[name=mgrStatusCd]").attr("disabled",false);
                $('#contractDl').css('display', 'block');
                realTimeContractAddOrDel($('#authority').val())
            } else {
                $('#contractDl').css('display', 'none');
                // 상태값 '사용'으로만 가능
                $('#mgrStatusUse').trigger("click");
                $("input[name=mgrStatusCd]").attr("disabled",true);
            }
        } else {
            $('#contractDl').css('display', 'none');
            $("input[name=mgrStatusCd]").attr("disabled",false);
        }
    }

    /** 상태값 변경 */
    $('input[type="radio"]').change(function () {
        // 정지라면
        if ($(this).val() == 40 && $('.area_inp .view_list li').length > 0) {
            var conf = confirm('정지로 변경하시면 관리계약건이 모두 삭제됩니다.');
            if(conf) {
                // 관리계약건 모두 삭제
                $('.area_inp .view_list ul').empty();
                $('.view_list').removeClass('on');
                realTimeContractAddOrDel();
            } else {
                $('#mgrStatusUse').trigger("click");
            }
        }
    });

    /** 계정삭제버튼 클릭 */
    function fn_delete() {

        // 마스터 이상의 권한은 삭제 할수 없음.
        if('${accountInfo.authorityCd}' < 30) {
            $('#failLayer .box_contpop .txt_top').html('해당 계정은 삭제할수 없습니다.');
            layerOpen('failLayer');
            return false;
        }


        if ($('.area_inp .view_list li').length > 0) {
            $('#deleteLayer .box_contpop .txt_top').html('해당 계정에 1개 이상의 계약을 관리하고 있습니다. 계정 삭제 시 관리되고 있는 계약은 모두 삭제 됩니다.');
        } else {
            $('#deleteLayer .box_contpop .txt_top').html('계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.');
        }

        layerOpen('deleteLayer');

    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }

    /** 계정삭제 */
    function accountDelete() {
        closeLayer();

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/delete",
            type: "POST",
            data : {mgrSeqs: '${accountInfo.mgrSeq}'},
            success: function(result) {
                if(result.code == '00') {
                    $(location).attr('href', '${pageContext.request.contextPath}/accounts');
                }else{
                    $('#failLayer .box_contpop .txt_top').html('해당 계정은 삭제할수 없습니다.');
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 취소버튼 클릭 */
    function accountCancel() {
        layerOpen('cancelLayer');
    }

    /** 리스트 화면으로 이동 */
    function goList() {
        $(location).attr('href', '${pageContext.request.contextPath}/accounts');
    }
    
</script>


