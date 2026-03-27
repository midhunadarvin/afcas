package com.afcas.engine;

/**
 * Enum representing the authorization engine mode.
 */
public enum EngineMode {
    /**
     * Use PostgreSQL stored procedures (original behavior).
     * Requires procedures to be installed in the database.
     */
    SQL,

    /**
     * Use application-layer Java logic.
     * Works with any database, logic is in Java.
     */
    JAVA
}
