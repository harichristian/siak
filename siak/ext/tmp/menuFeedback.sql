INSERT INTO SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(13098,'FeedbackInstansi_View','Allow to  view FeedbackInstansi',0),
(13099,'FeedbackInstansi_New','Allow create new FeedbackInstansi',0),
(13100,'FeedbackInstansi_Edit','Allow editing of FeedbackInstansi',0),
(13101,'FeedbackInstansi_Delete','Allow deleting of FeedbackInstansi',0);

INSERT INTO SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(12144,13098,10000,0),
(12145,13099,10000,0),
(12146,13100,10000,0),
(12147,13101,10000,0);

INSERT INTO SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES 
(15085,2,'menu_Item_FeedbackInstansi',0);

INSERT INTO SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(14694,13089,15085,0);

commit;
/* End Siak Menu */