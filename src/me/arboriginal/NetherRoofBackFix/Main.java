package me.arboriginal.NetherRoofBackFix;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  private final static int         roof = 128;
  private static FileConfiguration config;
  private static int               minimum, minAir, secure;

  @Override
  public void onEnable() {
    reloadConfig();
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();
    // Create default folder and configuration file if not exist
    saveDefaultConfig();
    // Load the configuration
    config = getConfig();
    // This ensure parameters added in next versions are stored in the file with their default values
    config.options().copyDefaults(true);
    // Re-save config to be sure missing parameter are available for admins
    saveConfig();
    // Set often used parameters not to call config.getInt() each time in loops
    minimum = config.getInt("minimum");
    minAir  = config.getInt("minAir");
    secure  = config.getInt("secure");
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    if (event.getPlayer().hasPermission("nrbf.bypass")) return;

    Location loc = event.getTo();

    if (loc.getBlockY() < roof && loc.getBlockY() > minimum) return;
    if (loc.getBlock().getBiome() != Biome.NETHER) return;

    if (findSafePlace(loc)) {
      secureLocation(loc);
      event.getPlayer().teleport(loc);
    }
    else if (config.getBoolean("avoid_if_no_safe_place")) {
      if (config.getBoolean("warn_player_if_avoided")) {
        message(event.getPlayer(), "no_safe_place_found");
      }
      
      event.setCancelled(true);
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("nrbfr")) {
      reloadConfig();
      message(sender, "configuration_reloaded");

      return true;
    }

    return super.onCommand(sender, command, label, args);
  }

  private boolean findSafePlace(Location from) {
    int air = 0, y = Math.max(from.getBlockY(), roof) - config.getInt("minDown");

    while (air < minAir && y > minimum) {
      from.setY(--y);

      if (from.getBlock().getType() == Material.AIR) air++;
    }

    return (air == minAir);
  }

  private void secureLocation(Location loc) {
    while (loc.getBlock().getType() == Material.AIR && loc.getBlockY() > minimum) {
      loc.setY(loc.getY() - 1.0);
    }

    if (loc.getBlock().getType() == Material.LAVA) {
      loc.setY(loc.getY() + 1.0);
    }

    for (int x = -secure; x <= secure; x++) for (int z = -secure; z <= secure; z++) {
      Block block = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z).getBlock();

      if (block.getType() == Material.AIR) block.setType(Material.NETHERRACK);
    }

    loc.setY(loc.getY() + 1.0);
  }

  private void message(CommandSender sender, String key) {
    sender.sendMessage(config.getString(key));
  }
}
