

ALTER TABLE ltem.company
  ADD CONSTRAINT fk_Company_Manager FOREIGN KEY (mgr_seq) REFERENCES ltem.manager (mgr_seq);

ALTER TABLE ltem.manager
  ADD CONSTRAINT fk_Manager_Company FOREIGN KEY (company_cd) REFERENCES ltem.company (company_cd);