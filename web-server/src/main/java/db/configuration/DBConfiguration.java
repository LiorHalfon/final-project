package db.configuration;

import db.api.DBController;
import db.impl.DBControllerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan
@Configuration
public class DBConfiguration {

    @Bean
    public DBController getDBController() {
        return new DBControllerImpl();
    }

}
