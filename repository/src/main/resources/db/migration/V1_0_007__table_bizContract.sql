CREATE TABLE ltem.biz_contract (
  biz_cont_serial  VARCHAR(10)                        NOT NULL,
  company_cd      VARCHAR(3) DEFAULT 'EST'           NOT NULL,
  business_cd     VARCHAR(3) DEFAULT 'HOM'           NOT NULL,
  name           VARCHAR(50) DEFAULT NULL,
  period_cd       CHAR(1) DEFAULT 'Y'                NOT NULL,
  period         INTEGER                            NOT NULL,
  start_date      DATE                               NOT NULL,
  end_date        DATE                               NOT NULL,
  capacity_unit_cd CHAR(1) DEFAULT 'P'                NOT NULL,
  capacity       BIGINT                             NOT NULL,
  description    VARCHAR(300),
  created        TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
  created_by      CHARACTER VARYING(14)              NOT NULL,
  modified       TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
  modified_by     CHARACTER VARYING(14)              NOT NULL
);
COMMENT ON TABLE ltem.biz_contract IS '비즈 계약';
COMMENT ON COLUMN ltem.biz_contract.biz_cont_serial IS 'Biz 계약번호';
COMMENT ON COLUMN ltem.biz_contract.company_cd IS '회사 코드 참조';
COMMENT ON COLUMN ltem.biz_contract.business_cd IS '사업 코드';
COMMENT ON COLUMN ltem.biz_contract.period_cd IS '계약 기간 단위 코드
년 : Y
월 : M';
COMMENT ON COLUMN ltem.biz_contract.period IS '계약 기간';
COMMENT ON COLUMN ltem.biz_contract.start_date IS '계약 시작 일시';
COMMENT ON COLUMN ltem.biz_contract.end_date IS '최대 계약 종료 일시';
COMMENT ON COLUMN ltem.biz_contract.capacity_unit_cd IS '허용량 단위 코드
G : Gagabyte
M : Megabyte
P : Packet';
COMMENT ON COLUMN ltem.biz_contract.capacity IS '허용량';
COMMENT ON COLUMN ltem.biz_contract.description IS '계약 설명';
COMMENT ON COLUMN ltem.biz_contract.created IS '생성 일시';
COMMENT ON COLUMN ltem.biz_contract.modified IS '최종 수정 일시';

CREATE INDEX idx_biz_contract_company_cd
  ON ltem.biz_contract (company_cd);
CREATE INDEX idx_biz_contract_start_date
  ON ltem.biz_contract (start_date);
CREATE INDEX idx_biz_contract_end_date
  ON ltem.biz_contract (end_date);

ALTER TABLE ltem.biz_contract
  ADD PRIMARY KEY (biz_cont_serial);

ALTER TABLE ltem.biz_contract
  ADD CONSTRAINT fk_Company_biz_contract FOREIGN KEY (company_cd) REFERENCES ltem.company (company_cd);

GRANT ALL ON TABLE ltem.biz_contract TO ltem_user;

INSERT INTO ltem.biz_contract VALUES (
  '1234567890', '001', 'HOM', '레미안 12차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);

INSERT INTO ltem.biz_contract VALUES (
  '123456789', '001', 'HOM', '레미안 9차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);

INSERT INTO ltem.biz_contract VALUES (
  '12345678', '001', 'HOM', '레미안 10차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);


INSERT INTO ltem.biz_contract VALUES (
  '234567890', '001', 'HOM', '아이파크 1차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);

INSERT INTO ltem.biz_contract VALUES (
  '23456789', '001', 'HOM', '아이파크 2차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);

INSERT INTO ltem.biz_contract VALUES (
  '2345678', '001', 'HOM', '아이파크 3차', 'Y', 10, NOW(), NOW(), 'P', 1234567, 'description', NOW(), 'B2BCore System',
  NOW(),
  'B2BCore System'
);