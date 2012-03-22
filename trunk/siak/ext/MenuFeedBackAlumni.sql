-- Menu Feedback Alumni
INSERT INTO SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(13102,'FeedbackAlumni_View','Allow to  view FeedbackAlumni',0),
(13103,'FeedbackAlumni_New','Allow create new FeedbackAlumni',0),
(13104,'FeedbackAlumni_Edit','Allow editing of FeedbackAlumni',0),
(13105,'FeedbackAlumni_Delete','Allow deleting of FeedbackAlumni',0);

INSERT INTO SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(12148,13102,10000,0),
(12149,13103,10000,0),
(12150,13104,10000,0),
(12151,13105,10000,0);

INSERT INTO SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES 
(15086,2,'menu_Item_FeedbackAlumni',0);

INSERT INTO SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(14695,13089,15086,0);

commit;