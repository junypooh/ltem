/*
CREATE TABLE public.spot_dev_bas
(
  spot_dev_seq numeric(19,0) NOT NULL, -- 현장장치기본 현장장치일련번호
  svc_tgt_seq numeric(10,0) NOT NULL, -- 현장장치기본 서비스대상일련번호
  dev_uu_id character varying(64) NOT NULL, -- 현장장치기본 장치UU아이디
  dev_group_id character varying(10), -- 현장장치기본 장치그룹아이디
  dev_model_seq numeric(10,0) NOT NULL, -- 현장장치기본 장치모델일련번호
  spot_dev_id character varying(64) NOT NULL, -- 현장장치기본 현장장치아이디
  dev_nm character varying(50) NOT NULL, -- 현장장치기본 장치명
  phys_dev_yn character varying(1) DEFAULT 'Y'::character varying, -- 현장장치기본 물리장치여부
  use_yn character varying(1) NOT NULL DEFAULT 'Y'::character varying, -- 현장장치기본 사용여부
  tmp_dev_yn character varying(1), -- 현장장치기본 임시장치여부
  ipadr character varying(39), -- 현장장치기본 IP주소
  athn_no character varying(64), -- 현장장치기본 인증번호
  athn_forml_cd character varying(4), -- 현장장치기본 인증방식코드
  colec_cycl_time numeric(10,0), -- 현장장치기본 수집주기시간
  idle_jdgm_base_time numeric(10,0), -- 현장장치기본 유휴판단기준시간
  recn_cycl_time numeric(10,0), -- 현장장치기본 재접속주기시간
  recn_try_tmscnt numeric(5,0), -- 현장장치기본 재접속시도횟수
  prod_seq character varying(64), -- 현장장치기본 상품일련번호
  reg_seq character varying(64), -- 현장장치기본 등록일련번호
  frmwr_ver_no character varying(15), -- 현장장치기본 펌웨어버전번호
  last_mtn_dt timestamp without time zone, -- 현장장치기본 최종동작일시
  eqp_lo_sbst character varying(50), -- 현장장치기본 설치위치내용
  trobl_admr_id character varying(20), -- 현장장치기본 장애관리자아이디
  dev_sttus_cd character varying(2), -- 현장장치기본 장치상태코드
  latit_val numeric(9,6), -- 실시간위치데이터내역 위도값
  latit_div_cd character varying(1), -- 실시간위치데이터내역 위도구분코드
  lngit_div_cd character varying(1), -- 실시간위치데이터내역 경도구분코드
  lngit_val numeric(9,6), -- 실시간위치데이터내역 경도값
  altt numeric(10,0), -- 실시간위치데이터내역 고도
  rmark character varying(250), -- 현장장치기본 비고
  del_yn character varying(1) NOT NULL DEFAULT 'N'::character varying, -- 현장장치기본 삭제여부
  cretr_id character varying(20), -- 현장장치기본 생성자아이디
  cret_dt timestamp without time zone NOT NULL DEFAULT now(), -- 현장장치기본 생성일시
  amdr_id character varying(20), -- 현장장치기본 수정자아이디
  amd_dt timestamp without time zone, -- 현장장치기본 수정일시
  up_spot_dev_seq numeric(19,0),
  up_svc_tgt_seq character varying(10),
  dev_conn_type_cd character varying(2),
  dev_pwd character varying(64),
  ottp_yn character varying(1),
  gw_cnct_div_cd character varying(4),
  gw_cnct_id character varying(30),
  std_prot_type_yn character varying(1),
  subs_sttus_cd character varying(2),
  unit_svc_cd character varying(9),
  up_spot_dev_id character varying(64), -- 상위현장장치아이디
  athn_type_cd character varying(4),
  conn_srvr_id character varying(30), -- 연결서버아이디
  conn_dconn_dt timestamp without time zone,
  m2m_svc_no numeric(8,0), -- m2m서비스번호
  CONSTRAINT spotDevBasPk PRIMARY KEY (spot_dev_seq, svc_tgt_seq)
);

COMMENT ON TABLE public.spot_dev_bas
  IS '1. 집합적 정의
  관제의 대상이 되는 서비스대상의 실제 현장장치 정보가 저장된다.

2. 기능적 의미
  실제 관제 서비스 대상이 되는 현장에 설치된 장치정보를 관리한다.
  하나의 서비스대상에 여러 현장장치가 속하게 되고, 현장장치는 실제 관제를 받는 대상이 된다.
  따라서 현장장치는 서비스대상을 참조한다.

3. 자료의 발생 규칙
  필요 시, 어드민 사이트에서 현장장치를 등록, 수정, 삭제 한다.';
COMMENT ON COLUMN public.spot_dev_bas.spot_dev_seq IS '현장장치기본 현장장치일련번호';
COMMENT ON COLUMN public.spot_dev_bas.svc_tgt_seq IS '현장장치기본 서비스대상일련번호';
COMMENT ON COLUMN public.spot_dev_bas.dev_uu_id IS '현장장치기본 장치UU아이디';
COMMENT ON COLUMN public.spot_dev_bas.dev_group_id IS '현장장치기본 장치그룹아이디';
COMMENT ON COLUMN public.spot_dev_bas.dev_model_seq IS '현장장치기본 장치모델일련번호';
COMMENT ON COLUMN public.spot_dev_bas.spot_dev_id IS '현장장치기본 현장장치아이디';
COMMENT ON COLUMN public.spot_dev_bas.dev_nm IS '현장장치기본 장치명';
COMMENT ON COLUMN public.spot_dev_bas.phys_dev_yn IS '현장장치기본 물리장치여부';
COMMENT ON COLUMN public.spot_dev_bas.use_yn IS '현장장치기본 사용여부';
COMMENT ON COLUMN public.spot_dev_bas.tmp_dev_yn IS '현장장치기본 임시장치여부';
COMMENT ON COLUMN public.spot_dev_bas.ipadr IS '현장장치기본 IP주소';
COMMENT ON COLUMN public.spot_dev_bas.athn_no IS '현장장치기본 인증번호';
COMMENT ON COLUMN public.spot_dev_bas.athn_forml_cd IS '현장장치기본 인증방식코드';
COMMENT ON COLUMN public.spot_dev_bas.colec_cycl_time IS '현장장치기본 수집주기시간';
COMMENT ON COLUMN public.spot_dev_bas.idle_jdgm_base_time IS '현장장치기본 유휴판단기준시간';
COMMENT ON COLUMN public.spot_dev_bas.recn_cycl_time IS '현장장치기본 재접속주기시간';
COMMENT ON COLUMN public.spot_dev_bas.recn_try_tmscnt IS '현장장치기본 재접속시도횟수';
COMMENT ON COLUMN public.spot_dev_bas.prod_seq IS '현장장치기본 상품일련번호';
COMMENT ON COLUMN public.spot_dev_bas.reg_seq IS '현장장치기본 등록일련번호';
COMMENT ON COLUMN public.spot_dev_bas.frmwr_ver_no IS '현장장치기본 펌웨어버전번호';
COMMENT ON COLUMN public.spot_dev_bas.last_mtn_dt IS '현장장치기본 최종동작일시';
COMMENT ON COLUMN public.spot_dev_bas.eqp_lo_sbst IS '현장장치기본 설치위치내용';
COMMENT ON COLUMN public.spot_dev_bas.trobl_admr_id IS '현장장치기본 장애관리자아이디';
COMMENT ON COLUMN public.spot_dev_bas.dev_sttus_cd IS '현장장치기본 장치상태코드';
COMMENT ON COLUMN public.spot_dev_bas.latit_val IS '실시간위치데이터내역 위도값';
COMMENT ON COLUMN public.spot_dev_bas.latit_div_cd IS '실시간위치데이터내역 위도구분코드';
COMMENT ON COLUMN public.spot_dev_bas.lngit_div_cd IS '실시간위치데이터내역 경도구분코드';
COMMENT ON COLUMN public.spot_dev_bas.lngit_val IS '실시간위치데이터내역 경도값';
COMMENT ON COLUMN public.spot_dev_bas.altt IS '실시간위치데이터내역 고도';
COMMENT ON COLUMN public.spot_dev_bas.rmark IS '현장장치기본 비고';
COMMENT ON COLUMN public.spot_dev_bas.del_yn IS '현장장치기본 삭제여부';
COMMENT ON COLUMN public.spot_dev_bas.cretr_id IS '현장장치기본 생성자아이디';
COMMENT ON COLUMN public.spot_dev_bas.cret_dt IS '현장장치기본 생성일시';
COMMENT ON COLUMN public.spot_dev_bas.amdr_id IS '현장장치기본 수정자아이디';
COMMENT ON COLUMN public.spot_dev_bas.amd_dt IS '현장장치기본 수정일시';
COMMENT ON COLUMN public.spot_dev_bas.up_spot_dev_id IS '상위현장장치아이디';
COMMENT ON COLUMN public.spot_dev_bas.conn_srvr_id IS '연결서버아이디';
COMMENT ON COLUMN public.spot_dev_bas.m2m_svc_no IS 'm2m서비스번호';
*/