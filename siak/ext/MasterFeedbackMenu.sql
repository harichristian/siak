INSERT INTO SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(20000,'Feedback_View','Allow to  view Feedback',0),
(20001,'Feedback_New','Allow create new Feedback',0),
(20002,'Feedback_Edit','Allow editing of Feedback',0),
(20003,'Feedback_Delete','Allow deleting of Feedback',0);

INSERT INTO SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(20144,20000,10000,0),
(20145,20001,10000,0),
(20146,20002,10000,0),
(20147,20003,10000,0);

INSERT INTO SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES 
(21085,2,'menu_Item_Feedback',0);

INSERT INTO SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(20694,13089,21085,0);

commit;
/* End Siak Menu */