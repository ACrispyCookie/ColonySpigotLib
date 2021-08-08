package net.colonymc.colonyspigotapi.api.player.visuals.nametag;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyspigotapi.api.player.ColonyPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class NameTagReceiver {

    ColonyPlayer p;
    NameTag tag;
    int period;
    BukkitTask task;
    HashMap<OfflinePlayer, EntityArmorStand> showing = new HashMap<>();
    private static ArrayList<NameTagReceiver> tags = new ArrayList<>();
    protected abstract String updatePrefix(ColonyPlayer p);
    protected abstract String updateSuffix(ColonyPlayer p);

    public NameTagReceiver(ColonyPlayer player, int period){
        this.p = player;
        this.tag = constructTag();
        this.period = period;
        tags.add(this);
    }

    public void startReceiving(){
        task = new BukkitRunnable() {
            @Override
            public void run(){
                ArrayList<OfflinePlayer> toRemove = new ArrayList<>();
                showing.forEach((p, a) -> {
                    if(!p.isOnline()) {
                        destroy(a);
                        toRemove.add(p);
                    }
                });
                toRemove.forEach((p) -> showing.remove(p));
                for(Player online : Bukkit.getOnlinePlayers()){
                    if(!online.equals(p.getBukkitPlayer())){
                        if(!showing.containsKey(online)){
                            spawn(online);
                        }
                        update(online);
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, period);
    }

    public void stopReceiving(){
        task.cancel();
        tag.stopUpdating();
        tags.remove(this);
        showing.forEach((p, a) -> destroy(a));
    }

    private void spawn(Player p){
        NameTag tag = NameTagReceiver.getByPlayer(p).getTag();
        EntityArmorStand as = new EntityArmorStand(((CraftWorld) p.getWorld()).getHandle(), p.getLocation().getX(), p.getLocation().getY() + 0.2, p.getLocation().getZ());
        NBTTagCompound compoundTag = new NBTTagCompound();
        as.c(compoundTag);
        compoundTag.setBoolean("Marker", true);
        as.f(compoundTag);
        as.setInvisible(true);
        as.setGravity(false);
        as.setCustomNameVisible(true);
        as.setCustomName(tag.getPrefix() + ChatColor.RESET + " " + p.getName() + (tag.getSuffix().equals("") ? "" : " " + tag.getSuffix()));
        as.setSmall(true);
        PacketPlayOutSpawnEntity spawn = new PacketPlayOutSpawnEntity(as, 78);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
        ((CraftPlayer) this.p.getBukkitPlayer()).getHandle().playerConnection.sendPacket(spawn);
        ((CraftPlayer) this.p.getBukkitPlayer()).getHandle().playerConnection.sendPacket(metadata);
        showing.put(p, as);
    }

    private void update(Player p){
        NameTag tag = NameTagReceiver.getByPlayer(p).getTag();
        EntityArmorStand as = showing.get(p);
        as.setCustomName(tag.getPrefix() + ChatColor.RESET + " " + p.getName() + (tag.getSuffix().equals("") ? "" : " " + tag.getSuffix()));
        as.setLocation(p.getEyeLocation().getX(), p.getEyeLocation().getY() + 0.2, p.getEyeLocation().getZ(), 0, 0);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
        PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(as);
        ((CraftPlayer) this.p.getBukkitPlayer()).getHandle().playerConnection.sendPacket(metadata);
        ((CraftPlayer) this.p.getBukkitPlayer()).getHandle().playerConnection.sendPacket(teleport);
    }

    private void destroy(EntityArmorStand a){
        PacketPlayOutEntityDestroy spawn = new PacketPlayOutEntityDestroy(a.getId());
        ((CraftPlayer) this.p.getBukkitPlayer()).getHandle().playerConnection.sendPacket(spawn);
    }

    private NameTag constructTag(){
        return new NameTag(p, period) {
            @Override
            protected String updatePrefix(ColonyPlayer p) {
                return NameTagReceiver.this.updatePrefix(p);
            }

            @Override
            protected String updateSuffix(ColonyPlayer p) {
                return NameTagReceiver.this.updateSuffix(p);
            }
        };
    }

    public ColonyPlayer getPlayer(){
        return p;
    }

    public NameTag getTag(){
        return tag;
    }

    public static NameTagReceiver getByPlayer(Player p){
        for(NameTagReceiver r : tags){
            if(r.p.getBukkitPlayer().equals(p)){
                return r;
            }
        }
        return null;
    }
}
