package luden.gym.command;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class commandgympesas implements CommandExecutor {

    private Plugin plugin;
    private Map<Player, Long> cooldowns;
    public commandgympesas(Plugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String CoolDown = "CoolDown";
        String HaciendoEjercio = "Mensaje-1";
        String Esperar = "Mensaje-2";
        String TerminarEjercio = "Mensaje-3";
        String TotalFuerza = "Mensaje-4";
        String RegionPesas = "Region-Pesas";

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo se puede ejecutar en el juego.");
            return true;
        }

        Player player = (Player) sender;

        WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin == null) {
            return true;
        }

        // Verificar si el jugador está dentro de la región "hola"
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(player.getWorld()));
        ProtectedRegion region = regionManager.getRegion(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(RegionPesas)));
        if (region != null && !region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
            return true;
        }

        if (cooldowns.containsKey(player)) {
            long lastExecutionTime = cooldowns.get(player);
            long currentTime = System.currentTimeMillis();
            long cooldownTime = 5 * 60 * 1000; // 5 minutos en milisegundos

            if (currentTime - lastExecutionTime < cooldownTime) {
                long remainingTime = (lastExecutionTime + cooldownTime - currentTime) / 1000;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(CoolDown).replace("%time%", String.valueOf(remainingTime))));
                return true;
            }
        }

        cooldowns.put(player, System.currentTimeMillis());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(HaciendoEjercio)));

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(Esperar)));

        player.setInvulnerable(true);
        player.setWalkSpeed(0f);
        player.setAllowFlight(false);
        player.setFlying(false);

         // Ejecutar el código después de 30 segundos
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(TerminarEjercio)));

                // Aumentar la fuerza del jugador en 0.1
                double currentStrengthValue = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                double newStrengthValue = currentStrengthValue + 0.1;
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newStrengthValue);

                // Obtener la configuración y la sección del jugador
                FileConfiguration config = plugin.getConfig();
                ConfigurationSection playerSection = config.getConfigurationSection("Gym." + player.getName());

                if (playerSection == null) {
                    playerSection = config.createSection("Gym." + player.getName());
                }

                // Obtener la fuerza actual del jugador desde el archivo de configuración
                double currentStrength = playerSection.getDouble("fuerza", 0.0);

                // Actualizar la fuerza y guardarla en la sección del jugador
                double newStrength = currentStrength + 0.1;
                playerSection.set("fuerza", newStrength);

                // Guardar la configuración en el archivo
                plugin.saveConfig();

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(TotalFuerza).replace("%fuerzanueva%",String.valueOf(newStrength))));


                // Habilitar daño y movimiento del jugador
                player.setInvulnerable(false);
                player.setWalkSpeed(0.2f);
                player.setAllowFlight(true);
                player.setFlying(false);
            }
        }.runTaskLater(plugin, 600L); // 30 segundos = 600 ticks

        return true;
    }
}