<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- layer pop -->
<div class="wrap_layerpop" id="layerProvision" style="display: none">

    <!-- 서비스 이용약관 -->
    <div class="layerpop pop_inf">
        <div class="box_titpop">
            <h2>서비스 이용 약관</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerProvision').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <div class="box_ter" id="provision">
            </div>
            <div class="btn_blk_mid"><a href="javascript:void(0)" onclick="$('#layerProvision').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
    <!-- //서비스 이용약관 -->

</div>
<!-- //layer pop -->

<!-- layer pop -->
<div class="wrap_layerpop" id="layerPolicy" style="display: none">

    <!-- 개인정보처리방침 -->
    <div class="layerpop pop_inf">
        <div class="box_titpop">
            <h2>개인정보처리방침</h2>
            <div class="btn_close"><button type="button" onclick="$('#layerPolicy').hide();layerPopDimmOff();">닫기</button></div>
        </div>
        <div class="box_contpop">
            <div class="sel_ter">
                <select name="termVersion" id="termVersion" onchange="policyVerChange()">
                </select>
            </div>
            <div class="box_ter" id="policy">
            </div>

            <div class="btn_blk_mid"><a href="javascript:void(0)" onclick="$('#layerPolicy').hide();layerPopDimmOff();">확인</a></div>
        </div>
    </div>
    <!-- //개인정보처리방침 -->

</div>
<!-- //layer pop -->

<script type="text/javascript">

    $(document).ready(function(){
    });

    /**
     * 이용약관
     */
    function layerProvisionShow() {
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/agree/provision",
            type: "GET",
            traditional: true,
            data : {categoryCd: '10'},
            success: function(result) {
                var contents;
                if (result.contents != null) {
                    contents = result.contents.replaceAll('\n', '<br>');
                }
                $('#provision').empty().append(contents);
            },
            error: function (e) {
                console.log(e);
            }
        });
        layerOpen('layerProvision');
    }

    /**
     * 개인정보처리방침
     */
    function layerPolicyShow() {
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/agree/policy",
            type: "GET",
            traditional: true,
            data : {categoryCd: '30', termSeq:''},
            success: function(result) {
                var option = [];
                option.push('<option value="'+ result.termList[0].termSeq +'">개인정보처리방침</option>');
                for(var i = 1; i < result.termList.length; i++) {
                    option.push('<option value="'+ result.termList[i].termSeq +'">이전 개인정보처리방침 | '+ result.termList[i].version +'</option>');
                }
                $('#termVersion').empty().append(option);

                var contents;
                if (result.term.contents != null) {
                    contents = result.term.contents.replaceAll('\n', '<br>')
                }
                $('#policy').empty().append(contents);
            },
            error: function (e) {
                console.log(e);
            }
        });
        layerOpen('layerPolicy');
    }

    /**
     * 개인정보처리방침 version 변경시
     */
    function policyVerChange() {
        // 조회
        $.ajax({
            url: "${pageContext.request.contextPath}/agree/policy",
            type: "GET",
            traditional: true,
            data : {categoryCd: '30', termSeq:$('#termVersion').val()},
            success: function(result) {
                var contents;
                if (result.term.contents != null) {
                    contents = result.term.contents.replaceAll('\n', '<br>');
                }
                $('#policy').empty().append(contents);
            },
            error: function (e) {
                console.log(e);
            }
        });


    }

</script>