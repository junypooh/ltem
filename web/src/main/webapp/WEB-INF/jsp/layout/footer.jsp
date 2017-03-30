<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="footer">
    <div>
        <h2>GIGA B2B 홈매니저</h2>
        <div class="foot_txt">
            <ul class="clfix">
                <li><a href="javascript:void(0)" onclick="layerProvisionShow()">이용약관</a></li>
                <li><a href="javascript:void(0)" onclick="layerPolicyShow()">개인정보처리방침</a></li>
                <li class="m_hidden"><a href="#">LTE-M 개인정보처리방침</a></li>
                <li class="m_hidden"><a href="#">책임의 한계와 법적고지</a></li>
                <li class="m_hidden"><a href="#">이메일 무단수집거부</a></li>
                <li class="m_view"><a href="javascript:void(0)" onclick="viewPcVersion()">PC버전</a></li>
            </ul>
            <div class="copyright">
                <p>(주)케이티 대표이사 황창규&nbsp;&nbsp;사업자등록번호 102-81-42945&nbsp;&nbsp;케이티 통신판매업신고 2002-경기성남-0048</p>
                <p>463-711 경기도 성남시 분당구 불정로 90 (정자동 206번지)&nbsp;&nbsp;Contact : iotmakers@kt.com</p>
                <p>Copyright © 2015 KT Corp. All rights reserved.</p>
            </div>
        </div>
        <div class="familysite">
            <select name="" id="">
                <option value="">Family Site</option>
            </select>
        </div>
    </div>
</div>

<!-- layer pop -->
<div class="wrap_layerpop" id="autoLogoutConfirm" style="display: none;">
    <!-- //자동 로그아웃 -->
    <!-- 로그인시간연장 -->
    <div class="layerpop pop1">
        <div class="box_contpop">
            <span class="txt_top txt_titnon">60초 후 자동 로그아웃 됩니다. 안전한 사이트 이용을 위해 30분 동안 이용이 없을 경우 자동 로그아웃 됩니다.<span class="mt10">로그인 시간을 연장 하시겠습니까?</span></span>
            <div class="btn_col2">
                <div class="btn_wht_full fl btn_close2"><button type="button" onclick="$('#autoLogoutConfirm').remove()">닫기</button></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="loginContinue()">로그인시간연장</a></div>
            </div>
        </div>
    </div>
    <!-- //로그인시간연장 -->
</div>
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function(){
    });
    /** 로그인시간연장 (의미없는 server call) */
    function loginContinue() {
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/refreshSession",
            type: "GET",
            data : {},
            success: function(result) {
                $('#autoLogoutConfirm').hide();
                clearInterval(sessioninterval);
                sessioninterval = sessionTimer( sessionTimeOut );
            },
            error: function (e) {
                console.log(e);
            }
        });

    }

</script>