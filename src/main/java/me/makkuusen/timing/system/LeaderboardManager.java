package me.makkuusen.timing.system;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.HologramLines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderboardManager
{

    private static final HashMap<Integer, Hologram> fastestHolograms = new HashMap<>();


    public static void updateFastestTimeLeaderboard(int id)
    {
        if (!Race.enableLeaderboards)
        {
            return;
        }
        var maybeTrack = RaceDatabase.getTrackById(id);
		if (maybeTrack.isEmpty())
		{
			RaceUtilities.msgConsole("Leaderboard couldn't update, track not found");
			return;
		}
		RaceTrack raceTrack = maybeTrack.get();

        var topTen = raceTrack.getTopList(10);
        List<String> textLines = new ArrayList<>();

        for (String line : Race.configuration.leaderboardsFastestTimeLines())
        {

            line = line.replace("{mapname}", raceTrack.getName());

            // Replace stuff

            for (int i = 1; i <= 10; i++)
            {
                String playerName;
                String time;
                try
                {
                    playerName = topTen.get(i - 1).getPlayer().getName();
                    time = RaceUtilities.formatAsTime(topTen.get(i - 1).getTime());
                } catch (IndexOutOfBoundsException e)
                {
                    playerName = "Tom";
                    time = "ingen";
                }
                line = line.replace("{name" + i + "}", playerName);
                line = line.replace("{time" + i + "}", time);
            }
            textLines.add(line);
        }
        Bukkit.getScheduler().runTask(Race.getPlugin(), () -> {
			Hologram holo;
			Location leaderBoardLocation = raceTrack.getLeaderboardLocation();

			if (fastestHolograms.get(id) == null)
			{
				holo = HolographicDisplaysAPI.get(Race.getPlugin()).createHologram(leaderBoardLocation);
				fastestHolograms.put(id, holo);
			}
			else if (fastestHolograms.get(id).getPosition().distance(leaderBoardLocation) > 1)
			{
				fastestHolograms.get(id).delete();
				holo = HolographicDisplaysAPI.get(Race.getPlugin()).createHologram(leaderBoardLocation);
				fastestHolograms.put(id, holo);
			}
			else
			{
				holo = fastestHolograms.get(id);
			}

			HologramLines hologramLines = holo.getLines();
			hologramLines.clear();

			for (String line : textLines)
			{
				hologramLines.appendText(line);
			}
		});

    }

    public static void updateAllFastestTimeLeaderboard()
    {
        if (!Race.enableLeaderboards)
        {
            return;
        }
        for (RaceTrack t : RaceDatabase.getRaceTracks())
        {
            updateFastestTimeLeaderboard(t.getId());
        }
    }

    public static void updateAllFastestTimeLeaderboard(CommandSender toNotify)
    {
        if (!Race.enableLeaderboards)
        {
            return;
        }
        for (RaceTrack rTrack : RaceDatabase.getRaceTracks())
        {
            updateFastestTimeLeaderboard(rTrack.getId());
        }
        toNotify.sendMessage("§aFinished updating all of the fastest time leaderboards.");
    }

    public static void removeLeaderboard(int id)
    {
        if (!Race.enableLeaderboards)
        {
            return;
        }
        Bukkit.getScheduler().runTask(Race.getPlugin(), () -> {
			fastestHolograms.get(id).delete();
			fastestHolograms.remove(id);
		});
    }

    public static void startUpdateTask()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Race.getPlugin(), (@NotNull Runnable) LeaderboardManager::updateAllFastestTimeLeaderboard, 30 * 20, Race.configuration.leaderboardsUpdateTick());
    }
}
