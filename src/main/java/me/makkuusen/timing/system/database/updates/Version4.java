package me.makkuusen.timing.system.database.updates;

import co.aikar.idb.DB;
import java.sql.SQLException;

public class Version4 {

    public static void updateMySQL() throws SQLException {
        try {
            DB.executeUpdate("ALTER TABLE `ts_players` ADD `lonely` tinyint(1) NOT NULL DEFAULT 0 AFTER `verbose`;");
            DB.executeUpdate("ALTER TABLE `ts_heats` ADD `lonely` tinyint(1) NOT NULL DEFAULT 0 AFTER `maxDrivers`;");
        } catch (SQLException e) {
            // Log the error for debugging
            System.err.println("Failed to update MySQL schema: " + e.getMessage());
            throw e;  // Rethrow to ensure the error is propagated correctly
        }
    }

    public static void updateSQLite() throws SQLException {
        try {
            DB.executeUpdate("ALTER TABLE `ts_players` ADD `lonely` INTEGER NOT NULL DEFAULT 0;");
            DB.executeUpdate("ALTER TABLE `ts_heats` ADD `lonely` INTEGER NOT NULL DEFAULT 0;");
        } catch (SQLException e) {
            // Log the error for debugging
            System.err.println("Failed to update SQLite schema: " + e.getMessage());
            throw e;  // Rethrow to ensure the error is propagated correctly
        }
    }
}
