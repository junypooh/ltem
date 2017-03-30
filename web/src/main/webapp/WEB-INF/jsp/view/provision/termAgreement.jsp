<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- content -->
<div id="container">
    <form id="frm">
        <input type="hidden" name="termSeqs" id="termSeqs">
    </form>
    <div class="wrap_member">
        <div>
            <section class="box_login">
                <div class="h2_web">
                    <h2>서비스 약관 동의</h2>
                </div>
                <div class="area_inp ter_tm">

                    <c:forEach items="${terms}" var="terms" varStatus="status">
                        <h3 class="h3_mt20">${terms.categoryName}[필수]</h3>
                        <div class="txt_ter">
                            <div>
                                ${fn:replace(terms.contents, crcn, '<br/>')}
                            </div>
                        </div>
                        <input type="checkbox" name="chkbox" id="chk_ter${status.index}" class="chk_ter" value="${terms.termSeq}"><label for="chk_ter${status.index}">동의합니다</label>
                    </c:forEach>

<%--

                    <h3>서비스이용약관[필수]</h3>
                    <div class="txt_ter">
                        <div>
                            <p>제1조 목적</p>
                            <p>본 약관은 서비스 이용자가 닷컴(이하 “회사”라 합니다)이 제공하는 온라인상의 인터넷 서비스(이하 “서비스”라고 하며, 접속 가능한 유∙무선 단말기의 종류와는 상관없이 이용 가능한 “회사”가 제공하는 모든 “서비스”를 의미합니다. 이하 같습니다)에 회원으로 가입하고 이를 이용함에 있어 회사와 회원(본 약관에 동의하고 </p>
                        </div>
                    </div>
                    <input type="checkbox" name="chkbox" id="chk_ter1" class="chk_ter"><label for="chk_ter1">동의합니다</label>

                    <h3 class="h3_mt20">개인정보 수집 및 이용동의[필수]</h3>
                    <div class="txt_ter">
                        <div>
                            <p>제1조 목적</p>
                        </div>
                    </div>
                    <input type="checkbox" name="chkbox" id="chk_ter2" class="chk_ter"><label for="chk_ter2">동의합니다</label>
--%>


                    <div class="box_allchk">
                        <input type="checkbox" name="allCheck" id="chk_terall" class="chk_ter"><label for="chk_terall">필수 약관 전체 동의</label>
                    </div>
                </div>
                <div class="btn_list">
                    <a href="javascript:void(0)" onclick="layerOpen('cancelLayer')" class="btn_left">취소</a><a href="javascript:void(0)" onclick="fn_save()" class="btn_right">확인</a>
                </div>
            </section>
        </div>
    </div>
</div>
<!-- //content -->

<!-- Layer -->
<!--  하단 버튼한개-->
<div class="wrap_layerpop" id="failLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>안내</h2>
            <div class="btn_close">
                <button type="button" onclick="closeLayer()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">필수항목을 선택해주세요</span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
        </div>
    </div>
</div>
<!-- // 하단 버튼한개 -->

<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="cancelLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>안내</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">약관 동의를 취소하시면 재 로그인 후 약관을 동의하셔야 서비스 이용이 가능합니다.<br>취소하시겠습니까?</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="fn_cancel()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->
<!-- // Layer -->

<script type="text/javascript">

    $(document).ready(function() {
        checkBoxControll();
    });


    /** 체크박스 전체 선택/해제 */
    function checkBoxControll() {
        $('input[name="allCheck"]').click(function () {
            if ( $('input[name="allCheck"]').is(':checked')) {
                $(':checkbox[name="chkbox"]').prop('checked', true);
            } else {
                $(':checkbox[name="chkbox"]').prop('checked', false);
            }
        });

        $('input[name="chkbox"]').click(function () {
            var chkboxCnt = $('input[name="chkbox"]').length;
            var chkCnt = 0;
            $.each($('input[name="chkbox"]'), function () {
                if($(this).is(':checked') == true) {
                    chkCnt++;
                }
            });

            if(chkboxCnt == chkCnt) {
                $('input[name="allCheck"]').prop('checked', true);
            }else {
                $('input[name="allCheck"]').prop('checked', false);
            }

        });
    }

    /** 레이어팝업 닫기 */
    function closeLayer() {
        $('.wrap_layerpop').hide();
        layerPopDimmOff();
    }

    /**
     * 약관 동의 저장
     */
    function fn_save() {

        var termSeqs = new Array();
        var checkCnt = 0;
        $(':checkbox[name="chkbox"]:checked').each(function () {
            termSeqs.push($(this).val());
            checkCnt++;
        });

        if($(':checkbox[name="chkbox"]').length > checkCnt) {
            layerOpen('failLayer');
            return;
        }

        $('#termSeqs').val(termSeqs);

        $('#frm').attr('action', '${pageContext.request.contextPath}/provision/termAgreement/save');
        $('#frm').attr('method', 'get');
        $('#frm').submit();

    }

    /**
     * 약관 취소
     */
    function fn_cancel() {
        $('#frm').attr('action', '${pageContext.request.contextPath}/provision/termAgreement/cancel');
        $('#frm').attr('method', 'get');
        $('#frm').submit();
    }

</script>