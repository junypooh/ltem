<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="isAuthenticated()">
    <security:authentication var="authorityCode" property="principal.authorityCd"/>
</security:authorize>

<!-- content -->
<div id="container">
    <div id="content">
        <div class="cont_top">
            <h2 class="h2_f">계정관리</h2>
            <div class="location"><a href="${pageContext.request.contextPath}/">홈</a> &gt; 계정관리</div>
        </div>

        <c:if test="${authorityCode eq '10'}">
        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li class="on"><a href="${pageContext.request.contextPath}/accounts">계정</a></li>
                    <li><a href="${pageContext.request.contextPath}/company">기업</a></li>
                </ul>
            </div>
        </div>
        </c:if>
        <div class="area_h3 area_h3_nx">
            <c:if test="${authorityCode eq manager.authorityCd and !unmask}">
            <div class="box_btnlist">
                <div class="btn_grey">
                    <button type="button" onclick="showDialog()">가려진 정보 표시</button>
                </div>
            </div>
            </c:if>
            <h3 class="h3_nor">상세정보</h3>
        </div>

        <div class="box_list2col">
            <form:form action="${pageContext.request.contextPath}/accounts/${manager.mgrSeq}" method="POST" commandName="manager" id="frm">
                <input type="hidden" name="password" id="passwordform" />
                <input type="hidden" name="reason" id="txtreason" />
                <input type="hidden" name="authorityCd" value="${manager.authorityCd}" />
                <form:hidden path="companyCd" />
                <div class="area_inp">
                    <dl>
                        <dt class="bg_line"><div>기업명</div></dt>
                        <dd><div>${manager.companyName}&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>아이디</div></dt>
                        <dd><div>${manager.username}</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>이름</div></dt>
                        <dd>
                            <div>${manager.name}</div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>휴대폰번호</div></dt>
                        <dd>
                            <div>${manager.telephone}</div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>이메일</div></dt>
                        <dd>
                            <div>${manager.email}</div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line tit2 m_flnon"><div>관리 계약건</div></dt>
                        <dd class="m_flnon">
                            <div>
                                <ul class="list_he">
                                    <c:forEach items="${contract}" var="contract">
                                        <li>${contract.name}(${contract.serialNumber})</li>
                                    </c:forEach>
                                    <c:if test="${empty contract}"><li>&nbsp;</li><li>&nbsp;</li></c:if>
                                </ul>
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line">
                        <div>권한</div>
                        </dt>
                        <dd>
                            <div>
                                ${manager.authorityName}
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>상태</div></dt>
                        <%--<dd><div><input type="radio" id="d_chkuse" <c:if test="${manager.mgrStatusCd eq '30'}">checked="checked"</c:if> name="d_chk"> <label for="d_chkuse">사용</label> <input type="radio" id="d_chknot" <c:if test="${manager.mgrStatusCd eq '40'}">checked="checked"</c:if> class="ml20" name="d_chk" disabled> <label for="d_chknot">일시정지</label></div></dd>--%>
                        <dd><div><c:if test="${manager.mgrStatusCd eq '30'}">사용</c:if><c:if test="${manager.mgrStatusCd eq '40'}">일시정지</c:if></div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>변경일시</div></dt>
                        <dd><div><javatime:format value="${manager.modified}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                    </dl>
                    <dl>
                        <dt class="bg_line"><div>최종로그인일시</div></dt>
                        <dd><div><javatime:format value="${manager.lastLoggedIn}" pattern="yyyy.MM.dd HH:mm:ss"/>&nbsp;</div></dd>
                    </dl>
                </div>
            </form:form>
        </div>

        <c:if test="${authorityCode < manager.authorityCd}">
        <div class="btn_btmlist">
            <div class="btn_left btn_white"><button type="button" onclick="fn_delete()">삭제</button></div>
            <div class="btn_right">
                <div class="btn_blk"><a href="javascript:void(0)" onclick="showDialog()">수정</a></div>
            </div>
        </div>
        </c:if>

    </div>
</div>
<!-- //content -->

