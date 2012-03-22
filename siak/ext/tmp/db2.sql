DROP TABLE SIAK.FILIALE;
DROP SEQUENCE SIAK.FILIALE_SEQ;
ALTER TABLE SIAK.FILIALE DROP PRIMARY KEY;

DROP TABLE SIAK.KUNDE;
DROP SEQUENCE SIAK.KUNDE_SEQ;
ALTER TABLE SIAK.KUNDE DROP PRIMARY KEY;

DROP TABLE SIAK.ARTIKEL;
DROP SEQUENCE SIAK.ARTIKEL_SEQ;
ALTER TABLE SIAK.ARTIKEL DROP PRIMARY KEY;

DROP TABLE SIAK.AUFTRAG;
DROP SEQUENCE SIAK.AUFTRAG_SEQ;
ALTER TABLE SIAK.AUFTRAG DROP PRIMARY KEY;

DROP TABLE SIAK.AUFTRAGPOSITION;
DROP SEQUENCE SIAK.AUFTRAGPOSITION_SEQ;
ALTER TABLE SIAK.AUFTRAGPOSITION DROP PRIMARY KEY;

DROP TABLE SIAK.BRANCHE;
DROP SEQUENCE SIAK.BRANCHE_SEQ;
ALTER TABLE SIAK.BRANCHE DROP PRIMARY KEY;

/* SECURITY */
DROP TABLE SIAK.SEC_USER;
DROP SEQUENCE SIAK.SEC_USER_SEQ;
ALTER TABLE SIAK.SEC_USER DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_USERROLE;
DROP SEQUENCE SIAK.SEC_USERROLE_SEQ;
ALTER TABLE SIAK.SEC_USERROLE DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_ROLE;
DROP SEQUENCE SIAK.SEC_ROLE_SEQ;
ALTER TABLE SIAK.SEC_ROLE DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_ROLEGROUP;
DROP SEQUENCE SIAK.SEC_ROLEGROUP_SEQ;
ALTER TABLE SIAK.SEC_ROLEGROUP DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_GROUP;
DROP SEQUENCE SIAK.SEC_GROUP_SEQ;
ALTER TABLE SIAK.SEC_GROUP DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_GROUPRIGHT;
DROP SEQUENCE SIAK.SEC_GROUPRIGHT_SEQ;
ALTER TABLE SIAK.SEC_GROUPRIGHT DROP PRIMARY KEY;

DROP TABLE SIAK.SEC_RIGHT;
DROP SEQUENCE SIAK.SEC_RIGHT_SEQ;
ALTER TABLE SIAK.SEC_RIGHT DROP PRIMARY KEY;
/* END: SECURITY */

DROP TABLE SIAK.SYS_COUNTRYCODE;
DROP SEQUENCE SIAK.SYS_COUNTRYCODE_SEQ;
ALTER TABLE SIAK.SYS_COUNTRYCODE DROP PRIMARY KEY;

DROP TABLE SIAK.SYS_IP4COUNTRY;
DROP SEQUENCE SIAK.SYS_IP4COUNTRY_SEQ;
ALTER TABLE SIAK.SYS_IP4COUNTRY DROP PRIMARY KEY;

DROP TABLE SIAK.YOUTUBE_LINK;
DROP SEQUENCE SIAK.YOUTUBE_LINK_SEQ;
ALTER TABLE SIAK.YOUTUBE_LINK DROP PRIMARY KEY;

DROP TABLE SIAK.IPC_IP2COUNTRY;
DROP SEQUENCE SIAK.IPC_IP2COUNTRY_SEQ;
ALTER TABLE SIAK.IPC_IP2COUNTRY DROP PRIMARY KEY;
 
DROP TABLE SIAK.SEC_LOGINLOG;
DROP SEQUENCE SIAK.SEC_LOGINLOG_SEQ;
ALTER TABLE SIAK.SEC_LOGINLOG DROP PRIMARY KEY;
 
DROP TABLE SIAK.LOG_IP2COUNTRY;
DROP SEQUENCE SIAK.LOG_IP2COUNTRY_SEQ;
ALTER TABLE SIAK.LOG_IP2COUNTRY DROP PRIMARY KEY;
 
DROP TABLE SIAK.GUESTBOOK;
DROP SEQUENCE SIAK.GUESTBOOK_SEQ;
ALTER TABLE SIAK.GUESTBOOK DROP PRIMARY KEY;
 
DROP TABLE SIAK.CALENDAR_EVENT;
DROP SEQUENCE SIAK.CALENDAR_EVENT_SEQ;
ALTER TABLE SIAK.CALENDAR_EVENT DROP PRIMARY KEY;

DROP TABLE SIAK.APP_NEWS;
DROP SEQUENCE SIAK.APP_NEWS_SEQ;
ALTER TABLE SIAK.APP_NEWS DROP PRIMARY KEY;

/********** HIBERNATE DB PERFORMANCE LOGGING ****************/
DROP TABLE SIAK.HIBERNATE_ENTITY_STATISTICS;
DROP SEQUENCE SIAK.HIBERNATE_ENTITY_STATISTICS_SEQ;
ALTER TABLE SIAK.HIBERNATE_ENTITY_STATISTICS DROP PRIMARY KEY;

DROP TABLE SIAK.HIBERNATE_STATISTICS;
DROP SEQUENCE SIAK.HIBERNATE_STATISTICS_SEQ;
ALTER TABLE SIAK.HIBERNATE_STATISTICS DROP PRIMARY KEY;

CREATE TABLE SIAK.HIBERNATE_STATISTICS
(
 ID INTEGER NOT NULL,
 FLUSHCOUNT INTEGER NOT NULL,
 PREPARESTATEMENTCOUNT INTEGER NOT NULL,
 ENTITYLOADCOUNT INTEGER NOT NULL,
 ENTITYUPDATECOUNT INTEGER NOT NULL,
 ENTITYINSERTCOUNT INTEGER NOT NULL,
 ENTITYDELETECOUNT INTEGER NOT NULL,
 ENTITYFETCHCOUNT INTEGER NOT NULL,
 COLLECTIONLOADCOUNT INTEGER NOT NULL,
 COLLECTIONUPDATECOUNT INTEGER NOT NULL,
 COLLECTIONREMOVECOUNT INTEGER NOT NULL,
 COLLECTIONRECREATECOUNT INTEGER NOT NULL,
 COLLECTIONFETCHCOUNT INTEGER NOT NULL,
 QUERYEXECUTIONCOUNT INTEGER NOT NULL,
 QUERYEXECUTIONMAXTIME INTEGER NOT NULL,
 OPTIMISTICFAILURECOUNT INTEGER NOT NULL,
 QUERYEXECUTIONMAXTIMEQUERYSTRING VARCHAR(1000),
 CALLMETHOD VARCHAR(1000) NOT NULL,
 JAVAFINISHMS BIGINT NOT NULL,
 FINISHTIME TIMESTAMP NOT NULL,
 CONSTRAINT HIBERNATESTATISTICS_PKEY PRIMARY KEY (ID)
);

/*==============================================================*/
/* TABLE: HIBERNATE_ */
/*==============================================================*/
CREATE TABLE SIAK.HIBERNATE_ENTITY_STATISTICS
(
 ID INTEGER NOT NULL,
 HIBERNATEENTITYSTATISTICSID BIGINT NOT NULL,
 ENTITYNAME VARCHAR(1000) NOT NULL,
 LOADCOUNT INTEGER NOT NULL,
 UPDATECOUNT INTEGER NOT NULL,
 INSERTCOUNT INTEGER NOT NULL,
 DELETECOUNT INTEGER NOT NULL,
 FETCHCOUNT INTEGER NOT NULL,
 OPTIMISTICFAILURECOUNT INTEGER NOT NULL,
 CONSTRAINT HIBERNATEENTITYSTATISTICS_PKEY PRIMARY KEY (ID)
);

CREATE INDEX SIAK.FKI_
 ON SIAK.HIBERNATE_ENTITY_STATISTICS (
 HIBERNATEENTITYSTATISTICSID
 );
/*************END HIBERNATE STATISTICS **********************/
 
 
/*==============================================================*/
/* TABLE: APP_NEWS */
/*==============================================================*/
CREATE TABLE SIAK.APP_NEWS (
 ANW_ID INTEGER NOT NULL,
 ANW_TEXT VARCHAR(1000) NULL,
 ANW_DATE DATE NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_APP_NEWS PRIMARY KEY (ANW_ID)
);


/*==============================================================*/
/* INDEX: INDEX_APP_NEWS_ID */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.INDEX_APP_NEWS_ID ON SIAK.APP_NEWS (
ANW_ID
);
 
 
/*==============================================================*/
/* TABLE: YOUTUBE_LINK */
/*==============================================================*/
CREATE TABLE SIAK.YOUTUBE_LINK (
 YTB_ID INTEGER NOT NULL,
 YTB_INTERPRET VARCHAR(50) NULL,
 YTB_TITLE VARCHAR(50) NULL,
 YTB_URL VARCHAR(250) NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_YOUTUBE_LINK PRIMARY KEY (YTB_ID)
);


/*==============================================================*/
/* TABLE: BRANCHE */
/*==============================================================*/
CREATE TABLE SIAK.BRANCHE (
 BRA_ID INTEGER NOT NULL,
 BRA_NR VARCHAR(20) NULL,
 BRA_BEZEICHNUNG VARCHAR(30) NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_BRANCHE PRIMARY KEY (BRA_ID)
);
/*==============================================================*/
/* INDEX: IDX_BRA_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_BRA_ID ON SIAK.BRANCHE (
BRA_ID
);

/*==============================================================*/
/* INDEX: IDX_BRA_BEZEICHNUNG */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_BRA_BEZEICHNUNG ON SIAK.BRANCHE (
BRA_BEZEICHNUNG
);

/*==============================================================*/
/* TABLE: ARTIKEL */
/*==============================================================*/
CREATE TABLE SIAK.ARTIKEL (
 ART_ID INTEGER NOT NULL,
 ART_KURZBEZEICHNUNG VARCHAR(50) NOT NULL,
 ART_LANGBEZEICHNUNG VARCHAR(1000) NULL,
 ART_NR VARCHAR(20) NOT NULL,
 ART_PREIS NUMERIC(12,2) NOT NULL DEFAULT 0.00,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_ARTIKEL PRIMARY KEY (ART_ID)
);
/*==============================================================*/
/* INDEX: IDX_ART_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ART_ID ON SIAK.ARTIKEL (
ART_ID
);

/*==============================================================*/
/* INDEX: IDX_ART_NR */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ART_NR ON SIAK.ARTIKEL (
ART_NR
);

/*==============================================================*/
/* INDEX: IDX_ART_BEZEICHNUNG */
/*==============================================================*/
CREATE INDEX IDX_ART_BEZEICHNUNG ON SIAK.ARTIKEL (
ART_KURZBEZEICHNUNG
);

/*==============================================================*/
/* TABLE: AUFTRAG */
/*==============================================================*/
CREATE TABLE SIAK.AUFTRAG (
 AUF_ID INTEGER NOT NULL,
 AUF_KUN_ID INTEGER NOT NULL,
 AUF_NR VARCHAR(20) NOT NULL,
 AUF_BEZEICHNUNG VARCHAR(50) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_AUFTRAG PRIMARY KEY (AUF_ID)
);
/*==============================================================*/
/* INDEX: IX_AUF_ID */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_AUF_ID ON SIAK.AUFTRAG (
AUF_ID
);

/*==============================================================*/
/* INDEX: IX_AUF_KUN_ID */
/*==============================================================*/
CREATE INDEX SIAK.IX_AUF_KUN_ID ON SIAK.AUFTRAG (
AUF_KUN_ID
);

/*==============================================================*/
/* INDEX: IX_AUF_NR */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_AUF_NR ON SIAK.AUFTRAG (
AUF_NR
);

/*==============================================================*/
/* TABLE: AUFTRAGPOSITION */
/*==============================================================*/
CREATE TABLE SIAK.AUFTRAGPOSITION (
 AUP_ID INTEGER NOT NULL,
 AUP_AUF_ID INTEGER NOT NULL,
 ART_ID INTEGER NULL,
 AUP_POSITION INTEGER NULL,
 AUP_MENGE NUMERIC(12,2) NULL,
 AUP_EINZELWERT NUMERIC(12,2) NULL,
 AUP_GESAMTWERT NUMERIC(12,2) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_AUFTRAGPOSITION PRIMARY KEY (AUP_ID)
);
/*==============================================================*/
/* INDEX: IX_AUP_AUF_ID */
/*==============================================================*/
CREATE INDEX SIAK.IX_AUP_AUF_ID ON SIAK.AUFTRAGPOSITION (
AUP_AUF_ID
);

/*==============================================================*/
/* INDEX: IX_AUP_ID */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_AUP_ID ON SIAK.AUFTRAGPOSITION (
AUP_ID
);

