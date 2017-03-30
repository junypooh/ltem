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
            <div class="location"><a href="#">홈</a> &gt; 계정관리</div>
        </div>

        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/accounts">계정</a></li>
                    <li class="on"><a href="${pageContext.request.contextPath}/company">기업</a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 정보</h3>
        </div>

        <!-- 파일업로드 폼 -->
        <form id="logoUploadForm" action="${pageContext.request.contextPath}/uploadFile" method="post" enctype="multipart/form-data">
            <input type="file" name="uploadFile" id="logo" style="display:none" accept=".gif, .jpg, .jpeg, .png"/>
            <input type="hidden" name="code" value="${companyInfo.code}" />
            <input type="hidden" name="type" value="LOGO" />
        </form>

        <div class="box_list2col">
            <div class="area_inp">
                <form:form action="${pageContext.request.contextPath}/company/${companyInfo.code}/save" method="POST" commandName="companyInfo" id="frm">
                <form:hidden path="fileSeq" />
                <dl>
                    <dt class="bg_line"><div>기업코드</div></dt>
                    <dd><div>${companyInfo.code}</div></dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden tit2"><div><label for="name">기업명</label></div></dt>
                    <dd>
                        <div class="inp_txt inp_txt2">
                            <span class="inp_sp"><form:input path="name" placeholder="기업명" maxlength="30" />&nbsp;</span>
                            <div class="txt_vi" id="errorCompany">
                                <form:errors path="name" cssClass="txt_des" />
                                <%--<p class="txt_des">기업명을 입력해주세요.</p>--%>
                            </div>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden tit3"><div>로고</div></dt>
                    <dd>
                        <div class="inp_txt inp_txt2">
                            <div class="file_inp">
                                <label for="" class="btn_filesearch"><span>찾아보기</span></label>
                                <span class="logo_list">
                                    <c:if test="${not empty companyInfo.fileSeq}">
                                        <button type="button" class="btn_logo" onclick="showLogo(${companyInfo.fileSeq})">로고 확인</button><button type="button" class="btn_del" onclick="delLogo(${companyInfo.fileSeq})">삭제</button>
                                    </c:if>
                                </span>
                            </div>
                            <span class="file_des">사이즈 192*68 픽셀 / jpg, png파일만 업로드 가능</span>
                            <div class="txt_vi" id="errorLogo">
                                <form:errors path="fileSeq" cssClass="txt_des" />
                                <%--<p class="txt_des">로고를 등록해주세요.</p>--%>
                            </div>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden tit2"><div><label for="telephone">대표번호</label></div></dt>
                    <dd>
                        <div class="inp_txt inp_txt2">
							<span class="inp_sp">
								<form:input path="telephone" placeholder="대표번호" onkeyup="autoHypenPhone(this)" maxlength="14"/>
							</span>
                            <div class="txt_vi" id="errorTel">
                                <form:errors path="telephone" cssClass="txt_des" />
                                <%--<p class="txt_des">대표번호를 입력해주세요.</p>--%>
                            </div>
                        </div>
                    </dd>
                </dl>
                <dl class="m_area">
                    <dt class="m_hidden tit2"><div><label for="mgrSeq">담당자</label></div></dt>
                    <dd>
                        <div class="inp_txt inp_txt2">
                            <c:if test="${empty companyInfo.name}">
                            <p class="txt_pi">기업 등록을 위해서는 담당자(Master) 계정을 등록 해야합니다. </p>
                            </c:if>
                            <div class="box_sechinp">
                                <div class="sech_inp">
                                    <input type="hidden" name="mgrSeq" id="mgrSeq" value="${companyInfo.manager.mgrSeq}" />
                                    <input type="text" id="mgrNameView" value="${companyInfo.manager.name}<c:if test="${not empty companyInfo.manager.username}">(${companyInfo.manager.username})</c:if>" placeholder="담당자" disabled="disabled">
                                    <ul class="list_sech" style="display: none">
                                    </ul>
                                </div>
                                <div class="txt_vi" id="errorManager">
                                    <form:errors path="mgrSeq" cssClass="txt_des" />
                                    <%--<p class="txt_des">담당자를 입력해주세요.</p>--%>
                                </div>
                            </div>
                        </div>
                    </dd>
                </dl>
                </form:form>
                <%--<dl>
                    <dt class="bg_line"><div>상태</div></dt>
                    <dd><div>사용(?)</div></dd>
                </dl>--%>
                <c:if test="${empty companyInfo.manager.mgrSeq}">
                <form id="managerFrm" method="POST" action="${pageContext.request.contextPath}/company/manager/save">
                    <input type="hidden" name="authorityCd" value="20" />
                    <input type="hidden" name="mgrStatusCd" value="30" />
                    <input type="hidden" name="companyCd" value="${companyInfo.code}" />
                <dl class="m_area box_newap on" id="newManager">
                    <dt class="m_hidden tit4"><div><label for="username">신규등록</label></div></dt>
                    <dd>
                        <div class="inp_txt inp_txt3">
                            <input type="hidden" id="tempId" />
                            <span class="inp_sp"><input type="text" id="username" name="userId" placeholder="아이디"></span>
                            <span class="btn_blk btn_conf btn_chk"><a href="javascript:void(0)" onclick="searchId()">중복 확인</a></span>
                            <div class="txt_vi" id="idAlert">
                                <%--<p class="txt_des">현재 사용중인 아이디 입니다.</p>--%>
                            </div>
                        </div>
                    </dd>
                    <dd class="bdrtnon">
                        <div class="inp_txt inp_txt2">
                            <span class="inp_sp"><input type="text" id="mgrName" name="name" placeholder="이름"></span>
                            <div class="txt_vi" id="nameAlert">
                                <%--<p class="txt_des">이름을 입력해주세요.</p>--%>
                            </div>
                        </div>
                    </dd>
                    <dd class="bdrtnon">
                        <div class="inp_txt inp_txt3">
                            <input type="hidden" id="tempTelephone" />
                            <span class="inp_sp"><input type="text" id="mgrTelephone" name="telephone" placeholder="휴대폰번호" onkeyup="autoHypenPhone(this)" maxlength="14"></span>
                            <span class="btn_blk btn_conf btn_chk"><a href="javascript:void(0)" onclick="searchTelephone()">중복 확인</a></span>
                            <div class="txt_vi" id="telephoneAlert">
                                <%--<p class="txt_des">휴대폰 번호를 입력해주세요.</p>--%>
                            </div>
                        </div>
                        <div class="btn_btmlist">
                            <div class="btn_left margnon">
                                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#managerFrm')[0].reset();">취소</a></div>
                                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#managerFrm').submit(); layerPopDimmOff()">저장</a></div>
                            </div>
                        </div>
                    </dd>
                </dl>
                </form>
                </c:if>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="layerOpen('layerCancel')">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff()">저장</a></div>
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
            <%--<div class="view_logo_non"><span>로고이미지</span></div><!-- 로고 없을경우 -->--%>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerLogo').hide(); layerPopDimmOff()">확인</a></div>
        </div>
    </div>
    <!-- //로고 -->
