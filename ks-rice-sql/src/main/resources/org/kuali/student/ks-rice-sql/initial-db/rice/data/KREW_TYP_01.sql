-- likely old demo data from rice

TRUNCATE TABLE KREW_TYP_T DROP STORAGE
/
INSERT INTO KREW_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','Sample Type','KR-SAP','sampleAppPeopleFlowTypeService',CONCAT('KS-', KS_RICE_ID_S.NEXTVAL),1)
/
