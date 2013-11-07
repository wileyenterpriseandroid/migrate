USE mysql;
CREATE DATABASE IF NOT EXISTS migrate DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
USE migrate;
CREATE TABLE IF NOT EXISTS `kv_table` (`namespace` varchar(255) NOT NULL DEFAULT '',
	`dataKey` varchar(255) NOT NULL DEFAULT '',
	`className` varchar(255) DEFAULT NULL,
	`value` mediumblob, `updateTime` bigint(20) DEFAULT NULL,
	`version` bigint(20) DEFAULT NULL, `deleted` tinyint DEFAULT NULL, PRIMARY KEY (`namespace`, `dataKey`), INDEX (updateTime))
	ENGINE=InnoDB DEFAULT CHARSET=utf8;
