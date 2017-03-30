CREATE TABLE ltem.svc_contract (
  svc_cont_serial  BIGINT                      NOT NULL,
  biz_cont_serial  VARCHAR(10)                 NOT NULL,
  iccid            VARCHAR(64)                 NOT NULL,
  imei             VARCHAR(64)                 NOT NULL,
  device_status_cd SMALLINT                    NOT NULL,
  ctn              VARCHAR(255),
  user_ctn         VARCHAR(255) DEFAULT NULL,
  auth_number      VARCHAR(44)  DEFAULT NULL,
  secret_key       VARCHAR(150),
  device_model_cd  VARCHAR(20)                 NOT NULL,
  svc_tgt_seq      BIGINT,
  spot_dev_seq     BIGINT,
  is_active        BOOLEAN DEFAULT 'false'     NOT NULL,
  activated        TIMESTAMP WITHOUT TIME ZONE,
  deactivated      TIMESTAMP WITHOUT TIME ZONE,
  created          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by       VARCHAR(14)                 NOT NULL,
  modified         TIMESTAMP WITHOUT TIME ZONE,
  modified_by      VARCHAR(14),
  device_place     VARCHAR(150),
  user_name        VARCHAR(20),
  is_certify       BOOLEAN DEFAULT 'false'     NOT NULL
);
COMMENT ON TABLE ltem.svc_contract IS '서비스 계약';
COMMENT ON COLUMN ltem.svc_contract.svc_cont_serial IS '서비스 계약 번호';
COMMENT ON COLUMN ltem.svc_contract.biz_cont_serial IS '모계약';
COMMENT ON COLUMN ltem.svc_contract.iccid IS 'USIM 일련번호 (AES256)';
COMMENT ON COLUMN ltem.svc_contract.imei IS '단말 일련번호 (AES256)';
COMMENT ON COLUMN ltem.svc_contract.device_status_cd IS '장치 상태 코드 참조';
COMMENT ON COLUMN ltem.svc_contract.ctn IS '전화번호 (AES256)';
COMMENT ON COLUMN ltem.svc_contract.user_ctn IS '사용자 전화번호 (AES256)';
COMMENT ON COLUMN ltem.svc_contract.auth_number IS '사용자 인증 번호';
COMMENT ON COLUMN ltem.svc_contract.secret_key IS '인증키 (AES256)';
COMMENT ON COLUMN ltem.svc_contract.device_model_cd IS '장치 모델 코드 참조';
COMMENT ON COLUMN ltem.svc_contract.svc_tgt_seq IS '서비스 대상 시퀀스 (EC, API 참조용)';
COMMENT ON COLUMN ltem.svc_contract.is_active IS '활성화(SA) 여부
true : (SA)
false : (SD)';
COMMENT ON COLUMN ltem.svc_contract.activated IS '활성화 일시';
COMMENT ON COLUMN ltem.svc_contract.deactivated IS '비활성화 일시';
COMMENT ON COLUMN ltem.svc_contract.created IS '생성 일시';
COMMENT ON COLUMN ltem.svc_contract.modified IS '최종 수정 일시';
COMMENT ON COLUMN ltem.svc_contract.modified_by IS '최종 수정자';
COMMENT ON COLUMN ltem.svc_contract.device_place IS '장치 설치 장소';
COMMENT ON COLUMN ltem.svc_contract.user_name IS '사용자이름';
COMMENT ON COLUMN ltem.svc_contract.is_certify IS '인증유무';

ALTER TABLE ltem.svc_contract
  ADD PRIMARY KEY (svc_cont_serial);

ALTER TABLE ltem.svc_contract
  ADD CONSTRAINT fk_devicel_model_svc_contract FOREIGN KEY (device_model_cd) REFERENCES ltem.device_model (device_model_cd);

CREATE INDEX idx_device_model_biz_cont_serial
  ON ltem.svc_contract (biz_cont_serial);

CREATE INDEX idx_device_model_iccid
  ON ltem.svc_contract (iccid);

CREATE INDEX idx_device_model_imei
  ON ltem.svc_contract (imei);

CREATE INDEX idx_device_model_device_status_cd
  ON ltem.svc_contract (device_status_cd);

GRANT ALL ON TABLE ltem.svc_contract TO ltem_user;

