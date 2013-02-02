USE mysql;
CREATE DATABASE IF NOT EXISTS migrate DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
USE migrate;
DROP TABLE IF EXISTS `kv_table`;


CREATE TABLE `kv_table` (`bucket` varchar(255) NOT NULL DEFAULT '',
	`dataKey` varchar(255) NOT NULL DEFAULT '', 
	`className` varchar(255) DEFAULT NULL, 
	`value` mediumblob, `updateTime` bigint(20) DEFAULT NULL, 
	`version` bigint(20) DEFAULT NULL, PRIMARY KEY (`bucket`, `dataKey`), INDEX (updateTime))
	ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
CREATE TABLE `kv_table` (`wd_className` varchar(255) NOT NULL DEFAULT '',
	`wd_id` varchar(255) NOT NULL DEFAULT '', 
	`wd_value` mediumblob, 
	`wd_updateTime` bigint(20) DEFAULT NULL, 
	`wd_version` bigint(20) DEFAULT NULL, PRIMARY KEY (`wd_className`,`wd_id`), INDEX (wd_updateTime))
	ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/