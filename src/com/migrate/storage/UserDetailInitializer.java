package com.migrate.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;

public class UserDetailInitializer implements InitializingBean {
    protected JdbcTemplate template;

    public UserDetailInitializer(DataSource dataSource) {
       Assert.notNull(dataSource, "DataSource required");
       this.template = new JdbcTemplate(dataSource);
    }

    public void afterPropertiesSet() throws Exception {
        // Normal authentication tables
        template.execute("CREATE TABLE IF NOT EXISTS USERS(USERNAME VARCHAR(50) NOT NULL PRIMARY KEY, PASSWORD VARCHAR(500) NOT NULL,ENABLED BOOLEAN NOT NULL);");
        template.execute("CREATE TABLE IF NOT EXISTS AUTHORITIES(USERNAME VARCHAR(50) NOT NULL,AUTHORITY VARCHAR(50) NOT NULL,CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME));");
//        template.execute("CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES(USERNAME, AUTHORITY);");
    }
}
