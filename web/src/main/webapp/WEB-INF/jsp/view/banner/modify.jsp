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
            <h2 class="h2_f">서비스관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 서비스관리</div>
        </div>

        <div class="box_tab tab3 tab3mbn">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/notice">공지사항</a></li>
                    <li class="on"><a href="${pageContext.request.contextPath}/banner">배너</a></li>
                    <li class="ln2"><a href="${pageContext.request.contextPath}/term">서비스 <span>이용약관</span></a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 내용</h3>
        </div>

        <!-- 파일업로드 폼 -->
        <form id="logoUploadForm" action="${pageContext.request.contextPath}/uploadFile" method="post" enctype="multipart/form-data">
            <input type="file" name="uploadFile" id="logo" style="display:none" accept=".gif, .jpg, .jpeg, .png"/>
            <input type="hidden" name="code" value="banner" />
            <input type="hidden" name="type" id="type" value="" />
        </form>


        <div class="box_list2col">
            <div class="area_inp">
                <form:form action="${pageContext.request.contextPath}/banner/${bannerInfo.bannerSeq}/save" method="POST" commandName="bannerInfo" id="frm">
                    <form:hidden path="fileSeq"/>
                    <form:hidden path="createdBy"/>
                    <form:hidden path="created"/>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>배너 위치</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="d_chkuseps" name="categoryCd" value="1" <c:if test="${bannerInfo.categoryCd == 1}">checked</c:if>> <label for="d_chkuseps">메인(400*400)</label> <input type="radio" id="d_chkusepsb" class="ml20" name="categoryCd" value="2" <c:if test="${bannerInfo.categoryCd == 2}">checked</c:if>> <label for="d_chkusepsb">서브(260*260)</label>
                                <div class="txt_vi">
                                    <%--<p class="txt_des">배너 위치를 선택해주세요.</p>--%>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div><label for="name">배너명</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp widlg"><form:input path="name" maxlength="100"/></span>
                                <div class="txt_vi">
                                    <%--<p class="txt_des">배너명을 입력해주세요.</p>--%>
                                    <form:errors path="name" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit3"><div>배너 이미지</div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <div class="file_inp">
                                    <%--<input type="text" class="uploadfile" value="배너" disabled="disabled">--%>
                                    <label for="filesearch" class="btn_filesearch"><span>찾아보기</span></label>
                                    <input type="file" id="filesearch" class="hidden">
                                    <span class="logo_list">
                                        <c:if test="${not empty bannerInfo.fileSeq}">
                                            <button type="button" class="btn_logo" onclick="showLogo(${bannerInfo.fileSeq})">${bannerInfo.oriFileName}</button><button type="button" class="btn_del" onclick="delLogo(${bannerInfo.fileSeq})">삭제</button>
                                        </c:if>
                                    </span>
                                </div>
                                <span class="file_des">사이즈 260*260픽셀 / jpg, png파일만 업로드 가능</span>
                                <div class="txt_vi">
                                    <%--<p class="txt_des">이미지를 등록해주세요</p>--%>
                                    <form:errors path="fileSeq" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_bk">
                        <dt class="tit2 bg_line"><div><label for="link">URL</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp"><input type="text" id="link" name="link" value="${bannerInfo.link}" class="mr10"></span>
                                <input type="checkbox" name="newWindow" value="true" <c:if test="${bannerInfo.newWindow}">checked</c:if>>새창이동
                                <div class="txt_vi" id="linkAlert">
                                    <form:errors cssClass="txt_des" path="link" />
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_bk">
                        <dt class="tit2 bg_line"><div><label for="order">순서</label></div></dt>
                        <dd>
                            <div class="inp_txt inp_txt2">
                                <span class="inp_sp numip">
                                    <select id="order" name="order"<c:if test="${!bannerInfo.current}">disabled</c:if>>
                                        <option value="">선택</option>
                                        <option value="1" <c:if test="${bannerInfo.order == 1}"> selected</c:if>>1</option>
                                        <option value="2" <c:if test="${bannerInfo.order == 2}"> selected</c:if>>2</option>
                                        <option value="3" <c:if test="${bannerInfo.order == 3}"> selected</c:if>>3</option>
                                    </select>
                                </span>
                                <div class="txt_vi">
                                    <%--<p class="txt_des">순서를 입력해주세요</p>--%>
                                    <form:errors path="order" cssClass="txt_des"/>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl class="m_area">
                        <dt class="m_hidden tit2"><div>노출여부</div></dt>
                        <dd>
                            <div class="inp_txt2">
                                <input type="radio" id="d_chkuse" name="current" value="true" <c:if test="${bannerInfo.current}">checked</c:if>> <label for="d_chkuse">노출</label> <input type="radio" id="d_chknot" class="ml20" name="current" value="false" <c:if test="${!bannerInfo.current}">checked</c:if>> <label for="d_chknot">비노출</label>
                                <div class="txt_vi">
                                    <%--<p class="txt_des">노출여부를 선택해주세요.</p>--%>
                                </div>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성자</div></dt>
                        <dd><div>${bannerInfo.createdBy}</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성일</div></dt>
                        <dd><div><javatime:format value="${bannerInfo.created}" pattern="yyyy.MM.dd HH:mm:ss"/></div></dd>
                    </dl>
                </form:form>
            </div>
        </div>

        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="layerOpen('#deleteLayer')">삭제</button></div>
            <div class="btn_right">
                <%--<div class="btn_blk btn_non"><a href="#">미리보기</a></div>--%>
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/banner">취소</a></div>
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