/*==============================================================*/
/* TABLE: CALENDAR_EVENT */
/*==============================================================*/
CREATE TABLE SIAK.CALENDAR_EVENT (
 CLE_ID INTEGER NOT NULL,
 CLE_TITLE VARCHAR(20) NULL,
 CLE_CONTENT VARCHAR(300) NOT NULL,
 CLE_BEGIN_DATE TIMESTAMP NOT NULL,
 CLE_END_DATE TIMESTAMP NOT NULL,
 CLE_HEADER_COLOR VARCHAR(10) NULL,
 CLE_CONTENT_COLOR VARCHAR(10) NULL,
 CLE_USR_ID INTEGER NOT NULL,
 CLE_LOCKED SMALLINT NULL DEFAULT 0,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_CALENDAR_EVENT PRIMARY KEY (CLE_ID)
);
/*==============================================================*/
/* INDEX: IDX_CLE_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_CLE_ID ON SIAK.CALENDAR_EVENT (
CLE_ID
);


/*==============================================================*/
/* TABLE: FILIALE */
/*==============================================================*/
CREATE TABLE SIAK.FILIALE (
 FIL_ID INTEGER NOT NULL,
 FIL_NR VARCHAR(20) NOT NULL,
 FIL_BEZEICHNUNG VARCHAR(50) NULL,
 FIL_NAME1 VARCHAR(50) NULL,
 FIL_NAME2 VARCHAR(50) NULL,
 FIL_ORT VARCHAR(50) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_FILIALE PRIMARY KEY (FIL_ID)
);
/*==============================================================*/
/* INDEX: IX_FIL_ID */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_FIL_ID ON SIAK.FILIALE (
FIL_ID
);

/*==============================================================*/
/* INDEX: IX_FIL_BEZEICHNUNG */
/*==============================================================*/
CREATE INDEX SIAK.IX_FIL_BEZEICHNUNG ON SIAK.FILIALE (
FIL_BEZEICHNUNG
);

/*==============================================================*/
/* INDEX: IX_FIL_NR */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_FIL_NR ON SIAK.FILIALE (
FIL_NR
);

/*==============================================================*/
/* TABLE: GUESTBOOK */
/*==============================================================*/
CREATE TABLE SIAK.GUESTBOOK (
 GUB_ID INTEGER NOT NULL,
 GUB_SUBJECT VARCHAR(40) NOT NULL,
 GUB_DATE TIMESTAMP NOT NULL,
 GUB_USR_NAME VARCHAR(40) NOT NULL,
 GUB_TEXT VARCHAR(1000) NULL,
 VERSION INTEGER NULL,
 CONSTRAINT PK_GUESTBOOK PRIMARY KEY (GUB_ID)
);
/*==============================================================*/
/* INDEX: IDX_GUB_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GUB_ID ON SIAK.GUESTBOOK (
GUB_ID
);

/*==============================================================*/
/* INDEX: IDX_GUB_SUBJECT */
/*==============================================================*/
CREATE INDEX IDX_GUB_SUBJECT ON SIAK.GUESTBOOK (
GUB_SUBJECT
);

/*==============================================================*/
/* INDEX: IDX_GUB_DATE */
/*==============================================================*/
CREATE INDEX IDX_GUB_DATE ON SIAK.GUESTBOOK (
GUB_DATE
);

/*==============================================================*/
/* INDEX: IDX_GUB_USR_NAME */
/*==============================================================*/
CREATE INDEX IDX_GUB_USR_NAME ON SIAK.GUESTBOOK (
GUB_USR_NAME
);

/*==============================================================*/
/* TABLE: IPC_IP2COUNTRY */
/*==============================================================*/
CREATE TABLE SIAK.IPC_IP2COUNTRY (
 IPC_ID INTEGER NOT NULL,
 IPC_IP_FROM INTEGER NULL,
 IPC_IP_TO INTEGER NULL,
 IPC_COUNTRY_CODE2 VARCHAR(2) NULL,
 IPC_COUNTRY_CODE3 VARCHAR(3) NULL,
 IPC_COUNTRY_NAME VARCHAR(50) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_IPC_IP2COUNTRY PRIMARY KEY (IPC_ID)
);
/*==============================================================*/
/* INDEX: IDX_IPC_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_IPC_ID ON SIAK.IPC_IP2COUNTRY (
IPC_ID
);

/*==============================================================*/
/* INDEX: IDX_IPC_IP_FROM */
/*==============================================================*/
CREATE INDEX IDX_IPC_IP_FROM ON SIAK.IPC_IP2COUNTRY (
IPC_IP_FROM
);

/*==============================================================*/
/* INDEX: IDX_IPC_IP_TO */
/*==============================================================*/
CREATE INDEX IDX_IPC_IP_TO ON SIAK.IPC_IP2COUNTRY (
IPC_IP_TO
);

/*==============================================================*/
/* INDEX: IDX_IPC_COUNTRY_CODE2 */
/*==============================================================*/
CREATE INDEX IDX_IPC_COUNTRY_CODE2 ON SIAK.IPC_IP2COUNTRY (
IPC_COUNTRY_CODE2
);

/*==============================================================*/
/* INDEX: IDX_IPC_COUNTRY_CODE3 */
/*==============================================================*/
CREATE INDEX IDX_IPC_COUNTRY_CODE3 ON SIAK.IPC_IP2COUNTRY (
IPC_COUNTRY_CODE3
);

/*==============================================================*/
/* INDEX: IDX_IPC_COUNTRY_NAME */
/*==============================================================*/
CREATE INDEX IDX_IPC_COUNTRY_NAME ON SIAK.IPC_IP2COUNTRY (
IPC_COUNTRY_NAME
);

/*==============================================================*/
/* TABLE: KUNDE */
/*==============================================================*/
CREATE TABLE SIAK.KUNDE (
 KUN_ID INTEGER NOT NULL,
 KUN_FIL_ID INTEGER NOT NULL,
 KUN_BRA_ID INTEGER NULL,
 KUN_NR VARCHAR(20) NOT NULL,
 KUN_MATCHCODE VARCHAR(20) NULL,
 KUN_NAME1 VARCHAR(50) NULL,
 KUN_NAME2 VARCHAR(50) NULL,
 KUN_ORT VARCHAR(50) NULL,
 KUN_MAHNSPERRE SMALLINT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_KUNDE PRIMARY KEY (KUN_ID)
);
/*==============================================================*/
/* INDEX: IX_KUN_ID */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_KUN_ID ON SIAK.KUNDE (
KUN_ID
);

/*==============================================================*/
/* INDEX: IX_KUN_FIL_ID */
/*==============================================================*/
CREATE INDEX SIAK.IX_KUN_FIL_ID ON SIAK.KUNDE (
KUN_FIL_ID
);

/*==============================================================*/
/* INDEX: IX_KUN_NR */
/*==============================================================*/
CREATE UNIQUE INDEX SIAK.IX_KUN_NR ON SIAK.KUNDE (
KUN_NR
);

/*==============================================================*/
/* TABLE: LOG_IP2COUNTRY */
/*==============================================================*/
CREATE TABLE SIAK.LOG_IP2COUNTRY (
 I2C_ID INTEGER NOT NULL,
 CCD_ID INTEGER NULL,
 I2C_CITY VARCHAR(50) NULL,
 I2C_LATITUDE DECIMAL NULL,
 I2C_LONGITUDE DECIMAL NULL,
 VERSION INTEGER NULL DEFAULT 0,
 CONSTRAINT PK_LOG_IP2COUNTRY PRIMARY KEY (I2C_ID)
);
/*==============================================================*/
/* INDEX: IDX_I2C_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_I2C_ID ON SIAK.LOG_IP2COUNTRY (
I2C_ID
);

/*==============================================================*/
/* INDEX: IDX_I2C_CCD_ID */
/*==============================================================*/
CREATE INDEX IDX_I2C_CCD_ID ON SIAK.LOG_IP2COUNTRY (
CCD_ID
);

/*==============================================================*/
/* TABLE: SEC_GROUP */
/*==============================================================*/
CREATE TABLE SIAK.SEC_GROUP (
 GRP_ID INTEGER NOT NULL,
 GRP_SHORTDESCRIPTION VARCHAR(40) NOT NULL,
 GRP_LONGDESCRIPTION VARCHAR(1000) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_GROUP PRIMARY KEY (GRP_ID)
);
/*==============================================================*/
/* INDEX: IDX_GRP_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GRP_ID ON SIAK.SEC_GROUP (
GRP_ID
);

/*==============================================================*/
/* INDEX: IDX_GRP_SHORTDESCRIPTION */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GRP_SHORTDESCRIPTION ON SIAK.SEC_GROUP (
GRP_SHORTDESCRIPTION
);

/*==============================================================*/
/* TABLE: SEC_GROUPRIGHT */
/*==============================================================*/
CREATE TABLE SIAK.SEC_GROUPRIGHT (
 GRI_ID INTEGER NOT NULL,
 GRP_ID INTEGER NOT NULL,
 RIG_ID INTEGER NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_GROUPRIGHT PRIMARY KEY (GRI_ID)
);
/*==============================================================*/
/* INDEX: IDX_GRI_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GRI_ID ON SIAK.SEC_GROUPRIGHT (
GRI_ID
);

/*==============================================================*/
/* INDEX: IDX_GRI_GRPRIG */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GRI_GRPRIG ON SIAK.SEC_GROUPRIGHT (
GRP_ID,
RIG_ID
);

/*==============================================================*/
/* TABLE: SEC_LOGINLOG */
/*==============================================================*/
CREATE TABLE SIAK.SEC_LOGINLOG (
 LGL_ID INTEGER NOT NULL,
 I2C_ID INTEGER NULL,
 LGL_LOGINNAME VARCHAR(50) NOT NULL,
 LGL_LOGTIME TIMESTAMP NOT NULL,
 LGL_IP VARCHAR(19) NULL,
 LGL_BROWSERTYPE VARCHAR(40) NULL,
 LGL_STATUS_ID INTEGER NOT NULL,
 LGL_SESSIONID VARCHAR(50) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_LOGINLOG PRIMARY KEY (LGL_ID)
);
/*==============================================================*/
/* INDEX: IDX_LGL_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_LGL_ID ON SIAK.SEC_LOGINLOG (
LGL_ID
);

/*==============================================================*/
/* INDEX: IDX_LGL_LOGTIME */
/*==============================================================*/
CREATE INDEX IDX_LGL_LOGTIME ON SIAK.SEC_LOGINLOG (
LGL_LOGTIME
);

/*==============================================================*/
/* INDEX: IDX_LGL_I2C_ID */
/*==============================================================*/
CREATE INDEX IDX_LGL_I2C_ID ON SIAK.SEC_LOGINLOG (
I2C_ID
);

/*==============================================================*/
/* TABLE: SEC_RIGHT */
/*==============================================================*/
CREATE TABLE SIAK.SEC_RIGHT (
 RIG_ID INTEGER NOT NULL,
 RIG_TYPE INTEGER NULL DEFAULT 1,
 RIG_NAME VARCHAR(50) NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_RIGHT PRIMARY KEY (RIG_ID)
);
/*==============================================================*/
/* INDEX: IDX_RIG_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_RIG_ID ON SIAK.SEC_RIGHT (
RIG_ID
);

/*==============================================================*/
/* INDEX: IDX_RIG_TYPE */
/*==============================================================*/
CREATE INDEX IDX_RIG_TYPE ON SIAK.SEC_RIGHT (
RIG_TYPE
);

/*==============================================================*/
/* INDEX: IDX_RIG_NAME */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_RIG_NAME ON SIAK.SEC_RIGHT (
RIG_NAME
);

/*==============================================================*/
/* TABLE: SEC_ROLE */
/*==============================================================*/
CREATE TABLE SIAK.SEC_ROLE (
 ROL_ID INTEGER NOT NULL,
 ROL_SHORTDESCRIPTION VARCHAR(30) NOT NULL,
 ROL_LONGDESCRIPTION VARCHAR(1000) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_ROLE PRIMARY KEY (ROL_ID)
);
/*==============================================================*/
/* INDEX: IDX_ROLE_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ROLE_ID ON SIAK.SEC_ROLE (
ROL_ID
);

/*==============================================================*/
/* INDEX: IDX_ROLE_SHORTDESCRIPTION */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ROLE_SHORTDESCRIPTION ON SIAK.SEC_ROLE (
ROL_SHORTDESCRIPTION
);

/*==============================================================*/
/* TABLE: SEC_ROLEGROUP */
/*==============================================================*/
CREATE TABLE SIAK.SEC_ROLEGROUP (
 RLG_ID INTEGER NOT NULL,
 GRP_ID INTEGER NOT NULL,
 ROL_ID INTEGER NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_ROLEGROUP PRIMARY KEY (RLG_ID)
);
/*==============================================================*/
/* INDEX: IDX_RLG_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_RLG_ID ON SIAK.SEC_ROLEGROUP (
RLG_ID
);

/*==============================================================*/
/* INDEX: IDX_RLG_GRPROL */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_RLG_GRPROL ON SIAK.SEC_ROLEGROUP (
GRP_ID,
ROL_ID
);

