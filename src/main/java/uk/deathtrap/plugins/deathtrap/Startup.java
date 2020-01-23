package uk.deathtrap.plugins.deathtrap;

import net.minecraft.server.v1_15_R1.EntityEnderDragon;
import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import uk.deathtrap.plugins.deathtrap.Entities.SpawnGuardian;

import java.util.Collection;
import java.util.List;

import static uk.deathtrap.plugins.deathtrap.DeathTrap.zeroZero;
import static uk.deathtrap.plugins.deathtrap.DeathTrap.spawnGuardian;

public class Startup {
    public Startup() {

        Bukkit.getServer().getConsoleSender().sendRawMessage("Death-Trap.uk starting up");

        //We just assume we're in the overworld for now
        List<World> worlds = Bukkit.getServer().getWorlds();
        World world = worlds.get(0);
        world.setKeepSpawnInMemory(true);

        zeroZero = new Location(world, 0, 70, 0);

        //dragons are stupid so we kill and replace them
        removeDragons(world);
        spawnDragon(world);

    }

    private static void removeDragons(World world) {

        Collection<EnderDragon> dragons = world.getEntitiesByClass(EnderDragon.class);

        if (dragons.size() > 0) {
            dragons.forEach(Entity::remove);

        }
    }

    private void spawnDragon(World world) {

        spawnGuardian = SpawnGuardian.spawnGuardian(zeroZero);

    }

}

