SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS; 
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `avatar` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` longblob,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK424996avyt89fp16dknjxyda` (`user_id`),
  CONSTRAINT `FK424996avyt89fp16dknjxyda` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `blocked_timeslot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day` varchar(255) DEFAULT NULL,
  `time_from` varchar(255) NOT NULL,
  `time_to` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK335ffqhaud9swwetl9ts0knuh` (`room_id`),
  CONSTRAINT `FK335ffqhaud9swwetl9ts0knuh` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `from_time` datetime(6) NOT NULL,
  `public_id` varchar(255) NOT NULL,
  `to_time` datetime(6) NOT NULL,
  `booked_by` bigint DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `booking_date_index` (`date`),
  KEY `booking_room_id_index` (`room_id`),
  KEY `booking_to_time_index` (`to_time`),
  KEY `booking_from_time_index` (`from_time`),
  KEY `FKt7y389kcrlnt8d2tfryyih6sv` (`booked_by`),
  CONSTRAINT `FKq83pan5xy2a6rn0qsl9bckqai` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FKt7y389kcrlnt8d2tfryyih6sv` FOREIGN KEY (`booked_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `org` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(200) NOT NULL,
  `public_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bwi2afygrefh9lie08dhcqt20` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `org_admin` (
  `org_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FKpky9u8s971qv1o45dh76c4fxw` (`user_id`),
  KEY `FK7t546jhm6emavp5sjrmkyokw8` (`org_id`),
  CONSTRAINT `FK7t546jhm6emavp5sjrmkyokw8` FOREIGN KEY (`org_id`) REFERENCES `org` (`id`),
  CONSTRAINT `FKpky9u8s971qv1o45dh76c4fxw` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `capacity` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `facilities` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `public_id` varchar(255) NOT NULL,
  `org_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4nqbt00ie742x1j40qmiiwfkd` (`name`,`org_id`),
  KEY `FKp8kakg12djqcxxyy2p7u49gc6` (`org_id`),
  CONSTRAINT `FKp8kakg12djqcxxyy2p7u49gc6` FOREIGN KEY (`org_id`) REFERENCES `org` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `room_admin` (
  `room_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  UNIQUE KEY `room_admin_unique_index` (`room_id`,`user_id`),
  KEY `FKca8w7wvw1ybke5mby48dkhsj6` (`user_id`),
  CONSTRAINT `FK7e6qlj9aok7y8odht1cr9r951` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FKca8w7wvw1ybke5mby48dkhsj6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `room_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image` longblob,
  `public_id` varchar(255) NOT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcme41omxvwoj00bhqk7fwt70v` (`room_id`),
  CONSTRAINT `FKcme41omxvwoj00bhqk7fwt70v` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `room_user` (
  `room_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  UNIQUE KEY `room_user_unique_index` (`room_id`,`user_id`),
  KEY `FK93pqtc1kyjxwoj26quvm5hugi` (`user_id`),
  CONSTRAINT `FK93pqtc1kyjxwoj26quvm5hugi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKtakjqllocgakgw0os4hygxfk1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `return_url` varchar(255) DEFAULT NULL,
  `token` varchar(255) NOT NULL,
  `type` varchar(30) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnurfdg0q3ns1vrwrsemivhiav` (`user_id`,`type`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `public_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_role` (
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;