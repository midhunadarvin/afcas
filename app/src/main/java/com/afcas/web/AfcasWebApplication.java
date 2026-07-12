package com.afcas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the AFCAS web UI.
 * Scans the full {@code com.afcas} package so engine, config, and JDBC
 * repositories are registered alongside web controllers.
 */
@SpringBootApplication(scanBasePackages = "com.afcas")
public class AfcasWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfcasWebApplication.class, args);
    }
}
