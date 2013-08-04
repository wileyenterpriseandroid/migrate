package com.migrate.storage.init.sql;

import org.apache.commons.dbcp.BasicDataSource;

public class DataSourceHolder {
    private BasicDataSource dataSource;

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
