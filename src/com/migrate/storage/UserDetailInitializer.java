package com.migrate.storage;

import com.migrate.gcm.GCMSyncNotification;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDetailInitializer implements InitializingBean {
    protected static final Logger logger =
            Logger.getLogger(UserDetailInitializer.class.getName());

    @Autowired
    private ServletContext ctx;

    protected JdbcTemplate template;

    public UserDetailInitializer(DataSource dataSource) {
       Assert.notNull(dataSource, "DataSource required");
       this.template = new JdbcTemplate(dataSource);
    }

    public void afterPropertiesSet() throws Exception {
        template.execute("CREATE DATABASE IF NOT EXISTS migrate DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;\n");
        template.execute("USE migrate;");

        // Note: always make tables lower case since mysql is case sensitive on linux

        // Normal authentication tables
        template.execute("CREATE TABLE IF NOT EXISTS users(USERNAME VARCHAR(50) NOT NULL PRIMARY KEY, PASSWORD VARCHAR(500) NOT NULL,ENABLED BOOLEAN NOT NULL);");
        template.execute("CREATE TABLE IF NOT EXISTS authorities(USERNAME VARCHAR(50) NOT NULL,AUTHORITY VARCHAR(50) NOT NULL,CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(USERNAME) REFERENCES users(USERNAME));");
//        template.execute("CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES(USERNAME, AUTHORITY);");

        initDatabase();
    }

    public void initDatabase() {
        try {
            URL databaseResource = ctx.getResource("/WEB-INF/sql/migrateData.sql");
            InputStream sqlStream = databaseResource.openStream();
            String createSql = IOUtils.toString(sqlStream, "UTF-8");
            String[] batch = createSql.split(";");
            for (String cmd : batch) {
                cmd = cmd.trim();
                if (!"".equals(cmd))  {
                    template.execute(cmd + ";");
                    logger.log(Level.INFO, "Batch cmd: " + cmd + ";");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Execute init sql", e);
        }
    }
}
