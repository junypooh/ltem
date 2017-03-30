<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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
                    <li class="on"><a href="${pageContext.request.contextPath}/notice">공지사항</a></li>
                    <li><a href="${pageContext.request.contextPath}/banner">배너</a></li>
                    <li class="ln2"><a href="#">서비스 <span>이용약관</span></a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 내용</h3>
        </div>


        <div class="box_list2col">
            <form:form method="POST" action="${pageContext.request.contextPath}/notice/${noticeInfo.noticeSeq}/save" commandName="noticeInfo" id="frm">
                <div class="area_inp">
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="title">제목</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp widlg"><form:input path="title" maxlength="50"/></span>
                                <div class="txt_vi" id="titleAlert">
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
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>노출여부</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="d_chkuse" name="isActive" value="true" <c:if test="${noticeInfo.isActive}">checked</c:if>> <label for="d_chkuse">노출</label> <input type="radio" id="d_chknot" class="ml20" name="isActive" value="false" <c:if test="${!noticeInfo.isActive}">checked</c:if>> <label for="d_chknot">비노출</label>
                                <div class="txt_vi" id="isActiveAlert">
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성자</div></dt>
                        <dd><div>${noticeInfo.manager.username}</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성일</div></dt>
                        <dd><div><javatime:format value="${noticeInfo.created}" pattern="yyyy.MM.dd HH:mm:ss"/></div></dd>
                    </dl>
                </div>
            </form:form>
        </div>

        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="layerOpen('deleteLayer')">삭제</button></div>
            <div class="btn_right">
                <%--<div class="btn_blk btn_non"><a href="#">미리보기</a></div>--%>
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/notice">취소</a></div>
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
            <h2>게시물 삭제</h2>
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

        });

    });

    /** 계정삭제 */
    function fn_delete() {
        var noticeSeqs = new Array();
        noticeSeqs.push(${noticeInfo.noticeSeq});

        $.ajax({
            url: "${pageContext.request.contextPath}/notice/delete",
            type: "POST",
            data : {'noticeSeqs':noticeSeqs},
            async : false,
            success: function(result) {
                $(location).attr('href', '${pageContext.request.contextPath}/notice');
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