/*==============================================================*/
/* TABLE: SEC_USER */
/*==============================================================*/
CREATE TABLE SIAK.SEC_USER (
 USR_ID INTEGER NOT NULL,
 USR_LOGINNAME VARCHAR(50) NOT NULL,
 USR_PASSWORD VARCHAR(50) NOT NULL,
 USR_LASTNAME VARCHAR(50) NULL,
 USR_FIRSTNAME VARCHAR(50) NULL,
 USR_EMAIL VARCHAR(200) NULL,
 USR_LOCALE VARCHAR(5) NULL,
 USR_ENABLED SMALLINT NOT NULL DEFAULT 0,
 USR_ACCOUNTNONEXPIRED SMALLINT NOT NULL DEFAULT 1,
 USR_CREDENTIALSNONEXPIRED SMALLINT NOT NULL DEFAULT 1,
 USR_ACCOUNTNONLOCKED SMALLINT NOT NULL DEFAULT 1,
 USR_TOKEN VARCHAR(20) NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_USER PRIMARY KEY (USR_ID)
);
/*==============================================================*/
/* INDEX: IDX_USR_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_USR_ID ON SIAK.SEC_USER (
USR_ID
);

/*==============================================================*/
/* INDEX: IDX_USR_LOGINNAME */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_USR_LOGINNAME ON SIAK.SEC_USER (
USR_LOGINNAME
);

/*==============================================================*/
/* TABLE: SEC_USERROLE */
/*==============================================================*/
CREATE TABLE SIAK.SEC_USERROLE (
 URR_ID INTEGER NOT NULL,
 USR_ID INTEGER NOT NULL,
 ROL_ID INTEGER NOT NULL,
 VERSION INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT PK_SEC_USERROLE PRIMARY KEY (URR_ID)
);
/*==============================================================*/
/* INDEX: IDX_URR_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_URR_ID ON SIAK.SEC_USERROLE (
URR_ID
);

/*==============================================================*/
/* INDEX: IDX_URR_USRROL */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_URR_USRROL ON SIAK.SEC_USERROLE (
USR_ID,
ROL_ID
);

/*==============================================================*/
/* TABLE: SYS_COUNTRYCODE */
/*==============================================================*/
CREATE TABLE SIAK.SYS_COUNTRYCODE (
 CCD_ID INTEGER NOT NULL,
 CCD_NAME VARCHAR(48) NULL,
 CCD_CODE2 VARCHAR(2) NOT NULL,
 VERSION INTEGER NULL DEFAULT 0,
 CONSTRAINT PK_SYS_COUNTRYCODE PRIMARY KEY (CCD_ID)
);
/*==============================================================*/
/* INDEX: IDX_CCD_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_CCD_ID ON SIAK.SYS_COUNTRYCODE (
CCD_ID
);

/*==============================================================*/
/* INDEX: IDX_CCD_CODE2 */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_CCD_CODE2 ON SIAK.SYS_COUNTRYCODE (
CCD_CODE2
);

/*==============================================================*/
/* TABLE: SYS_IP4COUNTRY */
/*==============================================================*/
CREATE TABLE SIAK.SYS_IP4COUNTRY (
 I4CO_ID INTEGER NOT NULL,
 I4CO_IP INTEGER NULL,
 I4CO_CCD_ID INTEGER NULL,
 VERSION INTEGER NULL DEFAULT 0,
 CONSTRAINT PK_SYS_IP4COUNTRY PRIMARY KEY (I4CO_ID)
);
/*==============================================================*/
/* INDEX: IDX_I4CO_ID */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_I4CO_ID ON SIAK.SYS_IP4COUNTRY (
I4CO_ID
);

/*==============================================================*/
/* INDEX: IDX_I4CO_IP */
/*==============================================================*/
CREATE INDEX IDX_I4CO_IP ON SIAK.SYS_IP4COUNTRY (
I4CO_IP
);

/*==============================================================*/
/* INDEX: IDX_I4CO_CCD_ID */
/*==============================================================*/
CREATE INDEX IDX_I4CO_CCD_ID ON SIAK.SYS_IP4COUNTRY (
I4CO_CCD_ID
);


