CREATE TABLE ltem.terms_agreed (
  term_seq   INTEGER NOT NULL,
  mgr_seq    BIGINT  NOT NULL,
  agreed    TIMESTAMP WITHOUT TIME ZONE,
  withdrawn TIMESTAMP WITHOUT TIME ZONE
);
COMMENT ON TABLE ltem.terms_agreed IS '관리자의 약관 동의 내역을 저장하는 테이블';
COMMENT ON COLUMN ltem.terms_agreed.term_seq IS '약관 시퀀스 참조';
COMMENT ON COLUMN ltem.terms_agreed.mgr_seq IS '관리자 시퀀스 참조';
COMMENT ON COLUMN ltem.terms_agreed.agreed IS '동의 일시';
COMMENT ON COLUMN ltem.terms_agreed.withdrawn IS '동의 철회 일시';

CREATE INDEX idx_terms_agreed
  ON ltem.terms_agreed (term_seq, mgr_seq);

ALTER TABLE ltem.terms_agreed
  ADD CONSTRAINT pk_terms_agreed PRIMARY KEY (term_seq, mgr_seq);

ALTER TABLE ltem.terms_agreed
  ADD CONSTRAINT fk_Manager_terms_agreed FOREIGN KEY (mgr_seq) REFERENCES ltem.manager (mgr_seq);
ALTER TABLE ltem.terms_agreed
  ADD CONSTRAINT fk_Terms_terms_agreed FOREIGN KEY (term_seq) REFERENCES ltem.terms (term_seq);

GRANT ALL ON TABLE ltem.terms_agreed TO ltem_user;

INSERT INTO ltem.terms_agreed VALUES (1, 101, NOW(), NULL);