<!-- layer pop -->
<!-- 가려진 정보 해제 -->
<div class="wrap_layerpop" id="layerPasswordConfirm" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>가려진 정보 해제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeDialog()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">정보수정을 위해서는 가려진 정보를 해제해야 수정이 가능합니다.<br>비밀번호와 사유를 입력해주세요. </span>
            <div class="inp_pop mb10"><input type="password" name="password" id="password" title="비밀번호" placeholder="비밀번호 입력"></div>
            <p class="colred mb10" style="display: none" id="passwordNotMatched">비밀번호가 일치하지 않습니다.<br>다시 확인해주세요.</p>
            <div class="inp_pop"><input id="reason" title="사유입력" class="textarea" placeholder="사유 입력(최대 30자)" maxlength="30"></div>
            <p class="colred mt10" id="reasonNotInput" style="display: none;">정보해제 사유를 입력해주세요.</p>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="$('#frm').submit(); layerPopDimmOff()">해제</a></div>
        </div>
    </div>
</div>
<!-- //가려진 정보 해제 -->



<!--  하단 버튼 두개-->
<div class="wrap_layerpop" id="deleteLayer" style="display: none;">
    <div class="layerpop pop2">
        <div class="box_titpop">
            <h2>계정 삭제</h2>
            <div class="btn_close"><button type="button" onclick="closeLayer()">닫기</button></div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.</span>
            <div class="btn_col2">
                <div class="btn_blk_full fl"><a href="javascript:void(0)" onclick="closeLayer()">취소</a></div>
                <div class="btn_blk_full fr"><a href="javascript:void(0)" onclick="accountDelete()">확인</a></div>
            </div>
        </div>
    </div>
</div>
<!-- // 하단 버튼두개 -->

<!--  하단 버튼한개-->
<div class="wrap_layerpop" id="failLayer" style="display: none;">
    <div class="layerpop pop2" >
        <div class="box_titpop">
            <h2>계정 삭제</h2>
            <div class="btn_close">
                <button type="button" onclick="closeLayer()">닫기</button>
            </div>
        </div>
        <div class="box_contpop">
            <span class="txt_top mb10">Master계정은 1건씩 삭제 가능합니다. <br>다시 선택해 주세요. </span>
            <div class="btn_blk_full"><a href="javascript:void(0)" onclick="closeLayer()">확인</a></div>
        </div>
    </div>
</div>
<!-- // 하단 버튼한개 -->

<!-- //layer pop -->

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


    /** 비밀번호 입력 layer open */
    function showDialog() {
        $('#passwordNotMatched').hide();
        $('#reasonNotInput').hide();
        layerOpen('layerPasswordConfirm');

        $('#password').val('');
        $('#reason').val('');
        $('#password').focus();
    }

    /** 비밀번호 입력 layer close */
    function closeDialog() {
        $('#layerPasswordConfirm').hide();
        layerPopDimmOff();
    }

    /** 계정삭제버튼 클릭 */
    function fn_delete() {

        // 마스터 이상의 권한은 삭제 할수 없음.
        if('${manager.authorityCd}' < 30) {
            $('#failLayer .box_contpop .txt_top').html('해당 계정은 삭제할수 없습니다.');
            layerOpen('failLayer');
            return false;
        }


        if ('${fn:length(contract)}' > 0) {
            $('#deleteLayer .box_contpop .txt_top').html('해당 계정에 1개 이상의 계약을 관리하고 있습니다. 계정 삭제 시 관리되고 있는 계약은 모두 삭제 됩니다.');
        } else {
            $('#deleteLayer .box_contpop .txt_top').html('계정을 삭제 하시겠습니까? <br>삭제된 계정은 복구가 불가능 합니다.');
        }

        layerOpen('deleteLayer');

    }

    /** 계정삭제 */
    function accountDelete() {
        closeLayer();

        $.ajax({
            url: "${pageContext.request.contextPath}/accounts/delete",
            type: "POST",
            data : {mgrSeqs: '${manager.mgrSeq}'},
            success: function(result) {
                if(result.code == '00') {
                    $(location).attr('href', '${pageContext.request.contextPath}/accounts');
                }else{
                    $('#failLayer .box_contpop .txt_top').html('해당 계정은 삭제할수 없습니다.');
                }
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