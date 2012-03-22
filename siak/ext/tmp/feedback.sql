-- This CLP file was created using DB2LOOK Version 9.7 
-- Timestamp: 3/10/2012 2:24:23 AM
-- Database Name: SIAK           
-- Database Manager Version: DB2/NT64 Version 9.7.4        
-- Database Codepage: 1208
-- Database Collating Sequence is: IDENTITY




------------------------------------------------
-- DDL Statements for table TFEEDBACKINSTANSI
------------------------------------------------
 

CREATE TABLE TFEEDBACKINSTANSI  (
		  ID INTEGER NOT NULL , 
		  CJNSINSTANSI CHAR(2) NOT NULL , 
		  CNMINSTANSI VARCHAR(60) NOT NULL , 
		  CALMINSTANSI VARCHAR(200) NOT NULL , 
		  ALUMNI_ID INTEGER NOT NULL , 
		  CKESANPESAN VARCHAR(500) NOT NULL , 
		  CCREATEDBY VARCHAR(40) , 
		  DCREATEDDATE DATE , 
		  CUPDATEDBY VARCHAR(40) , 
		  DUPDATEDATE DATE )   
		 IN USERSPACE1 ; 

COMMENT ON TABLE TFEEDBACKINSTANSI IS 'feedback instansi';


-- DDL Statements for primary key on Table TFEEDBACKINSTANSI

ALTER TABLE TFEEDBACKINSTANSI 
	ADD CONSTRAINT PK_FEDD PRIMARY KEY
		(ID);


-- DDL Statements for foreign keys on Table TFEEDBACKINSTANSI

ALTER TABLE TFEEDBACKINSTANSI 
	ADD CONSTRAINT FK_ALUMNI_FEED FOREIGN KEY
		(ALUMNI_ID)
	REFERENCES MALUMNI
		(ID)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	ENFORCED
	ENABLE QUERY OPTIMIZATION;







COMMIT;