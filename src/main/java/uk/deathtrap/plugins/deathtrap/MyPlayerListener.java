package uk.deathtrap.plugins.deathtrap;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

public final class MyPlayerListener implements Listener {
    public MyPlayerListener(DeathTrap plugin) {
        Bukkit.getServer().getConsoleSender().sendRawMessage("Death-Trap.uk starting up");
    }

    @EventHandler
    public void antiBookBan(PlayerEditBookEvent event){
        Player player = event.getPlayer();
        int pages = event.getNewBookMeta().getPageCount();

        if (pages > 10) {
            int slot = event.getSlot();
            ItemStack book = player.getInventory().getItem(slot);
            book.setAmount(0);
            player.sendMessage(ChatColor.GOLD + "Nobody wants to read your " + pages + " page novel " + player.getName());
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK, 1);
            player.getInventory().setItem(slot, item);
        }
    }

    @EventHandler
    public void goToBed(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        int numberOfPlayers = Bukkit.getOnlinePlayers().size();
        String name = event.getPlayer().getDisplayName();
        long time = player.getLocation().getWorld().getTime();

        if (numberOfPlayers > 1 && ((time < 12300) || (time > 23850))) {
            Bukkit.getConsoleSender().sendMessage(name + " is trying to sleep");
            Bukkit.broadcastMessage("Other players are trying to sleep, be polite and sleep or disconnect");
        }
    }

    @EventHandler
    public void getLastCoords(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getDisplayName();
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        DecimalFormat df = new DecimalFormat("#.##");
        String lastLog = df.format(location.getX()) + " " + df.format(location.getY()) + " " + df.format(location.getZ());
        Date today = new Date();
        JSONObject entry = new JSONObject();
        entry.put(name, uuid);
        entry.put(lastLog,today.toString());
        saveToJson("lastLog.json", entry);
    }

    @EventHandler
    public void bedrockSnitch(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();
        Location location = block.getLocation();
        Material material = block.getType();

        switch (material) {
            case BEDROCK:
            case END_PORTAL_FRAME:
            case END_GATEWAY:
            case END_PORTAL:
                JSONObject entry = new JSONObject();
                String player = event.getPlayer().getPlayerListName();
                UUID uuid = event.getPlayer().getUniqueId();
                Date today = new Date();
                entry.put(player, uuid);
                entry.put(material.toString() + " @ " + location.toString(),today.toString());

                saveToJson("illegalItems.json", entry);
                break;
        }
    }

    private void saveToJson (String filename, JSONObject entry) {
        try {
            if(!Files.exists(Paths.get(filename))){
                String data = "{}";
                Files.write(Paths.get(filename), data.getBytes());
            }
            System.out.println("Logging to Json");
            System.out.println(entry.toJSONString());
            Files.write(Paths.get(filename), entry.toString().getBytes());
        } catch(IOException e) {
            System.out.println("Failed to save logout details to " + filename);
            System.out.println(entry);
        }
    }

    @EventHandler
    public void newChunks(ChunkPopulateEvent event) {
        HandlerList handler = event.getHandlers();
        int X = event.getChunk().getX();
        int Y = event.getChunk().getZ();
        Bukkit.getServer().getConsoleSender().sendRawMessage("NEW CHUNK @ " + X + " " + Y);
    }

    @EventHandler
    public void noFly(PlayerVelocityEvent event){
        Vector vector = event.getVelocity();
        System.out.println(vector);
        Player player = event.getPlayer();
        Bukkit.broadcastMessage("flying" + player.getFlySpeed());
    }


    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getServer().getConsoleSender().sendRawMessage(player.getDisplayName() + " has joined the server!");
        if (!player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.GOLD + "Welcome to Death-Trap.uk");
            player.sendMessage(ChatColor.GOLD + "Visit Death-Trap.uk for voting rewards and updates");
        }
    }

    }
