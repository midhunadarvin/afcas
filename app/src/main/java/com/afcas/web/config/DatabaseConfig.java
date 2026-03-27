package com.afcas.web.config;

import com.afcas.utils.DatabaseHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${afcas.database.host:localhost}")
    private String dbHost;

    @Value("${afcas.database.port:5432}")
    private String dbPort;

    @Value("${afcas.database.username:postgres}")
    private String dbUsername;

    @Value("${afcas.database.password:123456}")
    private String dbPassword;

    @PostConstruct
    public void initDatabase() {
        DatabaseHelper.init(dbHost, dbPort, dbUsername, dbPassword);
    }

    /**
     * Creates a DataSource bean using the afcas.database.* properties.
     * This is required by:
     * - DatabaseSchemaInitializer (autowires DataSource)
     * - JdbcTemplate (used by JdbcEdgeRepository, JdbcAccessPredicateRepository)
     * 
     * Spring Boot's auto-configuration looks for spring.datasource.* properties,
     * so we must provide our own DataSource bean using custom afcas.database.* config.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String portPart = (dbPort != null && !dbPort.isEmpty()) ? ":" + dbPort : "";
        dataSource.setUrl("jdbc:postgresql://" + dbHost + portPart + "/");
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
