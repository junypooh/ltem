CREATE TABLE ltem.company (
  company_cd     VARCHAR(3) NOT NULL PRIMARY KEY,
  company_name   VARCHAR(100),
  mgr_seq        BIGINT,
  telephone     VARCHAR(64),
  file_seq       BIGINT,
  created       TIMESTAMP WITHOUT TIME ZONE,
  created_by     VARCHAR(14),
  modified      TIMESTAMP WITHOUT TIME ZONE,
  modified_by    VARCHAR(14)
)
WITH (
OIDS = FALSE
);


COMMENT ON TABLE ltem.company IS '회사';
COMMENT ON COLUMN ltem.company.company_cd IS '회사 코드

EST : kt estate

신규 사업자 추가시 재정의';
COMMENT ON COLUMN ltem.company.company_name IS '회사 이름';
COMMENT ON COLUMN ltem.company.mgr_seq IS '담당자(마스터)';
COMMENT ON COLUMN ltem.company.telephone IS '대표 전화번호 (AES256)';
COMMENT ON COLUMN ltem.company.created IS '생성 일시';
COMMENT ON COLUMN ltem.company.created_by IS '생성자';
COMMENT ON COLUMN ltem.company.modified IS '최종 수정 일시';
COMMENT ON COLUMN ltem.company.modified_by IS '최종 수정자';


ALTER TABLE ltem.company
  ADD CONSTRAINT fk_Company_File_Info FOREIGN KEY (file_seq) REFERENCES ltem.file_info (file_seq);

GRANT ALL ON TABLE ltem.company TO ltem_user;

INSERT INTO ltem.company VALUES ('001', 'BIZ25', 103, '7bae52095b6ddbde45785b9b3474cdf2' /*010-2222-2222*/, NULL, now(), 'flyway', NULL, NULL);

INSERT INTO ltem.company VALUES ('002', NULL , NULL, NULL, NULL, now(), 'flyway', NULL, NULL);