CREATE SEQUENCE ltem.seq_file_info INCREMENT BY 1 NO MINVALUE NO MAXVALUE START WITH 1 NO CYCLE;

GRANT ALL ON SEQUENCE ltem.seq_file_info
TO ltem_user;

CREATE TABLE ltem.file_info (
  file_seq       BIGINT                      NOT NULL,
  file_name      VARCHAR(100),
  file_path      VARCHAR(100),
  file_size      BIGINT,
  file_ext       VARCHAR(10),
  ori_file_name   VARCHAR(100),
  created       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by     VARCHAR(14)                 NOT NULL,
  modified      TIMESTAMP WITHOUT TIME ZONE,
  modified_by    VARCHAR(14)
);
COMMENT ON TABLE ltem.file_info IS '공통 파일 관리 테이블';
COMMENT ON COLUMN ltem.file_info.file_seq IS '파일 시퀀스';
COMMENT ON COLUMN ltem.file_info.file_name IS '저장된 파일 이름';
COMMENT ON COLUMN ltem.file_info.file_path IS '파일 경로';
COMMENT ON COLUMN ltem.file_info.file_size IS '파일 사이즈';
COMMENT ON COLUMN ltem.file_info.file_ext IS '파일 확장자';
COMMENT ON COLUMN ltem.file_info.ori_file_name IS '실제 파일 이름';
COMMENT ON COLUMN ltem.file_info.created IS '생성일시';
COMMENT ON COLUMN ltem.file_info.created_by IS '생성자';
COMMENT ON COLUMN ltem.file_info.modified IS '수정일시';
COMMENT ON COLUMN ltem.file_info.modified_by IS '수정자';

CREATE INDEX idx_file_info_file_seq
  ON ltem.file_info (file_seq);

ALTER TABLE ltem.file_info
  ADD CONSTRAINT pk_file_info PRIMARY KEY (file_seq);


GRANT ALL ON TABLE ltem.file_info TO ltem_user;
