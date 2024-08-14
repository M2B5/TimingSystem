package me.makkuusen.timing.system.boat;

import me.makkuusen.timing.system.participant.Driver;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

import static me.makkuusen.timing.system.database.EventDatabase.playerInRunningHeat;

public class ForceBoat implements Listener {
    @EventHandler
    public static void onExitBoat(VehicleExitEvent event) {
        if (event.isCancelled()) return;

        if ((event.getVehicle() instanceof Boat || event.getVehicle() instanceof ChestBoat) && event.getExited() instanceof Player) {
            Player player = (Player) event.getExited();
            Vehicle vehicle = event.getVehicle();

            if (vehicle.getPassengers().size() > 0 && vehicle.getPassengers().get(0) instanceof Player) {
                Player d = (Player) vehicle.getPassengers().get(0);

                if (d.equals(player)) {
                    for (Driver driver : playerInRunningHeat.values()) {
                        if (driver.getTPlayer().getUniqueId().equals(player.getUniqueId())) {
                            if (driver.getHeat().isForceBoat()) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
