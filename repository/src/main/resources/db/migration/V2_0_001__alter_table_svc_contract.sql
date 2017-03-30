ALTER TABLE ltem.svc_contract ADD COLUMN is_suspended BOOLEAN DEFAULT 'false' NOT NULL;
COMMENT ON COLUMN ltem.svc_contract.is_suspended IS '이용 정지 여부';
ALTER TABLE ltem.svc_contract ADD COLUMN is_mapped BOOLEAN DEFAULT 'false' NOT NULL;
COMMENT ON COLUMN ltem.svc_contract.is_mapped IS '사용자 등록 요청 여부';

UPDATE ltem.svc_contract SET device_status_cd = 40 WHERE device_status_cd = 50;
UPDATE ltem.svc_contract SET device_status_cd = device_status_cd - 20 WHERE device_status_cd > 50;
