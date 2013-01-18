USE mysql;
CREATE DATABASE IF NOT EXISTS migrate DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
USE migrate;
DROP TABLE IF EXISTS `kv_table`;
CREATE TABLE `kv_table` (`bucket` varchar(255) NOT NULL DEFAULT '', `dataKey` varchar(255) NOT NULL DEFAULT '', `className` varchar(255) DEFAULT NULL, `value` mediumblob, `updateTime` bigint(20) DEFAULT NULL, `version` bigint(20) DEFAULT NULL, PRIMARY KEY (`bucket`,`dataKey`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
