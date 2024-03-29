DROP TABLE MRUANG;

CREATE TABLE MRUANG (
		ID INTEGER NOT NULL,
		CKDRUANG CHAR(3) NOT NULL,
		CNM_RUANG VARCHAR(40) NOT NULL,
		NMAX_ISI INTEGER NOT NULL,
		NMAX_UJI INTEGER NOT NULL,
		CSTATUS CHAR(1) DEFAULT '1' NOT NULL,
		DCREATEDDATE DATE,
		CCREATEDBY VARCHAR(35),
		DUPDATEDDATE DATE,
		CUPDATEDBY VARCHAR(35)
	);

ALTER TABLE MRUANG ADD CONSTRAINT FK_RUANG PRIMARY KEY ( ID) ADD CONSTRAINT UN_KDRUANG UNIQUE ( CKDRUANG) ;

ALTER TABLE MFASILITASRUANG ADD CONSTRAINT FK_RUANGFAS FOREIGN KEY (RUANG_ID) REFERENCES MRUANG (ID)  ON DELETE NO ACTION ON UPDATE NO ACTION ENFORCED  ENABLE QUERY OPTIMIZATION ;

-- untuk table akademik
ALTER TABLE MCALAKADEMIK  ALTER COLUMN 	CKDKGT SET DATA TYPE INTEGER;
ALTER TABLE MCALAKADEMIK ADD CONSTRAINT FK_KEGCALAK FOREIGN KEY (CKDKGT) REFERENCES MKEGIATAN (ID)  ON DELETE NO ACTION ON UPDATE NO ACTION ENFORCED  ENABLE QUERY OPTIMIZATION ;
COMMIT;