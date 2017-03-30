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
                    <li><a href="${pageContext.request.contextPath}/notice">공지사항</a></li>
                    <li class="on"><a href="${pageContext.request.contextPath}/banner">배너</a></li>
                    <li class="ln2"><a href="${pageContext.request.contextPath}/term">서비스 <span>이용약관</span></a></li>
                </ul>
            </div>
        </div>

        <div class="area_h3 area_h3_nx">
            <h3 class="h3_nor">상세 내용</h3>
        </div>


        <div class="box_list2col">
            <form:form action="${pageContext.request.contextPath}/banner/${bannerInfo.bannerSeq}" method="POST" commandName="bannerInfo" id="frm">
                <input type="hidden" name="password" id="passwordform" />
                <input type="hidden" name="reason"  id="txtreason" />
                <form:hidden path="bannerSeq"/>
                <div class="area_inp">
                    <dl>
                        <dt class="bg_line"><div>배너 위치</div></dt>
                        <dd><div>${bannerInfo.categoryName}<c:if test="${bannerInfo.categoryCd == 1}">(400*400)</c:if><c:if test="${bannerInfo.categoryCd == 2}">(260*260)</c:if></div></dd>

                    </dl>

                    <dl>
                        <dt class="bg_line"><div>배너명</div></dt>
                        <dd><div>${bannerInfo.name}&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>배너이미지</div></dt>
                        <dd><div>
                            <button type="button" class="btn_logo" onclick="showLogo(${bannerInfo.fileSeq})">${bannerInfo.oriFileName}</button>
                        </div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>URL</div></dt>
                        <dd><div>${bannerInfo.link} / <input type="checkbox" <c:if test="${bannerInfo.newWindow}">checked</c:if> disabled>새창이동</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>순서</div></dt>
                        <dd><div>${bannerInfo.order}&nbsp;</div></dd>
                    </dl>

                    <dl>
                        <dt class="bg_line"><div>노출여부</div></dt>
                        <dd><div>${bannerInfo.currentName}&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성자</div></dt>
                        <dd><div>${bannerInfo.createdByMask}&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>작성일</div></dt>
                        <dd><div><javatime:format value="${bannerInfo.created}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                    </dl>
                </div>
            </form:form>
        </div>

        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="layerOpen('deleteLayer')">삭제</button></div>
            <div class="btn_right">
                <%--<div class="btn_blk btn_non"><a href="#">미리보기</a></div>--%>
                <div class="btn_blk"><a href="${pageContext.request.contextPath}/banner">취소</a></div>
                <div class="btn_blk"><a href="javascript:void(0)" onclick="showDialog()">수정</a></div>
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
            <span class="txt_top mb10">정보수정을 위해서는 가려진 정보를 해제해야 수정이 가능합니다.<br>비밀번호와 사유를 입력해주세요. </span>
            <div class="inp_pop mb10"><input type="password" name="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <div class="inp_pop"><input id="reason" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" id="reasonNotInput" style="display: none;">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff();">해제</a></div>
        </div>
    </div>
</div>
<!-- //가려진 정보 해제 -->

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
<!-- // Layer -->

<script type="text/javascript">

    $(document).ready(function () {
        <c:if test="${not empty doesNotPasswordMatched and doesNotPasswordMatched}">
        layerOpen('layerPasswordConfirm');
        $('#passwordNotMatched').show();
        </c:if>

        // password 저장
        $("#frm").submit(function(){

            if ($.trim($('#password').val()) == '') {
                $('#passwordNotMatched').html('비밀번호를 입력해주세요.');
                $('#passwordNotMatched').show();
                return false;
            } else {
                $('#passwordform').val($('#password').val());
            }

            if ($.trim($('#reason').val()) == '') {
                $('#reasonNotInput').show();
                return false;
            } else {
                $('#txtreason').val($('#reason').val());
            }

        });

    });

    /** 배너삭제 */
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
        layerPopDimmOff()();
    }

    /** 가려진 정보 해제 layer show */
    function showDialog() {
        layerOpen('layerPasswordConfirm');

        // 초기화
        $('#password').val('');
        $('#reason').val('');
        $('#passwordNotMatched').hide();
        $('#reasonNotInput').hide();
    }

    /**
     * 로고 미리보기
     * @param fileSeq
     */
    function showLogo(fileSeq) {
        var type;
        if('${bannerInfo.categoryCd == 1}') {
            type = 'BANNER';
        } else if ('${bannerInfo.categoryCd == 2}') {
            type = 'SUBBANNER';
        }

        $('#logoImg').attr('src', '${pageContext.request.contextPath}/fileDown?type=' + type + '&fileSeq=' + fileSeq);
        layerOpen('layerLogo');
    }


</script>