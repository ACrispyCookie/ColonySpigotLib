package net.colonymc.colonyspigotlib;

import net.colonymc.colonyspigotlib.lib.player.ColonyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {

	final Plugin plugin;
	
	public Placeholders(Plugin main) {
		this.plugin = main;
	}
	
	@Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if(ColonyPlayer.getByPlayer(p) == null) {
            return "";
        }
        ColonyPlayer cp = ColonyPlayer.getByPlayer(p);
        if(identifier.equals("rank_prefix")) {
            return cp.getRank().getPrefix();
        }
        else if(identifier.equals("rank")){
            return cp.getRank().getName();
        }
        else if(identifier.equals("hub_visibility")){
            return String.valueOf(cp.getVisibility());
        }
        else if(identifier.equals("votes")){
            return String.valueOf(cp.getVotes());
        }
        return null;
    }
	
	@Override
    public boolean persist(){
        return true;
    }
	
	@Override
    public boolean canRegister(){
        return true;
    }

	@Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

	@Override
    public String getIdentifier(){
        return "colonymc";
    }

	@Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

}
