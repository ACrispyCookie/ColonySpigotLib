package net.colonymc.colonyspigotapi.api.inventory.buttons;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class DatabaseButton extends Button {

    public abstract void onClick(Player p, ClickType type);
    protected abstract ItemStack constructItem();

    public DatabaseButton(int index, int refreshInterval){
        super(index, refreshInterval);
    }

    @Override
    public ItemStack construct() {
        startFinalConstruction();
        return new ItemStackBuilder(Material.STAINED_GLASS_PANE).durability((short) 15).name("&cLoading...").build();
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

    protected void startFinalConstruction(){
        Thread thread = new Thread(() -> {
            ItemStack finalItem = constructItem();
            if(finalItem == null){
                finalItem = new ItemStackBuilder(Material.BARRIER).name("&cDatabase error! Please reload the menu!").build();
            }
            this.itemStack = finalItem.clone();
            this.colonyInventory.getInventory().setItem(index, this.itemStack);
        });
        thread.start();
    }


}
