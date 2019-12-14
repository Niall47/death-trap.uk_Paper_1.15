package uk.deathtrap.plugins.deathtrap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class MyPLayerListener implements Listener {
    public MyPLayerListener(DeathTrap plugin) {
        Bukkit.getServer().getConsoleSender().sendRawMessage("My Player Listener starting up");
    }
    @EventHandler
    public void onConnect(PlayerLoginEvent event) {
        Player player = event.getPlayer();
    }


    @EventHandler
    public void antiBookBan(PlayerEditBookEvent event){
        Player player = event.getPlayer();
        int pages = event.getNewBookMeta().getPageCount();

        if (pages > 10) {
            int slot = event.getSlot();
            ItemStack book = player.getInventory().getItem(slot);
            book.setAmount(0);
            player.sendMessage(ChatColor.GOLD + "Nobody wants to read your " + pages + " page novel dude");

        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getServer().getConsoleSender().sendRawMessage(player.getDisplayName() + " has joined the server!");
        player.sendMessage(ChatColor.GOLD + "Welcome to the server");
        if (player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.GOLD + "You are not new");
            player.setGameMode(GameMode.SURVIVAL);
        }else{
            player.sendMessage(ChatColor.GOLD + "Welcome to Death-Trap.uk +" +
                    " Enjoy your stay");
        }
    }
    }

