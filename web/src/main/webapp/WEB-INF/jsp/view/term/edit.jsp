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
            <h3 class="h3_nor">등록</h3>
        </div>

        <div class="box_list2col">
            <form:form method="POST" action="${pageContext.request.contextPath}/term/save" commandName="termInfo" id="frm">
                <div class="area_inp">
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>분류</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte1" name="categoryCd" value="10" checked> <label for="d_chkusecte1">이용약관</label>
                                </span>
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte2" class="ml20" name="categoryCd" value="20"> <label for="d_chkusecte2">개인정보 수집 및 이용동의</label>
                                </span>
                                <span class="radios">
                                    <input type="radio" id="d_chkusecte3" class="ml20" name="categoryCd" value="30"> <label for="d_chkusecte3">개인정보처리방침</label>
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
                                <span class="inp_sp widlg"><input type="text" id="title" name="title" maxlength="20"></span>
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
                                    <textarea name="contents" id="contents"></textarea>
                                </span>
                                <div class="txt_vi" id="contentsAlert">
                                    <form:errors path="contents"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_bk">
                        <dt class="tit2 bg_line"><div><label for="version">버전</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp numip"><input type="text" id="version" name="version"></span>
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
                                <input type="radio" id="d_chkuse" name="active" value="true" checked> <label for="d_chkuse">노출</label> <input type="radio" id="d_chknot" class="ml20" name="active" value="false"> <label for="d_chknot">비노출</label>
                                <div class="txt_vi" id="activeAlert">
                                </div>
                            </div>
                        </dd>
                    </dl>
                </form:form>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_right">
                <%--<div class="btn_blk btn_non"><a href="#">미리보기</a></div>--%>
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/term">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="$('#frm').submit()">저장</a></div>
            </div>
        </div>

    </div>
</div>
<!-- //content -->

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


</script>