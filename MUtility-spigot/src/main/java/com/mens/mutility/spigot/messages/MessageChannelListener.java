package com.mens.mutility.spigot.messages;

import com.google.common.collect.Iterables;
import com.mens.mutility.spigot.MUtilitySpigot;
import com.mens.mutility.spigot.chat.PluginColors;
import com.mens.mutility.spigot.chat.Prefix;
import com.mens.mutility.spigot.commands.commands.tpdata.Tpdata;
import com.mens.mutility.spigot.portal.PortalManager;
import com.mens.mutility.spigot.utils.Checker;
import com.mens.mutility.spigot.utils.ServerInfo;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;


/**
 * Trida zajistujici komunikaci mezi pluginy na serverech v ramci BungeeCord
 */
public class MessageChannelListener implements PluginMessageListener {

    private final MUtilitySpigot plugin;
    private final Tpdata teleportDataManager;
    private final PluginColors colors;
    private final Prefix prefix;

    /**
     * Konstruktor tridy
     * @param plugin Odkaz na main tridu
     */
    public MessageChannelListener(MUtilitySpigot plugin) {
        this.plugin = plugin;
        teleportDataManager = new Tpdata(plugin);
        colors = new PluginColors();
        prefix = new Prefix();
    }

    @Override
    public void onPluginMessageReceived(@Nonnull String channel, @Nonnull Player player, @Nonnull byte[] message) {
        try {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            String subChannel = stream.readUTF();
            switch(subChannel) {
                case "mens:send-to-nether":
                    Player telPlayerNe = Bukkit.getPlayer(stream.readUTF());
                    assert telPlayerNe != null;
                    Location loc = player.getLocation();
                    boolean loadDataNe = stream.readBoolean();
                    double xNe = stream.readDouble();
                    double yNe = stream.readDouble();
                    double zNe = stream.readDouble();
                    String worldNe = "world_nether";
                    loc.setX(xNe);
                    loc.setY(yNe);
                    loc.setZ(zNe);
                    loc.setWorld(WorldCreator.name(worldNe).createWorld());
                    if(loadDataNe) {
                        teleportDataManager.applyData(telPlayerNe, teleportDataManager.loadNewestPlayerData(telPlayerNe), xNe, yNe, zNe, worldNe);
                    }
                    PortalManager pm = new PortalManager(player, loc);
                    pm.findPortal();
                    pm.createPortal();
                    if(pm.isPrepared()) {
                        telPlayerNe.teleport(pm.getPortalLocation());
                    }
                    break;
                case "mens:send-to-overworld":
                    Player telPlayerOw = Bukkit.getPlayer(stream.readUTF());
                    assert telPlayerOw != null;
                    loc = player.getLocation();
                    boolean loadDataOw = stream.readBoolean();
                    double xOw = stream.readDouble();
                    double yOw = stream.readDouble();
                    double zOw = stream.readDouble();
                    String worldOw = "world";
                    loc.setX(xOw);
                    loc.setY(yOw);
                    loc.setZ(zOw);
                    loc.setWorld(WorldCreator.name(worldOw).createWorld());
                    if(loadDataOw) {
                        teleportDataManager.applyData(telPlayerOw, teleportDataManager.loadNewestPlayerData(telPlayerOw), xOw, yOw, zOw, worldOw);
                    }
                    pm = new PortalManager(player, loc);
                    pm.findPortal();
                    pm.createPortal();
                    if(pm.isPrepared()) {
                        telPlayerOw.teleport(pm.getPortalLocation());
                    }
                    break;
                case "mens:send-to-end":
                    Player telPlayerEnd = Bukkit.getPlayer(stream.readUTF());
                    assert telPlayerEnd != null;
                    loc = player.getLocation();
                    boolean loadDataEnd = stream.readBoolean();
                    double xEnd = 98;
                    double yEnd = 48;
                    double zEnd = -2;
                    String worldEnd = "world_the_end";
                    loc.setX(xEnd);
                    loc.setY(yEnd);
                    loc.setZ(zEnd);
                    loc.setWorld(WorldCreator.name(worldEnd).createWorld());
                    if(loadDataEnd) {
                        teleportDataManager.applyData(telPlayerEnd, teleportDataManager.loadNewestPlayerData(telPlayerEnd), xEnd, yEnd, zEnd, worldEnd);
                    }
                    pm = new PortalManager(player, loc);
                    if(pm.createEndPlatform()) {
                        telPlayerEnd.teleport(pm.getEndPlatformLocation());
                    }
                    break;
                case "mens:permissionRequest":
                    String permission = stream.readUTF();
                    String returnChannel = stream.readUTF();
                    Checker checker = new Checker(plugin);
                    StringBuilder permPlayers = new StringBuilder();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if(checker.checkPermissions(onlinePlayer, permission)) {
                            permPlayers.append(onlinePlayer.getName()).append(";");
                        }
                    }
                    if(!permPlayers.toString().equalsIgnoreCase("")) {
                        permPlayers.substring(0, permPlayers.length() - 1);
                    }
                    MessageChannel messageChannel = new MessageChannel();
                    player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                    messageChannel.sendToBungeeCord(player, returnChannel, permPlayers.toString());
                    break;
                case "mens:servers-info-response":
                    String[] servers = stream.readUTF().split(";");
                    String serverName = stream.readUTF();
                    plugin.getServers().clear();
                    for(String serverLoc : servers) {
                        if(serverLoc.equals(serverName)) {
                            plugin.getServers().add(new ServerInfo(serverLoc, true));
                        } else {
                            plugin.getServers().add(new ServerInfo(serverLoc, false));
                        }
                    }
                    break;
                case "mens:teleport-request":
                    Player telPlayer = Bukkit.getPlayer(stream.readUTF());
                    assert telPlayer != null;
                    double x = stream.readDouble();
                    double y = stream.readDouble();
                    double z = stream.readDouble();
                    boolean loadDataTel = stream.readBoolean();
                    World world = WorldCreator.name(stream.readUTF()).createWorld();
                    Location location = new Location(world, x, y, z);
                    if(loadDataTel) {
                        assert world != null;
                        teleportDataManager.applyData(telPlayer, teleportDataManager.loadNewestPlayerData(telPlayer), x, y, z, world.getName());
                    }
                    telPlayer.teleport(location);
                    break;
                case "mens:teleport-data-request":
                    Player telDataPlayer = Bukkit.getPlayer(stream.readUTF());
                    assert telDataPlayer != null;
                    teleportDataManager.applyData(telDataPlayer, teleportDataManager.loadNewestPlayerData(telDataPlayer), telDataPlayer.getLocation().getX(), telDataPlayer.getLocation().getY(), telDataPlayer.getLocation().getZ(), Objects.requireNonNull(telDataPlayer.getLocation().getWorld()).getName());
                    break;
                case "mens:random-teleport-request":
                    Player telPlayerRt = Bukkit.getPlayer(stream.readUTF());
                    assert telPlayerRt != null;
                    double centerX = stream.readDouble();
                    double centerZ = stream.readDouble();
                    int radius = stream.readInt();
                    boolean loadDataRt = stream.readBoolean();
                    if(loadDataRt) {
                        teleportDataManager.applyData(telPlayerRt, teleportDataManager.loadNewestPlayerData(telPlayerRt), telPlayerRt.getLocation().getX(), telPlayerRt.getLocation().getY(), telPlayerRt.getLocation().getZ(), Objects.requireNonNull(telPlayerRt.getLocation().getWorld()).getName());
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers " + centerX + " " + centerZ + " 500 " + radius + " false " + telPlayerRt.getName());
                    telPlayerRt.sendMessage(prefix.getRandomTeleportPrefix(true, false) + "Pokud jsi byl portnut do země nebo se ti lokace nelíbí, zadej " + colors.getPrimaryColor() + "/spawn");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
