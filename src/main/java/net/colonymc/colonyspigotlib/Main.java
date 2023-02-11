package net.colonymc.colonyspigotlib;

import net.colonymc.colonyapi.database.MainDatabase;
import net.colonymc.colonyspigotlib.lib.AutomaticRestart;
import net.colonymc.colonyspigotlib.lib.holograms.PublicHologram;
import net.colonymc.colonyspigotlib.lib.image.BuiltMap;
import net.colonymc.colonyspigotlib.lib.image.Image;
import net.colonymc.colonyspigotlib.lib.image.ImageMap;
import net.colonymc.colonyspigotlib.lib.image.ImageMapBuilder;
import net.colonymc.colonyspigotlib.lib.inventory.ColonyInventoryListener;
import net.colonymc.colonyspigotlib.lib.player.ColonyPlayer;
import net.colonymc.colonyspigotlib.lib.survey.BookActionCommand;
import net.colonymc.colonyspigotlib.commands.TestCommand;
import net.colonymc.colonyspigotlib.commands.hologram.HologramCommand;
import net.colonymc.colonyspigotlib.commands.images.MapImgCommand;
import net.colonymc.colonyspigotlib.commands.images.ParticleImgCommand;
import net.colonymc.colonyspigotlib.commands.images.ReloadImagesCommand;
import net.colonymc.colonyspigotlib.commands.items.EnchantCommand;
import net.colonymc.colonyspigotlib.commands.items.GiveCommand;
import net.colonymc.colonyspigotlib.commands.items.LoreCommand;
import net.colonymc.colonyspigotlib.commands.items.RenameCommand;
import net.colonymc.colonyspigotlib.commands.player.FlyCommand;
import net.colonymc.colonyspigotlib.commands.player.GamemodeCommand;
import net.colonymc.colonyspigotlib.commands.player.HealCommand;
import net.colonymc.colonyspigotlib.commands.player.SpeedCommand;
import net.colonymc.colonyspigotlib.commands.warp.RemoveWarpCommand;
import net.colonymc.colonyspigotlib.commands.warp.SetWarpCommand;
import net.colonymc.colonyspigotlib.commands.warp.WarpCommand;
import net.colonymc.colonyspigotlib.commands.warp.WarpsCommand;
import net.colonymc.colonyspigotlib.commands.world.TimeCommand;
import net.colonymc.colonyspigotlib.other.Warp;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
	
	static Main instance;
	final File names = new File(this.getDataFolder(), "names.yml");
	private LuckPerms luckPerms;
	private final File scoreboards = new File(this.getDataFolder(), "scoreboards.yml");
	private final File warp = new File(this.getDataFolder(), "warps.yml");
	private final File holo = new File(this.getDataFolder(), "holos.yml");
	private final File maps = new File(this.getDataFolder(), "mapimgs.yml");
	private final File imgs = new File(this.getDataFolder(), "/imgs");
	private final FileConfiguration holoConfig = new YamlConfiguration();
	private final FileConfiguration warpConfig = new YamlConfiguration();
	private final FileConfiguration mapsConfig = new YamlConfiguration();
	private final FileConfiguration namesConfig = new YamlConfiguration();
	private final FileConfiguration scoreboardConfig = new YamlConfiguration();
	private final ArrayList<PublicHologram> publicHolos = new ArrayList<>();
	boolean started = false;
	
	public void onEnable() {
		instance = this;
		if(MainDatabase.isConnected()) {
			this.saveDefaultConfig();
			if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				new Placeholders(this).register();
			}
			luckPerms = LuckPermsProvider.get();
			setupConfigs();
			setupListeners();
			setupCommands();
			setupImages();
			setupMaps();
			setupWarps();
			setupPublicHolograms();
			setupPlayers();
			new AutomaticRestart();
			started = true;
			System.out.println(" » ColonySpigotLib has been enabled successfully!");
		}
		else {
			System.out.println(" » ColonySpigotLib couldn't connect to the main database!");
		}
	}

	public void onDisable() {
		if(started) {
			destroyHolograms();
		}
		System.out.println(" » ColonySpigotLib has been disabled successfully!");
	}

	private void destroyHolograms() {
		int size = PublicHologram.holos.size();
		for(int i = 0; i < size; i++) {
			PublicHologram p = PublicHologram.holos.get(0);
			p.destroy();
		}
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
		Bukkit.getPluginManager().registerEvents(new ColonyInventoryListener(), this);
	}
	
	private void setupCommands() {
		this.getCommand("bookaction").setExecutor(new BookActionCommand());
		this.getCommand("testcmd").setExecutor(new TestCommand());
		this.getCommand("enchantment").setTabCompleter(new EnchantCommand());
		this.getCommand("mapimg").setTabCompleter(new MapImgCommand());
		this.getCommand("reloadimgs").setExecutor(new ReloadImagesCommand());
		this.getCommand("particleimg").setExecutor(new ParticleImgCommand());
		this.getCommand("mapimg").setExecutor(new MapImgCommand());
		this.getCommand("enchantment").setExecutor(new EnchantCommand());
		this.getCommand("lore").setExecutor(new LoreCommand());
		this.getCommand("rename").setExecutor(new RenameCommand());
		this.getCommand("fly").setExecutor(new FlyCommand());
		this.getCommand("gamemode").setExecutor(new GamemodeCommand());
		this.getCommand("gmc").setExecutor(new GamemodeCommand());
		this.getCommand("gms").setExecutor(new GamemodeCommand());
		this.getCommand("gma").setExecutor(new GamemodeCommand());
		this.getCommand("gmsp").setExecutor(new GamemodeCommand());
		this.getCommand("give").setExecutor(new GiveCommand());
		this.getCommand("heal").setExecutor(new HealCommand());
		this.getCommand("feed").setExecutor(new HealCommand());
		this.getCommand("hologram").setExecutor(new HologramCommand());
		this.getCommand("removewarp").setExecutor(new RemoveWarpCommand());
		this.getCommand("setwarp").setExecutor(new SetWarpCommand());
		this.getCommand("warp").setExecutor(new WarpCommand());
		this.getCommand("warps").setExecutor(new WarpsCommand());
		this.getCommand("speed").setExecutor(new SpeedCommand());
		this.getCommand("time").setExecutor(new TimeCommand());
	}

	private void setupConfigs() {
		if(!names.exists()) {
			names.getParentFile().mkdirs();
			saveResource("names.yml", false);
		}
		if(!scoreboards.exists()) {
			scoreboards.getParentFile().mkdirs();
			saveResource("scoreboards.yml", false);
		}
		if(!holo.exists()) {
			holo.getParentFile().mkdirs();
			saveResource("holos.yml", false);
		}
		if(!warp.exists()) {
			saveResource("warps.yml", false);
		}
		if(!maps.exists()) {
			saveResource("mapimgs.yml", false);
		}
		if(!new File(this.getDataFolder(), "imgs/pfp.png").exists()) {
			saveResource("imgs/pfp.png", false);
		}
		try {
			holoConfig.load(holo);
			warpConfig.load(warp);
			mapsConfig.load(maps);
			namesConfig.load(names);
			scoreboardConfig.load(scoreboards);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void setupImages() {
		if(imgs != null) {
			Image.clearList();
			for(File f : imgs.listFiles()) {
				try {
					BufferedImage img = ImageIO.read(f);
					if(img != null) {
						new Image(f.getName(), img);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setupMaps() {
		ConfigurationSection sec = mapsConfig.getConfigurationSection("maps");
		if(sec != null) {
			for(String s : sec.getKeys(false)) {
				Location pos1 = new Location(Bukkit.getWorld(sec.getString(s + ".world")), sec.getInt(s + ".pos1.x"), sec.getInt(s + ".pos1.y"), sec.getInt(s + ".pos1.z"));
				Location pos2 = new Location(Bukkit.getWorld(sec.getString(s + ".world")), sec.getInt(s + ".pos2.x"), sec.getInt(s + ".pos2.y"), sec.getInt(s + ".pos2.z"));
				Image img = Image.getByName(sec.getString(s + ".image"));
				ImageMapBuilder.Side side = ImageMapBuilder.Side.valueOf(sec.getString(s + ".side"));
				String command = sec.getString(s + ".command.value");
				ImageMapBuilder.COMMAND_TYPE type = null;
				if(command != null){
					type = ImageMapBuilder.COMMAND_TYPE.valueOf(sec.getString(s + ".command.type"));
				}
				ImageMapBuilder b = new ImageMapBuilder(pos1, pos2, img, s, command, type, side) {
					@Override
					public void onClick(Player p) {
						if(command != null){
							if(this.getCommandType() == COMMAND_TYPE.CONSOLE){
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%name%", p.getName()));

							}
							else if(this.getCommandType() == COMMAND_TYPE.PLAYER){
								Bukkit.dispatchCommand(p, command.replaceAll("%name%", p.getName()));
							}
						}
					}
				};
				b.build();
			}
		}
	}

	private void setupWarps() {
		ConfigurationSection sec = warpConfig.getConfigurationSection("warps");
		if(sec != null) {
			for(String s : sec.getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(sec.getString(s + ".world")), sec.getDouble(s + ".posX"), sec.getDouble(s + ".posY"), sec.getDouble(s + ".posZ"), sec.getInt(s + ".yaw"), sec.getInt(s + ".pitch"));
				boolean isPublic = sec.getBoolean(s + ".public");
				new Warp(s, loc, isPublic, false);
			}
		}
	}

	private void setupPublicHolograms() {
		ConfigurationSection sec = holoConfig.getConfigurationSection("holograms");
		if(sec != null) {
			for(String s : sec.getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(sec.getString(s + ".world")), sec.getDouble(s + ".posX"), sec.getDouble(s + ".posY"), sec.getDouble(s + ".posZ"));
				StringBuilder lines = new StringBuilder();
				for(String ss : sec.getStringList(s + ".lines")) {
					lines.append(ss).append("\n");
				}
				PublicHologram holo = new PublicHologram(lines.toString(), loc, s);
				holo.show();
				publicHolos.add(holo);
			}
		}
	}

	private void setupPlayers(){
		for(Player p : Bukkit.getOnlinePlayers()){
			new ColonyPlayer(p);
		}
	}

	public void addWarpToConfig(Warp warp) {
		warpConfig.createSection("warps." + warp.getName());
		warpConfig.set("warps." + warp.getName() + ".world", warp.getLocation().getWorld().getName());
		warpConfig.set("warps." + warp.getName() + ".posX", warp.getLocation().getX());
		warpConfig.set("warps." + warp.getName() + ".posY", warp.getLocation().getY());
		warpConfig.set("warps." + warp.getName() + ".posZ", warp.getLocation().getZ());
		warpConfig.set("warps." + warp.getName() + ".yaw", warp.getLocation().getYaw());
		warpConfig.set("warps." + warp.getName() + ".pitch", warp.getLocation().getPitch());
		warpConfig.set("warps." + warp.getName() + ".public", warp.isPublic());
		try {
			warpConfig.save(this.warp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeWarpFromConfig(Warp warp) {
		warpConfig.set("warps." + warp.getName(), null);
		try {
			warpConfig.save(this.warp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateHologramOnConfig(PublicHologram holo) {
		if(holoConfig.isConfigurationSection("holograms." + holo.getName())) {
			holoConfig.set("holograms." + holo.getName() + ".posX", holo.getLocation().getX());
			holoConfig.set("holograms." + holo.getName() + ".posY", holo.getLocation().getY());
			holoConfig.set("holograms." + holo.getName() + ".posZ", holo.getLocation().getZ());
			List<String> list = new ArrayList<>(holo.getLines());
			holoConfig.set("holograms." + holo.getName() + ".lines", list);
		}
		else {
			holoConfig.createSection("holograms." + holo.getName());
			holoConfig.set("holograms." + holo.getName() + ".world", holo.getLocation().getWorld().getName());
			holoConfig.set("holograms." + holo.getName() + ".posX", holo.getLocation().getX());
			holoConfig.set("holograms." + holo.getName() + ".posY", holo.getLocation().getY());
			holoConfig.set("holograms." + holo.getName() + ".posZ", holo.getLocation().getZ());
			List<String> list = new ArrayList<>(holo.getLines());
			holoConfig.set("holograms." + holo.getName() + ".lines", list);
		}
		try {
			holoConfig.save(this.holo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		publicHolos.add(holo);
	}

	public void removeHologramFromConfig(PublicHologram holo) {
		holoConfig.set("holograms." + holo.getName(), null);
		try {
			holoConfig.save(this.holo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		publicHolos.remove(holo);
	}

	public void addMapToConfig(BuiltMap map){
		mapsConfig.createSection("maps." + map.getName());
		mapsConfig.set("maps." + map.getName() + ".world", map.getPos1().getWorld().getName());
		mapsConfig.set("maps." + map.getName() + ".pos1.x", map.getPos1().getBlockX());
		mapsConfig.set("maps." + map.getName() + ".pos1.y", map.getPos1().getBlockY());
		mapsConfig.set("maps." + map.getName() + ".pos1.z", map.getPos1().getBlockZ());
		mapsConfig.set("maps." + map.getName() + ".pos2.x", map.getPos2().getBlockX());
		mapsConfig.set("maps." + map.getName() + ".pos2.y", map.getPos2().getBlockY());
		mapsConfig.set("maps." + map.getName() + ".pos2.z", map.getPos2().getBlockZ());
		if(map.getCommand() != null){
			mapsConfig.set("maps." + map.getName() + ".command.type", map.getType().name());
			mapsConfig.set("maps." + map.getName() + ".command.value", map.getCommand());
		}
		mapsConfig.set("maps." + map.getName() + ".side", map.getSide().name());
		mapsConfig.set("maps." + map.getName() + ".image", map.getImg().getName());
		try {
			mapsConfig.save(this.maps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeMapFromConfig(BuiltMap map){
		mapsConfig.set("maps." + map.getName(), null);
		try {
			mapsConfig.save(this.maps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}

	public File getImgs() {
		return imgs;
	}
	
	public FileConfiguration getNamesConfig() {
		return namesConfig;
	}

	public FileConfiguration getScoreboards() {
		return scoreboardConfig;
	}

	public FileConfiguration getHolos() {
		return holoConfig;
	}

	public FileConfiguration getWarps() {
		return warpConfig;
	}

	public LuckPerms getLuckPerms(){
		return luckPerms;
	}


}
