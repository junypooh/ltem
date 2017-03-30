CREATE SEQUENCE ltem.seq_banner INCREMENT BY 1 NO MINVALUE NO MAXVALUE START WITH 1 NO CYCLE;

GRANT ALL ON SEQUENCE ltem.seq_banner
TO ltem_user;

CREATE TABLE ltem.banner (
  banner_seq   INTEGER                     NOT NULL,
  category     SMALLINT                    NOT NULL,
  name         VARCHAR(100)                NOT NULL,
  link         VARCHAR(255)                NULL,
  file_seq     BIGINT                      NOT NULL,
  banner_order SMALLINT                    NULL,
  is_current   BOOLEAN DEFAULT 'false'     NOT NULL,
  is_new_window BOOLEAN DEFAULT 'false'     NOT NULL,
  created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by   VARCHAR(14)                 NOT NULL,
  modified     TIMESTAMP WITHOUT TIME ZONE,
  modified_by  VARCHAR(14)
);
COMMENT ON TABLE ltem.banner IS '배너 관리 테이블';
COMMENT ON COLUMN ltem.banner.banner_seq IS '시퀀스';
COMMENT ON COLUMN ltem.banner.category IS '메인 : 1, 서브 : 2';
COMMENT ON COLUMN ltem.banner.name IS '배너명';
COMMENT ON COLUMN ltem.banner.link IS '링크';
COMMENT ON COLUMN ltem.banner.file_seq IS 'file_info Foreign Key';
COMMENT ON COLUMN ltem.banner.banner_order IS '배치 순서';
COMMENT ON COLUMN ltem.banner.is_current IS '노출 여부(true: 노출 / false : 비노출)';
COMMENT ON COLUMN ltem.banner.is_new_window IS '새창 이동 여부';
COMMENT ON COLUMN ltem.banner.created IS '등록일';
COMMENT ON COLUMN ltem.banner.created_by IS '등록자';
COMMENT ON COLUMN ltem.banner.modified IS '수정일';
COMMENT ON COLUMN ltem.banner.modified_by IS '수정자';

ALTER TABLE ltem.banner
  ADD CONSTRAINT fk_banner_file_info FOREIGN KEY (file_seq) REFERENCES ltem.file_info (file_seq);

CREATE INDEX idx_banner_seq
  ON ltem.banner (banner_seq);

ALTER TABLE ltem.banner
  ADD CONSTRAINT pk_banner PRIMARY KEY (banner_seq);

GRANT ALL ON TABLE ltem.banner TO ltem_user;