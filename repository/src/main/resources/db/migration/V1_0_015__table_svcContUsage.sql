CREATE TABLE ltem.svc_cont_usage (
  svc_cont_serial BIGINT   NOT NULL,
  year          SMALLINT NOT NULL,
  month         SMALLINT NOT NULL,
  usage         BIGINT   NOT NULL,
  usage_unit_cd   CHAR(1)  NOT NULL
);
COMMENT ON TABLE ltem.svc_cont_usage IS '장치별 사용량';
COMMENT ON COLUMN ltem.svc_cont_usage.svc_cont_serial IS '서비스 계약 번호';
COMMENT ON COLUMN ltem.svc_cont_usage.year IS '연도(4자리)';
COMMENT ON COLUMN ltem.svc_cont_usage.month IS '월';
COMMENT ON COLUMN ltem.svc_cont_usage.usage IS '사용량';
COMMENT ON COLUMN ltem.svc_cont_usage.usage_unit_cd IS '단위 코드';

ALTER TABLE ltem.svc_cont_usage
  ADD CONSTRAINT pk_svc_cont_usage PRIMARY KEY (svc_cont_serial, year, month);

ALTER TABLE ltem.svc_cont_usage
  ADD CONSTRAINT fk_svc_contract_svc_cont_usage FOREIGN KEY (svc_cont_serial) REFERENCES ltem.svc_contract (svc_cont_serial);

CREATE INDEX idx_svc_cont_usage
  ON ltem.svc_cont_usage (svc_cont_serial, year, month);

GRANT ALL ON TABLE ltem.svc_cont_usage TO ltem_user;

INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 7, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 8, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 9, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 10, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 11, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2016, 12, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 7, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 8, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 9, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 10, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 11, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (123456, 2017, 12, 30000, 'M');

INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 7, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 8, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 9, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 10, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 11, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2016, 12, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 7, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 8, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 9, 30000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 10, 10000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 11, 20000, 'M');
INSERT INTO ltem.svc_cont_usage VALUES (1234567, 2017, 12, 30000, 'M');
