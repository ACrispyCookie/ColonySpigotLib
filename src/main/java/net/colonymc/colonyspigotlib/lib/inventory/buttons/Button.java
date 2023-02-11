package net.colonymc.colonyspigotlib.lib.inventory.buttons;

import net.colonymc.colonyspigotlib.Main;
import net.colonymc.colonyspigotlib.lib.inventory.ColonyInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Button {

    protected ItemStack itemStack;
    protected final int index;
    protected ColonyInventory colonyInventory;
    protected final int refreshInterval;
    protected BukkitTask refresh;
    public abstract void onClick(Player p, ClickType type);
    public abstract ItemStack construct();

    public Button(int index, int refreshInterval){
        this.index = index;
        this.refreshInterval = refreshInterval;
    }

    public int getRefreshInterval(){
        return refreshInterval;
    }

    public BukkitTask getRefresh(){
        return refresh;
    }

    public int getIndex(){
        return index;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }

    public ColonyInventory getInventory(){
        return colonyInventory;
    }

    public void setInventory(ColonyInventory inventory){
        this.colonyInventory = inventory;
        itemStack = construct();
        this.getInventory().getInventory().setItem(index, itemStack);
        startRefreshing();
    }

    protected void startRefreshing(){
        refresh = new BukkitRunnable() {
            @Override
            public void run() {
                itemStack = construct();
                Button.this.getInventory().getInventory().setItem(index, itemStack);
            }
        }.runTaskTimer(Main.getInstance(), 0, refreshInterval);
    }
}
