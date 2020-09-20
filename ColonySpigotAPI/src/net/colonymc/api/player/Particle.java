package net.colonymc.api.player;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.colonymc.api.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Particle {
	
	EnumParticle effect;
	int data = 0;
	Location loc;
	BukkitRunnable runnable;
	
	public Particle(EnumParticle effect, int data, Location loc) {
		this.effect = effect;
		this.data = data;
		this.loc = loc;
	}
	
	public void play(Player player) {
		if(runnable != null) {
			runnable.cancel();
		}
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(effect, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ() , 0, 0, 0, 0, data, 0);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		};
		runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 1L);
	}
	
	public void play(Collection<? extends Player> collection) {
		if(runnable != null) {
			runnable.cancel();
		}
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : collection) {
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(effect, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ() , 0, 0, 0, 0, data, 0);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				}
			}
		};
		runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 1L);
	}
	
	public void play(Player player, int duration) {
		if(runnable != null) {
			runnable.cancel();
		}
		runnable = new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if(duration > 0) {
					if(i != duration) {
						PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(effect, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ() , 0, 0, 0, 0, data, 0);
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					}
					else {
						cancel();
					}
					i++;
				}
				else {
					cancel();
				}
			}
		};
		runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 1L);
	}
	
	public void play(Collection<? extends Player> collection, int duration) {
		if(runnable != null) {
			runnable.cancel();
		}
		runnable = new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if(duration > 0) {
					if(i != duration) {
						PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(effect, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ() , 0, 0, 0, 0, data, 0);
						for(Player player : collection) {
							((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
						}
					}
					else {
						cancel();
					}
					i++;
				}
				else {
					cancel();
				}
			}
		};
		runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 1L);
	}
	
	public void stop() {
		runnable.cancel();
	}
	
	public void setEffect(EnumParticle eff, int data) {
		effect = eff;
		this.data = data;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getLocation() {
		return loc.clone();
	}

}