<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="deleteLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>배너 삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">등록된 배너를 삭제하시겠습니까?</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="fn_delete()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function () {
        if('${bannerInfo.categoryCd}' == 1) {
            $('#type').val('BANNER');
        } else if ('${bannerInfo.categoryCd}' == 2) {
            $('#type').val('SUBBANNER');
        }

        // 배너이미지 찾아보기 버튼클릭
        $('.btn_filesearch').click(function(e) {
            e.preventDefault();
            $('#logoUploadForm #logo').click();

            if ($(":input:radio[name=categoryCd]:checked").val() == '1') {
                $('#type').val('BANNER');
            } else if($(":input:radio[name=categoryCd]:checked").val() == '2') {
                $('#type').val('SUBBANNER');
            }
        });

        $('#logoUploadForm #logo').change(function() {
            $(this).closest('form').submit();
            layerPopDimmOff();
        });

        /**
         * 배너이미지 업로드
         */
        $('#logoUploadForm').ajaxForm({
            success: function(response) {
                $('.logo_list').empty();
                $('.logo_list').append('<button type="button" class="btn_logo" onclick="showLogo(' + response.fileSeq + ')">'+response.oriFileName+'</button><button type="button" class="btn_del" onclick="delLogo(' + response.fileSeq + ')">삭제</button>');
                $("input[name='fileSeq']").val(response.fileSeq);
                $("input[name='oriFileName']").val(response.oriFileName);

            },
            error: function(e) {
                alert('계정 등록에 실패하였습니다.');
            }
        });

        categoryChange();

        /**
         * 노출여부 Change
         */
        $('input[name=current]').change(function () {
            if($(this).val() == 'true') {
                $('#order').attr('disabled', false);
            } else {
                $('#order').val('');
                $('#order').attr('disabled', true);
            }
        });

        $("#frm").submit(function(){

        });

    });

    /**
     * 로고 미리보기
     * @param fileSeq
     */
    function showLogo(fileSeq) {
        $('#logoImg').attr('src', '${pageContext.request.contextPath}/fileDown?type=' + $('#type').val() + '&fileSeq=' + fileSeq);
        layerOpen('layerLogo');
    }

    /**
     * 배너위치 변경시 배너이미지 삭제
     */
    function categoryChange() {
        $('input[name=categoryCd]').change(function () {
            delLogo();
        });
    }

    /**
     * 로고 삭제
     * @param fileSeq
     */
    function delLogo() {
        $('.logo_list').empty();
        $("input[name='fileSeq']").val('');
        console.log($("input[name='fileSeq']").val());
    }

    /** 계정삭제 */
    function fn_delete() {
        var bannerSeqs = new Array();
        bannerSeqs.push(${bannerInfo.bannerSeq});

        $.ajax({
            url: "${pageContext.request.contextPath}/banner/delete",
            type: "POST",
            data : {'bannerSeqs':bannerSeqs},
            async : false,
            success: function(result) {
                $(location).attr('href', '${pageContext.request.contextPath}/banner');
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

