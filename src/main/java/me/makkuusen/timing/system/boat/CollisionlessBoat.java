package me.makkuusen.timing.system.boat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CollisionlessBoat extends net.minecraft.world.entity.vehicle.Boat {

    public CollisionlessBoat(Level world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }
}
