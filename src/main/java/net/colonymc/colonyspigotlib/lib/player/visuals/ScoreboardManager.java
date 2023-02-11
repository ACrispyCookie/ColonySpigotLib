package net.colonymc.colonyspigotlib.lib.player.visuals;

import me.clip.placeholderapi.PlaceholderAPI;
import net.colonymc.colonyspigotlib.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {

    Player p;
    String name;
    BukkitTask task;
    static ArrayList<ScoreboardManager> scoreboards = new ArrayList<>();

    public ScoreboardManager(Player p, String type) {
        this.p = p;
        this.name = type;
        scoreboards.add(this);
        start();
    }

    public void start(){
        initialize();
        update();
    }

    public void setType(String name){
        this.name = name;
        task.cancel();
        start();
    }

    private void initialize(){
        org.bukkit.scoreboard.ScoreboardManager m = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard b = m.getNewScoreboard();
        Objective o = b.registerNewObjective("[Skyblock]", "dummy");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getScoreboards().getString("scoreboards." + name + ".title")));
        List<String> lines = Main.getInstance().getScoreboards().getStringList("scoreboards." + name + ".lines");
        int score = 15;
        for(int i = 0; i < lines.size(); i++){
            String line = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, lines.get(i)));
            ArrayList<String> strings = getStrings(line, i, b);
            String prefix = strings.get(0);
            String suffix = strings.get(1);
            String teamEntry = strings.get(2);
            Team team = b.registerNewTeam(String.valueOf(i));
            team.addEntry(teamEntry);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            o.getScore(teamEntry).setScore(score);
            score--;
        }
        Team team = b.registerNewTeam(("nameTagHide-" + p.getUniqueId().toString()).substring(0, 16));
        team.setNameTagVisibility(NameTagVisibility.NEVER);
        p.setScoreboard(b);
    }

    private void update(){
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(p.isOnline()){
                    updateLines();
                    updateTeams();
                }
                else{
                    cancel();
                    scoreboards.remove(ScoreboardManager.this);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    private void updateLines(){
        List<Integer> updating = Main.getInstance().getScoreboards().getIntegerList("scoreboards." + name + ".updating_lines");
        List<String> lines = Main.getInstance().getScoreboards().getStringList("scoreboards." + name + ".lines");
        for(int i : updating){
            String line = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, lines.get(i)));
            ArrayList<String> strings = getStrings(line, i, p.getScoreboard());
            String prefix = strings.get(0);
            String suffix = strings.get(1);
            Team team = p.getScoreboard().getTeam(String.valueOf(i));
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    private void updateTeams(){
        for(Player pl : Bukkit.getOnlinePlayers()){
            p.getScoreboard().getTeam(("nameTagHide-" + p.getUniqueId().toString()).substring(0, 16)).addPlayer(pl);
        }
    }

    public ArrayList<String> getStrings(String line, int i, Scoreboard b){
        ArrayList<String> list = new ArrayList<>();
        String prefix;
        String suffix;
        String teamEntry;
        if(line.length() >= 32 && line.charAt(15) == ChatColor.COLOR_CHAR){
            ArrayList<Integer> startEnd = legthOfColor(line, 15);
            int startOfCode = startEnd.get(0);
            int endOfCode = startEnd.get(1);
            prefix = line.substring(0, startOfCode);
            suffix = line.substring(endOfCode, 32);
            teamEntry = line.substring(startOfCode, endOfCode);
        }
        else if(line.length() > 16 && line.length() < 32 && line.charAt(15) == ChatColor.COLOR_CHAR){
            ArrayList<Integer> startEnd = legthOfColor(line, 15);
            int startOfCode = startEnd.get(0);
            prefix = line.substring(0, startOfCode);
            suffix = line.substring(startOfCode);
            teamEntry = String.valueOf(ChatColor.values()[i]);
        }
        else{
            prefix = line.length() > 16 ? line.substring(0, 16) : line;
            suffix = line.length() <= 16 ? "" : line.length() > 32 ? line.substring(16, 32) : line.substring(16);
            if(findLastColorCode(prefix) != -1){
                ArrayList<Integer> startEnd = legthOfColor(prefix, findLastColorCode(prefix));
                int startOfCode = startEnd.get(0);
                int endOfCode = startEnd.get(1);
                teamEntry = prefix.substring(startOfCode, endOfCode);
            }
            else{
                teamEntry = ChatColor.WHITE + "";
            }
            for(Team t : b.getTeams()){
                for(String s : t.getEntries()){
                    if(s.equals(teamEntry)){
                        teamEntry = ChatColor.COLOR_CHAR + "r" + teamEntry;
                    }
                }
            }
        }
        list.add(prefix);
        list.add(suffix);
        list.add(teamEntry);
        return list;
    }

    private ArrayList<Integer> legthOfColor(String s, int index){
        if(index < s.length()){
            if(s.charAt(index) == ChatColor.COLOR_CHAR){
                ArrayList<Integer> integers = new ArrayList<>();
                int start = 0;
                int end = 0;
                int increment = 0;
                while(index + increment < s.length() && s.charAt(index + increment) == ChatColor.COLOR_CHAR){
                    increment += 2;
                }
                end = Math.min(increment + index, s.length());
                increment = 0;
                while(index - increment > 0 && s.charAt(index - increment) == ChatColor.COLOR_CHAR){
                    increment += 2;
                }
                start = Math.max(index - increment + 2, 0);
                integers.add(start);
                integers.add(end);
                return integers;
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    private int findLastColorCode(String s){
        for(int i = s.length() - 1; i >= 0; i--){
            if(s.charAt(i) == ChatColor.COLOR_CHAR){
                return i;
            }
        }
        return -1;
    }

    public static ScoreboardManager getByPlayer(Player p){
        for(ScoreboardManager s : scoreboards){
            if(s.p.equals(p)){
                return s;
            }
        }
        return null;
    }
}
