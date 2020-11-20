package net.colonymc.colonyspigotapi.api.inventory.buttons;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.inventory.PageInventory;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class DatabasePageElement extends PageElement {

    public DatabasePageElement(int refreshInterval, Object id){
        super(refreshInterval, id);
    }

    @Override
    public void setInventory(PageInventory inventory){
        this.colonyInventory = inventory;
        startFinalConstruction();
        itemStack =  new ItemStackBuilder(Material.STAINED_GLASS_PANE).durability((short) 15).name("&cLoading...").build();
        this.getInventory().getInventory().setItem(index, itemStack);
        startRefreshing();
    }

    public void startFinalConstruction(){
        Thread thread = new Thread(() -> {
            ItemStack finalItem = colonyInventory.itemConstruct(id);
            this.itemStack = finalItem.clone();
            this.colonyInventory.getInventory().setItem(index, this.itemStack);
        });
        thread.start();
    }

    @Override
    protected void startRefreshing(){
        refresh = new BukkitRunnable() {
            @Override
            public void run() {
                startFinalConstruction();
            }
        }.runTaskTimer(Main.getInstance(), 0, refreshInterval);
    }

}
