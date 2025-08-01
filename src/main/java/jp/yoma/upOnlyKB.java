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

public final class upOnlyKB extends JavaPlugin implements Listener {

    private double horizontalMultiplier;
    private double verticalBoost;


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
        horizontalMultiplier = config.getDouble("knockback.horizontalMultiplier");
        verticalBoost = config.getDouble("knockback.verticalBoost");
    }


    /*@Override
    public void onDisable() {
        // Plugin shutdown logic
    }*/
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
            // ダメージを受けたエンティティを取得
        Entity entity = event.getEntity();
        //Entity attacker = event.getDamager();//今後使う可能性を考えて入れたままにしておく

        // エンティティがプレイヤーである場合の処理
        if (entity instanceof Player) {
            Player player = (Player) entity;

                
            if (player.getNoDamageTicks() <= 10) {
                // Motion[1]を上向きに設定
                if (!event.isCancelled()){
                    player.setVelocity(new Vector(player.getVelocity().getX()*horizontalMultiplier, verticalBoost, player.getVelocity().getZ()*horizontalMultiplier));
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

