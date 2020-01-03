package uk.deathtrap.plugins.deathtrap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathTrap extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        //Bukkit.getServer().getConsoleSender().sendRawMessage("I AM A PLUGIN AND I AM ALIVE");
        MyPlayerListener listener = new MyPlayerListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendRawMessage("I AM A PLUGIN AND I AM DEAD");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
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

        if((cmd).getName().equalsIgnoreCase(("lost"))){
            player.sendMessage(ChatColor.GOLD + "Like, how can you even get lost?");

//            int X1 = this.getConfig().getInt("SpawnX");
//            int Y1 = this.getConfig().getInt("SpawnY");
//            int Z1 = this.getConfig().getInt("SpawnZ");
//            SworldName = getConfig().getString("SWorld");
//            Location loc = player.getLocation();
//            loc.add(null, 0, 256, 0);//
//            player.teleport(new Location( Bukkit.getWorld(DeathTrap.SworldName), X1, Y1, Z1));
        }

        return false;

    }
}
