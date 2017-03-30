CREATE TABLE ltem.biz_contract_rel (
  prev_biz_cont_serial VARCHAR(10) NOT NULL,
  curr_biz_cont_serial VARCHAR(10) NOT NULL
);
COMMENT ON TABLE ltem.biz_contract_rel IS '계약 이력';
COMMENT ON COLUMN ltem.biz_contract_rel.prev_biz_cont_serial IS '이전 계약 번호';
COMMENT ON COLUMN ltem.biz_contract_rel.curr_biz_cont_serial IS '현재 계약 번호';

CREATE INDEX idx_biz_contract_rel_prev_biz_cont_serial
  ON ltem.biz_contract_rel (prev_biz_cont_serial);

CREATE INDEX idx_biz_contract_rel_curr_biz_cont_serial
  ON ltem.biz_contract_rel (curr_biz_cont_serial);

ALTER TABLE ltem.biz_contract_rel
  ADD PRIMARY KEY (prev_biz_cont_serial, curr_biz_cont_serial);

ALTER TABLE ltem.biz_contract_rel
  ADD CONSTRAINT fk_biz_contract_prev_biz_contract_rel FOREIGN KEY (prev_biz_cont_serial) REFERENCES ltem.biz_contract (biz_cont_serial);
ALTER TABLE ltem.biz_contract_rel
  ADD CONSTRAINT fk_biz_contract_biz_contract_rel FOREIGN KEY (curr_biz_cont_serial) REFERENCES ltem.biz_contract (biz_cont_serial);

GRANT ALL ON TABLE ltem.biz_contract_rel TO ltem_user;

INSERT INTO ltem.biz_contract_rel VALUES ('123456789', '1234567890');
