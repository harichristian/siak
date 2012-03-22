INSERT INTO SEC_GROUP (GRP_ID, GRP_SHORTDESCRIPTION, GRP_LONGDESCRIPTION, VERSION) VALUES
(20100,'Peminatan_View','Allow to  view Peminatan',0),
(20101,'Peminatan_New','Allow create new Peminatan',0),
(20102,'Peminatan_Edit','Allow editing of Peminatan',0),
(20103,'Peminatan_Delete','Allow deleting of Peminatan',0);

INSERT INTO SEC_ROLEGROUP (RLG_ID, GRP_ID, ROL_ID, VERSION) VALUES 
(20244,20100,10000,0),
(20245,20101,10000,0),
(20246,20102,10000,0),
(20247,20103,10000,0);

INSERT INTO SEC_RIGHT (RIG_ID, RIG_TYPE, RIG_NAME, VERSION) VALUES 
(21185,2,'menu_Item_Peminatan',0);

INSERT INTO SEC_GROUPRIGHT (GRI_ID, GRP_ID, RIG_ID, VERSION) VALUES 
(20794,13089,21185,0);

commit;
/* End Siak Menu */