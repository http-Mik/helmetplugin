package me.mik.helmetplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

public class HelmetPlugin extends JavaPlugin implements CommandExecutor, Listener {
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        // Verbindung zu LuckPerms herstellen
        luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();

        // Registriere den Befehl /hat
        getCommand("helmetplugin").setExecutor(this);

        // Registriere dieses Plugin als Event-Listener
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("helmetplugin")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveHat(player);
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        giveHat(player);
    }

    private void giveHat(Player player) {
        // Überprüfe, ob der Spieler die Berechtigung hat
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            for (String colorName : new String[]{"hellrot", "dunkelrot", "hellgrün", "dunkelgrün", "türkis", "hellblau", "dunkelblau", "gelb", "rosa", "lila", "orange"}) {
                boolean hasColorPermission = user.getCachedData().getPermissionData(QueryOptions.defaultContextualOptions()).checkPermission("helmetplugin." + colorName).asBoolean();
                if (hasColorPermission) {
                    Color color = getColor(colorName);
                    if (color != null) {
                        // Gib dem Spieler einen farbigen Lederhut
                        ItemStack hat = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta meta = (LeatherArmorMeta) hat.getItemMeta();
                        meta.setColor(color);
                        hat.setItemMeta(meta);
                        player.getInventory().setHelmet(hat);
                        player.sendMessage(ChatColor.GREEN + "Du hast einen " + colorName + " Lederhut erhalten!");
                        break;  // Beende die Schleife, sobald wir einen Hut gegeben haben
                    }
                }
            }
        }
    }

    private Color getColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "hellrot":
                return Color.fromRGB(255, 102, 102);
            case "dunkelrot":
                return Color.fromRGB(153, 0, 0);
            case "hellgrün":
                return Color.fromRGB(102, 255, 102);
            case "dunkelgrün":
                return Color.fromRGB(0, 153, 0);
            case "türkis":
                return Color.fromRGB(64, 224, 208);
            case "hellblau":
                return Color.fromRGB(173, 216, 230);
            case "dunkelblau":
                return Color.fromRGB(0, 0, 139);
            case "gelb":
                return Color.fromRGB(255, 255, 0);
            case "rosa":
                return Color.fromRGB(255, 105, 180);
            case "lila":
                return Color.fromRGB(128, 0, 128);
            case "orange":
                return Color.fromRGB(255, 165, 0);
            default:
                return null;
        }
    }
}
