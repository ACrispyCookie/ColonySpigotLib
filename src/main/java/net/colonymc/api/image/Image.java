package net.colonymc.api.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Image {
	
	final String name;
	final BufferedImage img;
	static final ArrayList<Image> imgs = new ArrayList<>();
	
	public Image(String name, BufferedImage img) {
		this.name = name;
		this.img = img;
		imgs.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public static Image getByName(String name) {
		for(Image i : imgs) {
			if(i.getName().equalsIgnoreCase(name)) {
				return i;
			}
		}
		return null;
	}
	
	public static ArrayList<Image> getImgs() {
		return imgs;
	}
	
	public static void clearList() {
		imgs.clear();
	}


}
