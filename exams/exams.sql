-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ras_exams
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ras_exams` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ras_exams` ;

-- -----------------------------------------------------
-- Table `ras_exams`.`exam`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`exam` (
  `examID` BINARY(16) NOT NULL,
  PRIMARY KEY (`examID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`examheader`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`examheader` (
  `examHeaderID` BINARY(16) NOT NULL,
  `examID` BINARY(16) NOT NULL,  
  `examName` VARCHAR(64) NOT NULL,
  `examUC` VARCHAR(64) NULL,
  `examAdmissionTime` TIME NULL DEFAULT NULL,
  PRIMARY KEY (`examHeaderID`),
  INDEX `examID_idx` (`examID` ASC) VISIBLE,
  CONSTRAINT `examIDunique`
    UNIQUE(`examID`),
  CONSTRAINT `examName`
    UNIQUE(`examName`),
  CONSTRAINT `examIDheader`
    FOREIGN KEY (`examID`)
    REFERENCES `ras_exams`.`exam` (`examID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`examschedules`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`examschedules` (
  `scheduleID` BINARY(16) NOT NULL,
  `examHeaderID` BINARY(16) NOT NULL,
  PRIMARY KEY (`scheduleID`, `examHeaderID`),
  INDEX `examSchedule_idx` (`scheduleID` ASC) VISIBLE,
  CONSTRAINT `examSchedule`
    FOREIGN KEY (`examHeaderID`)
    REFERENCES `ras_exams`.`examheader` (`examHeaderID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ras_exams`.`examversion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`examversion` (
  `examVersionID` BINARY(16) NOT NULL,
  `examID` BINARY(16) NOT NULL,
  `versionNumber` INT NULL DEFAULT NULL,
  PRIMARY KEY (`examVersionID`),
  INDEX `examID_idx` (`examID` ASC) VISIBLE,
  CONSTRAINT `examIDVersion`
    FOREIGN KEY (`examID`)
    REFERENCES `ras_exams`.`exam` (`examID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`question` (
  `questionID` BINARY(16) NOT NULL,
  `questionNumber` INT NOT NULL,
  `questionType` CHAR(1) NULL DEFAULT NULL,
  `question` VARCHAR(512) NULL DEFAULT NULL,
  `versionID` BINARY(16) NULL DEFAULT NULL,
  `score` INT NULL DEFAULT 0,
  PRIMARY KEY (`questionID`),
  INDEX `version_idx` (`versionID` ASC) VISIBLE,
  CONSTRAINT `version`
    FOREIGN KEY (`versionID`)
    REFERENCES `ras_exams`.`examversion` (`examVersionID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`completespacesquestion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`completespacesquestion` (
  `questionID` BINARY(16) NOT NULL,
  `text` VARCHAR(2048) NULL DEFAULT NULL COMMENT 'should be like \"complete the {blank,1} spaces with the {right,1} answers\", where {} is a blank space and the word inside is the correct word and the number how much the question is worth',
  PRIMARY KEY (`questionID`),
  CONSTRAINT `questionIDcs`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`question` (`questionID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`enrolledstudents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`enrolledstudents` (
  `studentID` BINARY(16) NOT NULL,
  `examID` BINARY(16) NOT NULL,
  PRIMARY KEY (`studentID`, `examID`),
  INDEX `enrolledExam_idx` (`examID` ASC) VISIBLE,
  CONSTRAINT `enrolledExam`
    FOREIGN KEY (`examID`)
    REFERENCES `ras_exams`.`exam` (`examID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`examanswer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`examanswer` (
  `examAnswerID` BINARY(16) NOT NULL,
  `examID` BINARY(16) NOT NULL,
  `studentID` BINARY(16) NOT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`examAnswerID`),
  INDEX `studentAnswer_idx` (`examID` ASC, `studentID` ASC) VISIBLE,
  CONSTRAINT `studentAnswer`
    FOREIGN KEY (`examID` , `studentID`)
    REFERENCES `ras_exams`.`enrolledstudents` (`examID` , `studentID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`answercompletespaces`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`answercompletespaces` (
  `answerID` BINARY(16) NOT NULL,
  `questionID` BINARY(16) NOT NULL,
  `text` VARCHAR(2048) NULL DEFAULT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`, `questionID`),
  INDEX `answerCompleteSpaces_idx` (`questionID` ASC) VISIBLE,
  CONSTRAINT `answerCompleteSpaces`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`completespacesquestion` (`questionID`),
  CONSTRAINT `examAnswerCS`
    FOREIGN KEY (`answerID`)
    REFERENCES `ras_exams`.`answer` (`answerID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`multiplechoicequestion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`multiplechoicequestion` (
  `questionID` BINARY(16) NOT NULL,
  `choiceNumber` INT NOT NULL,
  `description` VARCHAR(256) NULL DEFAULT NULL,
  `correction` TINYINT NULL DEFAULT NULL,
  `score` INT NULL DEFAULT NULL,
  PRIMARY KEY (`questionID`, `choiceNumber`),
  CONSTRAINT `questionIDmc`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`question` (`questionID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`answermultiplechoice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`answermultiplechoice` (
  `answerID` BINARY(16) NOT NULL,
  `questionID` BINARY(16) NOT NULL,
  `choiceNumber` INT NOT NULL,
  `selected` TINYINT NULL DEFAULT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`, `questionID`, `choiceNumber`),
  INDEX `answerMultipleChoice_idx` (`questionID` ASC, `choiceNumber` ASC) VISIBLE,
  CONSTRAINT `answerMultipleChoice`
    FOREIGN KEY (`questionID` , `choiceNumber`)
    REFERENCES `ras_exams`.`multiplechoicequestion` (`questionID` , `choiceNumber`),
  CONSTRAINT `examAnswerMC`
    FOREIGN KEY (`answerID`)
    REFERENCES `ras_exams`.`answer` (`answerID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`trueorfalsequestion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`trueorfalsequestion` (
  `questionID` BINARY(16) NOT NULL,
  `optionNumber` INT NOT NULL,
  `description` VARCHAR(256) NULL DEFAULT NULL,
  `correction` TINYINT NULL DEFAULT NULL,
  `score` INT NULL DEFAULT NULL,
  PRIMARY KEY (`questionID`, `optionNumber`),
  CONSTRAINT `questionIDtf`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`question` (`questionID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`answertrueorfalse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`answertrueorfalse` (
  `answerID` BINARY(16) NOT NULL,
  `questionID` BINARY(16) NOT NULL,
  `optionNumber` INT NOT NULL,
  `answer` TINYINT NULL DEFAULT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`, `questionID`, `optionNumber`),
  INDEX `answerTrueOrFalse_idx` (`questionID` ASC, `optionNumber` ASC) VISIBLE,
  CONSTRAINT `answerTrueOrFalse`
    FOREIGN KEY (`questionID` , `optionNumber`)
    REFERENCES `ras_exams`.`trueorfalsequestion` (`questionID` , `optionNumber`),
  CONSTRAINT `examAnswerTF`
    FOREIGN KEY (`answerID`)
    REFERENCES `ras_exams`.`answer` (`answerID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`writingquestion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`writingquestion` (
  `questionID` BINARY(16) NOT NULL,
  `criteria` VARCHAR(512) NULL DEFAULT NULL,
  `minimumLimit` INT NULL DEFAULT NULL,
  `maximumLimit` INT NULL DEFAULT NULL,
  PRIMARY KEY (`questionID`),
  CONSTRAINT `questionIDw`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`question` (`questionID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ras_exams`.`answerwriting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`answerwriting` (
  `answerID` BINARY(16) NOT NULL,
  `questionID` BINARY(16) NOT NULL,
  `text` VARCHAR(2048) NULL DEFAULT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`, `questionID`),
  INDEX `answerWriting_idx` (`questionID` ASC) VISIBLE,
  CONSTRAINT `answerWriting`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`writingquestion` (`questionID`),
  CONSTRAINT `examAnswerW`
    FOREIGN KEY (`answerID`)
    REFERENCES `ras_exams`.`answer` (`answerID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `ras_exams`.`answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ras_exams`.`answer` (
  `answerID` BINARY(16) NOT NULL,
  `examAnswerID` BINARY(16) NOT NULL,
  `questionID` BINARY(16) NOT NULL,
  `answerType` CHAR(1) NOT NULL,
  `grade` INT NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`),
  INDEX `answer_idx` (`answerID` ASC) VISIBLE,
  CONSTRAINT `answer`
    FOREIGN KEY (`questionID`)
    REFERENCES `ras_exams`.`question` (`questionID`),
  CONSTRAINT `examAnswer`
    FOREIGN KEY (`examAnswerID`)
    REFERENCES `ras_exams`.`examanswer` (`examAnswerID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
