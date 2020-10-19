package net.colonymc.api.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Image {
	
	String name;
	BufferedImage img;
	static ArrayList<Image> imgs = new ArrayList<Image>();
	
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
			if(i.getName().equals(name)) {
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
