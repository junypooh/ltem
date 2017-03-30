<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="container">
    <div class="wrap_login">
        <div>
            <section class="box_login box_logmn">
            <form method="post" id="loginFrm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="inp_id"><input type="text" name="username" id="login_id" title="아이디" placeholder="아이디"></div>
                <div class="inp_pass"><input type="password" name="password" id="login_pwd" title="비밀번호" placeholder="비밀번호"></div>
                <p class="txt_false" id="idPwd">아이디와 비밀번호가 일치하지 않습니다.<br>아이디 또는 비밀번호를 다시 한번 입력해주세요.</p><!-- 입력이 틀렸을 시 class on 노출 -->

                <div class="check_id">
                    <input type="checkbox" value="Y" name="idchk" id="idchk"> <label for="idchk">아이디 기억하기</label>
                </div>

                <div class="area_cap">
                    <span>아래의 보안문자를 입력해 주세요</span>
                    <div class="cap">
                        <div class="num"><img id="capchaImg" src=""></div>
                        <div class="btn_refresh"><button type="button" onclick="refresh()">새로고침</button></div>
                        <div class="inp_txt"><input type="text" name="captchaCode" id="captchaCode" title="보안문자 입력" placeholder="보안문자 입력"></div>
                        <p class="txt_false" id="secretCode">보안문자가 일치하지 않습니다. 다시 입력해주세요.</p><!-- 입력이 틀렸을 시 class on 노출 -->
                    </div>
                </div>

                <a href="javascript:void(0)" class="btn_login" onclick="login()">로그인</a>
                <div class="search_mem">
                    <a href="${pageContext.request.contextPath}/find/findId">아이디 찾기</a><a href="${pageContext.request.contextPath}/find/findPwd">비밀번호 찾기</a>
                </div>
            </form>
            </section>
            <section class="box_logbn">
                <div class="swiper-container">
                    <a class="arrow-left" href="#"></a> <a class="arrow-right" href="#"></a>
                    <div class="swiper-wrapper">
                        <c:forEach items="${banners}" var="banner">
                            <div class="swiper-slide"><a href="${banner.link}" <c:if test="${banner.newWindow}">target="_blank"</c:if>><img src="${pageContext.request.contextPath}/login/bannerDown?fileSeq=${banner.fileSeq}" alt=""></a></div>
                        </c:forEach>
                        <c:if test="${empty banners}">
                        <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_sample.jpg" alt=""></a></div>
                        <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_sample.jpg" alt=""></a></div>
                        <div class="swiper-slide"><a href=""><img src="${pageContext.request.contextPath}/resources/images/banner_sample.jpg" alt=""></a></div>
                        </c:if>
                    </div>
                    <!-- Add Pagination -->
                    <div class="swiper-pagination"></div>
                </div>
            </section>
        </div>
    </div>
</div>

<!-- layer pop -->
<div class="wrap_layerpop" id="layerDuplicate" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>중복 로그인</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerDuplicate').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">현재 해당 아이디로 접속 중입니다. 중복 로그인은 불가능 합니다.</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerDuplicate').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
</div>
<div class="wrap_layerpop" id="layerBlocking" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>계정 정지 상태</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerBlocking').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">해당 아이디는 정지 상태 입니다. 관리자에게 문의 부탁 드립니다.</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerBlocking').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
</div>
<div class="wrap_layerpop" id="layerBlank" style="display: none;">
    <div class="layerpop pop1">
        <div class="box_contpop">
            <span class="txt_top txt_titnon" id="layerText"></span></span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#layerBlank').hide();layerPopDimmOff();" id="layerBlankConfirm">확인</a></div>
        </div>
    </div>
</div>
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function () {

        <c:choose>
        <c:when test="${not empty error and not empty SPRING_SECURITY_LAST_EXCEPTION}">
            <c:choose>
                <c:when test="${not empty duplicate}">
                    layerOpen("layerDuplicate");
                </c:when>
                <c:when test="${not empty blocking}">
                    layerOpen("layerBlocking");
                </c:when>
                <c:when test="${not empty secretCode}">
                    $("#secretCode").addClass("on");
                </c:when>
                <c:otherwise>
                    $("#idPwd").addClass("on");
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
        $('#login_id').focus();
        </c:otherwise>
        </c:choose>


        // 캡챠 이미지를 제일 마지막에 로딩해야함.
        refresh();

        //최초 쿠키에 login_id라는 쿠키값이 존재하면
        var login_id = $.cookie('login_id');
        if (login_id != undefined) {
            //아이디에 쿠키값을 담는다
            $("#login_id").val(login_id);
            $("#login_id").css("background", "none");
            //아이디저장 체크박스 체크를 해놓는다
            $("#idchk").prop("checked", true);
        }

        enterFunc($('#login_id'), login);
        enterFunc($('#login_pwd'), login);
        enterFunc($('#captchaCode'), login);
    });

    /**
     * 보안 문자 갱신
     */
    function refresh() {
        var d = new Date();
        $('#capchaImg').attr('src', '${pageContext.request.contextPath}/login/captchaGenerateKey?' + d.getTime());
    }

    /**
     * 로그인 submit
     */
    function login() {
        //아이디 미입력시
        if($.trim($("#login_id").val()) == "") {
            layerOpen('layerBlank');
            $('#layerText').text('아이디를 입력해주세요.');
            $('#layerBlankConfirm').focus();
            $('#layerBlankConfirm').click(function() {
                $('#layerBlank').hide();
                layerPopDimmOff();
                $('#login_id').focus();
            });
            return;
        }
        //비밀번호 미입력시
        if($.trim($("#login_pwd").val()) == "") {
            layerOpen('layerBlank');
            $('#layerText').text('비밀번호를 입력해주세요.');
            $('#layerBlankConfirm').focus();
            $('#layerBlankConfirm').click(function() {
                $('#layerBlank').hide();
                layerPopDimmOff();
                $('#login_pwd').focus();
            });
            return;
        }
        //보안문자 미입력시
        if($.trim($("#captchaCode").val()) == "") {
            layerOpen('layerBlank');
            $('#layerText').text('보안문자를 입력해주세요.');
            $('#layerBlankConfirm').focus();
            $('#layerBlankConfirm').click(function() {
                $('#layerBlank').hide();
                layerPopDimmOff();
                $('#captchaCode').focus();
            });
            return;
        }

        //아이디저장 체크되어있으면 쿠키저장
        if ($("#idchk").prop("checked")) {
            $.cookie('login_id', $("#login_id").val(), {expires: 7, path: '/'});
            //아이디저장 미체크면 쿠키에 정보가 있던간에 삭제
        } else {
            $.removeCookie("login_id");
        }
        $('#loginFrm').submit();
    }

</script>