INSERT INTO ltem.svc_contract VALUES
  (10001, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', NULL, NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10002, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL, NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (123456, '123456789', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
           '2a8383f6e41b387c55cb2f85ce219dcf', NULL, NULL,
           '3ed5326cd92a81ef054e3c5dce3a63d3',
           'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (1234567, '123456789', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 20,
            '033345b76f320bf0f843d200c9c1285f', NULL, NULL,
            '8c0c380003f51c10c57eededfa1b27b3',
            'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');

INSERT INTO ltem.svc_contract VALUES
  (10003, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', NULL, NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10004, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL, NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (10005, '1234567890', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
          '2a8383f6e41b387c55cb2f85ce219dcf', '2a8383f6e41b387c55cb2f85ce219dcf', NULL,
          '3ed5326cd92a81ef054e3c5dce3a63d3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (10006, '1234567890', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 20,
          '033345b76f320bf0f843d200c9c1285f', '033345b76f320bf0f843d200c9c1285f', NULL,
          '8c0c380003f51c10c57eededfa1b27b3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10007, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', '22719a15e36f9920ada56e4407cf88a1', NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10008, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (10009, '1234567890', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
          '2a8383f6e41b387c55cb2f85ce219dcf', '2a8383f6e41b387c55cb2f85ce219dcf', NULL,
          '3ed5326cd92a81ef054e3c5dce3a63d3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (10010, '1234567890', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 20,
          '033345b76f320bf0f843d200c9c1285f', '033345b76f320bf0f843d200c9c1285f', NULL,
          '8c0c380003f51c10c57eededfa1b27b3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10011, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', '22719a15e36f9920ada56e4407cf88a1', NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10012, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (10013, '1234567890', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
          '2a8383f6e41b387c55cb2f85ce219dcf', '2a8383f6e41b387c55cb2f85ce219dcf', NULL,
          '3ed5326cd92a81ef054e3c5dce3a63d3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (10014, '1234567890', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 20,
          '033345b76f320bf0f843d200c9c1285f', '033345b76f320bf0f843d200c9c1285f', NULL,
          '8c0c380003f51c10c57eededfa1b27b3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10015, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', '22719a15e36f9920ada56e4407cf88a1', NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10016, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (10017, '1234567890', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
          '2a8383f6e41b387c55cb2f85ce219dcf', '2a8383f6e41b387c55cb2f85ce219dcf', NULL,
          '3ed5326cd92a81ef054e3c5dce3a63d3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (10018, '1234567890', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 20,
          '033345b76f320bf0f843d200c9c1285f', '033345b76f320bf0f843d200c9c1285f', NULL,
          '8c0c380003f51c10c57eededfa1b27b3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10019, '1234567890', '7ddf126b76f1186183b6520230e00b73', '41993f1497eae75ca7b356f49c457e68', 20,
          '22719a15e36f9920ada56e4407cf88a1', '22719a15e36f9920ada56e4407cf88a1', NULL,
          'f53d34fd730673de2a232c0471bb32de',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway');
INSERT INTO ltem.svc_contract VALUES
  (10020, '1234567890', '1a80777be476797e373dd633c8593cc4', '5f1a50593af18e4818d825a6654b9f81', 20,
          '4d2f5edb2f4f21b7835d4c0c1710cdc6', '4d2f5edb2f4f21b7835d4c0c1710cdc6', NULL,
          '80bcd3cac17a7432f44669ff20bb5417',
          'K7004292', NULL, NULL, FALSE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '서울 레미안 2차');
INSERT INTO ltem.svc_contract VALUES
  (10021, '1234567890', '975d50e34fd4162ec65db2e5e4b9c5c8', '0e1dd108c1d8675dea19903a5ea9256f', 20,
          '2a8383f6e41b387c55cb2f85ce219dcf', '2a8383f6e41b387c55cb2f85ce219dcf', NULL,
          '3ed5326cd92a81ef054e3c5dce3a63d3',
          'K7004292', NULL, NULL, TRUE, NULL, NULL, NOW(), 'flyway', NOW(), 'flyway', '안양 미소아파트');
INSERT INTO ltem.svc_contract VALUES
  (10022, '1234567890', '3ebb69648da921925573a8262ef94a06', 'a470b9618b11179c23f8bbecc7376ef5', 40, NULL,
          '8c0c380003f51c10c57eededfa1b27b3', '8c0c380003f51c10c57eededfa1b27b3', NULL, 'K7004292', NULL, NULL, FALSE,
   NULL,
   NULL, NOW(), 'flyway', NOW(), 'flyway');
