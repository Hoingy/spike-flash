package org.bordenj.hoyopark;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Bar {
    private final Main plugin;
    private BossBar bar;

    public Bar(Main plugin){
        this.plugin=plugin;
    }

    public void addPlayer(Player p){
        bar.addPlayer(p);
    }

    public BossBar getBar(){
        return bar;
    }

    public void createBar(String name, BarColor color, BarStyle style){
        bar=Bukkit.createBossBar(name,color,style);
    }
    public void setProgress(double d){
        bar.setProgress(d);
    }
    public double getProgress(){
        return bar.getProgress();
    }
    public void setVisible(Boolean b){
        bar.setVisible(b);
    }

}
