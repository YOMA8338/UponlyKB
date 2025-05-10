package jp.yoma;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
//import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class upOnlyKB extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public @NotNull Path getDataPath() {
        return super.getDataPath();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
            // ダメージを受けたエンティティを取得
        Entity entity = event.getEntity();
        Entity attacker = event.getDamager();
        // エンティティがプレイヤーである場合の処理
        if (entity instanceof Player) {
            Player player = (Player) entity;

                
            if (player.getNoDamageTicks() <= 10) {
                // Motion[1]を上向きに設定
                if (!event.isCancelled()){
                    player.setVelocity(new Vector(player.getVelocity().getX()*0.18, 0.353, player.getVelocity().getZ()*0.18));
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
}

