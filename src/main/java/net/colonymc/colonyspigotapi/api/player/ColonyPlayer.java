package net.colonymc.colonyspigotapi.api.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import net.colonymc.colonyapi.Rank;
import net.colonymc.colonyspigotapi.api.player.visuals.nametag.NameTagReceiver;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import net.colonymc.colonyspigotapi.Main;
import net.colonymc.colonyapi.database.MainDatabase;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

public class ColonyPlayer implements Listener {
	
	Player p;
	Rank rank;
	int votes;
	boolean visibility;
	boolean isFlying = false;
	static final ArrayList<ColonyPlayer> players = new ArrayList<>();
	
	public ColonyPlayer(Player p) {
		this.p = p;
		setupData();
		setupIngame();
		players.add(this);
		LuckPerms luckPerms = Main.getInstance().getLuckPerms();
		luckPerms.getEventBus().subscribe(UserDataRecalculateEvent.class, this::onUserRecalculate);
	}
	
	public ColonyPlayer() {
		if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("nametagHide") == null){
			Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("nametagHide");
			team.setNameTagVisibility(NameTagVisibility.NEVER);
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			new NameTagReceiver(getByPlayer(p), 1) {
				@Override
				protected String updatePrefix(ColonyPlayer p) {
					return p.getRank().getPrefix();
				}

				@Override
				protected String updateSuffix(ColonyPlayer p) {
					return "";
				}
			}.startReceiving();
		}
	}
	
	private void setupData() {
		ResultSet rs = MainDatabase.getResultSet("SELECT * FROM PlayerInfo WHERE uuid='" + p.getUniqueId().toString() + "';");
		try {
			if(rs.next()) {
				visibility = rs.getBoolean("hubVisibility");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet votes = MainDatabase.getResultSet("SELECT * FROM PlayerVotes WHERE uuid='" + p.getUniqueId().toString() + "';");
		try {
			if(votes.next()) {
				this.votes = votes.getInt("timesVoted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setupIngame(){
		if(p.hasPermission("*")) {
			togglePlayerFlight();
		}
		setRank();
		new NameTagReceiver(this, 1) {
			@Override
			protected String updatePrefix(ColonyPlayer p) {
				return p.getRank().getPrefix();
			}

			@Override
			protected String updateSuffix(ColonyPlayer p) {
				return "";
			}
		}.startReceiving();
	}

	private void remove(){
		NameTagReceiver.getByPlayer(getBukkitPlayer()).stopReceiving();
		players.remove(this);
	}

	private void setRank(){
		User u = Main.getInstance().getLuckPerms().getUserManager().getUser(p.getUniqueId());
		System.out.println(u.getPrimaryGroup());
		System.out.println(Rank.getByName(u.getPrimaryGroup()));
		System.out.println(Rank.getByName(u.getPrimaryGroup()).getPrefix());
		rank = Rank.getByName(u.getPrimaryGroup());
	}
	
	public void togglePlayerFlight() {
		if(isFlying) {
			setFlying(false);
			p.setFlying(false);
			if(!Main.getInstance().getConfig().getBoolean("shouldAllowFlight")) {
				p.setAllowFlight(false);
			}
		}
		else {
			setFlying(true);
			p.setAllowFlight(true);
			p.setFlying(true);
		}
	}
	
	public void addVotes(int amount) {
		votes = votes + amount;
	}
	
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	public void setFlying(boolean flying) {
		isFlying = flying;
	}
	
	public boolean isFlying() {
		return isFlying;
	}
	
	public boolean getVisibility() {
		return visibility;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public Player getBukkitPlayer() {
		return p;
	}

	public Rank getRank() { return rank; }
	
	public static ColonyPlayer getByPlayer(Player p) {
		for(ColonyPlayer cp : players) {
			if(p.equals(cp.getBukkitPlayer())) {
				return cp;
			}
		}
		return null;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		new ColonyPlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onLeave(PlayerQuitEvent e) {
		ColonyPlayer.getByPlayer(e.getPlayer()).remove();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onLeave(PlayerKickEvent e) {

		ColonyPlayer.getByPlayer(e.getPlayer()).remove();
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onFly(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if(!ColonyPlayer.getByPlayer(p).isFlying() && e.getPlayer().getGameMode() != GameMode.CREATIVE && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
			e.setCancelled(true);
			p.setFlying(false);
		}
		else {
			e.setCancelled(false);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onGamemodeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		if(ColonyPlayer.getByPlayer(p) != null){
			if(e.getNewGameMode() == GameMode.CREATIVE || e.getNewGameMode() == GameMode.SPECTATOR) {
				if(!ColonyPlayer.getByPlayer(p).isFlying()) {
					ColonyPlayer.getByPlayer(p).togglePlayerFlight();
				}
			}
			else {
				if(ColonyPlayer.getByPlayer(p).isFlying()) {
					ColonyPlayer.getByPlayer(p).togglePlayerFlight();
				}
			}
		}
	}

	public void onUserRecalculate(UserDataRecalculateEvent e) {
		setRank();
	}

}
