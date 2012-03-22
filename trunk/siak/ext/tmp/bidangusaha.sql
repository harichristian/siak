-- This CLP file was created using DB2LOOK Version 9.7 
-- Timestamp: 3/10/2012 2:36:04 AM
-- Database Name: SIAK           
-- Database Manager Version: DB2/NT64 Version 9.7.4        
-- Database Codepage: 1208
-- Database Collating Sequence is: IDENTITY

------------------------------------------------
-- DDL Statements for table MBIDANGUSAHA
------------------------------------------------
 

CREATE TABLE MBIDANGUSAHA  (
		  ID INTEGER NOT NULL , 
		  CBIDUSHINS CHAR(2) NOT NULL , 
		  CNMBIDUSAHA VARCHAR(60) FOR BIT DATA NOT NULL , 
		  CCREATEDBY VARCHAR(40) , 
		  DCREATEDDATE DATE , 
		  CUPDATEDBY VARCHAR(40) , 
		  CUPDATEDDATE DATE )   
		 IN USERSPACE1 ; 

COMMENT ON TABLE MBIDANGUSAHA IS 'bidang usaha instansi';


-- DDL Statements for primary key on Table MBIDANGUSAHA

ALTER TABLE MBIDANGUSAHA 
	ADD CONSTRAINT PK_BIDUSAHA PRIMARY KEY
		(ID);


-- DDL Statements for unique constraints on Table MBIDANGUSAHA


ALTER TABLE MBIDANGUSAHA 
	ADD CONSTRAINT UN_BIDUSAHA UNIQUE
		(CBIDUSHINS);








COMMIT;