package net.colonymc.colonyspigotlib.commands;

import net.colonymc.colonyapi.database.MainDatabase;
import net.colonymc.colonyspigotlib.lib.inventory.buttons.Button;
import net.colonymc.colonyspigotlib.lib.inventory.ColonyInventory;
import net.colonymc.colonyspigotlib.lib.inventory.PageInventory;
import net.colonymc.colonyspigotlib.lib.itemstack.ItemStackBuilder;
import net.colonymc.colonyspigotlib.lib.itemstack.SkullItemBuilder;
import net.colonymc.colonyspigotlib.lib.player.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("1")){
                    new ColonyInventory(p, "&5&lHello there, " + p.getName(), 45).addButton(new Button(0, 0) {
                        @Override
                        public void onClick(Player p, ClickType type) {
                            if(type == ClickType.LEFT) {
                                PlayerInventory.addItem(this.getItemStack(), p, 1);
                            }
                            else {
                                p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 2, 1);
                            }
                        }

                        @Override
                        public ItemStack construct() {
                            return new ItemStackBuilder(Material.GRASS).name("&6Hello you").lore("&2Click me and you shall receive me!\n&9Yes, just click me!\n\n&b(Only left clicks accepted)").build();
                        }
                    }).construct();
                }
                else if(args[0].equalsIgnoreCase("2")){
                    PageInventory inventory = new PageInventory(p, "&9Online players:", 54, Player.class, 1, 0, 44, 45, 53, false) {
                        @Override
                        public ArrayList<?> getList() {
                            ArrayList<Object> objects = new ArrayList<>();
                            objects.addAll(Bukkit.getOnlinePlayers());
                            return objects;
                        }

                        @Override
                        public void onClick(Player p, ClickType type, Object o) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis is &d" + ((Player) o).getName()));
                        }

                        @Override
                        public ItemStack itemConstruct(Object o) {
                            return new SkullItemBuilder().name(ChatColor.AQUA + ((Player) o).getName()).playerUuid(((Player) o).getUniqueId())
                                    .lore("&fCurrent millis: &d" + new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()))).build();
                        }
                    };
                    inventory.construct();
                }
                else if(args[0].equalsIgnoreCase("3")){
                    new ColonyInventory(p, "&bCurrent time in millis", 27).addButton(new Button(22, 1) {
                        @Override
                        public void onClick(Player p, ClickType type) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThe current time in millis is: &d" + System.currentTimeMillis()));
                        }

                        @Override
                        public ItemStack construct() {
                            return new ItemStackBuilder(Material.WATCH).name("&dCurrent time").lore("&fThe time is: &d" + System.currentTimeMillis()).build();
                        }
                    }).construct();
                }
                else if(args[0].equalsIgnoreCase("4")){
                    new PageInventory(p, "staff members", 54, String.class, 1, 0, 45, 45, 53, true) {
                        @Override
                        public ArrayList<?> getList() {
                            ArrayList<String> uuid = new ArrayList<>();
                            ResultSet rs = MainDatabase.getResultSet("SELECT uuid FROM StaffInfo;");
                            try {
                                while(rs.next()){
                                    uuid.add(rs.getString("uuid"));
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            return uuid;
                        }

                        @Override
                        public void onClick(Player p, ClickType type, Object o) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis is: &d" + MainDatabase.getName((String) o)));
                        }

                        @Override
                        public ItemStack itemConstruct(Object o) {
                            String uuid = (String) o;
                            return new SkullItemBuilder().playerUuid(UUID.fromString(uuid)).name("&d" + MainDatabase.getName(uuid)).build();
                        }
                    }.construct();
                }
            }
        }
        return false;
    }
}
