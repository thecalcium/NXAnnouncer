package com.projectbarks.nxannouncer.config;

import com.projectbarks.nxannouncer.NXAnnouncer;
import com.projectbarks.nxannouncer.announcer.Announcement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Brandon Barker
 */
public class Config {

    private double interval;
    private int minPlayers;
    private String pingSound;
    private double pingVolume;
    private double pingPitch;
    private String pluginName;
    private List<Announcement> announcements;
    private FileConfiguration config;
    private File file;
    private String color;

    public Config(NXAnnouncer main) {
        this.config = new YamlConfiguration();
        this.announcements = new ArrayList<Announcement>();
        this.file = new File(main.getDataFolder(), "config.yml");
        this.interval = 5;
        this.minPlayers = 5;
        this.pingSound = "none";
        this.pingVolume = 1.0;
        this.pingPitch = 1.0;
        this.pluginName = "[" + main.getName() + "] ";
    }

    public void load() {
        try {
            this.config.load(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        announcements.clear();
        announcements.addAll(getAnnouncements(config.getConfigurationSection("Announcements")));
        interval = config.getInt("Interval.Minutes", 5) * 60 + config.getInt("Interval.Seconds", 0);
        minPlayers = config.getInt("Min Players");
        pingSound = config.getString("Ping Sound");
        pingVolume = config.getDouble("Ping Volume");
        pingPitch = config.getDouble("Ping Pitch");
        color = Announcement.colorize(config.getString("Default Message Color", "&7"));

        NXAnnouncer.getLOG().log(Level.INFO, "{0}Has loaded {1} message(s).", new Object[]{pluginName, announcements.size()});
    }

    public void reload() {
        this.config = new YamlConfiguration();
        this.setup();
        this.load();
    }

    public void setup() {

        if (!file.exists()) {
            NXAnnouncer.getLOG().log(Level.INFO, "{0}Generating config.yml", pluginName);
            config.set("Interval.Minutes", 5);
            config.set("Interval.Seconds", 0);
            config.set("Min Players", 5);
            config.set("Ping Sound", "none");
            config.set("Ping Volume", 1.0);
            config.set("Ping Pitch", 1.0);
            config.set("Default Message Color", "&7");
            config.set("Announcements.AnnouncementGroup1.header", "&1-----------[BarksSimple&bAnnouncer]-----------");
            config.set("Announcements.AnnouncementGroup1.footer", "&1----------------------&b----------------------");
            config.set("Announcements.AnnouncementGroup1.messages", Arrays.asList("&oWelcome to Bukkit", "&lExample of an announce%nWith another line"));
            try {
                config.save(file);
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<Announcement> getAnnouncements(ConfigurationSection configSection) {
        List<Announcement> a = new ArrayList<Announcement>();
        for (String key : configSection.getKeys(false)) {
            String footer = config.getString("Announcements." + key + ".footer", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            String header = config.getString("Announcements." + key + ".header", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for (String message : config.getStringList("Announcements." + key + ".messages")) {
                a.add(new Announcement(message, footer, header));
            }
        }
        return a;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getPingSound() {
        return pingSound;
    }

    public double getPingVolume() {
        return pingVolume;
    }

    public double getPingPitch() {
        return pingPitch;
    }

    public double getInterval() {
        return interval;
    }

    public String getPluginName() {
        return this.pluginName;
    }

    public String getColor() {
        return color;
    }
}
