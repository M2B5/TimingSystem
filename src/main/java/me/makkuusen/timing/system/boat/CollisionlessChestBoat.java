package me.makkuusen.timing.system.boat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.Level;

public class CollisionlessChestBoat extends ChestBoat{


    public CollisionlessChestBoat(EntityType<? extends ChestBoat> entitytypes, Level world) {
        super(entitytypes, world);
    }

    public CollisionlessChestBoat(Level world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }
}
