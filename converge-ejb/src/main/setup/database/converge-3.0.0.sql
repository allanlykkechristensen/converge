SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `converge` ;
CREATE SCHEMA IF NOT EXISTS `converge` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `converge` ;

-- -----------------------------------------------------
-- Table `converge`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`permission` ;

CREATE TABLE IF NOT EXISTS `converge`.`permission` (
  `id` BIGINT NOT NULL,
  `permission` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`user_role` ;

CREATE TABLE IF NOT EXISTS `converge`.`user_role` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NULL,
  `description` BLOB NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`user_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`user_account` ;

CREATE TABLE IF NOT EXISTS `converge`.`user_account` (
  `id` BIGINT NOT NULL,
  `username` VARCHAR(255) NULL,
  `time_zone` VARCHAR(255) NULL,
  `preferred_language` VARCHAR(255) NULL,
  `display_name` VARCHAR(255) NULL,
  `given_name` VARCHAR(255) NULL,
  `surname` VARCHAR(255) NULL,
  `job_title` VARCHAR(255) NULL,
  `organization` BLOB NULL,
  `email` BLOB NULL,
  `phone` VARCHAR(255) NULL,
  `mobile` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`config` ;

CREATE TABLE IF NOT EXISTS `converge`.`config` (
  `id` BIGINT NOT NULL,
  `config_key` VARCHAR(255) NULL,
  `config_value` BLOB NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`user_role_has_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`user_role_has_permission` ;

CREATE TABLE IF NOT EXISTS `converge`.`user_role_has_permission` (
  `user_role_id` BIGINT NOT NULL,
  `permission_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_role_id`, `permission_id`),
  INDEX `fk_user_role_has_permission_permission1_idx` (`permission_id` ASC),
  INDEX `fk_user_role_has_permission_user_role_idx` (`user_role_id` ASC),
  CONSTRAINT `fk_user_role_has_permission_user_role`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `converge`.`user_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_has_permission_permission1`
    FOREIGN KEY (`permission_id`)
    REFERENCES `converge`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`user_account_has_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`user_account_has_permission` ;

CREATE TABLE IF NOT EXISTS `converge`.`user_account_has_permission` (
  `user_account_id` BIGINT NOT NULL,
  `permission_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_account_id`, `permission_id`),
  INDEX `fk_user_account_has_permission_permission1_idx` (`permission_id` ASC),
  INDEX `fk_user_account_has_permission_user_account1_idx` (`user_account_id` ASC),
  CONSTRAINT `fk_user_account_has_permission_user_account1`
    FOREIGN KEY (`user_account_id`)
    REFERENCES `converge`.`user_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_account_has_permission_permission1`
    FOREIGN KEY (`permission_id`)
    REFERENCES `converge`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `converge`.`user_account_has_user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `converge`.`user_account_has_user_role` ;

CREATE TABLE IF NOT EXISTS `converge`.`user_account_has_user_role` (
  `user_account_id` BIGINT NOT NULL,
  `user_role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_account_id`, `user_role_id`),
  INDEX `fk_user_account_has_user_role_user_role1_idx` (`user_role_id` ASC),
  INDEX `fk_user_account_has_user_role_user_account1_idx` (`user_account_id` ASC),
  CONSTRAINT `fk_user_account_has_user_role_user_account1`
    FOREIGN KEY (`user_account_id`)
    REFERENCES `converge`.`user_account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_account_has_user_role_user_role1`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `converge`.`user_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
