create database if not exists `db_gamified_marketing`;
use `db_gamified_marketing`;

# to drop all the foreign keys
set foreign_key_checks = 0;

drop table if exists `UserTable`;
create table `UserTable` (
    `id` int not null auto_increment,
    `username` varchar(45) not null,
    `email` varchar(45) not null,
    `password` varchar(45) not null,
    `name` varchar(45) not null,
    `surname` varchar(45) not null,
    `admin` bool default false,
    `blocked` bool default false,
    `gamification_points` int default 0,
    primary key (`id`)
);

insert into `UserTable` (`username`, `email`, `password`, `name`, `surname`, `admin`) values ('dami', 'damiano.derin@mail.polimi.it', 'pass', 'damiano', 'derin', true);

drop table if exists `LogInHistory`;
create table `LogInHistory` (
	`user_id` int not null,
    `date` datetime,
    primary key (`user_id`, `date`),
    foreign key (`user_id`)
        references `UserTable` (`id`)
        on delete cascade on update cascade
);

drop table if exists `Product`;
create table `Product` (
    `date` date,
    `name` varchar(255) not null,
    `image` longblob,
    primary key (`date`)
);

drop table if exists `Review`;
create table `Review` (
    `product_date` date not null,
    `id_creator` int not null,
    `content` text not null,
    primary key (`product_date` , `id_creator`),
    foreign key (`product_date`)
        references `Product` (`date`)
        on delete cascade on update cascade,
    foreign key (`id_creator`)
        references `UserTable` (`id`)
        on delete cascade on update cascade
);

drop table if exists `StatisticalData`;
create table `StatisticalData` (
    `product_date` date not null,
    `user_id` int not null,
    `age` int,
    `sex` varchar(4),
    `expertise_level` varchar(45),
    primary key (`product_date` , `user_id`),
    foreign key (`product_date`)
        references `Product` (`date`)
        on delete cascade on update cascade,
    foreign key (`user_id`)
        references `UserTable` (`id`)
        on delete cascade on update cascade
);

drop table if exists `Question`;
create table `Question` (
    `id` int not null auto_increment,
    `product_date` date not null,
    `content` text not null,
    primary key (`id`),
	foreign key (`product_date`)
        references `Product` (`date`)
        on delete cascade on update cascade
);

drop table if exists `Answer`;
create table `Answer` (
    `question_id` int not null,
    `id_creator` int not null,
    `content` text not null,
    primary key (`question_id` , `id_creator`),
    foreign key (`question_id`)
        references `Question` (`id`)
        on delete cascade on update cascade,
    foreign key (`id_creator`)
        references `UserTable` (`id`)
        on delete cascade on update cascade
);
