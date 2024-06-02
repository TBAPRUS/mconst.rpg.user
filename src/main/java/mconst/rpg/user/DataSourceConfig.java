package mconst.rpg.user;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://postgres:5432/users");
//        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/users");
        dataSourceBuilder.username("admin");
        dataSourceBuilder.password("secretpassword123");
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        return dataSourceBuilder.build();
    }
}

//#
//        ##development
//##spring.datasource.url=jdbc:postgresql://localhost:5432/users
//        ##docker
//#spring.datasource.url=jdbc:postgresql://postgres:5432/users
//        #spring.datasource.username=admin
//#spring.datasource.password=secretpassword123
//#spring.datasource.driver-class-name=org.postgresql.Driver
//#
//        #spring.jpa.generate-ddl=true