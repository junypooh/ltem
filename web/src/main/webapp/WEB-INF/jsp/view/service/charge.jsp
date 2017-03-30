<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div id="container">
    <div id="content">
        <div class="box_tab">
            <div class="tab_top">
                <ul class="clfix">
                    <li><a href="${pageContext.request.contextPath}/service/introduce">서비스 소개</a></li>
                    <li class="on"><a href="javascript:void(0)">요금안내</a></li>
                </ul>
            </div>
        </div>

        <div class="box_payinfo">
            <h3>서비스 이용요금</h3>

            <!-- table list -->
            <div class="box_tbllist">
                <table summary="구분,회선당 월요금">
                    <caption>서비스 이용요금</caption>
                    <thead>
                    <tr>
                        <th scope="col">구분</th>
                        <th scope="col">회선당 월요금</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>무약정</td>
                        <td>11,000원</td>
                    </tr>
                    <tr>
                        <td>1년 계약</td>
                        <td>8,800원</td>
                    </tr>
                    <tr>
                        <td>2년 계약</td>
                        <td>6,600원</td>
                    </tr>
                    <tr>
                        <td>3년 계약</td>
                        <td>4,400원</td>
                    </tr>
                    <tr class="last">
                        <td>4년 계약</td>
                        <td>2,200원</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
            <ul class="txt_btm">
                <li>※ 추가 기종 서비스이용료 (홈IoT 기기 10종 까지) 및 데이터이용료 포함 / 홈IoT 기기는 Z-Wave 모듈 탑재된 단말</li>
                <li>※ 10종 초과 시 별도 협의</li>
                <li>※ LTE 데이터이용료은 회선당 월 100M 기본 제공하며, 총량으로 계산</li>
            </ul>
            <p class="txt_pblu">[예시] 300회선의 경우 기본제공 데이터량 = 300 회선 x 100M = 30,000M = 29.3G<br>다만, 총량을 초과하는 경우 1M당 500원 추가 요금 부과</p>
        </div>

        <div class="box_payinfo">
            <h3>회선수 추가 할인율</h3>

            <!-- table list -->
            <div class="box_tbllist">
                <table summary="회선수, 할인율">
                    <caption>서비스 이용요금</caption>
                    <thead>
                    <tr>
                        <th scope="col">회선수</th>
                        <th scope="col">할인율</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>55~99회선</td>
                        <td>5%</td>
                    </tr>
                    <tr>
                        <td>100~299회선</td>
                        <td>10%</td>
                    </tr>
                    <tr>
                        <td>300~599회선</td>
                        <td>15%</td>
                    </tr>
                    <tr>
                        <td>600~999회선</td>
                        <td>20%</td>
                    </tr>
                    <tr class="last">
                        <td>1,000회선~</td>
                        <td>25%</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
            <ul class="txt_btm">
                <li>※ 중도 해지 시 할인액 반환금 기준으로 요금 계산 후 잔여금 있는 경우 환불 처리</li>
                <li>※ 할인액 반환금 = {회선수 x 이용월수 x [*할인전 서비스료(월) – 약정서비스이용료(월) ]x (1 - 다량 회선별 추가 할인율)}</li>
            </ul>
            <p class="txt_pnor">* 할인전 서비스료: 할인액 반환금에 적용되는 할인전 서비스료는 고객의 실제 이용 </p>
            <ul class="txt_btm txt_btm2">
                <li>기간이 1년 미만은 무약정 서비스 이용료(11,000원)</li>
                <li>1년 이상 2년 미만은 1년 약정 서비스 이용료(8,800원)</li>
                <li>2년 이상 3년 미만은 2년 약정 서비스 이용료 (6,600원)</li>
                <li>3년 이상 4년 미만은 3년 약정 서비스 이용료(4,400원)</li>
            </ul>
            <p class="txt_pblu">[예시]  300회선 2년 계약 후 13개월 차 계약 종료한 경우 할인액 반환금<br>300회선 x [13개월 x (8,800원 - 6,600원) x (1 - 0.15)]</p>
        </div>

        <!-- tab content-->
        <div class="tab_info">
            <ul>
                <li class="tab1 on"><button type="button">IoT 홈 허브</button></li>
                <li class="tab2"><button type="button">홈IoT 기기</button></li>
                <li class="tab3"><button type="button">설치비</button></li>
            </ul>
        </div>
        <div class="tf_cont tf_cont1 on">
            <div class="txt_tp">
                <span>단말 가격 :  미정 </span>
                <span>홈허브 대수별 할인율</span>
            </div>
            <!-- table list -->
            <div class="box_tbllist">
                <table summary="회선수, 할인율">
                    <caption>서비스 이용요금</caption>
                    <thead>
                    <tr>
                        <th scope="col">회선수</th>
                        <th scope="col">할인율</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>55~99회선</td>
                        <td>5%</td>
                    </tr>
                    <tr>
                        <td>100~299회선</td>
                        <td>10%</td>
                    </tr>
                    <tr>
                        <td>300~999회선</td>
                        <td>15%</td>
                    </tr>
                    <tr class="last">
                        <td>1,000회선~</td>
                        <td>20%</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
            <ul class="txt_btm">
                <li>※ 할인율을 적용한 금액은 중도 해지 시 환불 불가</li>
                <li>※ 단말 가격은 기종에 따라 변경 될 수 있음</li>
                <li>※ 50회선 미만 정상 가격 부과</li>
            </ul>
        </div>
        <div class="tf_cont tf_cont2">
            <div class="txt_tp">
                <span>단말 가격 : (부가세 포함) </span>
            </div>
            <!-- table list -->
            <div class="box_tbllist">
                <table summary="기기, 가격(일시불)">
                    <caption>단말가격(부가세 포함)</caption>
                    <thead>
                    <tr>
                        <th scope="col">기기</th>
                        <th scope="col">가격(일시불)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>플러그</td>
                        <td>37,400원</td>
                    </tr>
                    <tr>
                        <td>가스안전기</td>
                        <td>110,000원</td>
                    </tr>
                    <tr>
                        <td>열림감지기</td>
                        <td>30,000원</td>
                    </tr>
                    <tr class="last">
                        <td>1,000회선~</td>
                        <td>20%</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
            <ul class="txt_btm">
                <li>※ 중도해지 시 환불 금액 없음(고객측 소유)</li>
            </ul>

            <div class="txt_tp tp2">
                <span>단말 대수별 할인율</span>
            </div>
            <!-- table list -->
            <div class="box_tbllist">
                <table summary="회선수, 할인율">
                    <caption>서비스 이용요금</caption>
                    <thead>
                    <tr>
                        <th scope="col">회선수</th>
                        <th scope="col">할인율</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>55~99회선</td>
                        <td>5%</td>
                    </tr>
                    <tr>
                        <td>100~299회선</td>
                        <td>10%</td>
                    </tr>
                    <tr>
                        <td>300~999회선</td>
                        <td>15%</td>
                    </tr>
                    <tr class="last">
                        <td>1,000회선~</td>
                        <td>20%</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
        </div>
        <div class="tf_cont tf_cont3">
            <div class="txt_tp">
                <span>부가세 포함</span>
            </div>
            <!-- table list -->
            <div class="box_tbllist">
                <table summary="구분, 이용요금, 비고">
                    <caption>설치비</caption>
                    <thead>
                    <tr>
                        <th scope="col">구분</th>
                        <th scope="col">이용요금</th>
                        <th scope="col">비고</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="last">
                        <td>설치비</td>
                        <td>건당 11,000원</td>
                        <td>-</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <!-- //table list -->
            <ul class="txt_btm">
                <li>※ 설치비 면제 조건<br>- 2년 약정 가입 시, 설치비 면제</li>
            </ul>
        </div>
        <!-- //tab content-->
    </div>
</div>