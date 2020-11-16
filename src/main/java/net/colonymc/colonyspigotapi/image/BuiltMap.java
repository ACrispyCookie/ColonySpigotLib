package net.colonymc.colonyspigotapi.image;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public abstract class BuiltMap implements Listener {

    String name;
    String command;
    ImageMapBuilder.COMMAND_TYPE type;
    ImageMapBuilder.Side face;
    ArrayList<ImageMap> maps = new ArrayList<>();
    Location pos1;
    Location pos2;
    Image img;
    public abstract void onClick(Player p);
    static ArrayList<BuiltMap> builtMaps = new ArrayList<>();

    public BuiltMap(String name, Image img, ArrayList<ImageMap> maps, ImageMapBuilder.Side face, String command, ImageMapBuilder.COMMAND_TYPE type){
        this.name = name;
        this.command = command;
        this.type = type;
        this.face = face;
        this.img = img;
        this.maps = maps;
        this.pos1 = maps.get(0).getItemFrame().getLocation();
        this.pos2 = maps.get(maps.size() - 1).getItemFrame().getLocation();
        builtMaps.add(this);
    }

    public BuiltMap(){

    }

    public void remove(){
        builtMaps.remove(this);
        for(ImageMap imageMap : maps) {
            imageMap.remove();
        }
    }

    public String getName(){
        return name;
    }

    public Location getPos1(){
        return pos1;
    }

    public Location getPos2(){
        return pos2;
    }

    public Image getImg(){
        return img;
    }

    public ImageMapBuilder.Side getSide(){ return face; }

    public ArrayList<ImageMap> getMaps(){
        return maps;
    }

    public String getCommand() { return command; }

    public ImageMapBuilder.COMMAND_TYPE getType() {
         return type;
    }

    public static BuiltMap getByImageMap(ImageMap map){
        for(BuiltMap map1 : builtMaps){
            for(ImageMap imageMap : map1.getMaps()){
                if(map.equals(imageMap)){
                    return map1;
                }
            }
        }
        return null;
    }

    public static BuiltMap getByName(String name){
        for(BuiltMap map1 : builtMaps){
            if(map1.getName().equalsIgnoreCase(name)){
                return map1;
            }
        }
        return null;
    }

    public static ArrayList<BuiltMap> list(){
        return builtMaps;
    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ItemFrame){
            ItemFrame frame = (ItemFrame) e.getEntity();
            if(frame.getItem().getType() == Material.MAP){
                if(ImageMap.getById(Bukkit.getMap(frame.getItem().getDurability())) != null){
                    e.setCancelled(true);
                    if(e.getDamager() instanceof Player){
                        Player p = (Player) e.getDamager();
                        ImageMap map = ImageMap.getById(Bukkit.getMap(frame.getItem().getDurability()));
                        BuiltMap.getByImageMap(map).onClick(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof ItemFrame){
            ItemFrame itemFrame = (ItemFrame) e.getRightClicked();
            if(ImageMap.getByItemFrame(itemFrame) != null){
                e.setCancelled(true);
                BuiltMap.getByImageMap(ImageMap.getByItemFrame(itemFrame)).onClick(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onBreak(HangingBreakEvent e){
        if(e.getEntity() instanceof ItemFrame){
            ItemFrame frame = (ItemFrame) e.getEntity();
            if(frame.getItem().getType() == Material.MAP){
                if(ImageMap.getById(Bukkit.getMap(frame.getItem().getDurability())) != null){
                    e.setCancelled(true);
                }
            }
        }
    }
}
