CREATE SEQUENCE ltem.seq_privacy_audit_history INCREMENT BY 1 NO MINVALUE NO MAXVALUE START WITH 1 NO CYCLE;

GRANT ALL ON SEQUENCE ltem.seq_privacy_audit_history
TO ltem_user;

CREATE TABLE ltem.privacy_audit_history (
  history_seq     INTEGER      NOT NULL,
  company_cd      VARCHAR(3)   NOT NULL,
  company_name    VARCHAR(100) NOT NULL,
  biz_cont_serial VARCHAR(10)  NULL,
  manager_user_id VARCHAR(12)  NOT NULL,
  manager_name    VARCHAR(20)  NOT NULL,
  authority_name  VARCHAR(15)  NOT NULL,
  menu_path       VARCHAR(200) NOT NULL,
  user_name       VARCHAR(20)  NULL DEFAULT NULL,
  target_info     VARCHAR(200) NOT NULL,
  is_unmasked     BOOLEAN           DEFAULT FALSE ,
  is_downloaded   BOOLEAN           DEFAULT FALSE ,
  reason          VARCHAR(100) NULL,
  audited         TIMESTAMP WITHOUT TIME ZONE
);
COMMENT ON TABLE ltem.privacy_audit_history IS '개인 정보 열람 이력 조회 테이블';
COMMENT ON COLUMN ltem.privacy_audit_history.history_seq IS '시퀀스';
COMMENT ON COLUMN ltem.privacy_audit_history.company_cd IS '회사 코드';
COMMENT ON COLUMN ltem.privacy_audit_history.company_name IS '회사명';
COMMENT ON COLUMN ltem.privacy_audit_history.biz_cont_serial IS '비즈 계약 번호';
COMMENT ON COLUMN ltem.privacy_audit_history.manager_user_id IS '관리자 아이디';
COMMENT ON COLUMN ltem.privacy_audit_history.manager_name IS '관리자 이름';
COMMENT ON COLUMN ltem.privacy_audit_history.authority_name IS '권한명';
COMMENT ON COLUMN ltem.privacy_audit_history.menu_path IS '열람 경로(위치)';
COMMENT ON COLUMN ltem.privacy_audit_history.user_name IS '사용자 이름';
COMMENT ON COLUMN ltem.privacy_audit_history.target_info IS '열람 대상 정보';
COMMENT ON COLUMN ltem.privacy_audit_history.is_unmasked IS '마스킹 해제 여부';
COMMENT ON COLUMN ltem.privacy_audit_history.is_downloaded IS '다운로드 여부';
COMMENT ON COLUMN ltem.privacy_audit_history.reason IS '열람 사유';
COMMENT ON COLUMN ltem.privacy_audit_history.audited IS '열람 일시';

CREATE INDEX idx_privacy_audit_history_seq
  ON ltem.privacy_audit_history (history_seq);

ALTER TABLE ltem.privacy_audit_history
  ADD CONSTRAINT pk_privacy_audit_history PRIMARY KEY (history_seq);

GRANT ALL ON TABLE ltem.privacy_audit_history TO ltem_user;