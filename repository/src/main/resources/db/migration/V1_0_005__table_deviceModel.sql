CREATE TABLE ltem.device_model (
  device_model_cd   varchar(20) NOT NULL,
  device_model_name varchar(50) NOT NULL);
COMMENT ON TABLE ltem.device_model IS '장치 모델';
COMMENT ON COLUMN ltem.device_model.device_model_cd IS '장치 모델 코드';
COMMENT ON COLUMN ltem.device_model.device_model_name IS '장치 모델명';

ALTER TABLE ltem.device_model ADD PRIMARY KEY (device_model_cd);


CREATE INDEX idx_device_model_device_model_cd
  ON ltem.device_model (device_model_cd);

GRANT ALL ON TABLE ltem.device_model TO ltem_user;

Insert into LTEM.DEVICE_MODEL values ('K7004292','MC03-611ZM');