package net.colonymc.colonyspigotapi.api.inventory.buttons;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.inventory.PageInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class PageElement {

    protected Object id;
    protected ItemStack itemStack;
    protected int page;
    protected int index;
    protected PageInventory colonyInventory;
    protected final int refreshInterval;
    protected BukkitTask refresh;

    public PageElement(int refreshInterval, Object id) {
        this.refreshInterval = refreshInterval;
        this.id = id;
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

    public int getPage(){
        return page;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }

    public Object getId(){
        return id;
    }

    public PageInventory getInventory(){
        return colonyInventory;
    }

    public void remove(){
        refresh.cancel();
        colonyInventory.getInventory().setItem(index, null);
        colonyInventory = null;
    }

    public void setInventory(PageInventory inventory){
        this.colonyInventory = inventory;
        itemStack = colonyInventory.itemConstruct(id);
        this.getInventory().getInventory().setItem(index, itemStack);
        startRefreshing();
    }

    public void setIndexAndPage(int index, int page){
        this.index = index;
        this.page = page;
    }

    protected void startRefreshing(){
        refresh = new BukkitRunnable() {
            @Override
            public void run() {
                itemStack = colonyInventory.itemConstruct(id);
                PageElement.this.getInventory().getInventory().setItem(index, itemStack);
            }
        }.runTaskTimer(Main.getInstance(), 0, refreshInterval);
    }

}
