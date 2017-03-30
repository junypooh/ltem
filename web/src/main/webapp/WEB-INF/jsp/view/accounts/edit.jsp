<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="authorityCd" property="principal.authorityCd"/>
    <security:authentication var="companyCd" property="principal.companyCd"/>
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
            <h3 class="h3_nor">계정등록</h3>
        </div>

        <div class="box_list2col">
            <div class="area_inp">
                <form:form action="${pageContext.request.contextPath}/accounts/edit/save" method="POST" commandName="accountInfo" id="frm">
                    <dl class="m_area" <c:if test="${authorityCd ne '10'}">style="display: none" </c:if>>
                        <dt class="m_hidden tit2"><div><label for="companyCd">기업코드</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <div class="sech_inp">
                                    <%--<input type="text" name="companyCd" id="companyCd" title="검색" <c:if test="${authorityCd ne '10'}">value="${companyCd}" </c:if>>--%>
                                    <c:choose>
                                        <c:when test="${authorityCd ne '10'}">
                                            <form:input path="companyCd" title="검색" value="${companyCd}" />
                                        </c:when>
                                        <c:otherwise>
                                            <form:input path="companyCd" title="검색" placeholder="기업코드" />
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="btn_blk btn_conf"><a href="javascript:void(0)" onclick="searchCompany()">검색</a></span>
                                </div>
                                <div class="txt_vi" id="companyAlert">
                                    <form:errors path="companyCd" cssClass="txt_des"/>
                                </div>
                                <span class="btn_blk btn_conf2" style="display: none;" id="newCompanyEdit"><a href="${pageContext.request.contextPath}/company">신규 등록</a></span>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="userId">아이디</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt3">
                                <input type="hidden" id="tempId" name="tempId">
                                <span class="inp_sp"><form:input path="userId" placeholder="아이디"/></span>
                                <span class="btn_blk btn_conf btn_chk"><a href="javascript:void(0)" onclick="searchId()">중복 확인</a></span>
                                <div class="txt_vi" id="idAlert">
                                    <form:errors path="userId" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="name">이름</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">

                                <span class="inp_sp"><form:input path="name" placeholder="이름" maxlength="8"/></span>
                                <div class="txt_vi" id="nameAlert">
                                    <%--<p class="txt_des" id="nameAlert">이름을 입력해주세요.</p>--%>
                                    <form:errors path="name" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="telephone">휴대폰번호</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <input type="hidden" id="tempTelephone" name="tempTelephone">
                                <span class="inp_sp"><input type="text" id="telephone" name="telephone" placeholder="휴대폰번호" onkeyup="autoHypenPhone(this)" value="${accountInfo.telephone}" maxlength="14"></span>
                                <span class="btn_blk btn_conf btn_chk"><a href="javascript:void(0)" onclick="searchTelephone()">중복 확인</a></span>
                                <div class="txt_vi" id="telephoneAlert">
                                    <form:errors path="telephone" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="email">이메일</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <input type="hidden" id="email" name="email">
                                <input type="text" id="email1" name="email1" class="d_email" value="${fn:substring(accountInfo.email,  0, fn:indexOf(accountInfo.email, "@"))}"> @ <input type="text" id="email2" name="email2" class="emailbak" value="${fn:substring(accountInfo.email, fn:indexOf(accountInfo.email, "@")+1 , -1)}">
                                <div class="txt_vi" id="emailAlert">
                                    <form:errors path="email" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="authority">권한</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp">
                                    <select name="authorityCd" id="authority" onchange="authorityChange()">
                                        <option value="">선택하세요</option>
                                    </select>
                                </span>
                                <div class="txt_vi" id="authorityCdAlert">
                                    <form:errors path="authorityCd" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area" style="display: none;" id="contractDl">
                        <dt class="m_hidden tit2"><div><label for="manageContract">관리 계약건</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt4">
                                <span class="inp_sp">
                                    <select name="manageContract" id="manageContract">
                                        <option value="">계약명(Biz계약번호)</option>
                                    </select>
                                </span>
                                <span class="btn_blk btn_conf btn_add"><button type="button" onclick="bizContractAccountAdd()">추가</button></span>
                                <div class="txt_vi" id="serialNumberAlert">
                                    <form:errors path="serialNumber" cssClass="txt_des"/>
                                </div>

                                <!-- 리스트 -->
                                <div class="view_list">
                                    <ul>
                                        <%--
                                        <li class="list_first">
                                            <span>
                                                <span class="first">래미안 12차(1231231) </span><button type="button" class="btn_del">삭제</button>
                                            </span>
                                        </li>
                                        <li>
                                            <span>
                                                <span class="first">래미안 12차(1231231) </span><button type="button" class="btn_del">삭제</button>
                                            </span>
                                        </li>
                                        <li>
                                            <span>
                                                <span class="first">래미안 12차(1231231) </span><button type="button" class="btn_del">삭제</button>
                                            </span>
                                        </li>
                                        --%>
                                    </ul>
                                </div>
                                <!-- //리스트 -->
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>상태</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="mgrStatusUse" checked="checked" name="mgrStatusCd" value="30"> <label for="mgrStatusUse">사용</label> <input type="radio" id="mgrStatusStop" class="ml20" name="mgrStatusCd" value="40"> <label for="mgrStatusStop">정지</label>
                                <div class="txt_vi" id="mgrStatusCdAlert">
                                    <form:errors path="mgrStatusCd" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                </form:form>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="layerOpen('cancelLayer')">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff()">등록</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->

