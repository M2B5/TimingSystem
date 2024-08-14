package me.makkuusen.timing.system.database.updates;

import co.aikar.idb.DB;
import java.sql.SQLException;

public class Version5 {

    public static void updateMySQL() throws SQLException {
        try {
            DB.executeUpdate("ALTER TABLE `ts_heats` ADD `forceBoat` tinyint(1) NOT NULL DEFAULT 0 AFTER `lonely`;");
        } catch (SQLException e) {
            // Log the error for debugging
            System.err.println("Failed to update MySQL schema: " + e.getMessage());
            throw e;  // Rethrow to ensure the error is propagated correctly
        }
    }

    public static void updateSQLite() throws SQLException {
        try {
            DB.executeUpdate("ALTER TABLE `ts_heats` ADD `forceBoat` INTEGER NOT NULL DEFAULT 0;");
        } catch (SQLException e) {
            // Log the error for debugging
            System.err.println("Failed to update SQLite schema: " + e.getMessage());
            throw e;  // Rethrow to ensure the error is propagated correctly
        }
    }
}