</div>
<!-- //layer pop -->

<!-- layer pop -->
<div class="wrap_layerpop" id="layerCancel" style="display: none;">
    <!-- 사용자 해제 -->
    <div class="layerpop pop2">
        <div class="box_titpop">
            <c:if test="${empty companyInfo.name}">
            <h2>등록 취소</h2>
            </c:if>
            <c:if test="${not empty companyInfo.name}">
            <h2>안내</h2>
            </c:if>
            <div class="btn_close"><button type="button">닫기</button></div>
        </div>
        <div class="box_contpop">
            <c:if test="${empty companyInfo.name}">
            <span class="txt_top mb10">기업등록을 취소하시겠습니까?</span>
            </c:if>
            <c:if test="${not empty companyInfo.name}">
            <span class="txt_top mb10">변경내용 저장을 취소 하시겠습니까?</span>
            </c:if>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="$('#layerCancel').hide(); layerPopDimmOff()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="cancel()">확인</a></div>
            </div>
        </div>
    </div>
    <!-- //사용자 해제 -->
</div>
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function () {

        /**
         * 저장
         */
        $("#frm").submit(function(){

            $('.txt_des').remove();
            if (!$("input[name='name']").val()) {
                $('#errorCompany').append('<p class="txt_des">기업명을 입력해주세요.</p>');
                return false;
            }
            if (!$("input[name='fileSeq']").val()) {
                $('#errorLogo').append('<p class="txt_des">로고를 등록해주세요.</p>');
                return false;
            }
            if (!$("input[name='telephone']").val()) {
                $('#errorTel').append('<p class="txt_des">대표번호를 입력해주세요.</p>');
                return false;
            }

            if (!$("input[name='mgrSeq']").val()) {
                $('#errorManager').append('<p class="txt_des">담당자를 입력해주세요.</p>');
                return false;
            }

        });

        /**
         * 계정 신규 등록
         */
        $("#managerFrm").submit(function(){

            $('.txt_des').remove();

            // 아이디 중복체크가 되어있는지 확인
            if (!$("#username").val()) {
                $('#idAlert').append('<p class="txt_des">아이디를 입력해주세요.</p>');
                return false;
            } else {
                if($('#idAlert').attr('data') != '00') {
                    $('#idAlert').append('<p class="txt_des">아이디 중복확인을 해주세요.</p>');
                    return false;
                } else {
                    // 중복확인을 한 후 Text 변경시
                    if($.trim($('#username').val()) != $.trim($('#tempId').val())) {
                        $('#idAlert').append('<p class="txt_des">아이디 중복확인을 해주세요.</p>');
                        return false;
                    }
                }
            }

            // 이름 확인
            if (!$("#mgrName").val()) {
                $('#nameAlert').append('<p class="txt_des">이름을 입력해주세요.</p>');
                return false;
            }

            // 휴대폰번호 확인
            if (!$("#mgrTelephone").val()) {
                $('#telephoneAlert').append('<p class="txt_des">휴대폰번호를 입력해주세요.</p>');
                return false;
            } else {
                if($('#telephoneAlert').attr('data') != '00') {
                    $('#telephoneAlert').append('<p class="txt_des">휴대폰번호 중복확인을 해주세요.</p>');
                    return false;
                } else {
                    // 중복확인을 한 후 Text 변경시
                    if($.trim($('#mgrTelephone').val()) != $.trim($('#tempTelephone').val())) {
                        $('#telephoneAlert').append('<p class="txt_des">휴대폰번호 중복확인을 해주세요.</p>');
                        return false;
                    }
                }
            }

        });

        $('#managerFrm').ajaxForm({
            success: function(response) {
                console.log(response);
                if(response.errorCd == '00') {
                    $("input[name='mgrSeq']").val(response.manager.mgrSeq);
                    $("#mgrNameView").val(response.manager.name+"("+ response.manager.userId + ")");
                    $('#newManager').hide();
                } else if(response.errorCd == '01' || response.errorCd == '02') {
                    alert(response.msg);
                } else {
                    alert('계정 등록에 실패하였습니다.');
                }
            }
        });

        // 로고 찾아보기 버튼클릭
        $('.btn_filesearch').click(function(e) {
            e.preventDefault();
            $('#logoUploadForm #logo').click();
        });

        $('#logoUploadForm #logo').change(function() {
            $(this).closest('form').submit();
            layerPopDimmOff();
        });

        /**
         * 로고 업로드
         */
        $('#logoUploadForm').ajaxForm({
            success: function(response) {
                $('.logo_list').empty();
                $('.logo_list').append('<button type="button" class="btn_logo" onclick="showLogo(' + response.fileSeq + ')">로고 확인</button><button type="button" class="btn_del" onclick="delLogo(' + response.fileSeq + ')">삭제</button>');
                $("input[name='fileSeq']").val(response.fileSeq);
            },
            error: function(e) {
                alert('계정 등록에 실패하였습니다.');
            }
        });

    });

    /**
     * 로고 미리보기
     * @param fileSeq
     */
    function showLogo(fileSeq) {

        $('#logoImg').attr('src', '${pageContext.request.contextPath}/fileDown?type=LOGO&fileSeq=' + fileSeq);
        layerOpen('layerLogo');
    }

    /**
     * 로고 삭제
     * @param fileSeq
     */
    function delLogo(fileSeq) {
        $('.logo_list').empty();
        $("input[name='fileSeq']").val('');
    }

    /**
     * 취소 클릭
     */
    function cancel() {
        $(location).attr('href', '${pageContext.request.contextPath}/company');
    }

    /** 아이디 중복확인 */
    function searchId() {
        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/existsId",
            type: "GET",
            data : {id: $('#username').val()},
            async : false,
            success: function(result) {
                $('.txt_des').remove();
                $('#idAlert').append('<p class="txt_des">'+result.msg+'</p>');
                $('#idAlert').attr("data", result.code);

                if(result.code == '00') {
                    $('#tempId').val($('#username').val());
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    /** 휴대폰번호 중복확인 */
    function searchTelephone() {
        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/edit/existsTelephone",
            type: "GET",
            data : {telephone: $('#mgrTelephone').val()},
            async : false,
            success: function(result) {
                $('.txt_des').remove();
                $('#telephoneAlert').append('<p class="txt_des">'+result.msg+'</p>');
                $('#telephoneAlert').attr("data", result.code);

                if(result.code == '00') {
                    $('#tempTelephone').val($('#mgrTelephone').val());
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

</script>