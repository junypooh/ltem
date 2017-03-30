CREATE TABLE ltem.biz_cont_svc_cont_rel (
  biz_cont_serial VARCHAR(10) NOT NULL,
  svc_cont_serial BIGINT      NOT NULL
);
COMMENT ON TABLE ltem.biz_cont_svc_cont_rel IS '계약 장치 내역 관계';
COMMENT ON COLUMN ltem.biz_cont_svc_cont_rel.biz_cont_serial IS '비즈 계약 번호';
COMMENT ON COLUMN ltem.biz_cont_svc_cont_rel.svc_cont_serial IS '서비스 계약번호';

CREATE INDEX idx_biz_cont_svc_cont_rel
  ON ltem.biz_cont_svc_cont_rel (biz_cont_serial, svc_cont_serial);

/*
CREATE INDEX idx_bizContSvcContRel_svcContSerial
  ON ltem.bizContSvcContRel (svcContSerial);
*/

ALTER TABLE ltem.biz_cont_svc_cont_rel
  ADD PRIMARY KEY (biz_cont_serial, svc_cont_serial);

ALTER TABLE ltem.biz_cont_svc_cont_rel
  ADD CONSTRAINT fk_biz_contract_biz_cont_svc_cont_rel FOREIGN KEY (biz_cont_serial) REFERENCES ltem.biz_contract (biz_cont_serial);

ALTER TABLE ltem.biz_cont_svc_cont_rel
  ADD CONSTRAINT fk_svc_contract_biz_cont_svc_cont_rel FOREIGN KEY (svc_cont_serial) REFERENCES ltem.svc_contract (svc_cont_serial);

GRANT ALL ON TABLE ltem.biz_cont_svc_cont_rel TO ltem_user;


INSERT INTO ltem.biz_cont_svc_cont_rel VALUES ('1234567890', 123456);
INSERT INTO ltem.biz_cont_svc_cont_rel VALUES ('1234567890', 1234567);