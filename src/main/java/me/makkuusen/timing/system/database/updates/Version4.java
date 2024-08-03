package me.makkuusen.timing.system.database.updates;

import co.aikar.idb.DB;

import java.sql.SQLException;

public class Version4 {

    public static void updateMySQL() throws SQLException {
        DB.executeUpdate("ALTER TABLE `ts_players` ADD `lonely` tinyint(1) NOT NULL DEFAULT 0 after 'verbose';");
        DB.executeUpdate("ALTER TABLE `ts_heats` ADD `lonely` tinyint(1) NOT NULL DEFAULT 0 after 'maxDrivers';");
    }

    public static void updateSQLite() throws SQLException {
        DB.executeUpdate("ALTER TABLE `ts_players` ADD `lonely` INTEGER NOT NULL DEFAULT 0;");
        DB.executeUpdate("ALTER TABLE `ts_heats` ADD `lonely` INTEGER NOT NULL DEFAULT 0;");
    }
}
