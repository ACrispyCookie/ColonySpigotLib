package net.colonymc.colonyspigotapi.api.player.visuals;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.player.ColonyPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public abstract class NameTag {
	
	private ColonyPlayer p;
	private String prefix = "";
	private String suffix = "";
	private	EntityArmorStand as;
	private int updateInterval;
	private BukkitTask updateTask;
	static ArrayList<NameTag> tags = new ArrayList<>();
	protected abstract String updatePrefix(ColonyPlayer p);
	protected abstract String updateSuffix(ColonyPlayer p);
	
	public NameTag(ColonyPlayer p, int updateInterval) {
		if(getByPlayer(p.getBukkitPlayer()) == null) {
			this.p = p;
			this.updateInterval = updateInterval;
			tags.add(this);
		}
	}
	
	private void prefix(String s) {
		String prefix = ChatColor.translateAlternateColorCodes('&', s);
		if(s.length() > 16) {
			prefix = s.substring(0, 16);
		}
		this.prefix = prefix;
	}

	private void suffix(String s) {
		String suffix = ChatColor.translateAlternateColorCodes('&', s);
		if(s.length() > 16) {
			suffix = s.substring(0, 16);
		}
		this.suffix = suffix;
	}
	
	public void send() {
		updateTask = new BukkitRunnable() {
			@Override
			public void run(){
				prefix(updatePrefix(p));
				suffix(updateSuffix(p));
				for(Player online : Bukkit.getOnlinePlayers()){
					if(!online.equals(p.getBukkitPlayer())){
						Scoreboard sb = online.getScoreboard();
						Team t = sb.getTeam(p.getBukkitPlayer().getName());
						if(t == null){
							t = sb.registerNewTeam(p.getBukkitPlayer().getName());
							t.setNameTagVisibility(NameTagVisibility.NEVER);
							t.addPlayer(p.getBukkitPlayer());
							spawn(online);
						}
						update(online);
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, updateInterval);
	}

	public void stop(){
		updateTask.cancel();
		for(Player online : Bukkit.getOnlinePlayers()){
			Scoreboard sb = online.getScoreboard();
			Team t = sb.getTeam(p.getBukkitPlayer().getName());
			if(t != null){
				t.removePlayer(p.getBukkitPlayer());
				t.unregister();
			}
			PacketPlayOutEntityDestroy spawn = new PacketPlayOutEntityDestroy(as.getId());
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(spawn);
		}
	}

	private void spawn(Player p){
		as = new EntityArmorStand(((CraftWorld) this.p.getBukkitPlayer().getWorld()).getHandle(), this.p.getBukkitPlayer().getLocation().getX(), this.p.getBukkitPlayer().getLocation().getY() + 0.2, this.p.getBukkitPlayer().getLocation().getZ());
		NBTTagCompound compoundTag = new NBTTagCompound();
		as.c(compoundTag);
		compoundTag.setBoolean("Marker", true);
		as.f(compoundTag);
		as.setInvisible(true);
		as.setGravity(false);
		as.setCustomNameVisible(true);
		as.setCustomName(prefix + ChatColor.RESET + " " + this.p.getBukkitPlayer().getName() + (suffix.equals("") ? "" : " " + suffix));
		as.setSmall(true);
		PacketPlayOutSpawnEntity spawn = new PacketPlayOutSpawnEntity(as, 78);
		PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata);
	}

	private void update(Player p){
		as.setCustomName(prefix + ChatColor.RESET + " " + this.p.getBukkitPlayer().getName() + (suffix.equals("") ? "" : " " + suffix));
		as.setLocation(this.p.getBukkitPlayer().getEyeLocation().getX(), this.p.getBukkitPlayer().getEyeLocation().getY() + 0.2, this.p.getBukkitPlayer().getEyeLocation().getZ(), 0, 0);
		PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
		PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(as);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(teleport);
	}

	public String getPrefix(){
		return prefix;
	}

	public String getSuffix(){
		return suffix;
	}

	public Player getPlayer(){
		return p.getBukkitPlayer();
	}

	public static NameTag getByPlayer(Player p){
		for(NameTag tag : tags){
			if(tag.p.getBukkitPlayer().equals(p)){
				return tag;
			}
		}
		return null;
	}

}
