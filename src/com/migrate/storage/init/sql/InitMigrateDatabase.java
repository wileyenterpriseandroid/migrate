package com.migrate.storage.init.sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitMigrateDatabase implements ServletContextListener {
    public InitMigrateDatabase() {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();

        try {
            WebApplicationContext wac = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(ctx);
            DataSourceHolder dataHolder = (DataSourceHolder) wac.getBean("dataHolder");
            BasicDataSource dataSource = dataHolder.getDataSource();

            String dataSourceURL = dataSource.getUrl();
            Connection c = DriverManager.getConnection(dataSourceURL,
                    dataSource.getUsername(), dataSource.getPassword());

            initDatabase(c, ctx);

            ctx.log("DB script executed");

        } catch (Exception e) {
            ctx.log("Unable to execute DB script", e);
        }
    }

    public void initDatabase(Connection c, ServletContext ctx) {
        Statement s = null;
        try {
            s = c.createStatement();

            URL databaseResource = ctx.getResource("/WEB-INF/sql/migrateData.sql");
            InputStream sqlStream = databaseResource.openStream();
            String createSql = IOUtils.toString(sqlStream, "UTF-8");
            String[] batch = createSql.split(";");
            for (String cmd : batch) {
                cmd = cmd.trim();
                if (!"".equals(cmd))  {
                    s.addBatch(cmd + ";");
                    ctx.log("Batch cmd: " + cmd + ";");
                }
            }
            s.executeBatch();

        } catch (SQLException e) {
            ctx.log("Execute init sql", e);
        } catch (Exception e) {
            ctx.log("Execute init sql", e);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch(SQLException ignore){
            }

            try {
                if (c != null) {
                    c.close();
                }
            } catch(SQLException alsoIgnore) {
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
