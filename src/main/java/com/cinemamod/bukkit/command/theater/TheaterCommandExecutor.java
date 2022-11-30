package com.cinemamod.bukkit.command.theater;

import com.cinemamod.bukkit.CinemaModPlugin;
import com.cinemamod.bukkit.theater.StaticTheater;
import com.cinemamod.bukkit.theater.Theater;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class TheaterCommandExecutor implements CommandExecutor {

    protected CinemaModPlugin cinemaModPlugin;

    public TheaterCommandExecutor(CinemaModPlugin cinemaModPlugin) {
        this.cinemaModPlugin = cinemaModPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length < 1) return true;
            String rawTheater = args[0];
            Theater theater = cinemaModPlugin.getTheaterManager()
                    .getTheaters().stream()
                    .filter(theater1 -> theater1.getId().equals(rawTheater))
                    .findFirst().orElse(null);
            if (theater == null) {
                sender.sendMessage("theater with id: " + rawTheater + " not found");
                return true;
            }
            return onConsoleTheaterCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length), theater);
        }

        Player player = (Player) sender;

        Theater theater = cinemaModPlugin.getTheaterManager().getCurrentTheater(player);

        if (theater == null || theater instanceof StaticTheater) {
            player.sendMessage(ChatColor.RED + "You must be in a theater to use this command.");
            return true;
        }

        return onTheaterCommand(player, command, label, args, theater);
    }

    protected boolean onConsoleTheaterCommand(CommandSender sender, Command command, String label, String[] args, Theater theater) {
        return true;
    }

    public abstract boolean onTheaterCommand(Player player, Command command, String label, String[] args, Theater theater);

}
