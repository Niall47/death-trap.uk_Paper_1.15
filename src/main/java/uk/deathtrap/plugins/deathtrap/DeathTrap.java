package uk.deathtrap.plugins.deathtrap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uk.deathtrap.plugins.deathtrap.Entities.SpawnGuardian;

import java.util.List;
import java.util.UUID;

public final class DeathTrap extends JavaPlugin implements Listener {

    public static Location zeroZero ;
    public static SpawnGuardian spawnGuardian;

    @Override
    public void onEnable() {

        Events events = new Events(this);
        Bukkit.getServer().getPluginManager().registerEvents(events, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendRawMessage("I AM A PLUGIN AND I AM DEAD");
    }

    public static void consoleCommand(CommandSender sender, Command cmd){

        UUID niall = UUID.fromString("6dd4cfec-d838-446d-b500-619316e41e41");
        List<Player> players = (List<Player>) Bukkit.getServer().getOnlinePlayers();
        System.out.println(players.toString());

        for(Player player:players){
            if(player.getUniqueId().equals(niall)){
                System.out.println("Niall47 is online.");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("Not a player command");
            consoleCommand(sender, cmd);
            return false;
        }

        Player player = (Player) sender;

        if((cmd).getName().equalsIgnoreCase("starving"))
        {
            player.sendMessage(ChatColor.GOLD + "Hi Starving, I'm Dad");
            player.setFoodLevel(1);
            player.setDisplayName("Starving");
            player.setPlayerListName("Starving");
            return true;
        }

        return false;

    }
}
