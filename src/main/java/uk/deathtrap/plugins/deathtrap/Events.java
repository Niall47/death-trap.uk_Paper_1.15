package uk.deathtrap.plugins.deathtrap;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.deathtrap.plugins.deathtrap.Entities.SpawnGuardian;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

import static uk.deathtrap.plugins.deathtrap.DeathTrap.spawnGuardian;

public class Events implements Listener {

    public Events(DeathTrap deathTrap){

        Startup startup = new Startup();

    }

    @EventHandler
    public void bullyNewPlayers(PlayerJoinEvent event){

        Player player = event.getPlayer();
        Bukkit.getServer().getConsoleSender().sendRawMessage(player.getDisplayName() + " has joined the server!");

        //for testing this bullies returning players
        if (!player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.GOLD + "Welcome to Death-Trap.uk");
            player.sendMessage(ChatColor.GOLD + "Visit Death-Trap.uk for voting rewards and updates");
        }
        else {

            spawnGuardian.scanForPlayers();
            spawnGuardian.move(player.getLocation());
            spawnGuardian.land();
            spawnGuardian.setPhase(EnderDragon.Phase.CHARGE_PLAYER);
        }

    }

    @EventHandler
    public void spawnGuardianTargeting(EntityTargetLivingEntityEvent event){

        if (event.getEntity().getClass().isInstance(SpawnGuardian.class)) {
            //spawnGuardian just targeted someone.

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
        entry.put(lastLog, today.toString());
        saveToJson("lastLog.json", uuid, entry);

    }

    @EventHandler
    public void bedrockSnitch(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();
        Material material = block.getType();

        switch (material) {
            case BEDROCK:
            case END_PORTAL_FRAME:
            case SPAWNER:
            case DRAGON_EGG:
                Location location = block.getLocation();
                JSONObject playerEntry = new JSONObject();
                JSONObject entry = new JSONObject();
                String player = event.getPlayer().getPlayerListName();
                UUID uuid = event.getPlayer().getUniqueId();
                Date today = new Date();
                String locationString = location.getWorld().toString() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
                entry.put(material.toString() + " @ " + locationString, today.toString());

                saveToJson("illegalItems.json", uuid, entry);
                break;

        }
    }

    private void saveToJson(String filename, UUID uuid, JSONObject entry) {

        //I feel like this should be a class
        try {
            if (!Files.exists(Paths.get(filename))) {
                String data = "{}";
                Files.write(Paths.get(filename), data.getBytes());
            }
        } catch (IOException e) {
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
            if (jsonFile.containsKey(uuid.toString())) {
                JSONArray userHistory = (JSONArray) jsonFile.get(uuid.toString());
                userHistory.add(entry);
                jsonFile.put(uuid.toString(), userHistory);

            } else {
                JSONArray userHistory = new JSONArray();
                userHistory.add(entry);
                jsonFile.put(uuid.toString(), userHistory);
            }

        } catch (Exception e) {
            System.out.println("Error locating user in " + filename);
        }

        try {
            Files.write(Paths.get(filename), jsonFile.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Crashed trying to write to " + filename);
        }
    }
}
