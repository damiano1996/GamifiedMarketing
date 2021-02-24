create database if not exists `db_gamified_marketing`;
use `db_gamified_marketing`;

# to drop all the foreign keys
set foreign_key_checks = 0;

drop table if exists `UserTable`;
CREATE TABLE `UserTable` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `surname` VARCHAR(45) NOT NULL,
    `admin` BOOL DEFAULT FALSE,
    `blocked` BOOL DEFAULT FALSE,
    PRIMARY KEY (`id`)
);

insert into `UserTable` (`username`, `email`, `password`, `name`, `surname`, `admin`) values ('admin', 'admin@gamifiedmarketing.com', 'admin', 'admin', 'admin', true);
insert into `UserTable` (`username`, `email`, `password`, `name`, `surname`) values ('aa', 'aa@gamifiedmarketing.com', 'aa', 'aa', 'aa');
insert into `UserTable` (`username`, `email`, `password`, `name`, `surname`) values ('bb', 'bb@gamifiedmarketing.com', 'bb', 'bb', 'bb');
insert into `UserTable` (`username`, `email`, `password`, `name`, `surname`) values ('cc', 'cc@gamifiedmarketing.com', 'cc', 'cc', 'cc');

drop table if exists `LogInHistory`;
CREATE TABLE `LogInHistory` (
    `user_id` INT NOT NULL,
    `date` DATETIME,
    PRIMARY KEY (`user_id` , `date`),
    FOREIGN KEY (`user_id`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `Product`;
CREATE TABLE `Product` (
    `date` DATE,
    `name` VARCHAR(255) NOT NULL,
    `image` LONGBLOB,
    PRIMARY KEY (`date`)
);

drop table if exists `Review`;
CREATE TABLE `Review` (
    `product_date` DATE NOT NULL,
    `id_creator` INT NOT NULL,
    `content` TEXT NOT NULL,
    PRIMARY KEY (`product_date` , `id_creator`),
    FOREIGN KEY (`product_date`)
        REFERENCES `Product` (`date`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`id_creator`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `StatisticalData`;
CREATE TABLE `StatisticalData` (
    `product_date` DATE NOT NULL,
    `user_id` INT NOT NULL,
    `age` INT,
    `sex` ENUM('M', 'F', 'O'),
    `expertise_level` ENUM('low', 'medium', 'high'),
    PRIMARY KEY (`product_date` , `user_id`),
    FOREIGN KEY (`product_date`)
        REFERENCES `Product` (`date`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `Question`;
CREATE TABLE `Question` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `product_date` DATE NOT NULL,
    `content` TEXT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_date`)
        REFERENCES `Product` (`date`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `Answer`;
CREATE TABLE `Answer` (
    `question_id` INT NOT NULL,
    `id_creator` INT NOT NULL,
    `content` TEXT NOT NULL,
    PRIMARY KEY (`question_id` , `id_creator`),
    FOREIGN KEY (`question_id`)
        REFERENCES `Question` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`id_creator`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `CancelledQuestionnaire`;
CREATE TABLE `CancelledQuestionnaire` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `product_date` DATE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_date`)
        REFERENCES `Product` (`date`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `GamificationPoint`;
CREATE TABLE `GamificationPoint` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `product_date` DATE NOT NULL,
    `points` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`)
        REFERENCES `UserTable` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_date`)
        REFERENCES `Product` (`date`)
        ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists `Badword`;
CREATE TABLE `Badword` (
    `word` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`word`)
);

CREATE 
    TRIGGER  marketing_questionnaire_gamification_points
 AFTER INSERT ON `Answer` FOR EACH ROW 
    INSERT INTO `GamificationPoint` (`user_id` , `product_date` , `points`) VALUES (new.id_creator , (SELECT 
            q.product_date
        FROM
            `Question` AS q
        WHERE
            q.id = new.question_id) , 1);

CREATE 
    TRIGGER  statistical_questionnaire_sex_gamification_points
 AFTER INSERT ON `StatisticalData` FOR EACH ROW 
    INSERT INTO `GamificationPoint` (`user_id` , `product_date` , `points`) VALUES (new.user_id , new.product_date , IF(new.age != - 1, 2, 0) + IF(new.sex IS NOT NULL, 2, 0) + IF(new.expertise_level IS NOT NULL, 2, 0));