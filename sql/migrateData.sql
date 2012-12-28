DROP TABLE IF EXISTS `kv_table`;
CREATE TABLE `kv_table` (`bucket` varchar(255) NOT NULL DEFAULT '', `dataKey` varchar(255) NOT NULL DEFAULT '', `className` varchar(255) DEFAULT NULL, `value` mediumblob, `updateTime` bigint(20) DEFAULT NULL, `version` bigint(20) DEFAULT NULL, PRIMARY KEY (`bucket`,`dataKey`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
