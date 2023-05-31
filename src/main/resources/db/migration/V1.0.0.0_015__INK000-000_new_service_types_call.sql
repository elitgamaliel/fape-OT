INSERT INTO service_type (code,description) VALUES 
('INKATRACKER_CALL_AM_PM','Inkatracker con delivery de call center')
,('INKATRACKER_CALL_EXP','Inkatracker con delivery de call center')
,('INKATRACKER_CALL_PROG','Inkatracker con delivery de call center')
,('INKATRACKER_CALL_RAD','Inkatracker con delivery de call center')
;

INSERT INTO motorized_service (motorized_type_code, service_type_code) VALUES
('DELIVERY_CENTER', 'INKATRACKER_CALL_AM_PM')
,('DELIVERY_CENTER', 'INKATRACKER_CALL_EXP')
,('DELIVERY_CENTER', 'INKATRACKER_CALL_PROG')
,('DELIVERY_CENTER', 'INKATRACKER_CALL_RAD')
;