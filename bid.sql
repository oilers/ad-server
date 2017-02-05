CREATE TABLE `bid` (
  `bid_id`         INT          NOT NULL AUTO_INCREMENT,
  `provider_id`    INT          NOT NULL,
  `transaction_id` VARCHAR(128) NOT NULL,
  `bid_price`      DOUBLE       NOT NULL,
  `ad_html`        VARCHAR(512) NOT NULL,
  PRIMARY KEY (`bid_id`)
);
ALTER TABLE `bid`
  ADD CONSTRAINT `bid_provider_fk0` FOREIGN KEY (`provider_id`)
REFERENCES `provider` (`provider_id`);

ALTER TABLE `bid`
  ADD CONSTRAINT `bid_auction_fk0` FOREIGN KEY (`transaction_id`)
REFERENCES `auction` (`transaction_id`);
