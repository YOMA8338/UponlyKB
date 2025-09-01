package jp.yoma;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import java.io.File;

public final class upOnlyKB extends JavaPlugin implements Listener {

    //values
    private double horizontalMultiplier;
    private double verticalBoost;
    private boolean ckbactive; //Custom KnockBack Active flag
    private int configversion;
    private int requiredconfigversion = 1;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("uponlykb").setExecutor(this);
        getLogger().info("UpOnlyKB Loaded.");
    }
    private void loadConfigValues() {
        reloadConfig();
        FileConfiguration config = getConfig();
        ckbactive = config.getBoolean("knockback.active",true);
        horizontalMultiplier = config.getDouble("knockback.horizontalkb",1.0);
        verticalBoost = config.getDouble("knockback.verticalkb",0.35);
        configversion = config.getInt("config-version",0);
        if (configversion < requiredconfigversion){
            File configFile = new File(getDataFolder(), "config.yml");
            File oldConfigFile = new File(getDataFolder(), "config_old.yml");
            if (configFile.exists()) {
                if (oldConfigFile.exists()) {
                    oldConfigFile.delete();
                }
                boolean renamed = configFile.renameTo(oldConfigFile);
                if (renamed) {saveDefaultConfig();}
                if (renamed){
                    getLogger().info("Renamed the old config file to config_old.yml and regenerated the config file.");
                    loadConfigValues();
                }else{
                    getLogger().warning("Failed to backup the old config file!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        //Get damaged entity
        if (!ckbactive){return;}
        Entity entity = event.getEntity();
        //Entity attacker = event.getDamager();//ダメージを与えたエンティティを取得
        //When Entity is Player, apply upward knockback
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getNoDamageTicks() <= 10) {
                if (!event.isCancelled()){
                    final Vector playerVelocity = player.getVelocity();
                    player.setVelocity(new Vector((playerVelocity.getX()* horizontalMultiplier), verticalBoost, (playerVelocity.getZ()* horizontalMultiplier)));
                    //player.setVelocity(new Vector(player.getVelocity().getX()*horizontalMultiplier, verticalBoost, player.getVelocity().getZ()*horizontalMultiplier));
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("uponlykb") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("uponlykb.reload")) {
                sender.sendMessage(ChatColor.RED + "You have no permission!");
                return true;
            }
            loadConfigValues();
            sender.sendMessage(ChatColor.GREEN + "UponlyKB's config reloaded.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Usage: /uponlykb reload");
        return false;
    }
}

