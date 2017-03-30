CREATE TABLE ltem.biz_cont_usage (
  biz_cont_serial  VARCHAR(10) NOT NULL,
  year             SMALLINT    NOT NULL,
  month            SMALLINT    NOT NULL,
  capacity         BIGINT      NULL,
  usage            BIGINT      NULL,
  capacity_unit_cd CHAR(1)     NULL,
  activated_count  INT         NULL,
  mapped_count     INT         NULL
);
COMMENT ON TABLE ltem.biz_cont_usage IS '계약별 사용량';
COMMENT ON COLUMN ltem.biz_cont_usage.biz_cont_serial IS '계약 번호';
COMMENT ON COLUMN ltem.biz_cont_usage.year IS '연도(4자리)';
COMMENT ON COLUMN ltem.biz_cont_usage.month IS '월';
COMMENT ON COLUMN ltem.biz_cont_usage.capacity IS '허용량';
COMMENT ON COLUMN ltem.biz_cont_usage.usage IS '사용량';
COMMENT ON COLUMN ltem.biz_cont_usage.capacity_unit_cd IS '허용량 단위 코드';
COMMENT ON COLUMN ltem.biz_cont_usage.activated_count IS '사용중 회선수';
COMMENT ON COLUMN ltem.biz_cont_usage.mapped_count IS '사용자 지정 회선수';

CREATE INDEX idx_biz_cont_usage
  ON ltem.biz_cont_usage (biz_cont_serial, year, month);

ALTER TABLE ltem.biz_cont_usage
  ADD CONSTRAINT pk_biz_cont_usage PRIMARY KEY (biz_cont_serial, year, month);

ALTER TABLE ltem.biz_cont_usage
  ADD CONSTRAINT fk_biz_contract_biz_cont_usage FOREIGN KEY (biz_cont_serial) REFERENCES ltem.biz_contract (biz_cont_serial);

GRANT ALL ON TABLE ltem.biz_cont_usage TO ltem_user;

INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 1, 5, 3, 'M', 5, 10);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 2, 6, 3, 'M', 215, 384);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 3, 7, 3, 'M', 80, 1024);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 4, 8, 3, 'M', 484, 37820);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 5, 9, 3, 'M', 1234, 4321);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 6, 10, 3, 'M', 0, 0);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 7, 11, 3, 'M', 0, 5);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 8, 12, 3, 'M', 1, 3);
INSERT INTO ltem.biz_cont_usage VALUES ('1234567890', 2016, 9, 13, 3, 'M', 3, 3);
