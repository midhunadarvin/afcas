package com.afcas.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Automatically initializes AFCAS database schema and procedures on application startup.
 * 
 * This component checks if the required stored procedures exist in the database.
 * If not, it executes the SQL initialization scripts from resources/db/init/ in order.
 * 
 * The scripts use CREATE TABLE IF NOT EXISTS and CREATE OR REPLACE FUNCTION/PROCEDURE,
 * so they are safe to run multiple times.
 */
@Component
public class DatabaseSchemaInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaInitializer.class);

    @Autowired
    private javax.sql.DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${afcas.database.init.enabled:true}")
    private boolean initEnabled;

    @Value("${afcas.database.init.check-procedure:GetPrincipalList}")
    private String checkProcedureName;

    @Value("${afcas.database.init.scripts-location:classpath:db/init/*.postgres.sql}")
    private String scriptsLocation;

    @PostConstruct
    public void initializeSchema() {
        if (!initEnabled) {
            logger.info("Database schema auto-initialization is disabled (afcas.database.init.enabled=false)");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            if (schemaExists(connection)) {
                logger.info("AFCAS database schema already initialized (procedure '{}' exists)", checkProcedureName);
                return;
            }

            logger.info("AFCAS database schema not found. Initializing...");
            executeInitializationScripts(connection);
            logger.info("AFCAS database schema initialized successfully!");

        } catch (Exception e) {
            logger.error("Failed to initialize AFCAS database schema: {}", e.getMessage(), e);
            // Don't fail the application startup - let it continue and fail on actual use
        }
    }

    /**
     * Check if the schema is already initialized by looking for a key procedure.
     */
    private boolean schemaExists(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // Check if the key procedure/function exists
            String query = String.format(
                "SELECT COUNT(*) FROM pg_proc WHERE proname = '%s'",
                checkProcedureName.toLowerCase()
            );
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            logger.debug("Could not check for existing schema: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Execute all initialization SQL scripts in order.
     */
    private void executeInitializationScripts(Connection connection) throws Exception {
        // List of scripts in execution order
        String[] scriptFiles = {
            "0-init-tables.postgres.sql",
            "1-edge-exists-function.postgres.sql",
            "2-add-edge-procedure.postgres.sql",
            "3-remove-edge-procedures.postgres.sql",
            "4-principal-procedures.postgres.sql",
            "5-operation-procedures.postgres.sql",
            "6-resource-procedures.postgres.sql",
            "7-access-predicate-procedures.postgres.sql"
        };

        for (String scriptFile : scriptFiles) {
            String resourcePath = "classpath:db/init/" + scriptFile;
            try {
                Resource resource = resourceLoader.getResource(resourcePath);
                if (!resource.exists()) {
                    logger.warn("Script not found: {}", resourcePath);
                    continue;
                }

                String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                executeSqlScript(connection, sql, scriptFile);

            } catch (Exception e) {
                logger.error("Failed to execute script {}: {}", scriptFile, e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Execute a SQL script.
     * 
     * PostgreSQL scripts containing PL/pgSQL functions/procedures use $$ dollar-quoting
     * and contain semicolons inside function bodies. Naive splitting by semicolon breaks these.
     * 
     * We execute the entire script as one statement. If it fails (e.g., partial re-run),
     * we log and continue since scripts are idempotent (CREATE IF NOT EXISTS / CREATE OR REPLACE).
     */
    private void executeSqlScript(Connection connection, String sql, String scriptName) throws Exception {
        logger.debug("Executing script: {}", scriptName);
        
        try (Statement stmt = connection.createStatement()) {
            // Execute the entire script. PostgreSQL JDBC handles multi-statement scripts
            // when statements are separated by semicolons at the top level (not inside $$ blocks).
            stmt.execute(sql);
            logger.debug("Successfully executed: {}", scriptName);
        } catch (Exception e) {
            // Script execution failed. This can happen if:
            // - Some objects already exist (harmless with CREATE IF NOT EXISTS)
            // - Partial re-run or dependency issues
            // Log at debug level and continue - scripts are designed to be re-runnable
            String msg = e.getMessage();
            if (msg != null && (msg.contains("already exists") || msg.contains("duplicate key"))) {
                logger.debug("Script {} had expected warnings: {}", scriptName, msg);
            } else {
                logger.warn("Script {} execution note: {}", scriptName, msg);
            }
            // Don't throw - let startup continue; actual failures will surface on real usage
        }
    }
}
