package me.makkuusen.timing.system.boat;

import me.makkuusen.timing.system.api.events.BoatSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.Boat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftBoat;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftChestBoat;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BoatSpawner {

    public static org.bukkit.entity.Boat spawnBoat(Location location) {
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        CollisionlessBoat boat = new CollisionlessBoat(level, location.getX(), location.getY(), location.getZ());
        float yaw = Location.normalizeYaw(location.getYaw());
        boat.setYRot(yaw);
        boat.yRotO = yaw;
        boat.setYHeadRot(yaw);
        level.addFreshEntity(boat, CreatureSpawnEvent.SpawnReason.COMMAND);
        boat.setVariant(Boat.Type.OAK);
        var craftBoat = new CraftBoat((CraftServer) Bukkit.getServer(), boat);

        // Create and fire BoatSpawnEvent
        BoatSpawnEvent boatSpawnEvent = new BoatSpawnEvent(null, location); // Replace null with the appropriate Player if available
        boatSpawnEvent.setBoat(craftBoat);
        Bukkit.getServer().getPluginManager().callEvent(boatSpawnEvent);

        // Handle event cancellation
        if (boatSpawnEvent.isCancelled()) {
            craftBoat.remove();
            return null;
        }

        return craftBoat;
    }

    public static org.bukkit.entity.ChestBoat spawnChestBoat(Location location) {
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        CollisionlessChestBoat boat = new CollisionlessChestBoat(level, location.getX(), location.getY(), location.getZ());
        float yaw = Location.normalizeYaw(location.getYaw());
        boat.setYRot(yaw);
        boat.yRotO = yaw;
        boat.setYHeadRot(yaw);
        level.addFreshEntity(boat, CreatureSpawnEvent.SpawnReason.COMMAND);
        boat.setVariant(Boat.Type.OAK);
        var craftBoat = new CraftChestBoat((CraftServer) Bukkit.getServer(), boat);

        // Create and fire BoatSpawnEvent
        BoatSpawnEvent boatSpawnEvent = new BoatSpawnEvent(null, location); // Replace null with the appropriate Player if available
        boatSpawnEvent.setBoat(craftBoat);
        Bukkit.getServer().getPluginManager().callEvent(boatSpawnEvent);

        // Handle event cancellation
        if (boatSpawnEvent.isCancelled()) {
            craftBoat.remove();
            return null;
        }

        return craftBoat;
    }

    public boolean isCollisionless(org.bukkit.entity.Boat boat) {
        return ((CraftBoat) boat).getHandle() instanceof CollisionlessBoat;
    }
}
