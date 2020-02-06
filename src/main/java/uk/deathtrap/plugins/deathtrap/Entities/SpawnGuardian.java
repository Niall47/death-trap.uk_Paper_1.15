package uk.deathtrap.plugins.deathtrap.Entities;

import net.minecraft.server.v1_15_R1.EntityEnderDragon;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import java.util.ArrayList;
import java.util.List;

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

    public void setAI(Boolean ai){

        this.spawnGuardian.setAI(ai);

    }
}
