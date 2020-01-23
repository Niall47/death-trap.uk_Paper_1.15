package uk.deathtrap.plugins.deathtrap.Entities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

import static uk.deathtrap.plugins.deathtrap.DeathTrap.spawnGuardian;

public class SpawnGuardian {

    private final CraftEnderDragon spawnGuardian;

    public static SpawnGuardian spawnGuardian(Location location) {

        CraftEnderDragon dragon = (CraftEnderDragon) location.getWorld().spawn(location, EnderDragon.class);
        dragon.setCustomName("Spawn Guardian");
        dragon.setCustomNameVisible(true);
        return new SpawnGuardian(dragon);

    }

    private SpawnGuardian(CraftEnderDragon dragon) {

        this.spawnGuardian = dragon;

    }

    public List<Entity> scanForEntities(){

        Location location = this.spawnGuardian.getLocation();
        List<Entity> nearby = this.spawnGuardian.getNearbyEntities(location.getX(), location.getY(), location.getZ());
        System.out.println(nearby);
        return nearby;

        }

    public List<Entity> scanForPlayers(){

        List<Entity> nearbyEntities = scanForEntities();
        ArrayList<Entity> nearbyHumans = null;

        if (nearbyEntities.size() > 0) {
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity.getClass().isInstance(HumanEntity.class)) {
                    nearbyHumans.add(nearbyEntity);
                }
            }
        }
        return nearbyHumans;

    }

    public void move(Location location){

        this.spawnGuardian.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);

    }

    public void land(){

        this.spawnGuardian.setRemainingAir(1);

    }

    public void setPhase(EnderDragon.Phase phase){

        spawnGuardian.setPhase(phase);

    }




}
