package net.colonymc.colonyspigotapi.api.inventory;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.inventory.buttons.Button;
import net.colonymc.colonyspigotapi.api.inventory.buttons.DatabasePageElement;
import net.colonymc.colonyspigotapi.api.inventory.buttons.PageElement;
import net.colonymc.colonyspigotapi.api.itemstack.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public abstract class PageInventory extends ColonyInventory {

    protected Class<?> type;
    protected int refreshInterval;
    protected int startPos;
    protected int endPos;
    protected int previousPageSlot;
    protected int nextPageSlot;
    protected int page = 0;
    protected int totalPages;
    protected boolean usesDatabase;
    protected BukkitTask elementRefresh;
    protected ArrayList<PageElement> elements = new ArrayList<>();
    public abstract ArrayList<?> getList();
    public abstract void onClick(Player p, ClickType type, Object o);
    public abstract ItemStack itemConstruct(Object o);

    public PageInventory(Player p, String title, int size, Class<?> used, int refreshInterval, int start, int end, int previousPage, int nextPage, boolean usesDatabase) {
        super(p, title, size);
        this.type = used;
        this.refreshInterval = refreshInterval;
        this.startPos = start;
        this.endPos = end;
        this.previousPageSlot = previousPage;
        this.nextPageSlot = nextPage;
        this.usesDatabase = usesDatabase;
    }

    @Override
    public ColonyInventory construct(){
        openPage(0);
        return this;
    }

    private void openPage(int index) {
        p.closeInventory();
        checkForPageButtons();
        initializeButtons();
        initializeElements(index);
        p.openInventory(inventory);
        startElementChecking();
    }

    private void initializeElements(int index){
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(getList());
        for(int i = startPos; i <= endPos; i++){
            if(objects.size() > index * size +  (i - startPos)){
                Object o = objects.get(index * size + (i - startPos));
                PageElement e = usesDatabase ? new DatabasePageElement(refreshInterval, o) {} : new PageElement(refreshInterval, o) {};
                e.setIndexAndPage(i, index);
                e.setInventory(this);
                addElement(e);
            }
            else{
                return;
            }
        }
    }

    private void startElementChecking() {
        elementRefresh = new BukkitRunnable(){
            @Override
            public void run(){
                ArrayList<Object> objects = new ArrayList<>();
                objects.addAll(getList());
                ArrayList<PageElement> toRemove = new ArrayList<>();
                for(PageElement e : elements){
                    if(!objects.contains(e.getId())){
                        toRemove.add(e);
                    }
                    else{
                        objects.remove(e.getId());
                    }
                }
                for(PageElement e : toRemove){
                    elements.remove(e);
                    e.remove();
                }
                for(Object o : objects){
                    PageElement element = new PageElement(refreshInterval, o) {};
                    element.setIndexAndPage(findNextAvailable(page), page);
                    element.setInventory(PageInventory.this);
                    addElement(element);
                }
                for(PageElement element : elements){
                    if(!isInOptimalPlace(element)){
                        int oldIndex = element.getIndex();
                        element.setIndexAndPage(findNextAvailable(page), page);
                        inventory.setItem(element.getIndex(), new ItemStack(Material.AIR));
                        inventory.setItem(oldIndex, new ItemStack(Material.AIR));
                    }
                }
                p.updateInventory();
            }
        }.runTaskTimer(Main.getInstance(), 0, 5);
    }

    private boolean isInOptimalPlace(PageElement e){
        for(int i = 0; i < size; i++){
            boolean isEmpty = true;
            for(PageElement el : elements){
                if(el.getIndex() == i && el.getPage() == page){
                    isEmpty = false;
                    break;
                }
            }
            if(isEmpty && e.getIndex() > i){
                return false;
            }
        }
        return true;
    }

    private int findNextAvailable(int page){
        for(int i = 0; i < size; i++){
            boolean isEmpty = true;
            for(PageElement e : elements){
                if(e.getIndex() == i && e.getPage() == page){
                    isEmpty = false;
                    break;
                }
            }
            if(isEmpty){
                return i;
            }
        }
        return findNextAvailable(page + 1);
    }

    private void nextPage(){
        if(page + 1 < totalPages){
            page += 1;
            openPage(page);
        }
    }

    private void previousPage() {
        if (page - 1 >= 0) {
            page -= 1;
            openPage(page);
        }
    }

    private void checkForPageButtons(){
        if(page > 0){
            addButton(new Button(previousPageSlot, 0) {
                @Override
                public void onClick(Player p, ClickType type) {
                    previousPage();
                }

                @Override
                public ItemStack construct() {
                    return new ItemStackBuilder(Material.ARROW).name("&dPrevious page").build();
                }
            });
        }
        else{
            buttonArrayList.remove(getByIndex(previousPageSlot));
        }
        if(page < totalPages - 1){
            addButton(new Button(nextPageSlot, 0) {
                @Override
                public void onClick(Player p, ClickType type) {
                    nextPage();
                }

                @Override
                public ItemStack construct() {
                    return new ItemStackBuilder(Material.ARROW).name("&dNext page").build();
                }
            });
        }
        else{
            buttonArrayList.remove(getByIndex(nextPageSlot));
        }
    }

    public PageInventory addElement(PageElement element){
        elements.add(element);
        return this;
    }

    public int getPage(){
        return page;
    }

    public int getTotalPages(){
        return totalPages;
    }

    public ArrayList<PageElement> getElements(){
        return elements;
    }

    public int getRefreshInterval(){
        return refreshInterval;
    }

    public BukkitTask getElementRefresh(){
        return elementRefresh;
    }

    public PageElement getPageElementByIndex(int index){
        for(PageElement element : elements){
            if(element.getPage() == page && element.getIndex() == index){
                return element;
            }
        }
        return null;
    }
}