ALTER TABLE SIAK.AUFTRAG
 ADD CONSTRAINT REF_AUF_TO_KUN FOREIGN KEY (AUF_KUN_ID)
 REFERENCES SIAK.KUNDE (KUN_ID)
 ON DELETE CASCADE ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.AUFTRAGPOSITION
 ADD CONSTRAINT REF_AUP_TO_ART FOREIGN KEY (ART_ID)
 REFERENCES SIAK.ARTIKEL (ART_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.AUFTRAGPOSITION
 ADD CONSTRAINT REF_AUP_TO_AUF FOREIGN KEY (AUP_AUF_ID)
 REFERENCES SIAK.AUFTRAG (AUF_ID)
 ON DELETE CASCADE ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.KUNDE
 ADD CONSTRAINT REF_KUN_TO_BRA FOREIGN KEY (KUN_BRA_ID)
 REFERENCES SIAK.BRANCHE (BRA_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.KUNDE
 ADD CONSTRAINT REF_KUN_TO_FIL FOREIGN KEY (KUN_FIL_ID)
 REFERENCES SIAK.FILIALE (FIL_ID)
 ON DELETE CASCADE ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.LOG_IP2COUNTRY
 ADD CONSTRAINT REF_I2C_TO_CCD FOREIGN KEY (CCD_ID)
 REFERENCES SIAK.SYS_COUNTRYCODE (CCD_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_GROUPRIGHT
 ADD CONSTRAINT REF_GRI_TO_GRP FOREIGN KEY (GRP_ID)
 REFERENCES SIAK.SEC_GROUP (GRP_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_GROUPRIGHT
 ADD CONSTRAINT REF_GRI_TO_RIG FOREIGN KEY (RIG_ID)
 REFERENCES SIAK.SEC_RIGHT (RIG_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_LOGINLOG
 ADD CONSTRAINT REF_LGL_TO_I2C FOREIGN KEY (I2C_ID)
 REFERENCES SIAK.LOG_IP2COUNTRY (I2C_ID)
 ON DELETE CASCADE ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_ROLEGROUP
 ADD CONSTRAINT REF_RLG_TO_GRP FOREIGN KEY (GRP_ID)
 REFERENCES SIAK.SEC_GROUP (GRP_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_ROLEGROUP
 ADD CONSTRAINT REF_RLG_TO_ROL FOREIGN KEY (ROL_ID)
 REFERENCES SIAK.SEC_ROLE (ROL_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_USERROLE
 ADD CONSTRAINT REF_AUT_TO_ROL FOREIGN KEY (ROL_ID)
 REFERENCES SIAK.SEC_ROLE (ROL_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

ALTER TABLE SIAK.SEC_USERROLE
 ADD CONSTRAINT REF_AUT_TO_USR FOREIGN KEY (USR_ID)
 REFERENCES SIAK.SEC_USER (USR_ID)
 ON DELETE RESTRICT ON UPDATE RESTRICT ENFORCED ENABLE QUERY OPTIMIZATION;

 /******************** Filiale Daten ********************/
INSERT INTO SIAK.FILIALE (FIL_ID, FIL_NR, FIL_BEZEICHNUNG,FIL_NAME1,FIL_NAME2,FIL_ORT,VERSION) VALUES
(1,'0001','Filiale Muenchen','Hoermann Gmbh','Personaldienstleistungen','Muenchen',0),
(2,'0002','Filiale Berlin',  'Hoermann Gmbh','Personaldienstleistungen','Berlin',  0);

/******************** Security: USERS ********************/  
INSERT INTO SIAK.SEC_USER (USR_ID, USR_LOGINNAME, USR_PASSWORD, USR_LASTNAME, USR_FIRSTNAME, USR_EMAIL, USR_LOCALE, USR_ENABLED, USR_ACCOUNTNONEXPIRED, USR_CREDENTIALSNONEXPIRED, USR_ACCOUNTNONLOCKED, USR_TOKEN,  VERSION) VALUES 
(10, 'guest', 'guest', 'guestFirstname', 'guestlastname', 'guest@web.de', NULL, 1, 1, 1, 1, null, 0),
(11, 'admin', 'admin', 'Visor', 'Super', 'admin@web.de', NULL, 1, 1, 1, 1, null, 0),
(12, 'user1', 'user1', 'Kingsley', 'Ben', 'B.Kingsley@web.de', NULL, 1, 1, 1, 1, null, 0),
(13, 'headoffice', 'headoffice', 'Willis', 'Bruce', 'B.Willis@web.de', NULL, 1, 1, 1, 1, null, 0),
(14, 'user2', 'user2', 'Kingdom', 'Marta', 'M.Kingdom@web.de', NULL, 1, 1, 1, 1, null, 0);

/******************** Security: ROLES ********************/  
INSERT INTO SIAK.SEC_ROLE (ROL_ID, ROL_SHORTDESCRIPTION, ROL_LONGDESCRIPTION, VERSION) VALUES
(10000,'ROLE_ADMIN','Administrator Role', 0),
(10002,'ROLE_OFFICE_ALL_RIGHTS','Office User role with all rights granted.', 0),
(10003,'ROLE_GUEST','Guest Role', 0),
(10004,'ROLE_OFFICE_ONLY_VIEW','Office user with rights that granted only view of data.', 0),
(10005,'ROLE_HEADOFFICE_USER','Headoffice User Role', 0);


/******************** Security: USER-ROLES ********************/  
/* Guest account authorities */
INSERT INTO SIAK.SEC_USERROLE (URR_ID, USR_ID, ROL_ID, VERSION) VALUES
(11000, 10, 10003, 0),
/* User1 Usr-Id: 12 */
(11001, 12, 10002, 0),
/* Headoffice user account authorities */
(11010, 13, 10005, 0);

/* Admin Usr-ID: 11 */
INSERT INTO SIAK.SEC_USERROLE (URR_ID, USR_ID, ROL_ID, VERSION) VALUES
(11003, 11, 10000, 0),
(11005, 11, 10002, 0),
(11006, 11, 10003, 0),
(11008, 11, 10004, 0),
(11009, 11, 10005, 0),
/* User2 Usr-ID: 14 */
(11007, 14, 10004, 0);

/******************** Security: SEC_GROUPS ********************/  
INSERT INTO SIAK.SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(13001, 'Headoffice Supervisor Group', 'kjhf ff hgfd', 0),
(13002, 'Admin Group - user accounts', 'create/modify user accounts', 0),
(13003, 'Guest Group', 'Minimal Rights for the guests', 0),
(13004, 'Admin Group - user rights', 'edit/modify user rights', 0),
/* Customers */
(13000, 'Customers_View', 'Allow to  view customers data', 0),
(13008, 'Customers_New', 'Allow create new customers', 0),
(13006, 'Customers_Edit', 'Allow editing of customers', 0),
(13007, 'Customers_Delete', 'Allow deleting of customers', 0),
/* Orders */
(13010, 'Orders_View', 'Allow to view orders data', 0),
(13011, 'Orders_New', 'Allow create new orders', 0),
(13012, 'Orders_Edit', 'Allow editing of orders', 0),
(13013, 'Orders_Delete', 'Allow deleting of orders', 0),
/* Branches */
(13020, 'Branch_View', 'Allow to view branches data', 0),
(13021, 'Branch_New', 'Allow create new branches', 0),
(13022, 'Branch_Edit', 'Allow editing of branches', 0),
(13023, 'Branch_Delete', 'Allow deleting of branches', 0),
/* Articles */
(13030, 'Articles_View', 'Allow to view articles data', 0),
(13031, 'Articles_New', 'Allow create new articles', 0),
(13032, 'Articles_Edit', 'Allow editing of articles', 0),
(13033, 'Articles_Delete', 'Allow deleting of articles', 0),
/* Offices */
(13040, 'Offices_View', 'Allow to view offices data', 0),
(13041, 'Offices_New', 'Allow create new offices', 0),
(13042, 'Offices_Edit', 'Allow editing of offices', 0),
(13043, 'Offices_Delete', 'Allow deleting of offices', 0),
/* Users */
(13060, 'User_View_UsersOnly', 'Allow to view own user data.', 0),
(13061, 'User_Edit_UsersOnly', 'Allow to edit own user data.', 0),
(13062, 'Users_View', 'Allow to view all users data.', 0),
(13063, 'Users_New', 'Allow create new users', 0),
(13064, 'Users_Edit', 'Allow editing of users', 0),
(13065, 'Users_Delete', 'Allow deleting of users', 0),
(13066, 'Users_Search', 'Allow searching of users', 0),
/* secGroup */
(13070, 'Security_Groups', 'Allow to view the securityGroups Dialog', 0),
/* secRole */
(13071, 'Security_Roles', 'Allow to view the securityRoles Dialog', 0),
/* secRight */
(13072, 'Security_Rights', 'Allow to view the securityRights Dialog', 0);


/******************** Security: SEC_ROLE-GROUPS ********************/  
/* ROLE_OFFICE_ALL_RIGHTS */
/* Group: Customers_View */
INSERT INTO SIAK.SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(12000, 13000, 10002, 0),
/* Group: Customers_New */
(12001, 13008, 10002, 0),
/*  Group: Customers_Edit */
(12002, 13006, 10002, 0),
/*  Group: Customers_Delete */
(12003, 13007, 10002, 0),
/*  Group: Orders_View */
(12004, 13010, 10002, 0),
/*  Group: Orders_New */
(12005, 13011, 10002, 0),
/*  Group: Orders_Edit */
(12006, 13012, 10002, 0),
/*  Group: Orders_Delete */
(12007, 13013, 10002, 0),
/*  Group: User_View_UsersOnly */
(12008, 13060, 10002, 0),
/*  Group: User_Edit_UsersOnly */
(12009, 13061, 10002, 0),
/* ROLE_OFFICE_ONLY_VIEW */
/* Group: Customers_View */
(12010, 13000, 10004, 0),
/*  Group: Orders_View */
(12011, 13010, 10004, 0),
/*  Group: User_View_UsersOnly */
(12012, 13060, 10004, 0),
/* ROLE_GUEST */
(12020, 13003, 10003, 0),
/* ROLE_ADMIN */
(12050, 13002, 10000, 0),
(12051, 13000, 10000, 0),
(12052, 13001, 10000, 0),
(12053, 13003, 10000, 0),
(12054, 13004, 10000, 0),
(12055, 13006, 10000, 0),
(12056, 13007, 10000, 0),
(12057, 13008, 10000, 0),
(12058, 13010, 10000, 0),
(12059, 13011, 10000, 0),
(12060, 13012, 10000, 0),
(12061, 13013, 10000, 0),
(12062, 13020, 10000, 0),
(12063, 13021, 10000, 0),
(12064, 13022, 10000, 0),
(12065, 13023, 10000, 0),
(12066, 13030, 10000, 0),
(12067, 13031, 10000, 0),
(12068, 13032, 10000, 0),
(12069, 13033, 10000, 0),
(12070, 13040, 10000, 0),
(12071, 13041, 10000, 0),
(12072, 13042, 10000, 0),
(12073, 13043, 10000, 0),
/* Group: Users_View */
(12074, 13062, 10000, 0),
/* Group: Users_New */
(12075, 13063, 10000, 0),
/* Group: Users_Edit */
(12076, 13064, 10000, 0),
/* Group: Users_Delete */
(12077, 13065, 10000, 0),
/* Group: Users_Search */
(12078, 13066, 10000, 0),

/* ROLE_HEADOFFICE_USER */
/* Group: Branch_View */
(12100, 13020, 10005, 0),
/* Group: Branch_New */
(12101, 13021, 10005, 0),
/* Group: Branch_Edit */
(12102, 13022, 10005, 0),
/* Group: Branch_Delete */
(12103, 13023, 10005, 0),
/* Group: Articles_View */
(12104, 13030, 10005, 0),
/* Group: Articles_New */
(12105, 13031, 10005, 0),
/* Group: Articles_Edit */
(12106, 13032, 10005, 0),
/* Group: Articles_Delete */
(12107, 13033, 10005, 0),
/* Group: Offices_View */
(12108, 13040, 10005, 0),
/* Group: Offices_New */
(12109, 13041, 10005, 0),
/* Group: Offices_Edit */
(12110, 13042, 10005, 0),
/* Group: Offices_Delete */
(12111, 13043, 10005, 0),
/*  Group: User_View_UsersOnly */
(12115, 13060, 10005, 0),
/*  Group: User_Edit_UsersOnly */
(12116, 13061, 10005, 0),
     
/* ROLE_ADMIN */
/* Group: Security_Groups */
(12117, 13070, 10000, 0),
/* Group: Security_Roles */
(12118, 13071, 10000, 0),
/* Group: Security_Rights */
(12119, 13072, 10000, 0);


/******************** Security: SEC_RIGHTS ********************/  
INSERT INTO SIAK.SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES
(15000, 1, 'menuCat_OfficeData', 0),
(15001, 2, 'menuItem_OfficeData_Customers', 0),
(15002, 2, 'menuItem_OfficeData_Orders', 0),
(15003, 1, 'menuCat_MainData', 0),
(15004, 2, 'menuItem_MainData_ArticleItems', 0),
(15005, 2, 'menuItem_MainData_Branch', 0),
(15006, 2, 'menuItem_MainData_Office', 0),
(15007, 1, 'menuCat_Administration', 0),
(15008, 2, 'menuItem_Administration_Users', 0),
(15009, 2, 'menuItem_Administration_UserRoles', 0),
(15010, 2, 'menuItem_Administration_Roles', 0),
(15011, 2, 'menuItem_Administration_RoleGroups', 0),
(15012, 2, 'menuItem_Administration_Groups', 0),
(15013, 2, 'menuItem_Administration_GroupRights', 0),
(15014, 2, 'menuItem_Administration_Rights', 0),
(15015, 1, 'menuCat_UserRights', 0),
(15016, 2, 'menuItem_Administration_LoginsLog', 0),
(15017, 2, 'menuItem_Administration_HibernateStats', 0),
(15018, 2, 'menu_Item_Calendar', 0),

/* Pages = Type(0) */
/* --> Page Customer */
(15100, 0, 'window_customerList', 0),
(15101, 0, 'window_customerDialog', 0),
/* --> Page Orders */
(15102, 0, 'orderListWindow', 0),
(15103, 0, 'orderDialogWindow', 0),
/* --> Page Articles */
(15104, 0, 'windowArticlesList', 0),
(15105, 0, 'window_ArticlesDialog', 0),
/* --> Page Branches */
(15106, 0, 'window_BranchesList', 0),
(15107, 0, 'window_BranchesDialog', 0),
/* --> Page Offices */
(15108, 0, 'window_OfficeList', 0),
(15109, 0, 'window_OfficeDialog', 0),
/* --> Page Admin - Users */
(15110, 0, 'page_Admin_UserList', 0),
(15111, 0, 'page_Admin_UserDialog', 0),
/* --> Page Admin - UserRoles */
(15112, 0, 'page_Security_UserRolesList', 0),
(15113, 0, 'page_Security_RolesList', 0),
/* --> Page Admin - Roles */
(15114, 0, 'page_Security_RolesDialog', 0),
/* --> Page Admin - RoleGroups */
(15115, 0, 'page_Security_RoleGroupsList', 0),
/* --> Page Admin - Groups */
(15116, 0, 'page_Security_GroupsList', 0),
(15117, 0, 'page_Security_GroupsDialog', 0),
/* --> Page Admin - GroupRights */
(15118, 0, 'page_Security_GroupRightsList', 0),
/* --> Page Admin - Rights */
(15119, 0, 'page_Security_RightsList', 0),
(15120, 0, 'page_Security_RightsDialog', 0),
/* --> Page Login Log */
(15121, 0, 'page_Admin_LoginLogList', 0),
/* --> Nachtrag Page Orders */
(15122, 0, 'orderPositionDialogWindow', 0),

/* Tabs = Type(5) */
(15200, 5, 'tab_CustomerDialog_Address', 0),
(15201, 5, 'tab_CustomerDialog_Chart', 0),
(15202, 5, 'tab_CustomerDialog_Orders', 0),
(15203, 5, 'tab_CustomerDialog_Memos', 0),

/* Components = Type(6) */
/* --> CustomerList BUTTON */
(15300, 6, 'button_CustomerList_btnHelp', 0),
(15301, 6, 'button_CustomerList_NewCustomer', 0),
(15302, 6, 'button_CustomerList_CustomerFindDialog', 0),
(15303, 6, 'button_CustomerList_PrintList', 0),
/* --> CustomerDialog BUTTON */
(15305, 6, 'button_CustomerDialog_btnHelp', 0),
(15306, 6, 'button_CustomerDialog_btnNew', 0),
(15307, 6, 'button_CustomerDialog_btnEdit', 0),
(15308, 6, 'button_CustomerDialog_btnDelete', 0),
(15309, 6, 'button_CustomerDialog_btnSave', 0),
(15310, 6, 'button_CustomerDialog_btnClose', 0),
/* --> OrderList BUTTON */
(15400, 6, 'button_OrderList_btnHelp', 0),
(15401, 6, 'button_OrderList_NewOrder', 0),
/* --> OrderDialog BUTTON */
(15410, 6, 'button_OrderDialog_btnHelp', 0),
(15411, 6, 'button_OrderDialog_btnNew', 0),
(15412, 6, 'button_OrderDialog_btnEdit', 0),
(15413, 6, 'button_OrderDialog_btnDelete', 0),
(15414, 6, 'button_OrderDialog_btnSave', 0),
(15415, 6, 'button_OrderDialog_btnClose', 0),
(15416, 6, 'button_OrderDialog_PrintOrder', 0),
(15417, 6, 'button_OrderDialog_NewOrderPosition', 0),
/* --> OrderPositionDialog BUTTON */
(15430, 6, 'button_OrderPositionDialog_btnHelp', 0),
(15431, 6, 'button_OrderPositionDialog_PrintOrderPositions', 0),
(15432, 6, 'button_OrderPositionDialog_btnNew', 0),
(15433, 6, 'button_OrderPositionDialog_btnEdit', 0),
(15434, 6, 'button_OrderPositionDialog_btnDelete', 0),
(15435, 6, 'button_OrderPositionDialog_btnSave', 0),
(15436, 6, 'button_OrderPositionDialog_btnClose', 0),
/* USERS */
/* --> userListWindow */
(15470, 0, 'userListWindow', 0),
/* --> userListWindow BUTTONS*/
(15471, 6, 'button_UserList_btnHelp', 0),
(15472, 6, 'button_UserList_NewUser', 0),
(15473, 6, 'button_UserList_PrintUserList', 0),
(15474, 6, 'button_UserList_SearchLoginname', 0),
(15475, 6, 'button_UserList_SearchLastname', 0),
(15476, 6, 'button_UserList_SearchEmail', 0),
(15477, 6, 'checkbox_UserList_ShowAll', 0),
/* --> Mehode onDoubleClick Listbox */
(15778, 3, 'UserList_listBoxUser.onDoubleClick', 0),
/* --> userDialogWindow */
(15480, 0, 'userDialogWindow', 0),
/* --> userDialogWindow BUTTONS*/
(15481, 6, 'button_UserDialog_btnHelp', 0),
(15482, 6, 'button_UserDialog_btnNew', 0),
(15483, 6, 'button_UserDialog_btnEdit', 0),
(15484, 6, 'button_UserDialog_btnDelete', 0),
(15485, 6, 'button_UserDialog_btnSave', 0),
(15486, 6, 'button_UserDialog_btnClose', 0),
/* --> userDialogWindow Special Admin Panels */
(15487, 6, 'panel_UserDialog_Status', 0),
(15488, 6, 'panel_UserDialog_SecurityToken', 0),
/* --> userListWindow Search panel */
(15489, 6, 'hbox_UserList_SearchUsers', 0),
/* Tab Details */
(15490, 6, 'tab_UserDialog_Details', 0),
(15491, 3, 'data_SeeAllUserData', 0),
/* BRANCHES */
/* branchListWindow Buttons*/
/* --> button_BranchList_btnHelp */
(15502, 0, 'button_BranchMain_PrintBranches', 0),
(15503, 0, 'button_BranchMain_Search_BranchName', 0),
/* branchDialogWindow BUTTONS */
(15510, 6, 'button_BranchMain_btnHelp', 0),
(15511, 6, 'button_BranchMain_btnNew', 0),
(15512, 6, 'button_BranchMain_btnEdit', 0),
(15513, 6, 'button_BranchMain_btnDelete', 0),
(15514, 6, 'button_BranchMain_btnSave', 0),
(15515, 6, 'button_BranchMain_btnClose', 0),
/* ARTICLES */
/* window_ArticlesList Buttons*/
(15530, 6, 'button_ArticlesList_btnHelp', 0),
(15531, 6, 'button_ArticleList_NewArticle', 0),
(15532, 6, 'button_ArticleList_PrintList', 0),
(15533, 6, 'button_ArticleList_SearchArticleID', 0),
(15534, 6, 'button_ArticleList_SearchName', 0),
/* window_ArticlesDialog Buttons*/
(15540, 6, 'button_ArticlesDialog_btnHelp', 0),
(15541, 6, 'button_ArticlesDialog_btnNew', 0),
(15542, 6, 'button_ArticlesDialog_btnEdit', 0),
(15543, 6, 'button_ArticlesDialog_btnDelete', 0),
(15544, 6, 'button_ArticlesDialog_btnSave', 0),
(15545, 6, 'button_ArticlesDialog_btnClose', 0),
/* OFFICES */
/* window_OfficeList Buttons*/
/* --> button_BranchList_btnHelp */
(15602, 6, 'button_OfficeList_PrintList', 0),
(15603, 6, 'button_OfficeList_SearchNo', 0),
(15604, 6, 'button_OfficeList_SearchName', 0),
(15605, 6, 'button_OfficeList_SearchCity', 0),
/* window_OfficeDialog BUTTONS */
(15611, 6, 'button_OfficeMain_btnHelp', 0),
(15612, 6, 'button_OfficeMain_btnNew', 0),
(15613, 6, 'button_OfficeMain_btnEdit', 0),
(15614, 6, 'button_OfficeMain_btnDelete', 0),
(15615, 6, 'button_OfficeMain_btnSave', 0),
(15616, 6, 'button_OfficeMain_btnClose', 0),

/* Method/Event = Type(3) */
/* --> CustomerList BUTTON */
(15700, 3, 'CustomerList_listBoxCustomer.onDoubleClick', 0),
/* --> secRoleDialogWindow */
(15750, 0, 'secRoleDialogWindow', 0),
/* --> secRoleDialogWindow BUTTONS*/
(15751, 6, 'button_SecRoleDialog_btnHelp', 0),
(15752, 6, 'button_SecRoleDialog_btnNew', 0),
(15753, 6, 'button_SecRoleDialog_btnEdit', 0),
(15754, 6, 'button_SecRoleDialog_btnDelete', 0),
(15755, 6, 'button_SecRoleDialog_btnSave', 0),
(15756, 6, 'button_SecRoleDialog_btnClose', 0),
/* --> secGroupDialogWindow */
(15760, 0, 'secGroupDialogWindow', 0),
/* --> secGroupDialogWindow BUTTONS*/
(15761, 6, 'button_SecGroupDialog_btnHelp', 0),
(15762, 6, 'button_SecGroupDialog_btnNew', 0),
(15763, 6, 'button_SecGroupDialog_btnEdit', 0),
(15764, 6, 'button_SecGroupDialog_btnDelete', 0),
(15765, 6, 'button_SecGroupDialog_btnSave', 0),
(15766, 6, 'button_SecGroupDialog_btnClose', 0),
/* --> secRightDialogWindow */
(15770, 0, 'secRightDialogWindow', 0),
/* --> secRightDialogWindow BUTTONS*/
(15771, 6, 'button_SecRightDialog_btnHelp', 0),
(15772, 6, 'button_SecRightDialog_btnNew', 0),
(15773, 6, 'button_SecRightDialog_btnEdit', 0),
(15774, 6, 'button_SecRightDialog_btnDelete', 0),
(15775, 6, 'button_SecRightDialog_btnSave', 0),
(15776, 6, 'button_SecRightDialog_btnClose', 0);

/******************** Security: SEC_GROUP-RIGHTS ********************/  
/* Headoffice Supervisor Group*/
INSERT INTO SIAK.SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(14003, 13001, 15003, 0),
(14004, 13001, 15004, 0),
(14005, 13001, 15005, 0),
(14006, 13001, 15006, 0),
/* Administration Group*/
(14007, 13002, 15007, 0),
(14008, 13002, 15008, 0),
(14009, 13002, 15009, 0),
(14010, 13002, 15010, 0),
(14011, 13002, 15011, 0),
(14012, 13002, 15012, 0),
(14013, 13002, 15013, 0),
(14014, 13002, 15014, 0),
(14015, 13002, 15015, 0),
(14016, 13002, 15016, 0),
(14017, 13002, 15017, 0),

/* New */
/* Group: Customers_View */
/* Right: menuCat_OfficeData */
(14200, 13000, 15000, 0),
/* Right: menuItem_OfficeData_Customers */
(14201, 13000, 15001, 0),
/* Right: customerListWindow */
(14202, 13000, 15100, 0),
/* Right: button_CustomerList_Help */
(14203, 13000, 15305, 0),
/* Right: CustomerList_listBoxCustomer.onDoubleClick */
(14204, 13000, 15700, 0),

/* Right: customerDialogWindow */
(14205, 13000, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14206, 13000, 15310, 0),
/* Right: button_CustomerList_Help */
(14207, 13000, 15300, 0),
/* Right: button_CustomerList_CustomerFindDialog */
(14208, 13000, 15302, 0),
/* Right: button_CustomerList_PrintList */
(14209, 13000, 15303, 0),

/* Tab tab_CustomerDialog_Address */
(14210, 13000, 15200, 0),
/* Tab tab_CustomerDialog_Addition */
(14211, 13000, 15201, 0),
/* Tab tab_CustomerDialog_Orders */
(14212, 13000, 15202, 0),
/* Tab tab_CustomerDialog_Memos */
(14213, 13000, 15203, 0),

/* Group: Customers_New */
/* Right: customerListWindow */
(14230, 13008, 15100, 0),
/* Right: button_CustomerList_NewCustomer */
(14231, 13008, 15301, 0),
/* Right: customerDialogWindow */
(14232, 13008, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14233, 13008, 15310, 0),
/* Right: button_CustomerDialog_btnNew */
(14234, 13008, 15306, 0),
/* Right: button_CustomerDialog_btnEdit */
(14235, 13008, 15307, 0),
/* Right: button_CustomerDialog_btnSave */
(14236, 13008, 15309, 0),

/* Group: Customers_Edit */
/* Right: customerListWindow */
(14240, 13006, 15100, 0),

/* Right: customerDialogWindow */
(14241, 13006, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14242, 13006, 15310, 0),
/* Right: button_CustomerDialog_btnEdit */
(14243, 13006, 15307, 0),
/* Right: button_CustomerDialog_btnSave */
(14244, 13006, 15309, 0),

/* Group: Customers_Delete */
/* Right: customerListWindow */
(14250, 13007, 15100, 0),
/* Right: customerDialogWindow */
(14251, 13007, 15101, 0),
/* Right: button_CustomerDialog_btnClose */
(14252, 13007, 15310, 0),
/* Right: button_CustomerDialog_btnDelete */
(14253, 13007, 15308, 0),

/*----------------------------------------------------------*/
/* Group: Orders_View */
/* Right: menuCat_OfficeData */
(14300, 13010, 15000, 0),
/* Right: menuItem_OfficeData_Orders */
(14301, 13010, 15002, 0),
/* Right: orderListWindow */
(14302, 13010, 15102, 0),
/* Right: button_OrderList_btnHelp */
(14303, 13010, 15400, 0),
/* Right: CustomerList_listBoxCustomer.onDoubleClick */
(14304, 13010, 15700, 0),
/* Right: orderDialogWindow */
(14305, 13010, 15103, 0),
/* Right: button_OrderDialog_btnClose */
(14306, 13010, 15415, 0),
/* Right: button_OrderDialog_btnHelp */
(14307, 13010, 15410, 0),
/* Right: button_OrderDialog_PrintOrder */
(14308, 13010, 15416, 0),
/* Right: orderPositionDialogWindow */
(14309, 13010, 15122, 0),
/* Right: button_OrderPositionDialog_btnClose */
(14310, 13010, 15436, 0),
/* Right: button_OrderPositionDialog_btnHelp */
(14311, 13010, 15430, 0),
/* Right: button_OrderPositionDialog_PrintOrder */
(14312, 13010, 15431, 0),

/* Group: Orders_New */
/* Right: button_OrderList_NewOrder */
(14320, 13011, 15401, 0),
/* Right: button_OrderDialog_btnClose */
(14321, 13011, 15415, 0),
/* Right: button_OrderDialog_btnNew */
(14322, 13011, 15411, 0),
/* Right: button_OrderDialog_btnEdit */
(14323, 13011, 15412, 0),
/* Right: button_CustomerDialog_btnSave */
(14324, 13011, 15414, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14325, 13011, 15417, 0),
/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14326, 13011, 15436, 0),
/* Right: button_OrderPositionDialog_btnNew */
(14327, 13011, 15432, 0),
/* Right: button_OrderPositionDialog_btnEdit */
(14328, 13011, 15433, 0),
/* Right: button_OrderPositionDialog_btnSave */
(14329, 13011, 15435, 0),

/* Group: Orders_Edit */
/* Right: button_OrderDialog_btnClose */
(14330, 13012, 15415, 0),
/* Right: button_OrderDialog_btnEdit */
(14331, 13012, 15412, 0),
/* Right: button_CustomerDialog_btnSave */
(14332, 13012, 15414, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14333, 13012, 15417, 0),

/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14334, 13012, 15436, 0),
/* Right: button_OrderPositionDialog_btnEdit */
(14335, 13012, 15433, 0),
/* Right: button_OrderPositionDialog_btnSave */
(14336, 13012, 15435, 0),

/* Group: Orders_Delete */
/* Right: button_OrderDialog_btnClose */
(14340, 13013, 15415, 0),
/* Right: button_OrderDialog_btnDelete */
(14341, 13013, 15413, 0),
/* Right: button_OrderDialog_NewOrderPosition */
(14342, 13013, 15417, 0),

/* OrderPositions -------------------*/
/* Right: button_OrderPositionDialog_btnClose */
(14343, 13013, 15436, 0),
/* Right: button_OrderPositionDialog_btnDelete */
(14344, 13013, 15434, 0),

/* USERS ----------------- */
/* Group: User_View_UsersOnly */
/* Right: menuCat_Administration */
(14360, 13060, 15007, 0),
/* Right: menuItem_Administration_Users */
(14361, 13060, 15008, 0),
/* Right: userListWindow */
(14362, 13060, 15470, 0),
/* Right: button_UserList_btnHelp */
(14363, 13060, 15471, 0),
/* Right: UserList_listBoxUser.onDoubleClick */
(14364, 13060, 15778, 0),
/* Right: userDialogWindow */
(14365, 13060, 15480, 0),
/* Right: tab_UserDialog_Details */
(14366, 13060, 15490, 0),
/* Right: button_UserDialog_btnHelp */
(14367, 13060, 15481, 0),
/* Right: button_Dialog_btnClose */
(14368, 13060, 15486, 0),

/* Group: User_Edit_UsersOnly */
/* Right: button_UserDialog_btnEdit */
(14370, 13061, 15483, 0),
/* Right: button_Dialog_btnSave */
(14371, 13061, 15485, 0),

/* Group: Users_View */
/* Right: menuCat_Administration */
(14380, 13062, 15007, 0),
/* Right: menuItem_Administration_Users */
(14381, 13062, 15008, 0),
/* Right: userListWindow */
(14382, 13062, 15470, 0),
/* Right: button_UserList_btnHelp */
(14383, 13062, 15471, 0),
/* Right: button_UserList_PrintUserList */
(14384, 13062, 15473, 0),
/* Right: UserList_listBoxUser.onDoubleClick */
(14385, 13062, 15778, 0),
/* Right: userDialogWindow */
(14386, 13062, 15480, 0),
/* Right: tab_UserDialog_Details */
(14387, 13062, 15490, 0),
/* Right: button_UserDialog_btnHelp */
(14388, 13062, 15481, 0),
/* Right: button_UserDialog_btnClose */
(14389, 13062, 15486, 0),
/* Right: panel_UserDialog_Status */
(14390, 13062, 15487, 0),
/* Right: panel_UserDialog_SecurityToken */
(14391, 13062, 15488, 0),
/* Right: data_SeeAllUserData */
(14392, 13062, 15491, 0),

/* Group: Users_New */
/* Right: button_UserList_NewUser */
(14395, 13063, 15472, 0),
/* Right: button_UserDialog_btnNew */
(14396, 13063, 15482, 0),
/* Right: button_UserDialog_btnEdit */
(14397, 13063, 15483, 0),
/* Right: button_UserDialog_btnSave */
(14398, 13063, 15485, 0),

/* Group: Users_Edit */
/* Right: button_UserDialog_btnEdit */
(14400, 13064, 15483, 0),
/* Right: button_UserDialog_btnSave */
(14401, 13064, 15485, 0),

/* Group: Users_Delete */
/* Right: button_UserDialog_btnDelete */
(14410, 13065, 15484, 0),

/* Group: Users_Search */
/* Right: hbox_UserList_SearchUsers */
(14420, 13066, 15489, 0),

/* B r a n c h e s */
/* Group: Branch_View */
/* Right: menuCat_MainData */
(14500, 13020, 15003, 0),
/* Right: menuItem_MainData_Branch */
(14501, 13020, 15005, 0),
/* Right: page_BranchesList */
(14502, 13020, 15106, 0),
/* Right: button_BranchList_PrintBranches */
(14504, 13020, 15502, 0),
/* Right: button_BranchList_Search_BranchName */
(14505, 13020, 15503, 0),
/* Right: page_BranchesDialog */
(14507, 13020, 15107, 0),
/* Right: button_BranchDialog_btnHelp */
(14508, 13020, 15510, 0),
/* Right: button_BranchDialog_btnClose */
(14509, 13020, 15515, 0),

/* Group: Branch_New */
/* Right: button_BranchDialog_btnNew */
(14511, 13021, 15511, 0),
/* Right: button_BranchDialog_btnSave */
(14512, 13021, 15514, 0),

/* Group: Branch_Edit */
/* Right: button_BranchDialog_btnEdit */
(14520, 13022, 15512, 0),
/* Right: button_BranchDialog_btnSave */
(14521, 13022, 15514, 0),

/* Group: Branch_Delete */
/* Right: button_BranchDialog_btnDelete */
(14530, 13023, 15513, 0),

/* A r t i c l e s */
/* Group: Articles_View */
/* Right: menuCat_MainData */
(14540, 13030, 15003, 0),
/* Right: menuItem_MainData_ArticleItems */
(14541, 13030, 15004, 0),
/* Right: window_ArticlesList */
(14542, 13030, 15104, 0),
/* Right: button_ArticlesList_btnHelp */
(14543, 13030, 15530, 0),
/* Right: button_ArticleList_PrintList */
(14544, 13030, 15532, 0),
/* Right: window_ArticlesDialog */
(14545, 13030, 15105, 0),
/* Right: button_ArticlesDialog_btnHelp */
(14546, 13030, 15540, 0),
/* Right: button_ArticlesDialog_btnClose */
(14547, 13030, 15545, 0),
/* Right: button_ArticleList_SearchArticleID */
(14548, 13030, 15533, 0),
/* Right: button_ArticleList_SearchName */
(14549, 13030, 15534, 0),

/* Group: Articles_New */
/* Right: button_ArticleList_NewArticle */
(14550, 13031, 15531, 0),
/* Right: button_ArticlesDialog_btnNew */
(14551, 13031, 15541, 0),
/* Right: button_ArticlesDialog_btnSave */
(14552, 13031, 15544, 0),

/* Group: Articles_Edit */
/* Right: button_ArticlesDialog_btnEdit */
(14555, 13032, 15542, 0),
/* Right: button_ArticlesDialog_btnSave */
(14556, 13032, 15544, 0),

/* Group: Articles_Delete */
/* Right: button_ArticlesDialog_btnDelete */
(14560, 13033, 15543, 0),

/* O F F I C E S */
/* Group: Offices_View */
/* Right: menuCat_MainData */
(14570, 13040, 15003, 0),
/* Right: menuItem_MainData_Office */
(14571, 13040, 15006, 0),
/* Right: window_OfficesList */
(14572, 13040, 15108, 0),
/* Right: button_OfficeList_PrintList */
(14574, 13040, 15602, 0),
/* Right: button_OfficeList_SearchNo */
(14575, 13040, 15603, 0),
/* Right: button_OfficeList_SearchName */
(14576, 13040, 15604, 0),
/* Right: button_OfficeList_SearchCity */
(14577, 13040, 15605, 0),
/* Right: window_OfficesDialog */
(14578, 13040, 15109, 0),
/* Right: button_OfficeDialog_btnHelp */
(14579, 13040, 15611, 0),
/* Right: button_OfficeDialog_btnClose */
(14580, 13040, 15616, 0),

/* Group: Offices_New */
/* Right: button_OfficeDialog_btnNew */
(14586, 13041, 15612, 0),
/* Right: button_OfficeDialog_btnSave */
(14587, 13041, 15615, 0),

/* Group: Offices_Edit */
/* Right: button_OfficeDialog_btnEdit */
(14590, 13042, 15613, 0),
/* Right: button_OfficeDialog_btnSave */
(14591, 13042, 15615, 0),

/* Group: Offices_Delete */
/* Right: button_OfficeDialog_btnDelete */
(14595, 13043, 15614, 0),

/* Group: Security_Groups */
/* Right: secGroupDialogWindow */
(14600, 13070, 15760, 0),
/* Right: button_SecGroupDialog_btnHelp */
(14601, 13070, 15761, 0),
/* Right: button_SecGroupDialog_btnNew */
(14602, 13070, 15762, 0),
/* Right: button_SecGroupDialog_btnEdit */
(14603, 13070, 15763, 0),
/* Right: button_SecGroupDialog_btnDelete */
(14604, 13070, 15764, 0),
/* Right: buton_SecGroupDialog_btnSave */
(14605, 13070, 15765, 0),
/* Right: button_SecGroupDialog_btnClose */
(14606, 13070, 15766, 0),

/* Group: Security_Roles */
/* Right: secRoleDialogWindow */
(14610, 13071, 15750, 0),
/* Right: button_SecRoleDialog_btnHelp */
(14611, 13071, 15751, 0),
/* Right: button_SecRoleDialog_btnNew */
(14612, 13071, 15752, 0),
/* Right: button_SecRoleDialog_btnEdit */
(14613, 13071, 15753, 0),
/* Right: button_SecRoleDialog_btnDelete */
(14614, 13071, 15754, 0),
/* Right: button_SecRoleDialog_btnSave */
(14615, 13071, 15755, 0),
/* Right: button_SecRoleDialog_btnClose */
(14616, 13071, 15756, 0),

/* Group: Security_Rights */
/* Right: secRightDialogWindow */
(14620, 13072, 15770, 0),
/* Right: button_SecRightDialog_btnHelp */
(14621, 13072, 15771, 0),
/* Right: button_SecRightDialog_btnNew */
(14622, 13072, 15772, 0),
/* Right: button_SecRightDialog_btnEdit */
(14623, 13072, 15773, 0),
/* Right: button_SecRightDialog_btnDelete */
(14624, 13072, 15774, 0),
/* Right: button_SecRightDialog_btnSave */
(14625, 13072, 15775, 0),

/* Right: button_SecRightDialog_btnClose */
(14626, 13072, 15776, 0);

/* Siak Menu */
--
INSERT INTO SIAK.SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(13073,'Administrasi_View','Allow to  view Administrasi',0),
(13074,'Administrasi_New','Allow create new Administrasi',0),
(13075,'Administrasi_Edit','Allow editing of Administrasi',0),
(13076,'Administrasi_Delete','Allow deleting of Administrasi',0),
(13077,'ManajemenKurikulum_View','Allow to  view ManajemenKurikulum',0),
(13078,'ManajemenKurikulum_New','Allow create new ManajemenKurikulum',0),
(13079,'ManajemenKurikulum_Edit','Allow editing of ManajemenKurikulum',0),
(13080,'ManajemenKurikulum_Delete','Allow deleting of ManajemenKurikulum',0),
(13081,'IsianRencanaStudi_Vew','Allow to  view IsianRencanaStudi',0),
(13082,'IsianRencanaStudi_New','Allow create new IsianRencanaStudi',0),
(13083,'IsianRencanaStudi_Edit','Allow editing of IsianRencanaStudi',0),
(13084,'IsianRencanaStudi_Delete','Allow deleting of IsianRencanaStudi',0),
(13085,'KegiatanBelajarMengajar_View','Allow to  view KegiatanBelajarMengajar',0),
(13086,'KegiatanBelajarMengajar_New','Allow create new KegiatanBelajarMengajar',0),
(13087,'KegiatanBelajarMengajar_Edit','Allow editing of KegiatanBelajarMengajar',0),
(13088,'KegiatanBelajarMengajar_Delete','Allow deleting of KegiatanBelajarMengajar',0),
(13089,'TugasMenulis_View','Allow to  view TugasMenulis',0),
(13090,'TugasMenulis_New','Allow create new TugasMenulis',0),
(13091,'TugasMenulis_Edit','Allow editing of TugasMenulis',0),
(13092,'TugasMenulis_Delete','Allow deleting of TugasMenulis',0);
--
INSERT INTO SIAK.SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(12120,13073,10000,0),
(12121,13074,10000,0),
(12122,13075,10000,0),
(12123,13076,10000,0),
(12124,13077,10000,0),
(12125,13078,10000,0),
(12126,13079,10000,0),
(12127,13080,10000,0),
(12128,13081,10000,0),
(12129,13082,10000,0),
(12130,13083,10000,0),
(12131,13084,10000,0),
(12132,13085,10000,0),
(12133,13086,10000,0),
(12134,13087,10000,0),
(12135,13088,10000,0),
(12136,13089,10000,0),
(12137,13090,10000,0),
(12138,13091,10000,0),
(12139,13092,10000,0);
--
INSERT INTO SIAK.SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES 
(15019,1,'menuCat_Administrasi',0),
(15020,2,'menuItem_Admin_MasterUniversitas',0),
(15021,2,'menuItem_Admin_MasterMahasiswa',0),
(15022,2,'menuItem_Admin_MasterStatusMahasiswa',0),
(15023,2,'menuItem_Admin_MasterRuangan',0),
(15024,2,'menuItem_Admin_MasterFasilitas',0),
(15025,2,'menuItem_Admin_MasterFasilitasRuangan',0),
(15026,2,'menuItem_Admin_MasterKodePos',0),
(15027,2,'menuItem_Admin_MasterPegawai',0),
(15028,2,'menuItem_Admin_MasterJabatan',0),
(15029,2,'menuItem_Admin_MasterSekolah',0),
(15030,2,'menuItem_Admin_MasterPangkat',0),
(15031,2,'menuItem_Admin_MasterProdi',0),
(15032,2,'menuItem_Admin_MasterGrade',0),
(15033,2,'menuItem_Admin_MasterHari',0),
(15034,2,'menuItem_Admin_MasterJenjang',0),
(15035,2,'menuItem_Admin_MasterKegiatan',0),
(15036,2,'menuItem_Admin_MasterKalenderKegiatanAkademik',0),
(15037,2,'menuItem_Admin_UpdateKonsentrasiMahasiswa',0),
(15038,2,'menuItem_Admin_UpdateStatusMahasiswa',0),
(15039,1,'menuCat_ManajemenKurikulum',0),
(15040,2,'menuItem_Manajemen_MasterSesiKuliah',0),
(15041,2,'menuItem_Manajemen_MasterMatakuliah',0),
(15042,2,'menuItem_Manajemen_MasterKurikulum',0),
(15043,2,'menuItem_Manajemen_EntriKurikulumMahasiswa',0),
(15044,2,'menuItem_Manajemen_UpdateKurikulumProdiMahasiswa',0),
(15045,2,'menuItem_Manajemen_MasterPeminatan',0),
(15046,1,'menuCat_IsianRencanaStudi',0),
(15047,2,'menuItem_IRS_EntriJadwalKuliah',0),
(15048,2,'menuItem_IRS_EntriIRS',0),
(15049,2,'menuItem_IRS_EntriPaketKuliah',0),
(15050,2,'menuItem_IRS_TransaksiPembagianMataKuliahPaket',0),
(15051,2,'menuItem_IRS_CetakPengajuanCutiKuliah',0),
(15052,2,'menuItem_IRS_CetakJadwalKuliah',0),
(15053,2,'menuItem_IRS_CetakKartuStudiTetapMahasiswa',0),
(15054,2,'menuItem_IRS_EntriAktifKembali',0),
(15055,1,'menuCat_KegiatanBelajarMengajar',0),
(15056,2,'menuItem_KBM_EntryNilaiUjian',0),
(15057,2,'menuItem_KBM_VerifikasiNilaiUjian',0),
(15058,2,'menuItem_KBM_EntryJadwalUjianAkhir',0),
(15059,2,'menuItem_KBM_EntryBeritaAcaraPengajaran',0),
(15060,2,'menuItem_KBM_CetakBeritaAcaraPengajaran',0),
(15061,2,'menuItem_KBM_EntryAbsensiDosenMengajar',0),
(15062,2,'menuItem_KBM_CetakAbsensiDosenMengajar',0),
(15063,2,'menuItem_KBM_EntryAbsensiMahasiswa',0),
(15064,2,'menuItem_KBM_CetakAbsensiMahasiswaManual',0),
(15065,2,'menuItem_KBM_CetakAbsensiMahasiswaLaporanabsensi',0),
(15066,2,'menuItem_KBM_EntryRekapAbsensiMahasiswa',0),
(15067,2,'menuItem_KBM_CetakRekapAbsensiMahasiswa',0),
(15068,2,'menuItem_KBM_CetakTranskripNilai',0),
(15069,2,'menuItem_KBM_CetakLaporanDaftarMatakuliah',0),
(15070,2,'menuItem_KBM_CetakDaftarNilai',0),
(15071,2,'menuItem_KBM_CetakJadwalUjianAkhir',0),
(15072,1,'menuCat_TugasMenulis',0),
(15073,2,'menuItem_Menulis_EntryPendaftaranThesis',0),
(15074,2,'menuItem_Menulis_EntryPengumpulanProposalThesis',0),
(15075,2,'menuItem_Menulis_EntryJadwalSidangMakalah',0),
(15076,2,'menuItem_Menulis_EntryPendSidangProposalThesis',0),
(15077,2,'menuItem_Menulis_EntryJadwalSidangProposalThesis',0),
(15078,2,'menuItem_Menulis_RekapHasilSidangProposalThesis',0),
(15079,2,'menuItem_Menulis_EntryDosenPembimbingThesis',0),
(15080,2,'menuItem_Menulis_EntryPendaftaranSidangThesis',0),
(15081,2,'menuItem_Menulis_EntryJadwalSidangThesis',0),
(15082,2,'menuItem_Menulis_EntryHasilSidangThesis',0),
(15083,2,'menuItem_Menulis_EntryPendaftaranWisuda',0);

/******************** Security: SEC_GROUP-RIGHTS ********************/  
/* Headoffice Supervisor Group*/
INSERT INTO SIAK.SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(14627,13073,15019,0),
(14628,13073,15021,0),
(14629,13073,15022,0),
(14630,13073,15020,0),
(14631,13073,15023,0),
(14632,13073,15024,0),
(14633,13073,15025,0),
(14634,13073,15026,0),
(14635,13073,15027,0),
(14636,13073,15028,0),
(14637,13073,15029,0),
(14638,13073,15030,0),
(14639,13073,15031,0),
(14640,13073,15032,0),
(14641,13073,15033,0),
(14642,13073,15034,0),
(14643,13073,15035,0),
(14644,13073,15036,0),
(14645,13073,15037,0),
(14646,13073,15038,0),
(14647,13081,15046,0),
(14648,13081,15047,0),
(14649,13081,15048,0),
(14650,13081,15049,0),
(14651,13081,15050,0),
(14652,13081,15051,0),
(14653,13081,15052,0),
(14654,13081,15053,0),
(14655,13081,15054,0),
(14656,13085,15055,0),
(14657,13085,15056,0),
(14658,13085,15057,0),
(14659,13085,15058,0),
(14660,13085,15059,0),
(14661,13085,15060,0),
(14662,13085,15061,0),
(14663,13085,15062,0),
(14664,13085,15063,0),
(14665,13085,15064,0),
(14666,13085,15065,0),
(14667,13085,15066,0),
(14668,13085,15067,0),
(14669,13085,15068,0),
(14670,13085,15069,0),
(14671,13085,15070,0),
(14672,13085,15071,0),
(14673,13077,15039,0),
(14674,13077,15040,0),
(14675,13077,15041,0),
(14676,13077,15042,0),
(14677,13077,15043,0),
(14678,13077,15044,0),
(14679,13077,15045,0),
(14680,13089,15073,0),
(14681,13089,15074,0),
(14682,13089,15075,0),
(14683,13089,15076,0),
(14684,13089,15077,0),
(14685,13089,15078,0),
(14686,13089,15079,0),
(14687,13089,15080,0),
(14688,13089,15081,0),
(14689,13089,15082,0),
(14690,13089,15083,0),
(14691,13089,15072,0);
/* End Siak Menu */

/******************** Branche Daten ********************/
INSERT INTO SIAK.BRANCHE (BRA_ID, BRA_NR, BRA_BEZEICHNUNG, VERSION) VALUES
(1000, '100', 'Elektro',0),
(1001, '101', 'Maler',0),
(1002, '102', 'Holzverabeitung',0),
(1003, '103', 'Kaufmaennisch',0),
(1004, '104', 'Versicherung',0),
(1005, '105', 'Mess- und Regeltechnik',0),
(1006, '106', 'Industriemontagen',0),
(1007, '107', 'KFZ',0),
(1008, '108', 'Banken',0),
(1009, '109', 'Grosshandel',0),
(1010, '110', 'Einzelhandel',0),
(1011, '111', 'Werbung',0),
(1012, '112', 'Gastronomie',0),
(1014, '114', 'Pflegedienste',0),
(1015, '115', 'Transportwesen',0),
(1016, '116', 'Metallverarbeitung',0),
(1017, '117', 'Schlosserei',0),
(1018, '118', 'Sanitaer',0),
(1019, '119', 'Heizungsbau',0),
(1020, '120', 'Wasserwirtschaft',0),
(1021, '121', 'Schiffsbau',0),
(1022, '122', 'Laermschutz',0),
(1023, '123', 'Geruestbau',0),
(1024, '124', 'Fassadenbau',0),
(1025, '125', 'Farbherstellung',0),
(1026, '126', 'Kieswerk',0),
(1027, '127', 'Blechnerei',0),
(1028, '128', 'Geruestverleih',0),
(1029, '129', 'Pflasterarbeiten',0),
(1030, '130', 'Trockenbau',0),
(1031, '131', 'Trockenbau- und Sanierung',0),
(1032, '132', 'Huehnerfarm',0),
(1033, '000', '.',0),
(1034, '134', 'Transportwesen allgemein',0),
(1035, '135', 'Schwertransport',0),
(1036, '136', 'Gefahrgut Transport',0),
(1037, '137', 'Spedition',0);

/******************** Kunden Daten ********************/
INSERT INTO SIAK.KUNDE (KUN_ID,KUN_FIL_ID,KUN_BRA_ID, KUN_NR, KUN_MATCHCODE,KUN_NAME1,KUN_NAME2,KUN_ORT,KUN_MAHNSPERRE,VERSION) VALUES 
(  20,1,1000, '20', 'MUELLER','--> Mueller','Elektroinstallationen','Freiburg',1,0),
(  21,1,1000, '21', 'HUBER','--> Huber','Elektroinstallationen','Oberursel',1,0),
(  22,1,1000, '22', 'SIEMENS','Siemens AG','Elektroinstallationen','Braunschweig',0,0),
(  23,1,1000, '23', 'AEG','AEG','Elektroinstallationen','Stuttgart',0,0),
(  24,1,1019, '24', 'BUDERUS','Buderus Heizungsbau GmbH','Elektroinstallationen','Rastatt',1,0),
(  25,1,1000, '25', 'MEILER','Elektro Meiler','Inhaber W. Erler','Karlsruhe',1,0),
(  26,1,1000, '26', 'BADER','Bader GmbH','Elektroinstallationen','Berlin',0,0),
(  27,1,1000, '27', 'HESKENS','Heskens GmbH','Elektroinstallationen','Badenweiler',0,0),
(  28,1,1000, '28', 'MAIER','Maier GmbH','Elektroinstallationen','Friedberg',0,0),
(  29,1,1000, '29', 'SCHULZE','Schulze GmbH','Elektroinstallationen','Freiburg',1,0),
(  30,1,1004, '30', 'SCHMIERFINK','Schmierfink AG','Versicherungen','Freiburg',1,0),
(  31,1,1005, '31', 'SCHULZE','Schulze Ltd.','Anlagenbau','Buxtehude',1,0),
(  32,1,1005, '32', 'SCHREINER','Schreiner','SPS-Steuerungsbau','Hamburg',1,0),
(  33,1,1004, '33', 'GUTE RUHE','Gute Ruhe AG','Versicherungen','Berlin',1,0),
(  34,1,1003, '34', 'FREIBERGER','Freiberger GmbH','In- und Export','Aachen',1,0),
(  35,1,1002, '35', 'BERGMANN','Saegewerk Bergmann','Holzverarbeitung','Neustadt',1,0),
(2000,1,1002, '2000', 'SEILER','Saegewerk Seiler','Holzverarbeitung','Freiburg',1,0),
(2001,1,1002, '2001', 'BAUER','Hermann Bauer','Sgerwerk','Titisee-Neustadt',1,0),
(2002,1,1000, '2002', 'BEINHARD','Gebrueder Beinhard GbR','Elektroinstallationen','Muenchen',1,0),
(2003,1,1000, '2003', 'ADLER','Reiner Adler','Elektro Montagen','Dreieich',1,0),
(2004,1,1000, '2004', 'FINK','Hartmut Fink GmbH','Elektro- und Industriemontagen','Stuttgart',1,0),
(2005,1,1000, '2005', 'GERHARD','Huber u. Gerhard GbR','Elektroinstallationen','Stuttgart',1,0),
(2006,1,1004, '2006', 'BELIANZ','BELIANZ','Versicherungs AG','Berlin',1,0),
(2007,1,1004, '2007', 'KINTERTHUR','Kinterthur','Versicherungs AG','Rastatt',1,0),
(2008,1,1004, '2008', 'WOLFRAM','Peter Wolfram','Freier Versicherungsvertreter','Norderstedt',1,0),
(2009,1,1000, '2009', 'HESSING','Mario Hessing GmbH','Elektroinstallationen','Rheinweiler',0,0),
(2010,1,1000, '2010', 'FREIBERG','Werner Freiberg GmbH','Elektroinstallationen','Rheinstetten',0,0);

/******************** Auftrag Daten ********************/
INSERT INTO SIAK.AUFTRAG (AUF_ID,AUF_KUN_ID, AUF_NR, AUF_BEZEICHNUNG, VERSION) VALUES
(40, 20, 'AUF-40', 'EGRH Modul1',0),
(41, 20, 'AUF-41', 'OMEGA Modul2',0),
(42, 20, 'AUF-42', 'Keller',0),
(43, 20, 'AUF-43', 'Schlossallee 23',0),
(44, 21, 'AUF-44', 'Renovierung Keller',0),
(45, 21, 'AUF-45', 'Renovierung Hochhaus Ilsestrasse 5',0),
(46, 21, 'AUF-46', 'Verteilerschrank Umbau; Fa. Kloeckner EHG',0);

/******************** Artikel Daten ********************/
INSERT INTO SIAK.ARTIKEL (ART_ID,ART_KURZBEZEICHNUNG,ART_LANGBEZEICHNUNG,ART_NR,ART_PREIS,VERSION) VALUES 
(3000,'Siak (Sistem Akademik)','Universitas Pertahanan','KS3000',4.23,0);



/******************** Auftrag Positionen Daten ********************/
/* Auftrag: 40 */
INSERT INTO SIAK.AUFTRAGPOSITION (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES 
(60,40, 3000, 1, 240, 1.20, 288.00, 0),
(61,40, 3001, 2, 1200, 0.45, 540.00, 0),
(62,40, 3002, 3, 40, 0.20, 8.00, 0),
(63,40, 3003, 4, 15, 4.20, 63.00, 0),
(64,40, 3004, 5, 20, 4.30, 86.00, 0),
(65,40, 3005, 6, 15, 40.00, 600.00, 0);
/* Auftrag: 41 */
INSERT INTO SIAK.AUFTRAGPOSITION (AUP_ID, AUP_AUF_ID, ART_ID, AUP_POSITION, AUP_MENGE, AUP_EINZELWERT, AUP_GESAMTWERT, VERSION) VALUES
(66,41, 3005, 1, 18, 40.00, 720.00, 0),
(67,41, 3006, 2, 800 , 0.45, 360.00, 0),
(68,41, 3002, 3, 40, 0.20, 8.00, 0),
(69,44, 3007, 1, 3, 2.10, 6.30, 0),
(70,44, 3001, 2, 1200, 0.45, 540.00, 0),
(71,44, 3002, 3, 40, 0.20, 8.00, 0),
(72,44, 3003, 4, 15, 4.20, 63.00, 0),
(73,44, 3004, 5, 20, 4.30, 86.00, 0),
(74,44, 3005, 6, 15, 40.00, 600.00, 0),
(75,45, 3008, 0, 240, 3.20, 768.00, 0),
(76,45, 3009, 1, 444, 3.80, 1687.20, 0),
(77,45, 3010, 2, 240, 4.10, 984.00, 0),
(78,46, 3011, 0, 40, 2.20, 88.00, 0),
(79,46, 3012, 1, 80, 3.60, 288.00, 0),
(80,46, 3002, 2, 90, 1.50, 135.00, 0),
(81,46, 3010, 3, 100, 4.10, 410.00, 0),
(82,46, 3011, 4, 400, 2.20, 880.00, 0),
(83,46, 3006, 5, 60.00, 0.45, 27.00, 0);


 /* fill the countrycodes */
INSERT INTO SIAK.SYS_COUNTRYCODE(CCD_ID, CCD_NAME, CCD_CODE2, VERSION) VALUES 
(-1,'UNROUTABLE ADDRESS','xx', 0),
(1, 'AFGHANISTAN','AF', 0),
(2, 'ALBANIA','AL', 0),
(3, 'ALGERIA','DZ', 0),
(4, 'AMERICAN SAMOA','AS', 0),
(5, 'ANDORRA','AD', 0),
(6, 'ANGOLA','AO', 0),
(7, 'ANGUILLA','AI', 0),
(8, 'ANTARCTICA','AQ', 0),
(9, 'ANTIGUA AND BARBUDA','AG', 0),
(10,'ARGENTINA','AR', 0),
(11,'ARMENIA','AM', 0),
(12,'ARUBA','AW', 0),
(13,'AUSTRALIA','AU', 0),
(14,'AUSTRIA','AT', 0),
(15,'AZERBAIJAN','AZ', 0),
(16,'BAHAMAS','BS', 0),
(17,'BAHRAIN','BH', 0),
(18,'BANGLADESH','BD', 0),
(19,'BARBADOS','BB', 0),
(20,'BELARUS','BY', 0),
(21,'BELGIUM','BE', 0),
(22,'BELIZE','BZ', 0),
(23,'BENIN','BJ', 0),
(24,'BERMUDA','BM', 0),
(25,'BHUTAN','BT', 0),
(26,'BOLIVIA','BO', 0),
(27,'BOSNIA AND HERZEGOVINA','BA', 0),
(28,'BOTSWANA','BW', 0),
(29,'BOUVET ISLAND','BV', 0),
(30,'BRAZIL','BR', 0),
(31,'BRITISH INDIAN OCEAN TERRITORY','IO', 0),
(32,'BRUNEI DARUSSALAM','BN', 0),
(33,'BULGARIA','BG', 0),
(34,'BURKINA FASO','BF', 0),
(35,'BURUNDI','BI', 0),
(36,'CAMBODIA','KH', 0),
(37,'CAMEROON','CM', 0),
(38,'CANADA','CA', 0),
(39,'CAPE VERDE','CV', 0),
(40,'CAYMAN ISLANDS','KY', 0),
(41,'CENTRAL AFRICAN REPUBLIC','CF', 0),
(42,'CHAD','TD', 0),
(43,'CHILE','CL', 0),
(44,'CHINA','CN', 0),
(45,'CHRISTMAS ISLAND','CX', 0),
(46,'COCOS (KEELING) ISLANDS','CC', 0),
(47,'COLOMBIA','CO', 0),
(48,'COMOROS','KM', 0),
(49,'CONGO','CG', 0),
(50,'CONGO, THE DEMOCRATIC REPUBLIC OF THE','CD', 0),
(51,'COOK ISLANDS','CK', 0),
(52,'COSTA RICA','CR', 0),
(53,'COTE D IVOIRE','CI', 0),
(54,'CROATIA','HR', 0),
(55,'CUBA','CU', 0),
(56,'CYPRUS','CY', 0),
(57,'CZECH REPUBLIC','CZ', 0),
(58,'DENMARK','DK', 0),
(59,'DJIBOUTI','DJ', 0),
(60,'DOMINICA','DM', 0),
(61,'DOMINICAN REPUBLIC','DO', 0),
(62,'ECUADOR','EC', 0),
(63,'EGYPT','EG', 0),
(64,'EL SALVADOR','SV', 0),
(65,'EQUATORIAL GUINEA','GQ', 0),
(66,'ERITREA','ER', 0),
(67,'ESTONIA','EE', 0),
(68,'ETHIOPIA','ET', 0),
(69,'FALKLAND ISLANDS','FK', 0),
(70,'FAROE ISLANDS','FO', 0),
(71,'FIJI','FJ', 0),
(72,'FINLAND','FI', 0),
(73,'FRANCE','FR', 0),
(74,'FRENCH GUIANA','GF', 0),
(75,'FRENCH POLYNESIA','PF', 0),
(76,'FRENCH SOUTHERN TERRITORIES','TF', 0),
(77,'GABON','GA', 0),
(78,'GAMBIA','GM', 0),
(79,'GEORGIA','GE', 0),
(80,'GERMANY','DE', 0),
(81,'GHANA','GH', 0),
(82,'GIBRALTAR','GI', 0),
(83,'GREECE','GR', 0),
(84,'GREENLAND','GL', 0),
(85,'GRENADA','GD', 0),
(86,'GUADELOUPE','GP', 0),
(87,'GUAM','GU', 0),
(88,'GUATEMALA','GT', 0),
(89,'GUINEA','GN', 0),
(90,'GUINEA-BISSAU','GW', 0),
(91,'GUYANA','GY', 0),
(92,'HAITI','HT', 0),
(93,'HEARD ISLAND AND MCDONALD ISLANDS','HM', 0),
(94,'HOLY SEE (VATICAN CITY STATE)','VA', 0),
(95,'HONDURAS','HN', 0),
(96,'HONG KONG','HK', 0),
(97,'HUNGARY','HU', 0),
(98,'ICELAND','IS', 0),
(99,'INDIA','IN', 0),
(100,'INDONESIA','ID', 0),
(101,'IRAN, ISLAMIC REPUBLIC OF','IR', 0),
(102,'IRAQ','IQ', 0),
(103,'IRELAND','IE', 0),
(104,'ISRAEL','IL', 0),
(105,'ITALY','IT', 0),
(106,'JAMAICA','JM', 0),
(107,'JAPAN','JP', 0),
(108,'JORDAN','JO', 0),
(109,'KAZAKHSTAN','KZ', 0),
(110,'KENYA','KE', 0),
(111,'KIRIBATI','KI', 0),
(112,'KOREA, DEMOCRATIC REPUBLIC OF','KP', 0),
(113,'KOREA, REPUBLIC OF','KR', 0),
(114,'KUWAIT','KW', 0),
(115,'KYRGYZSTAN','KG', 0),
(116,'LAO DEMOCRATIC REPUBLIC','LA', 0),
(117,'LATVIA','LV', 0),
(118,'LEBANON','LB', 0),
(119,'LESOTHO','LS', 0),
(120,'LIBERIA','LR', 0),
(121,'LIBYAN ARAB JAMAHIRIYA','LY', 0),
(122,'LIECHTENSTEIN','LI', 0),
(123,'LITHUANIA','LT', 0),
(124,'LUXEMBOURG','LU', 0),
(125,'MACAO','MO', 0),
(126,'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF','MK', 0),
(127,'MADAGASCAR','MG', 0),
(128,'MALAWI','MW', 0),
(129,'MALAYSIA','MY', 0),
(130,'MALDIVES','MV', 0),
(131,'MALI','ML', 0),
(132,'MALTA','MT', 0),
(133,'MARSHALL ISLANDS','MH', 0),
(134,'MARTINIQUE','MQ', 0),
(135,'MAURITANIA','MR', 0),
(136,'MAURITIUS','MU', 0),
(137,'MAYOTTE','YT', 0),
(138,'MEXICO','MX', 0),
(139,'MICRONESIA, FEDERATED STATES OF','FM', 0),
(140,'MOLDOVA, REPUBLIC OF','MD', 0),
(141,'MONACO','MC', 0),
(142,'MONGOLIA','MN', 0),
(143,'MONTSERRAT','MS', 0),
(144,'MOROCCO','MA', 0),
(145,'MOZAMBIQUE','MZ', 0),
(146,'MYANMAR','MM', 0),
(147,'NAMIBIA','NA', 0),
(148,'NAURU','NR', 0),
(149,'NEPAL','NP', 0),
(150,'NETHERLANDS','NL', 0),
(151,'NETHERLANDS ANTILLES','AN', 0),
(152,'NEW CALEDONIA','NC', 0),
(153,'NEW ZEALAND','NZ', 0),
(154,'NICARAGUA','NI', 0),
(155,'NIGER','NE', 0),
(156,'NIGERIA','NG', 0),
(157,'NIUE','NU', 0),
(158,'NORFOLK ISLAND','NF', 0),
(159,'NORTHERN MARIANA ISLANDS','MP', 0),
(160,'NORWAY','NO', 0),
(161,'OMAN','OM', 0),
(162,'PAKISTAN','PK', 0),
(163,'PALAU','PW', 0),
(164,'PALESTINIAN TERRITORY, OCCUPIED','PS', 0),
(165,'PANAMA','PA', 0),
(166,'PAPUA NEW GUINEA','PG', 0),
(167,'PARAGUAY','PY', 0),
(168,'PERU','PE', 0),
(169,'PHILIPPINES','PH', 0),
(170,'PITCAIRN','PN', 0),
(171,'POLAND','PL', 0),
(172,'PORTUGAL','PT', 0),
(173,'PUERTO RICO','PR', 0),
(174,'QATAR','QA', 0),
(175,'REUNION','RE', 0),
(176,'ROMANIA','RO', 0),
(177,'RUSSIAN FEDERATION','RU', 0),
(178,'RWANDA','RW', 0),
(179,'SAINT HELENA','SH', 0),
(180,'SAINT KITTS AND NEVIS','KN', 0),
(181,'SAINT LUCIA','LC', 0),
(182,'SAINT PIERRE AND MIQUELON','PM', 0),
(183,'SAINT VINCENT AND THE GRENADINES','VC', 0),
(184,'SAMOA','WS', 0),
(185,'SAN MARINO','SM', 0),
(186,'SAO TOME AND PRINCIPE','ST', 0),
(187,'SAUDI ARABIA','SA', 0),
(188,'SENEGAL','SN', 0),
(189,'SERBIA','RS', 0),
(190,'SEYCHELLES','SC', 0),
(191,'SIERRA LEONE','SL', 0),
(192,'SINGAPORE','SG', 0),
(193,'SLOVAKIA','SK', 0),
(194,'SLOVENIA','SI', 0),
(195,'SOLOMON ISLANDS','SB', 0),
(196,'SOMALIA','SO', 0),
(197,'SOUTH AFRICA','ZA', 0),
(198,'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS','GS', 0),
(199,'SPAIN','ES', 0),
(200,'SRI LANKA','LK', 0),
(201,'SUDAN','SD', 0),
(202,'SURINAME','SR', 0),
(203,'SVALBARD AND JAN MAYEN','SJ', 0),
(204,'SWAZILAND','SZ', 0),
(205,'SWEDEN','SE', 0),
(206,'SWITZERLAND','CH', 0),
(207,'SYRIAN ARAB REPUBLIC','SY', 0),
(208,'TAIWAN','TW', 0),
(209,'TAJIKISTAN','TJ', 0),
(210,'TANZANIA, UNITED REPUBLIC OF','TZ', 0),
(211,'THAILAND','TH', 0),
(212,'TIMOR-LESTE','TL', 0),
(213,'TOGO','TG', 0),
(214,'TOKELAU','TK', 0),
(215,'TONGA','TO', 0),
(216,'TRINIDAD AND TOBAGO','TT', 0),
(217,'TUNISIA','TN', 0),
(218,'TURKEY','TR', 0),
(219,'TURKMENISTAN','TM', 0),
(220,'TURKS AND CAICOS ISLANDS','TC', 0),
(221,'TUVALU','TV', 0),
(222,'UGANDA','UG', 0),
(223,'UKRAINE','UA', 0),
(224,'UNITED ARAB EMIRATES','AE', 0),
(225,'UNITED KINGDOM','GB', 0),
(226,'UNITED STATES','US', 0),
(227,'UNITED STATES MINOR OUTLYING ISLANDS','UM', 0),
(228,'URUGUAY','UY', 0),
(229,'UZBEKISTAN','UZ', 0),
(230,'VANUATU','VU', 0),
(231,'VENEZUELA','VE', 0),
(232,'VIET NAM','VN', 0),
(233,'VIRGIN ISLANDS, BRITISH','VG', 0),
(234,'VIRGIN ISLANDS, U.S.','VI', 0),
(235,'WALLIS AND FUTUNA','WF', 0),
(236,'WESTERN SAHARA','EH', 0),
(237,'YEMEN','YE', 0),
(238,'ZAMBIA','ZM', 0),
(239,'ZIMBABWE','ZW', 0),
(240,'UNITED KINGDOM','UK', 0),
(241,'EUROPEAN UNION','EU', 0),
(242,'YUGOSLAVIA','YU', 0),
(244,'ARIPO','AP', 0),
(245,'ASCENSION ISLAND','AC', 0),
(246,'GUERNSEY','GG', 0),
(247,'ISLE OF MAN','IM', 0),
(248,'JERSEY','JE', 0),
(249,'EAST TIMOR','TP', 0),
(250,'MONTENEGRO','ME', 0);


/* fill sample logins */
INSERT INTO SIAK.SEC_LOGINLOG(LGL_ID, I2C_ID, LGL_LOGINNAME,LGL_LOGTIME, LGL_IP, LGL_STATUS_ID,LGL_SESSIONID, VERSION) VALUES 
( 1, NULL, 'admin',      '2010-08-12 13:52:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 2, NULL, 'user1',      '2010-08-12 10:12:33', '203.237.141.216', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 3, NULL, 'admin',      '2010-12-08 11:12:33', '202.96.188.101', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 4, NULL, 'aaaa',       '2010-12-08 12:22:33', '84.234.27.179', 0, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 5, NULL, 'admin',      '2010-12-08 12:32:33', '84.139.11.102', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 6, NULL, 'user2',      '2010-01-08 13:52:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 7, NULL, 'admin',      '2010-01-08 14:45:33', '212.227.148.189', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 8, NULL, 'admin',      '2010-01-08 15:33:33', '84.185.153.21', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
( 9, NULL, 'admin',      '2010-01-08 17:22:33', '212.156.5.254', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(10, NULL, 'user1',      '2010-01-08 17:22:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(11, NULL, 'admin',      '2010-01-08 17:22:33', '121.242.65.131', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(12, NULL, 'admin',      '2010-01-08 17:22:33', '202.96.188.101', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(13, NULL, 'headoffice', '2010-01-08 17:22:33', '118.68.97.90', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(14, NULL, 'test',       '2010-01-08 17:22:33', '125.160.32.182', 0, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(15, NULL, 'headoffice', '2010-01-08 17:22:33', '70.171.254.160', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(16, NULL, 'headoffice', '2010-01-08 17:22:33', '89.218.26.20', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(17, NULL, 'headoffice', '2010-01-08 17:22:33', '118.68.97.45', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0),
(18, NULL, 'admin',      '2010-01-08 17:22:33', '87.118.90.17', 1, 'hjfjgfdfggzgzufuzfuzdfgfgfdvfv', 0);


/* fill application history for changes */
INSERT INTO SIAK.APP_NEWS (ANW_ID,ANW_DATE,ANW_TEXT,VERSION) VALUES
(    1, '2012-03-01', 'Sistem Akademik Unhan Start Development link : http://www.idu.ac.id/',  0),;

/* create the sequences */
CREATE SEQUENCE SIAK.FILIALE_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.KUNDE_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.ARTIKEL_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.AUFTRAG_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.AUFTRAGPOSITION_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.BRANCHE_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_USER_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_USERROLE_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_ROLE_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_ROLEGROUP_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_GROUP_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_GROUPRIGHT_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_RIGHT_SEQ  INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SYS_COUNTRYCODE_SEQ INCREMENT BY 1 START WITH 300;
CREATE SEQUENCE SIAK.SYS_IP4COUNTRY_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.YOUTUBE_LINK_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.APP_NEWS_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.HIBERNATE_ENTITY_STATISTICS_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.HIBERNATE_STATISTICS_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.IPC_IP2COUNTRY_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.SEC_LOGINLOG_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.LOG_IP2COUNTRY_SEQ INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.GUESTBOOK_SEQ  INCREMENT BY 1 START WITH 100000;
CREATE SEQUENCE SIAK.CALENDAR_EVENT_SEQ INCREMENT BY 1 START WITH 100000;

commit;
