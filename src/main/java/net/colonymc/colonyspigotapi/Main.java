package net.colonymc.colonyspigotapi;

import java.io.File;
import java.io.IOException;

import net.colonymc.colonyspigotapi.image.BuiltMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.colonymc.colonyspigotapi.book.survey.BookActionCommand;
import net.colonymc.colonyspigotapi.image.ImageMap;
import net.colonymc.colonyspigotapi.image.ImageMapBuilder;
import net.colonymc.colonyspigotapi.player.ColonyPlayer;
import net.colonymc.colonyspigotapi.player.PublicHologram;
import net.colonymc.colonyapi.database.MainDatabase;

public class Main extends JavaPlugin {
	
	static Main instance;
	final File names = new File(this.getDataFolder(), "names.yml");
	FileConfiguration namesConfig;
	boolean started = false;
	
	public void onEnable() {
		instance = this;
		if(MainDatabase.isConnected()) {
			this.saveDefaultConfig();
			setupConfigFiles();
			setupListeners();
			setupCommands();
			new AutomaticRestart();
			started = true;
			System.out.println(" » ColonySpigotAPI has been enabled successfully!");
		}
		else {
			System.out.println(" » ColonySpigotAPI couldn't connect to the main database!");
		}
	}

	public void onDisable() {
		if(started) {
			destroyHolograms();
		}
		System.out.println(" » ColonySpigotAPI has been disabled successfully!");
	}

	private void setupListeners() {
		Bukkit.getPluginManager().registerEvents(new ImageMap(), this);
		Bukkit.getPluginManager().registerEvents(new BuiltMap() {
			@Override
			public void onClick(Player p) {

			}
		}, this);
		Bukkit.getPluginManager().registerEvents(new ImageMapBuilder() {
			@Override
			public void onClick(Player p) {

			}
		}, this);
		Bukkit.getPluginManager().registerEvents(new ColonyPlayer(), this);
		Bukkit.getPluginManager().registerEvents(new PublicHologram(), this);
	}
	
	private void setupCommands() {
		getCommand("bookaction").setExecutor(new BookActionCommand());
	}
	
	private void destroyHolograms() {
		int size = PublicHologram.holos.size();
		for(int i = 0; i < size; i++) {
			PublicHologram p = PublicHologram.holos.get(0);
			p.destroy();
		}
		
	}
	
	private void setupConfigFiles() {
		if(!names.exists()) {
			names.getParentFile().mkdirs();
			saveResource("names.yml", false);
		}
		namesConfig = new YamlConfiguration();
		try {
			namesConfig.load(names);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public FileConfiguration getNamesConfig() {
		return namesConfig;
	}

}
