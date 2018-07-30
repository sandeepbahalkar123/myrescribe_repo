BEGIN TRANSACTION;

ALTER TABLE `investigation_table` ADD `inv_type` TEXT;


COMMIT;
