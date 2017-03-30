CREATE TABLE ltem.mgr_biz_contract_rel (
  biz_cont_serial VARCHAR(10) NOT NULL,
  mgr_seq        BIGINT      NOT NULL
);
COMMENT ON TABLE ltem.mgr_biz_contract_rel IS '관리자 계약 관계';
COMMENT ON COLUMN ltem.mgr_biz_contract_rel.biz_cont_serial IS '계약 번호';
COMMENT ON COLUMN ltem.mgr_biz_contract_rel.mgr_seq IS '관리자 번호';

CREATE INDEX idx_mgr_biz_contract_rel
  ON ltem.mgr_biz_contract_rel (biz_cont_serial, mgr_seq);

ALTER TABLE ltem.mgr_biz_contract_rel
  ADD PRIMARY KEY (biz_cont_serial, mgr_seq);

ALTER TABLE ltem.mgr_biz_contract_rel
  ADD CONSTRAINT fk_biz_contract_MgrContractRel FOREIGN KEY (biz_cont_serial) REFERENCES ltem.biz_contract (biz_cont_serial);

ALTER TABLE ltem.mgr_biz_contract_rel
  ADD CONSTRAINT fk_Manager_MgrContractRel FOREIGN KEY (mgr_seq) REFERENCES ltem.manager (mgr_seq);

GRANT ALL ON TABLE ltem.mgr_biz_contract_rel TO ltem_user;


INSERT INTO ltem.mgr_biz_contract_rel VALUES ('1234567890', 101);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('1234567890', 102);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('1234567890', 103);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('123456789', 101);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('123456789', 102);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('123456789', 103);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('12345678', 101);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('12345678', 102);
INSERT INTO ltem.mgr_biz_contract_rel VALUES ('12345678', 103);