<!-- Layer -->
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

    $(document).ready(function () {

        <c:if test="${not empty error}">
        alert('${error}');
        </c:if>

        enterFunc($('#companyCd'), searchCompany);
        enterFunc($('#userId'), searchId);
        enterFunc($('#telephone'), searchTelephone);

        if ('${authorityCd}' != '10') {
            searchCompany();
        }


        // 저장
        $("#frm").submit(function(){

            $('.txt_des').remove();
            // 기업코드 확인
            if ($.trim($('#companyCd').val()) == '' || $('#companyCd').val() == null) {
                $('#companyAlert').append('<p class="txt_des">기업코드를 입력해주세요.</p>');
                return false;
            } else {
                if($('#companyAlert').attr('data') != '00') {
                    $('#companyAlert').append('<p class="txt_des">기업코드 검색을 해주세요.</p>');
                    return false;
                }
            }

            // 아이디 중복체크가 되어있는지 확인
            if ($.trim($('#userId').val()) == '' || $.trim($('#userId').val()) == null) {
                $('#idAlert').append('<p class="txt_des">아이디를 입력해주세요.</p>');
                return false;
            } else {
                var regType = /^[A-Za-z0-9_-]{6,12}$/;
                // 정규식 체크
                if (!regType.test($.trim($('#userId').val()))) {
                    $('#idAlert').append('<p class="txt_des">아이디는 6~12자 사이의 영문,숫자만 입력가능합니다.</p>');
                    return false;
                } else {

                    if($('#idAlert').attr('data') != '00') {
                        $('#idAlert').append('<p class="txt_des">아이디 중복확인을 해주세요.</p>');
                        return false;
                    } else {
                        // 중복확인을 한 후 Text 변경시
                        if($.trim($('#userId').val()) != $.trim($('#tempId').val())) {
                            $('#idAlert').append('<p class="txt_des">아이디 중복확인을 해주세요.</p>');
                            return false;
                        }
                    }

                }
            }

            // 이름 확인
            if ($.trim($('#name').val()) == '' || $.trim($('#name').val()) == null) {
                $('#nameAlert').append('<p class="txt_des">이름을 입력해주세요.</p>');
                return false;
            }

            // 휴대폰번호 확인
            if ($.trim($('#telephone').val()) == '' || $.trim($('#telephone').val()) == null) {
                $('#telephoneAlert').append('<p class="txt_des">휴대폰번호를 입력해주세요.</p>');
                return false;
            } else {
                if($('#telephoneAlert').attr('data') != '00') {
                    $('#telephoneAlert').append('<p class="txt_des">휴대폰번호 중복확인을 해주세요.</p>');
                    return false;
                } else {
                    // 중복확인을 한 후 Text 변경시
                    if($.trim($('#telephone').val()) != $.trim($('#tempTelephone').val())) {
                        $('#telephoneAlert').append('<p class="txt_des">휴대폰번호 중복확인을 해주세요.</p>');
                        return false;
                    }
                }
            }

            // 이메일 확인
            if($.trim($('#email1').val() + $('#email2').val()) == '') {
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

    /** 기업코드 검색 */
    function searchCompany() {

        if ($.trim($('#companyCd').val()) == '' || $('#companyCd').val() == null) {
            $('.txt_des').remove();
            $('#companyAlert').append('<p class="txt_des">기업코드를 입력해주세요.</p>');
            return;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/existsCompany",
            type: "GET",
            data : {companyCode: $('#companyCd').val()},
            async : false,
            success: function(result) {
                if (result.code == '00') {
                    var authoritys = result.authorityList;
                    var html = [];
                    html.push('<option value="">선택하세요</option>');
                    for(var i = 0; i < authoritys.length; i++) {
                        html.push('<option value="' + authoritys[i].authorityCd +'">'+ authoritys[i].authorityName +'</option>')
                    }
                    $('#authority').empty().append(html);
                    $('#newCompanyEdit').hide();
                } else{
                    $('#newCompanyEdit').show();
                }
                // superMaster일때만 remove
                if ('${authorityCd}' == '10') {$('.txt_des').remove();}
                $('#companyAlert').append('<p class="txt_des">'+result.msg+'</p>');
                $('#companyAlert').attr('data', result.code);

            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 아이디 중복확인 */
    function searchId() {
        $('.txt_des').remove();

        if($.trim($('#userId').val()) == '') {
            $('#idAlert').append('<p class="txt_des">아이디를 입력해주세요.</p>');
            return false;
        }


        var regType = /^[A-Za-z0-9_-]{6,12}$/;
        // 정규식 체크
        if (!regType.test($.trim($('#userId').val()))) {
            $('#idAlert').append('<p class="txt_des">아이디는 6~12자 사이의 영문,숫자만 입력가능합니다.</p>');
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/existsId",
            type: "GET",
            data : {id: $('#userId').val()},
            async : false,
            success: function(result) {
                $('#idAlert').append('<p class="txt_des">'+result.msg+'</p>');
                $('#idAlert').attr("data", result.code);

                if(result.code == '00') {
                    $('#tempId').val($('#userId').val());
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 휴대폰번호 중복확인 */
    function searchTelephone() {
        if (!(11 < $('#telephone').val().length && $('#telephone').val().length < 14)) {
            $('.txt_des').remove();
            $('#telephoneAlert').append('<p class="txt_des">휴대폰번호 형식에 맞지않습니다.</p>');
            return false;
        }

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/existsTelephone",
            type: "GET",
            data : {telephone: $('#telephone').val()},
            async : false,
            success: function(result) {
                $('.txt_des').remove();
                $('#telephoneAlert').append('<p class="txt_des">'+result.msg+'</p>');
                $('#telephoneAlert').attr("data", result.code);

                if(result.code == '00') {
                    $('#tempTelephone').val($('#telephone').val());
                }
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
                realTimeContractAddOrDel();
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

    /** 관리계약건 추가버튼 클릭 */
    function bizContractAccountAdd() {
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

    /** 관리계약건 삭제 버튼 클릭 */
    function bizContractAccountDel(serialNumber) {

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

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }

    /** 리스트 화면으로 이동 */
    function goList() {
        $(location).attr('href', '${pageContext.request.contextPath}/accounts');
    }


</script>