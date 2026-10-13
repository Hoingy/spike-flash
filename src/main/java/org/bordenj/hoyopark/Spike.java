package org.bordenj.hoyopark;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Spike implements Listener {
    Bar plantbar=Main.plantbar;
    Bar defusebar=Main.defusebar;
    private final Main plugin;
    public Spike (Main plugin){
        this.plugin=plugin;
    }

    public void flashPlayer(Player p,int ticks){
        ProtocolManager manager= ProtocolLibrary.getProtocolManager();
    }

    public void plantSound(Player p){
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT,3,0);
        new BukkitRunnable(){
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR,3,2);
            }
        }.runTaskLater(plugin,4);
        new BukkitRunnable(){
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR,3,3);
            }
        }.runTaskLater(plugin,7);
    }

    public void drawCircle(Location center, double radius, int points) {
        World world = center.getWorld();
        if (world == null || points <= 0 || radius < 0) return;

        double cx = center.getX();
        double y  = center.getY();
        double cz = center.getZ();

        for (int i = 0; i < points; i++) {
            double theta = (2.0 * Math.PI * i) / points;
            double x = cx + radius * Math.cos(theta);
            double z = cz + radius * Math.sin(theta);
            //world.spawnParticle(Particle.ENCHANTED_HIT, x, y, z, 1, 0.05, 0.05, 0.05, 0);
            world.spawnParticle(Particle.END_ROD, x, y, z, 1, 0, 0, 0,0);
        }
    }
    public void createVoidSphere(Location location,int size){
        for (double i = 0; i <= Math.PI; i += Math.PI / 90) {
            double radius = Math.sin(i)*size;
            double y = Math.cos(i)*size;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 90) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                location.add(x, y, z);
                location.getWorld().spawnParticle(Particle.SQUID_INK,location,0,0,0,0,0);
                location.subtract(x, y, z);
            }
        }
    }
    public void spikeExplode(Location loc){
        new BukkitRunnable(){
            int x=1;
            @Override
            public void run() {
                if(x<35){
                    for(Entity en:loc.getWorld().getNearbyEntities(loc,x,x,x)){
                        if(en instanceof LivingEntity&&!en.isInvulnerable()){
                            ((LivingEntity) en).setHealth(0);
                        }
                    }
                    createVoidSphere(loc,x);
                }
                if(x>=35&&x<80){
                    for(Entity en:loc.getWorld().getNearbyEntities(loc,35,35,35)){
                        if(en instanceof LivingEntity&&!en.isInvulnerable()){
                            ((LivingEntity) en).setHealth(0);
                        }
                    }
                    createVoidSphere(loc,35);
                }
                if(x==80){
                    this.cancel();
                }
                x++;
            }
        }.runTaskTimer(plugin,0,1);
    }

    public void spikeProcedure(Player p){
        ArmorStand spike = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);
        Objects.requireNonNull(spike.getEquipment()).setHelmet(new ItemStack(Material.BEACON));
        spike.setInvisible(true);
        spike.setInvulnerable(true);
        spike.customName(Component.text("Planted"));
        spike.setCustomNameVisible(false);
        Block block = p.getLocation().getBlock();
        Location loc=block.getLocation();
        loc.setX(loc.getX()+0.5);
        loc.setZ(loc.getZ()+0.5);
        loc.setY(loc.getY()-1);
        spike.teleport(loc);
        p.getWorld().getBlockAt(p.getLocation()).setType(Material.POLISHED_BLACKSTONE_SLAB);
        //Location up = p.getLocation();
        //up.setY(up.getY()+1);
        //p.getWorld().getBlockAt(up).setType(Material.BLACK_CARPET);

        new BukkitRunnable(){
            int time = 0;
            int x = -180;
            int spin=2;
            @Override
            public void run() {
                Location loc = spike.getLocation();
                loc.setYaw(x);
                spike.teleport(loc);
                x+=spin;
                drawCircle(spike.getEyeLocation().subtract(0,0.75,0),1.3,55);
                if(!spike.isValid()|| Component.text("Defused").equals(spike.customName())){
                    this.cancel();
                }
                if(x>180) {
                    x = -180;
                }
                time++;
                if(time>45*20){//spike blows up
                    spikeExplode(spike.getLocation());
                    Location up=spike.getLocation();
                    up.setY(up.getY()+1);
                    up.getBlock().setType(Material.AIR);
                    spike.remove();
                    this.cancel();
                }
                if(time<25*20){//normal beep
                    if(time%20==0){
                        p.playSound(spike.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,3,3);
                    }
                }
                if(time>=25*20&&time<35*20){//double beep
                    spin=5;
                    if(time%10==0){
                        p.playSound(spike.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,3,3);
                    }
                }
                if(time>=35*20&&time<38*20){//triple beep
                    spin=10;
                    if(time%5==0){
                        p.playSound(spike.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,3,3);
                    }
                }
                if(time>=38*20&&time<42*20){//fast beep
                    //p.getWorld().spawnParticle(Particle.DUST_PLUME,spike.getLocation(),4,3,2,3,1);
                    spin=15;
                    if(time%2==0){
                        p.playSound(spike.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,3,3);
                    }
                }
                if(time>=42*20){//abt to blow up beep
                    p.getWorld().spawnParticle(Particle.FLASH,spike.getLocation(),1,0,0,0,1);
                    p.playSound(spike.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT,3,3);
                }
            }
        }.runTaskTimer(plugin,0,1);
    }

    public void defuseSound(Player p){
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE,3,2);
        new BukkitRunnable(){
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE,3,3);
            }
        }.runTaskLater(plugin,2);
    }
    @EventHandler
    public void CrouchEvent(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        Block b = p.getLocation().subtract(0,1,0).getBlock();
        if(e.isSneaking()&&b.getType()==Material.COBBLESTONE) {
            for(Entity en:p.getNearbyEntities(0.75,1,0.75)){//start defuse
                if(en instanceof ArmorStand&& Component.text("Planted").equals(en.customName())){
                    defuseSound(p);//start defuse
                    defusebar.setVisible(true);
                    Location startloc = p.getLocation();
                    new BukkitRunnable() {
                        int x=0;
                        int y=70;
                        @Override
                        public void run() {
                            if (x > 7 * 20||y>7*20) {//defused
                                p.showTitle(Title.title(Component.text(""),Component.text("Spike Defused"),5,15,5));
                                p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 2, 1);
                                defusebar.setVisible(false);
                                defusebar.setProgress(0);
                                en.customName(Component.text("Defused"));
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        ((ArmorStand) en).getEquipment().setHelmet(new ItemStack(Material.NETHERITE_BLOCK));

                                    }
                                }.runTaskLater(plugin,20);
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        ((ArmorStand) en).getEquipment().setHelmet(new ItemStack(Material.POLISHED_DEEPSLATE_SLAB));

                                    }
                                }.runTaskLater(plugin,40);
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        en.remove();
                                    }
                                }.runTaskLater(plugin,60);
                                this.cancel();
                                this.cancel();
                            }
                            if (p.isSneaking()&&p.getLocation().distance(startloc)<0.2) {//defusing
                                if(defusebar.getProgress()>=0.5){
                                    y++;
                                }
                                else{
                                    x++;
                                }

                                if(defusebar.getProgress() + ((double) 1 / 141)>1){
                                    defusebar.setProgress(1);
                                }
                                else{
                                    defusebar.setProgress(defusebar.getProgress() + ((double) 1 / 141));
                                }
                            }
                            else{//defuse cancel
                                this.cancel();
                                defusebar.setVisible(false);
                                if(defusebar.getProgress()>=0.5){
                                    defusebar.setProgress(0.5);
                                }
                                else{
                                    defusebar.setProgress(0);
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 0, 1);
                    return;
                }
            }
            plantSound(p);//start plant
            plantbar.setVisible(true);
            Location startloc = p.getLocation();
            new BukkitRunnable() {
                int x = 0;

                @Override
                public void run() {
                    if (x > 4 * 20) {//planted
                        p.showTitle(Title.title(Component.text(""),Component.text("Spike Planted"),5,15,5));
                        p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 2, 1);
                        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 2, 1);
                        plantbar.setVisible(false);
                        plantbar.setProgress(0);
                        this.cancel();

                        spikeProcedure(p);
                    }
                    if (p.isSneaking()&&p.getLocation().distance(startloc)<0.2) {//planting
                        x++;
                        if(plantbar.getProgress() + ((double) 1 / 81)>1){
                            plantbar.setProgress(1);
                        }
                        else{
                            plantbar.setProgress(plantbar.getProgress() + ((double) 1 / 81));
                        }
                    } else {//canceled
                        this.cancel();
                        plantbar.setVisible(false);
                        plantbar.setProgress(0);
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}
