package db.impl;

import db.api.DBController;
import db.api.UserFeedbackManager;
import db.api.UserManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * Created by ndayan on 03/06/2017.
 */
@Controller
public class DBControllerImpl implements DBController {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private UserManager userManager;
    private UserFeedbackManager userFeedbackManager;


    public DBControllerImpl() {
        dataSource = createDataSource();
        jdbcTemplate = createJdbcTemplate();
        userManager = createUserManager();
        userFeedbackManager = createUserFeedbackManager();
    }

    @Override
    public void shutdown() {
        jdbcTemplate.execute("SHUTDOWN");
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public UserFeedbackManager getUserFeedbackManager() {
        return userFeedbackManager;
    }

    private UserManager createUserManager() {
        return new UserManagerImpl(jdbcTemplate);
    }

    private DataSource createDataSource() {
        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = getEmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setName("testdb")
                .setType(EmbeddedDatabaseType.HSQL)
//                .addScript("db/sql/create-db.sql")
                .build();
        return db;
    }

    private EmbeddedDatabaseBuilder getEmbeddedDatabaseBuilder() {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        databaseBuilder.setDataSourceFactory(new DataSourceFactory() {
            private final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
            @Override
            public ConnectionProperties getConnectionProperties() {
                return new ConnectionProperties() {
                    @Override
                    public void setDriverClass(Class<? extends Driver> driverClass) {
                        dataSource.setDriverClass(org.hsqldb.jdbcDriver.class);
                    }

                    @Override
                    public void setUrl(String url) {
//                        dataSource.setUrl("jdbc:hsqldb:mem:testdb;sql.syntax_mys=true");
//                        dataSource.setUrl("jdbc:hsqldb:file:target/testdb;sql.syntax_mys=true");
                        dataSource.setUrl("jdbc:hsqldb:file:src/main/resources/db/sql/testdb;sql.syntax_mys=true");
                    }

                    @Override
                    public void setUsername(String username) {
                        dataSource.setUsername("sa");
                    }

                    @Override
                    public void setPassword(String password) {
                        dataSource.setPassword("");
                    }
                };
            }

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });

        return databaseBuilder;
    }

    private UserFeedbackManager createUserFeedbackManager() {
        return new UserFeedbackManagerImpl(jdbcTemplate);
    }

    private JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
