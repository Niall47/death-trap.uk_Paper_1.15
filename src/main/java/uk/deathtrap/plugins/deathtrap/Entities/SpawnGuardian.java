package uk.deathtrap.plugins.deathtrap.Entities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

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

        //We grab a list of nearby players
        if (nearbyEntities.size() > 0) {
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity.getClass().isInstance(HumanEntity.class)) {
                    nearbyHumans.add(nearbyEntity);
                }
            }
        }
        return nearbyHumans;

    }

    public void kill(){

//        this.spawnGuardian.setMomentum();
//        this.spawnGuardian.setRotation();
//        this.spawnGuardian.setAI();
//        this.spawnGuardian.setAI();
    }


}
