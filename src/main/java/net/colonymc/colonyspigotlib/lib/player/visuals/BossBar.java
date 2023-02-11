package net.colonymc.colonyspigotlib.lib.player.visuals;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.colonymc.colonyspigotlib.Main;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class BossBar {
	
	private String text;
	private List<String> animatedText;
	private final Player p;
	private EntityWither wither;
	private BukkitTask refresh;
	private int ticksToRefresh;
	static final ArrayList<BossBar> bars = new ArrayList<>();
	
	public BossBar(String text, Player p) {
		this.text = text;
		this.p = p;
		bars.add(this);
	}
	
	public BossBar(List<String> text, Player p, int ticksToRefresh) {
		this.animatedText = text;
		this.ticksToRefresh = ticksToRefresh;
		this.p = p;
		bars.add(this);
	}
	
	public void sendPlayer() {
		spawnWither();
		if(animatedText != null) {
			refresh = new BukkitRunnable() {
				int i = 0;
				@Override
				public void run() {
					setText(animatedText.get(i));
					if(i + 1 == animatedText.size()) {
						i = 0;
					}
					else {
						i++;
					}
				}
			}.runTaskTimer(Main.getInstance(), ticksToRefresh, ticksToRefresh);
		}
		else if(text != null) {
			refresh = new BukkitRunnable() {
				@Override
				public void run() {
					despawnWither();
					spawnWither();
				}
			}.runTaskTimer(Main.getInstance(), 0, 10);
		}
	}
	
	public void sendPlayer(int ticks) {
		sendPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				stopShowing();
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), ticks);
	}
	
	public void setText(String text) {
		this.text = text;
		changeName();
	}
	
	public void stopShowing() {
		refresh.cancel();
		despawnWither();
		bars.remove(this);
	}
	
	private void spawnWither() {
		Vector d = p.getLocation().getDirection();
		if(wither == null) {
			wither = new EntityWither(((CraftWorld) p.getWorld()).getHandle());
			wither.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
			wither.setInvisible(true);
			wither.setCustomNameVisible(false);
			wither.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
		}
		else {
			wither.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
		}
		wither.teleportTo(p.getLocation().add(d.multiply(20)), false);
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void despawnWither() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void changeName() {
		despawnWither();
		wither.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
		spawnWither();
	}

}
