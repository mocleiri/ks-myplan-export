ALTER TABLE KSPL_LRNG_PLAN_ITEM ADD CATEGORY VARCHAR(255);

Alter table KSPL_LRNG_PLAN_ATTR add(TEMP_ATTR_VALUE CLOB);
UPDATE KSPL_LRNG_PLAN_ATTR SET TEMP_ATTR_VALUE = ATTR_VALUE;
COMMIT;
ALTER TABLE KSPL_LRNG_PLAN_ATTR DROP COLUMN ATTR_VALUE;
ALTER TABLE KSPL_LRNG_PLAN_ATTR RENAME COLUMN TEMP_ATTR_VALUE TO ATTR_VALUE;

UPDATE KSPL_LRNG_PLAN set STATE = 'kuali.academicplan.plan.state.active' where TYPE_ID = 'kuali.academicplan.type.plan' and STATE is NULL;