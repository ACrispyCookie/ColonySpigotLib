package net.colonymc.colonyspigotapi.player;

import java.util.Collection;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.colonymc.colonyspigotapi.Main;

public class Particle {
	
	Effect effect;
	float r = 0;
	float g = 0;
	float b = 0;
	int data = 0;
	Location loc;
	BukkitRunnable runnable;
	
	public Particle(Effect effect, int data, Location loc) {
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
				if(effect != Effect.COLOURED_DUST) {
					player.spigot().playEffect(loc, effect, 0, data, 0, 0, 0, 1, 1, 160);
				}
				else {
					player.spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 1, r, g, b, 1, 0, 160);
				}
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
					if(effect != Effect.COLOURED_DUST) {
						player.spigot().playEffect(loc, effect, 0, data, 0, 0, 0, 1, 1, 160);
					}
					else {
						player.spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 1, r, g, b, 1, 0, 160);
					}
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
						if(effect != Effect.COLOURED_DUST) {
							player.spigot().playEffect(loc, effect, 0, data, 0, 0, 0, 1, 1, 160);
						}
						else {
							player.spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 1, r, g, b, 1, 0, 160);
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
						if(effect != Effect.COLOURED_DUST) {
							for(Player player : collection) {
								player.spigot().playEffect(loc, effect, 0, data, 0, 0, 0, 1, 1, 160);
							}
						}
						else {
							for(Player player : collection) {
								player.spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 1, r, g, b, 1, 0, 160);
							}
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
	
	public void setEffect(Effect eff, int data) {
		effect = eff;
		this.data = data;
	}
	
	public void setRgb(float r, float g, float b) {
		this.r = r/255.0F - 1.0F;
		this.g = g/255.0F;
		this.b = b/255.0F;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getLocation() {
		return loc.clone();
	}

}
