INSERT INTO service_type (code,description) VALUES 
('INKATRACKER_AM_PM','Inkatracker con delivery AM y PM')
,('INKATRACKER_EXP','Inkatracker con delivery express')
,('INKATRACKER_LITE_AM_PM','Inkatrackerlite con delivery AM y PM')
,('INKATRACKER_LITE_EXP','Inkatrackerlite con delivery express')
,('INKATRACKER_LITE_PROG','Inkatrackerlite con delivery programado')
,('INKATRACKER_PROG','Inkatracker con delivery programado')
,('TEMPORARY_AM_PM','Inkatracker TEMPORARY con delivery AM y PM')
,('TEMPORARY_EXP','Inkatracker TEMPORARY con delivery express')
,('TEMPORARY_PROG','Inkatracker  con delivery programado')
;

INSERT INTO motorized_service (motorized_type_code, service_type_code) VALUES
('DELIVERY_CENTER', 'INKATRACKER_AM_PM')
,('DELIVERY_CENTER', 'INKATRACKER_EXP')
,('DELIVERY_CENTER', 'INKATRACKER_PROG')
,('DRUGSTORE', 'INKATRACKER_LITE_AM_PM')
,('DRUGSTORE', 'INKATRACKER_LITE_EXP')
,('DRUGSTORE', 'INKATRACKER_LITE_PROG')
;