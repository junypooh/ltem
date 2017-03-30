CREATE TABLE ltem.manager (
  mgr_seq         BIGINT     NOT NULL PRIMARY KEY,
  authority_cd    SMALLINT   NOT NULL,
  company_cd      VARCHAR(3) NOT NULL REFERENCES ltem.company (company_cd),
  mgr_status_cd    INTEGER    NOT NULL,
  user_id         VARCHAR(12),
  passwd         VARCHAR(80),
  is_passwd_init   BOOLEAN,
  auth_number     VARCHAR(44),
  auth_sent       TIMESTAMP,
  name           VARCHAR(20),
  telephone      VARCHAR(44),
  email          VARCHAR(255),
  dept           VARCHAR(50),
  rank           VARCHAR(50),
  created        TIMESTAMP,
  created_by      VARCHAR(14),
  modified       TIMESTAMP,
  modified_by     VARCHAR(14),
  passwd_modified TIMESTAMP,
  last_logged_in   TIMESTAMP,
  passwd_fail_cnt  SMALLINT,
  captcha_fail_cnt SMALLINT,
  is_locked       BOOLEAN
)
WITH (
OIDS = FALSE
);

COMMENT ON TABLE ltem.manager IS '관리자';
COMMENT ON COLUMN ltem.manager.mgr_seq IS '관리자 시퀀스';
COMMENT ON COLUMN ltem.manager.authority_cd IS '권한 코드
10 : Super Master
20 : Master
30 : Supervisor
40 : Staff';
COMMENT ON COLUMN ltem.manager.company_cd IS '회사 코드 참조 (SUPER MASTER인 경우에는 NULL)';
COMMENT ON COLUMN ltem.manager.mgr_status_cd IS '관리자 상태 코드
10 : 대기
20 : 반려
30 : 사용
40 : 정지
50 : 삭제';
COMMENT ON COLUMN ltem.manager.user_id IS '아이디';
COMMENT ON COLUMN ltem.manager.passwd IS '비밀번호 (SHA256)';
COMMENT ON COLUMN ltem.manager.is_passwd_init IS '패스워드 초기화 여부 - true 일 경우 로그인시 패스워드 강제 변경 필요';
COMMENT ON COLUMN ltem.manager.auth_number IS '가장 최근 발송된 SMS 인증번호';
COMMENT ON COLUMN ltem.manager.auth_sent IS '가장 최근 발송된 SMS 인증 번호 발송 일시';
COMMENT ON COLUMN ltem.manager.name IS '이름';
COMMENT ON COLUMN ltem.manager.telephone IS '전화번호 (AES256)';
COMMENT ON COLUMN ltem.manager.email IS '이메일 (AES256)';
COMMENT ON COLUMN ltem.manager.dept IS '부서';
COMMENT ON COLUMN ltem.manager.rank IS '직급';
COMMENT ON COLUMN ltem.manager.created IS '관리자 생성 일시';
COMMENT ON COLUMN ltem.manager.created_by IS '생성자';
COMMENT ON COLUMN ltem.manager.modified IS '최종 수정 일시';
COMMENT ON COLUMN ltem.manager.modified_by IS '최종 수정자';
COMMENT ON COLUMN ltem.manager.passwd_modified IS '최종 비밀번호 변경 일시';
COMMENT ON COLUMN ltem.manager.last_logged_in IS '최종 로그인 일시';
COMMENT ON COLUMN ltem.manager.passwd_fail_cnt IS '로그인 실패 횟수';

GRANT ALL ON TABLE ltem.manager TO ltem_user;

INSERT INTO ltem.manager VALUES (
  102, 10, '001', 30, 'master1',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '최순실', '7bae52095b6ddbde45785b9b3474cdf2', /*010-2222-2222*/
                              '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                              /* whoareu@mail.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  103, 20, '001', 30, 'master2',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '정유라', '873e0bc28af19100f10949c7955360b9', /*010-3333-3333*/
                              '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                              /* whoareu@mail.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  101, 30, '001', 30, 'user', '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '트럼프', 'c3013dc4ddda60ed9c09ae873f49a9e9', /*010-1111-1111*/
                              '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                              /* whoareu@mail.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  104, 40, '001', 30, 'staff1',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '이재용', '873e0bc28af19100f10949c7955360b9', /*010-3333-3333*/
                              '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                              /* whoareu@mail.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  105, 40, '001', 30, 'staff2',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '사블리코바', '873e0bc28af19100f10949c7955360b9', /*010-3333-3333*/
                                '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                                /* whoareu@mail.com */
                                NULL, NULL, now(), 'flyway', NULL, NULL,
                                now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  106, 10, '001', 30, 'kjh56k',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '김재현', '03ba836ea0a3a37a387fb90a283ebaa7', /*010-7748-2016*/
                              'af1442c717fa3023e9a49ce993605209',
                              /* kim.jh@kt.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);


INSERT INTO ltem.manager VALUES (
  107, 10, '001', 30, 'selena',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '최선영', '4856e556422b2a79fc0afee1e881be56', /*010-8618-0615*/
                              '669d76a688e1a81f8ac401cfd0820f2484067caa40bb3afe25962898d05f458c',
                              /* selena0615@naver.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);

INSERT INTO ltem.manager VALUES (
  108, 10, '001', 30, 'leenk95',
       '27abde80e5e182163155de9dff87a452db9ef1de9ae81c535baab107bef2a753bf1477cdd03b2bda'
  , FALSE, NULL, NULL, '이나경', 'b6ae08bc46105874a654499772fe1eae', /* 010-4439-2773*/
                              '867165d2d77b1ddf1c14734726e1323b251d296ec20d2605e5fcda0df4884efd',
                              /* whoareu@mail.com */
                              NULL, NULL, now(), 'flyway', NULL, NULL,
                              now() - INTERVAL '5 day', NULL,
  0, 0, FALSE);