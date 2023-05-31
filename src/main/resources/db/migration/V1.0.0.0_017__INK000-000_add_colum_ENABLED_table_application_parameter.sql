ALTER TABLE application_parameter  ADD COLUMN enabled bool DEFAULT 1;

UPDATE application_parameter SET `enabled` = 0 WHERE `code` not in ('ARRIVED_STATUS_VALIDATED','ORDER_DISTANCE_TO_MARK_ARRIVE');