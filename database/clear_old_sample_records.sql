-- OPTIONAL ONE-TIME CLEANUP for a database that already contains the old sample rows.
-- Review before running: this deletes all current records from these project tables.
USE SimPortal;
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM Customer;
DELETE FROM CustomerIdentity;
DELETE FROM CustomerAddress;
DELETE FROM SimOffers;
DELETE FROM SimDetails;
ALTER TABLE SimDetails AUTO_INCREMENT = 1;
ALTER TABLE CustomerAddress AUTO_INCREMENT = 1;
ALTER TABLE SimOffers AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;
