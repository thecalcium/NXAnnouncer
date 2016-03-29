package com.projectbarks.nxannouncer.announcer;

import com.projectbarks.nxannouncer.NXAnnouncer;
import com.projectbarks.nxannouncer.config.Config;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable implements Runnable {

    private int count;
    private NXAnnouncer main;
    private Config config;

    public Timer(NXAnnouncer main) {
        this.count = 0;
        this.main = main;
        this.config = main.getConf();
    }

    @Override
    public void run() {
        if (config.getAnnouncements().size() > 0) {
            if (main.getServer().getOnlinePlayers().size() < config.getMinPlayers()) {
                return;
            }
            Announcement announcement = config.getAnnouncements().get(getCount());
            this.broadcast(config.getColor() + announcement.getColorizedHeader());
            for (String message : announcement.getTranslatedMessage()) {
                String colorizedMessage = Announcement.colorize(message);
                String dynamicChar = Announcement.formatChar(colorizedMessage,
                                                             announcement.getColorizedHeader(),
                                                             announcement.getColorizedFooter());
                this.broadcast(config.getColor() + dynamicChar + colorizedMessage);
            }
            this.broadcast(config.getColor() + announcement.getColorizedFooter());
            setCount(count + 1);
            if (getCount() >= config.getAnnouncements().size()) {
                setCount(0);
            }
        }
    }

    private void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
