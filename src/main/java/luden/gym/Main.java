package luden.gym;

import luden.gym.command.commandgymcorrer;
import luden.gym.command.commandgympesas;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {

    ConsoleCommandSender mycmd = Bukkit.getConsoleSender();
    public String ruta;

    @Override
    public void onLoad() {
        this.mycmd.sendMessage("[JosifRoleplay-Gym] Loading...");
        this.mycmd.sendMessage("[JosifRoleplay-Gym] onEnable successfully...");
    }

    public void onEnable() {
        insertConfig();
        this.getCommand("gympesas").setExecutor(new commandgympesas(this));
        this.getCommand("gymcorrer").setExecutor(new commandgymcorrer(this));

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            this.mycmd.sendMessage("");
            this.mycmd.sendMessage("§b¡Se ha encontrado el plugin WorldGuard!");
            this.mycmd.sendMessage("");
            //Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            this.mycmd.sendMessage("");
            this.mycmd.sendMessage("§e¡No se ha encontrado el plugin WorldGuard!");
            this.mycmd.sendMessage("");
        }
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            this.mycmd.sendMessage("§b¡Se ha encontrado el plugin WorldEdit!");
            this.mycmd.sendMessage("");
            //Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            this.mycmd.sendMessage("§e¡No se ha encontrado el plugin WorldEdit!");
            this.mycmd.sendMessage("");
        }
        this.mycmd.sendMessage("§b JosifRoleplay-Gym");
        this.mycmd.sendMessage("§e  Purpur | 1.17.1 ");
        this.mycmd.sendMessage("");
        this.mycmd.sendMessage("[JosifRoleplay-Gym] Successfully enabled.");
    }

    public void onDisable() {
        this.mycmd.sendMessage("[JosifRoleplay-Gym] Goodbay!");
        this.mycmd.sendMessage("[JosifRoleplay-Gym] Saved Configs!");
    }

    public void insertConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        ruta = config.getPath();

        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }
}
