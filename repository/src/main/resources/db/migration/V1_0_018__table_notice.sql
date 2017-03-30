CREATE SEQUENCE ltem.seq_notice INCREMENT BY 1 NO MINVALUE NO MAXVALUE START WITH 1 NO CYCLE;

GRANT ALL ON SEQUENCE ltem.seq_notice
TO ltem_user;

CREATE TABLE ltem.notice (
  notice_seq     INTEGER                     NOT NULL,
  notice_cate_cd  SMALLINT                    NOT NULL,
  mgr_seq        BIGINT                      NOT NULL,
  notice_title   VARCHAR(50),
  notice_content TEXT,
  is_active      BOOL DEFAULT 'false'        NOT NULL,
  created       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by     VARCHAR(14)                 NOT NULL,
  modified      TIMESTAMP WITHOUT TIME ZONE,
  modified_by    VARCHAR(14)
);
COMMENT ON TABLE ltem.notice IS '공지 테이블
- 서비스관리 공지
- 고객센터 공지

위 2가지 공지를 저장하는 테이블';
COMMENT ON COLUMN ltem.notice.notice_seq IS '공지 시퀀스';
COMMENT ON COLUMN ltem.notice.notice_cate_cd IS '공지 종류 코드
10 : 서비스관리 공지
20 : 고객센터 공지';
COMMENT ON COLUMN ltem.notice.notice_title IS '공지 제목';
COMMENT ON COLUMN ltem.notice.notice_content IS '공지 내용';
COMMENT ON COLUMN ltem.notice.is_active IS '공지 활성화 여부';
COMMENT ON COLUMN ltem.notice.created IS '공지 생성일시';
COMMENT ON COLUMN ltem.notice.created_by IS '공지 생성자';
COMMENT ON COLUMN ltem.notice.modified IS '공지 수정일시';
COMMENT ON COLUMN ltem.notice.modified_by IS '공지 수정자';

CREATE INDEX idx_notice_notice_seq
  ON ltem.notice (notice_seq);

ALTER TABLE ltem.notice
  ADD CONSTRAINT pk_notice PRIMARY KEY (notice_seq);

ALTER TABLE ltem.notice
  ADD CONSTRAINT fk_manager_notice FOREIGN KEY (mgr_seq) REFERENCES ltem.manager (mgr_seq);


GRANT ALL ON TABLE ltem.notice TO ltem_user;

INSERT INTO ltem.notice VALUES (nextval('ltem.seq_notice'), 10, 101, '공지제목 1', '내용...', TRUE, NOW(), 'flyway', NULL, NULL);
INSERT INTO ltem.notice VALUES (nextval('ltem.seq_notice'), 20, 101, '공지제목 2', '내용...', TRUE, NOW(), 'flyway', NULL, NULL);
