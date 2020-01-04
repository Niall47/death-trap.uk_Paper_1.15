package uk.deathtrap.plugins.deathtrap;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

public final class MyPlayerListener implements Listener {

    public MyPlayerListener(DeathTrap plugin) {
        Bukkit.getServer().getConsoleSender().sendRawMessage("Death-Trap.uk starting up");
    }

    @EventHandler
    public void onStart (WorldLoadEvent event) {
        //never fires. probably happens before plugin loads
        System.out.println("WORLD LOAD EVENT DETECTED!");
        World world = event.getWorld();
        Collection dragons = world.getEntitiesByClass(EnderDragon.class);
        if (dragons.size() > 0){
            //delete any existing dragons here.
            System.out.println(dragons.toString());
        }
        else{
            System.out.println("Couldn't find any dragons");

        }
        spawnGuardian(world);
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

    private static void spawnGuardian(World world) {
        Location zeroZero = new Location(world, 0, 265,0);
        EntityType entity = null;
        Mob spawnGuardian = (Mob) world.spawnEntity(zeroZero, EntityType.ENDER_DRAGON);
        EnderDragon guardian = (EnderDragon) spawnGuardian;
        System.out.println("Spawn Guardian created at " + spawnGuardian.getLocation().toString());
        System.out.println("Spawn Guardian is targeting " + spawnGuardian.getTarget().toString());
        spawnGuardian.setCustomName("Spawn Guardian");
        spawnGuardian.setCustomNameVisible(true);
        spawnGuardian.setAI(false);

        //Keep the guardian in spawn, which stays in memory. Make the object public, and let it loose on new players
        //guardian.setPhase(EnderDragon.Phase.CHARGE_PLAYER);

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
    public void newChunks(ChunkPopulateEvent event) {
        HandlerList handler = event.getHandlers();
        World world = event.getWorld();
        int X = event.getChunk().getX();
        int Y = event.getChunk().getZ();
        Bukkit.getServer().getConsoleSender().sendRawMessage("Creating new chunk @ " + X + " " + Y);
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
        JSONObject playerEntry = new JSONObject();
        JSONObject entry = new JSONObject();
        entry.put(lastLog,today.toString());
        saveToJson("lastLog.json", uuid, entry);
    }

    @EventHandler
    public void bedrockSnitch(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();
        Material material = block.getType();

        switch (material) {
            case BEDROCK:
            case END_PORTAL_FRAME:
                Location location = block.getLocation();
                JSONObject playerEntry = new JSONObject();
                JSONObject entry = new JSONObject();
                String player = event.getPlayer().getPlayerListName();
                UUID uuid = event.getPlayer().getUniqueId();
                Date today = new Date();
                String locationString = location.getWorld().toString() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
                entry.put(material.toString() + " @ " + locationString,today.toString());

                saveToJson("illegalItems.json", uuid, entry);
                break;
        }
    }

    private void saveToJson (String filename, UUID uuid, JSONObject entry) {

        try {
            if(!Files.exists(Paths.get(filename))){
                String data = "{}";
                Files.write(Paths.get(filename), data.getBytes());
            }
        } catch(IOException e) {
            System.out.println("Failed to access or create " + filename);
        }

        JSONObject jsonFile = new JSONObject();

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(filename);
            jsonFile = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Failed to read " + filename.toString());
        }

        try {
            if(jsonFile.containsKey(uuid.toString())){
                JSONArray userHistory = (JSONArray) jsonFile.get(uuid.toString());
                userHistory.add(entry);
                jsonFile.put(uuid.toString(), userHistory);

            } else {
                JSONArray userHistory = new JSONArray();
                userHistory.add(entry);
                jsonFile.put(uuid.toString(), userHistory);
            }

        } catch(Exception e){
            System.out.println("Error locating user in " + filename);
        }

        try {
            Files.write(Paths.get(filename), jsonFile.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Crashed trying to write to " + filename);
        }
    }
    }

