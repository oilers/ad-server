CREATE TABLE `auction` (
  `transaction_id`      VARCHAR(128) NOT NULL UNIQUE,
  `user_id`             INT          NOT NULL,
  `winning_price`       DOUBLE       NOT NULL,
  `winning_provider_id` INT          NOT NULL,
  `performed`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `click_result`        VARCHAR(50)  NOT NULL,
  PRIMARY KEY (`transaction_id`)
);

ALTER TABLE `auction`
  ADD CONSTRAINT `auction_user_fk0` FOREIGN KEY (`user_id`)
REFERENCES `user` (`user_id`);

ALTER TABLE `auction`
  ADD CONSTRAINT `auction_provider_fk0` FOREIGN KEY (`winning_provider_id`)
REFERENCES `provider` (`provider_id`);
