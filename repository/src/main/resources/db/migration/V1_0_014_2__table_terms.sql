CREATE TABLE ltem.terms (
  term_seq      INTEGER                     NOT NULL,
  terms_cate_cd  SMALLINT                    NOT NULL,
  terms_ver     VARCHAR(10)                 NOT NULL,
  terms_title   VARCHAR(50),
  terms_content TEXT,
  is_active     BOOL DEFAULT 'false'        NOT NULL,
  created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_by    VARCHAR(14)                 NOT NULL,
  modified     TIMESTAMP WITHOUT TIME ZONE,
  modified_by   VARCHAR(14)
);
COMMENT ON TABLE ltem.terms IS '약관 테이블
- 서비스 이용 약관
- 개인정보 수집 및 이용동의
- 제3자 취급위탁 약관

위 3가지 약관을 저장하는 테이블';
COMMENT ON COLUMN ltem.terms.term_seq IS '약관 시퀀스';
COMMENT ON COLUMN ltem.terms.terms_cate_cd IS '약관 종류 코드
10 : 서비스 이용 약관
20 : 개인정보 수집 및 이용 동의
30 : 제3자 취급위탁 약관';
COMMENT ON COLUMN ltem.terms.terms_ver IS '약관 버전';
COMMENT ON COLUMN ltem.terms.terms_title IS '약관 제목';
COMMENT ON COLUMN ltem.terms.terms_content IS '약관 내용';
COMMENT ON COLUMN ltem.terms.is_active IS '약관 활성화 여부 (동의가 필요한 최신 약관을 SELECT 시에는 이 컬럼이 true인 컬럼만 SELECT)';
COMMENT ON COLUMN ltem.terms.created IS '약관 생성일시';
COMMENT ON COLUMN ltem.terms.created_by IS '약관 생성자';
COMMENT ON COLUMN ltem.terms.modified IS '약관 수정일시';
COMMENT ON COLUMN ltem.terms.modified_by IS '약관 수정자';

CREATE INDEX idx_terms_term_seq
  ON ltem.terms (term_seq);
CREATE INDEX idx_terms_terms_ver
  ON ltem.terms (terms_ver);

ALTER TABLE ltem.terms
  ADD CONSTRAINT pk_terms PRIMARY KEY (term_seq);

GRANT ALL ON TABLE ltem.terms TO ltem_user;

INSERT INTO ltem.terms VALUES (nextval('ltem.seq_terms'), 10, '1.0.00', '서비스 이용 약관', '내용...', TRUE, NOW(), 'flyway', NULL, NULL);
INSERT INTO ltem.terms VALUES (nextval('ltem.seq_terms'), 20, '1.0.00', '개인정보 수집 및 이용동의', '내용...', TRUE, NOW(), 'flyway', NULL, NULL);
INSERT INTO ltem.terms VALUES (nextval('ltem.seq_terms'), 30, '1.0.00', '제3자 취급위탁 약관', '내용...', TRUE, NOW(), 'flyway', NULL, NULL);
