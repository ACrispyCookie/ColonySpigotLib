package net.colonymc.colonyspigotapi.api.inventory;

import net.colonymc.colonyspigotapi.api.inventory.buttons.Button;
import net.colonymc.colonyspigotapi.api.inventory.buttons.PageElement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class ColonyInventoryListener implements Listener {

    @EventHandler
    public void onButtonClick(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof ColonyInventory && e.getWhoClicked() instanceof Player){
            e.setCancelled(true);
            if(e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.PLAYER){
                ColonyInventory colonyInventory = (ColonyInventory) e.getInventory().getHolder();
                if(colonyInventory.getByIndex(e.getSlot()) != null){
                    colonyInventory.getByIndex(e.getSlot()).onClick((Player) e.getWhoClicked(), e.getClick());
                }
                else if(colonyInventory instanceof PageInventory && ((PageInventory) colonyInventory).getPageElementByIndex(e.getSlot()) != null){
                    PageElement element = ((PageInventory) colonyInventory).getPageElementByIndex(e.getSlot());
                    ((PageInventory) colonyInventory).onClick((Player) e.getWhoClicked(), e.getClick(), element.getId());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ColonyInventory){
            ColonyInventory colonyInventory = (ColonyInventory) e.getInventory().getHolder();
            for(Button button : colonyInventory.getButtons()){
                if(button.getRefreshInterval() > 0){
                    button.getRefresh().cancel();
                }
            }
            if (colonyInventory instanceof PageInventory) {
                for(PageElement element : ((PageInventory) colonyInventory).getElements()){
                    if(element.getRefreshInterval() > 0){
                        element.getRefresh().cancel();
                    }
                }
                PageInventory pageInventory = (PageInventory) colonyInventory;
                pageInventory.getElementRefresh().cancel();
            }
        }
    }


}
