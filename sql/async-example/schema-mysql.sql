CREATE TABLE `customer`
(
    `id`         mediumint(1) unsigned NOT NULL auto_increment,
    `first_name` varchar(255) default NULL,
    `last_name`  varchar(255) default NULL,
    `birth_date` varchar(255),
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;


CREATE TABLE `customer2`
(
    `id`         mediumint(1) unsigned NOT NULL auto_increment,
    `first_name` varchar(255) default NULL,
    `last_name`  varchar(255) default NULL,
    `full_name`  varchar(255) default NULL,
    `birth_date` varchar(255),
    `age`        SMALLINT(1) unsigned,
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;
