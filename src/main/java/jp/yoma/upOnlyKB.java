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
    }/*なんか上手くいかない
    public void onEntityKnockbackByEntity(EntityKnockbackByEntityEvent event) {
        // 攻撃されたエンティティがプレイヤーである場合
        if (event.getEntity() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getEntity();
            org.bukkit.entity.Entity attacker = (org.bukkit.entity.Entity) event.getHitBy();
            if(attacker instanceof org.bukkit.entity.Player){
                if (((Player) attacker).isSprinting()){
                    Vector newkbsprint = new Vector(event.getKnockback().getX() * 2, 0.36, event.getKnockback().getZ() * 2);
                    Bukkit.broadcastMessage(String.valueOf(newkbsprint));
                    Bukkit.broadcastMessage("Sprint");
                    event.setCancelled(true);
                    player.setVelocity(newkbsprint);

                }else{
                    Vector newkb = new Vector(event.getKnockback().getX() * 0.95, 0.352, event.getKnockback().getZ() * 0.95);
                    Bukkit.broadcastMessage(String.valueOf(newkb));
                    Bukkit.broadcastMessage("NotSprint or NotPlayer");
                    event.setCancelled(true);
                    player.setVelocity(newkb);

                }
            }else{
                Vector newkb = new Vector(event.getKnockback().getX() * 0.95, 0.352, event.getKnockback().getZ() * 0.95);
                attacker.sendMessage(String.valueOf(newkb));
                attacker.sendMessage("NotSprint or NotPlayer");
                event.setCancelled(true);
                player.setVelocity(newkb);

            }


        }





    }*/
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("uponlykb") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            loadConfigValues();
            sender.sendMessage(ChatColor.GREEN + "UponlyKB's config reloaded.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Usage: /uponlykb reload");
        return false;
    }

}

