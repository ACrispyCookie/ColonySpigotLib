package net.colonymc.colonyspigotapi.api.inventory;

import net.colonymc.colonyspigotapi.api.inventory.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;

public class ColonyInventory implements InventoryHolder {

    protected Player p;
    protected Inventory inventory;
    protected String title;
    protected int size;
    protected  ArrayList<Button> buttonArrayList = new ArrayList<>();

    public ColonyInventory(Player p, String title, int size){
        this.p = p;
        this.size = size;
        this.title = title;
    }

    public ColonyInventory construct(){
        initializeButtons();
        p.openInventory(inventory);
        return this;
    }

    protected void initializeButtons(){
        inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));
        for(Button b : buttonArrayList){
            b.setInventory(this);
        }
    }

    public Player getPlayer(){
        return p;
    }

    public String getTitle(){
        return title;
    }

    public int getSize(){
        return size;
    }

    public ArrayList<Button> getButtons(){
        return buttonArrayList;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public ColonyInventory addButton(Button button){
        buttonArrayList.add(button);
        return this;
    }

    protected Button getByIndex(int index) {
        for(Button b : buttonArrayList){
            if(b.getIndex() == index){
                return b;
            }
        }
        return null;
    }

}
