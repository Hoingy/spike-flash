package org.bordenj.hoyopark;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public static Bar plantbar;
    public static Bar defusebar;
    @Override
    public void onEnable() {
        plantbar=new Bar(this);
        plantbar.createBar("Planting", BarColor.WHITE, BarStyle.SOLID);
        defusebar=new Bar(this);
        defusebar.createBar("Defusing", BarColor.RED, BarStyle.SOLID);
        if(!Bukkit.getOnlinePlayers().isEmpty()){
            for(Player p: Bukkit.getOnlinePlayers()){
                plantbar.addPlayer(p);
                plantbar.setVisible(false);
                plantbar.setProgress(0);
                defusebar.addPlayer(p);
                defusebar.setVisible(false);
                defusebar.setProgress(0);
            }
        }
        getServer().getPluginManager().registerEvents(new Spike(this),this);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        if(!plantbar.getBar().getPlayers().contains(e.getPlayer())){
            plantbar.addPlayer(e.getPlayer());
            plantbar.setVisible(false);
            plantbar.setProgress(0);
            defusebar.addPlayer(e.getPlayer());
            defusebar.setVisible(false);
            defusebar.setProgress(0);
        }
    }

}
