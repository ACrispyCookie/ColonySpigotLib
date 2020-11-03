package net.colonymc.colonyspigotapi.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.colonymc.colonyspigotapi.player.Particle;
import net.colonymc.colonyspigotapi.utils.VectorUtils;

public class ParticleShape {
	
	final Player p;
	static final ArrayList<ParticleShape> classes = new ArrayList<>();
	
	public ParticleShape(Player p) {
		this.p = p;
		classes.add(this);
	}
	
	public void playImage(Image img, Location l, int keepFor) {
		BufferedImage image = img.getImg();
		Location loc = l.clone();
		int maximumX = image.getWidth();
		int maximumY = image.getHeight();
        Vector toCenter = VectorUtils.rotateVector(new Vector(0,maximumY * 0.1,-maximumX/2 * 0.1), l.getYaw(), l.getPitch());
        loc.add(toCenter);
		for(int y = 0; y < maximumY; y++) {
			double toSubtract = 0;
			for(int x = 0; x < maximumX; x++) {
				int clr = image.getRGB(x, y);
				int a = (clr>>24)&0xff;
				int r = (clr>>16)&0xff;
				int g = (clr>>8)&0xff;
				int b = clr&0xff;
	        	if(a != 0) {
					Particle par = new Particle(Effect.COLOURED_DUST, 0, loc.clone());
					par.setRgb(r, g, b);
					par.play(p, keepFor);
	        	}
				toSubtract += 0.1;
	            Vector vector = VectorUtils.rotateVector(new Vector(0,0,0.1), l.getYaw(), l.getPitch());
	            loc.add(vector);
			}
            Vector vector = VectorUtils.rotateVector(new Vector(0,-0.1,-toSubtract), l.getYaw(), l.getPitch());
            loc.add(vector);
		}
	}
	
	public void playHeart(Effect eff, Location l, double size, int keepFor, float r, float g, float b) {
		for(double i = 0; i < size * 20; i+=0.1) {
			Location loc = l.clone();
			double x = 3.2 * size * Math.pow(Math.sin(i), 3);
			double y = 2.6 * size * Math.cos(i) - size * Math.cos(2 * i) - 0.4 * size * Math.cos(3 * i) - 0.2 * size * Math.cos(4 * i);
            Vector vector = VectorUtils.rotateVector(new Vector(0,y,x), l.getYaw(), l.getPitch());
            loc.add(vector);
			Particle par = new Particle(eff, 0, loc);
			if(eff == Effect.COLOURED_DUST) {
				par.setRgb(r, g, b);
			}
			par.play(p, keepFor);
		}
	}
	
	public void playInfinite(Effect eff, Location l, int size, int keepFor, float r, float g, float b) {
        double amount = size * 60;
        double inc = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {
    		Location loc = l.clone();
            double t = i * inc;
            double sin = Math.sin(t);
            double z = (size * Math.sqrt(2) * Math.cos(t)) / (Math.pow(sin, 2) + 1);
            double y = (size * Math.sqrt(2) * Math.cos(t) * Math.sin(t)) / (Math.pow(sin, 2) + 1);
            Vector vector = VectorUtils.rotateVector(new Vector(0,y,z), l.getYaw(), l.getPitch());
            loc.add(vector);
			Particle par = new Particle(eff, 0, loc);
			if(eff == Effect.COLOURED_DUST) {
				par.setRgb(r, g, b);
			}
			par.play(p, keepFor);
        }
	}
	
	public static ParticleShape getByPlayer(Player p) {
		for(ParticleShape o : classes) {
			if(o.p.equals(p)) {
				return o;
			}
		}
		return null;
	}

}
