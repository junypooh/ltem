<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">서비스관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 서비스관리</div>
        </div>

        <div class="box_tab tab3 tab3mbn">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/notice">공지사항</a></li>
                    <li><a href="${pageContext.request.contextPath}/banner">배너</a></li>
                    <li class="ln2 on"><a href="${pageContext.request.contextPath}/term">서비스 <span>이용약관</span></a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 내용</h3>
        </div>

        <div class="box_list2col">
            <form:form method="POST" action="${pageContext.request.contextPath}/term/${termInfo.termSeq}/save" commandName="termInfo" id="frm">
                <div class="area_inp">
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>분류</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte1" name="categoryCd" value="10" <c:if test="${termInfo.categoryCd eq '10'}">checked</c:if>> <label for="d_chkusecte1">이용약관</label>
                                </span>
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte2" class="ml20" name="categoryCd" value="20" <c:if test="${termInfo.categoryCd eq '20'}">checked</c:if>> <label for="d_chkusecte2">개인정보 수집 및 이용동의</label>
                                </span>
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte3" class="ml20" name="categoryCd" value="30" <c:if test="${termInfo.categoryCd eq '30'}">checked</c:if>> <label for="d_chkusecte3">개인정보처리방침</label>
                                </span>
                                <div class="txt_vi" id="categoryCdAlert">
                                    <form:errors path="categoryCd"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="title">제목</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp widlg"><form:input path="title" maxlength="20"/></span>
                                <div class="txt_vi" id="titleAlert">
                                    <form:errors path="title"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="contents">내용</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp widlg">
                                    <form:textarea path="contents"/>
                                </span>
                                <div class="txt_vi" id="contentsAlert">
                                    <form:errors path="contents"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_bk m_hidden">
                        <dt class="tit2 bg_line"><div><label for="version">버전</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp numip"><form:input path="version"/></span>
                                <div class="txt_vi" id="versionAlert">
                                    <form:errors path="version"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>노출여부</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="d_chkuse" name="active" <c:if test="${termInfo.active}">checked</c:if>> <label for="d_chkuse">노출</label> <input type="radio" id="d_chknot" class="ml20" name="active" <c:if test="${!termInfo.active}">checked</c:if>> <label for="d_chknot">비노출</label>
                                <div class="txt_vi" id="activeAlert">
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성자</div></dt>
                        <dd><div id="createdBy">${termInfo.createdByMask} <span class="btn_grey2 btn_conf btn_add"><button type="button" onclick="showDialog()">표시</button></span></div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성일</div></dt>
                        <dd><div><javatime:format value="${termInfo.created}" pattern="yyyy.MM.dd HH:mm:ss"/></div></dd>
                    </dl>
                </div>
            </form:form>
        </div>

        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="layerOpen('deleteLayer')">삭제</button></div>
            <div class="btn_right">
                <%--<div class="btn_blk btn_non"><a href="#">미리보기</a></div>--%>
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/term">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit()">저장</a></div>
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
            <h2>삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">해당 게시물을 삭제하시겠습니까?</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="fn_delete()">확인</a></div>
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
                <button type="button" onclick="closeLayer()">닫기</button>
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
</div>
<!-- //가려진 정보 해제 -->
<!-- // Layer -->

<script type="text/javascript">

    $(document).ready(function () {

        // 저장
        $('#frm').submit(function () {
            $('.txt_des').remove();

            if($.trim($('#title').val()) == '') {
                $('#titleAlert').append('<p class="txt_des">제목을 입력해주세요.</p>');
                return false;
            }

            if($.trim($('#contents').val()) == '') {
                $('#contentsAlert').append('<p class="txt_des">내용을 입력해주세요.</p>');
                return false;
            }
            if($.trim($('#version').val()) == '') {
                $('#versionAlert').append('<p class="txt_des">버전을 입력해주세요.</p>');
                return false;
            }

        });

    });

    /** 가려진 정보 해제 layer show */
    function showDialog(mgrSeq, pageNumber) {
        layerOpen('layerPasswordConfirm');

        // 초기화
        $('#passWord').val('');
        $('#reason').val('');
        $('#passwordNotMatched').hide();
        $('#reasonNotInput').hide();


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
            url: "${pageContext.request.contextPath}/term/unmask",
            type: "POST",
            data : {'password':$('#passWord').val(), 'termSeq': ${termInfo.termSeq}, 'reason': $('#reason').val()},
            async : false,
            success: function(result) {
                if (result.errorCd == 0) {
                    $('#createdBy').text(result.info.createdBy);
                    closeLayer();
                } else {
                    $('#passwordNotMatched').html('비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.');
                    $('#passwordNotMatched').show();
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    }




    /** 계정삭제 */
    function fn_delete() {
        var termSeqs = new Array();
        termSeqs.push(${termInfo.termSeq});

        $.ajax({
            url: "${pageContext.request.contextPath}/term/delete",
            type: "POST",
            data : {'termSeqs':termSeqs},
            async : false,
            success: function(result) {
                $(location).attr('href', '${pageContext.request.contextPath}/term');
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

</script>