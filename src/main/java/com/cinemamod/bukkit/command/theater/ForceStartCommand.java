package com.cinemamod.bukkit.command.theater;

import com.cinemamod.bukkit.CinemaModPlugin;
import com.cinemamod.bukkit.service.VideoURLParser;
import com.cinemamod.bukkit.theater.Theater;
import com.cinemamod.bukkit.video.Video;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand extends TheaterCommandExecutor{



    public ForceStartCommand(CinemaModPlugin cinemaModPlugin) {
        super(cinemaModPlugin);
    }

    @Override
    public boolean onTheaterCommand(Player player, Command command, String label, String[] args, Theater theater) {
        return true;
    }

    @Override
    protected boolean onConsoleTheaterCommand(CommandSender sender, Command command, String label, String[] args, Theater theater) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Invalid URL. /" + label + " <url>");
            sender.sendMessage(ChatColor.RED + "Example: /" + label + " https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            return true;
        }

        String url = args[0];
        sender.sendMessage("Start loading " + url);
        VideoURLParser parser = new VideoURLParser(cinemaModPlugin, url);

        parser.parse(null);

        if (!parser.found()) {
            sender.sendMessage(ChatColor.RED + "This URL or video type is not supported.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "Fetching video information...");


        parser.getInfoFetcher().fetch().thenAccept(videoInfo -> {

            Video currentVideo = theater.getPlaying();
            if (currentVideo != null) {
                theater.getVideoQueue().addForce(new Video(currentVideo.getVideoInfo(), currentVideo.getRequester()));
            }
            theater.startVideo(new Video(videoInfo, null));
        });
        return true;
    }
}
