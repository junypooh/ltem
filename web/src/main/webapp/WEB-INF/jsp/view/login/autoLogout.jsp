<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="container">
    <div id="content">
        <div class="box_sei">
            <div>
                <p>자동 로그아웃 되었습니다.<br>안전한 사용을 위해 30분간 미사용 시 자동으로 로그아웃 됩니다.<br>서비스를 계속 사용하시려면 로그인 버튼을 클릭하세요</p>
                <div class="btn_blk_mid"><a href="${pageContext.request.contextPath}/">메인으로 이동</a></div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function(){

        $.ajax({
            url: "${pageContext.request.contextPath}/logout",
            data: {},
            type: "POST",
            success: function (obj) {
            },
            error: function (e) {
            }
        });

    });
</script>