package uk.deathtrap.plugins.deathtrap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uk.deathtrap.plugins.deathtrap.Entities.SpawnGuardian;


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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println("Not a player command");
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
