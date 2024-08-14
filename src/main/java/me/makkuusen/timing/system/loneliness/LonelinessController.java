package me.makkuusen.timing.system.loneliness;

import me.makkuusen.timing.system.api.TimingSystemAPI;
import me.makkuusen.timing.system.api.events.BoatSpawnEvent;
import me.makkuusen.timing.system.tplayer.TPlayer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

public class LonelinessController implements Listener {
    private static Plugin plugin = null;

    public LonelinessController(Plugin plugin) {
        this.plugin = plugin;
    }

    public static void hideAllBoatsAndPassengers(Player player) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.getWorld().getEntitiesByClass(Boat.class).stream()
                    .filter(boat -> !boat.getPassengers().contains(player))
                    .forEach(boat -> {
                        player.hideEntity(plugin, boat);
                        boat.getPassengers().forEach(passenger -> player.hideEntity(plugin, passenger));
                    });

            player.getWorld().getEntitiesByClass(ChestBoat.class).stream()
                    .filter(boat -> !boat.getPassengers().contains(player))
                    .forEach(boat -> {
                        player.hideEntity(plugin, boat);
                        boat.getPassengers().forEach(passenger -> player.hideEntity(plugin, passenger));
                    });
        });
    }

    public static void showAllBoatsAndPassengers(Player player) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.getWorld().getEntitiesByClass(Boat.class).stream()
                    .filter(boat -> !boat.getPassengers().contains(player))
                    .forEach(boat -> {
                        player.showEntity(plugin, boat);
                        boat.getPassengers().forEach(passenger -> player.showEntity(plugin, passenger));
                    });

            player.getWorld().getEntitiesByClass(ChestBoat.class).stream()
                    .filter(boat -> !boat.getPassengers().contains(player))
                    .forEach(boat -> {
                        player.showEntity(plugin, boat);
                        boat.getPassengers().forEach(passenger -> player.showEntity(plugin, passenger));
                    });
        });
    }

    public static void hideBoatAndPassengers(Entity boat) {
        if (boat == null) {
            plugin.getLogger().warning("Attempted to hide a null boat.");
            return;
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
            if (tPlayer != null && tPlayer.getSettings().isLonely() && !boat.getPassengers().contains(player) &&
                    player.isInsideVehicle() && (player.getVehicle() instanceof Boat || player.getVehicle() instanceof ChestBoat)) {
                player.hideEntity(plugin, boat);
                boat.getPassengers().forEach(passenger -> player.hideEntity(plugin, passenger));
            }
        }
    }

    @EventHandler
    public void onEnterBoat(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player &&
                (event.getVehicle() instanceof Boat || event.getVehicle() instanceof ChestBoat)) {

            TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
            if (tPlayer != null && tPlayer.getSettings().isLonely()) {
                hideAllBoatsAndPassengers(player);
            }
        }

        if (event.getVehicle() instanceof Boat boat) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
                if (tPlayer != null && tPlayer.getSettings().isLonely() && player.isInsideVehicle() &&
                        (player.getVehicle() instanceof Boat || player.getVehicle() instanceof ChestBoat)) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        player.hideEntity(plugin, event.getEntered());
                    });
                }
            }
        }
    }

    @EventHandler
    public void onExitBoat(VehicleExitEvent event) {

        if (event.isCancelled()) return;

        if (event.getExited() instanceof Player player &&
                (event.getVehicle() instanceof Boat || event.getVehicle() instanceof ChestBoat)) {

            TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
            if (tPlayer != null && tPlayer.getSettings().isLonely()) {
                showAllBoatsAndPassengers(player);
            }
        }

        if (event.getVehicle() instanceof Boat boat) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
                if (tPlayer != null && tPlayer.getSettings().isLonely() && player.isInsideVehicle() &&
                        (player.getVehicle() instanceof Boat || player.getVehicle() instanceof ChestBoat)) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        player.showEntity(plugin, event.getExited());
                    });
                }
            }
        }
    }

    @EventHandler
    public void onBoatSpawn(BoatSpawnEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Entity boat = event.getBoat();
            if (boat == null) {
                plugin.getLogger().warning("Boat in BoatSpawnEvent is null.");
                return;
            }
            hideBoatAndPassengers(boat);
        }, 5);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        TPlayer tPlayer = TimingSystemAPI.getTPlayer(player.getUniqueId());
        if (tPlayer != null && tPlayer.getSettings().isLonely() && player.isInsideVehicle() &&
                (player.getVehicle() instanceof Boat || player.getVehicle() instanceof ChestBoat)) {
            hideAllBoatsAndPassengers(player);
        }
    }
}
