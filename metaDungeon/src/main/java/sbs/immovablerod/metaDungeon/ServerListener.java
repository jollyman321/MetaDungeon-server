package sbs.immovablerod.metaDungeon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import sbs.immovablerod.metaDungeon.classes.*;
import sbs.immovablerod.metaDungeon.game.GConfig;

public class ServerListener implements Listener {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Welcome to the server - niggers are halve price today so get them while they last!");

        GConfig.playerManager.add(event.getPlayer());
        GConfig.messageManager.addPlayer(event.getPlayer());

    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            // handles damage dealt to player
            Player player = (Player) event.getEntity();
            MetaDungeonPlayer hitPlayer = GConfig.playerManager.getFromID(player.getUniqueId());
            if (hitPlayer.isInvincible()) event.setCancelled(true);

            else {
                MetaDungeonMonster attacker = GConfig.entityManager.getFromID(event.getDamager().getUniqueId());
                event.setDamage(0);

                MetaDungeonAttack attack = new MetaDungeonAttack(attacker, hitPlayer);
                attack.resolve();
            }

        } else if (event.getDamager() instanceof Player) {
            // handles player inflicted damage
            Player player = (Player) event.getDamager();
            MetaDungeonPlayer attackingPlayer = GConfig.playerManager.getFromID(player.getUniqueId());

            event.setDamage(0);

            if (!attackingPlayer.canAttack()) {
                event.setCancelled(true);}
            else {
                MetaDungeonMonster target = GConfig.entityManager.getFromID(event.getEntity().getUniqueId());

                if (attackingPlayer.getDamage() < 1) event.setCancelled(true);

                MetaDungeonAttack attack = new MetaDungeonAttack(attackingPlayer, target);
                attack.resolve();

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            Player player = (Player) event.getEntity();
            MetaDungeonPlayer playerConfig = GConfig.playerManager.getFromID(player.getUniqueId());
            playerConfig.setHealth(playerConfig.getHealth() - (int) event.getDamage() * 5);
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof Player) {
            Entity entity = (Entity) event.getEntity().getShooter();
            MetaDungeonMonster shooter = GConfig.entityManager.getFromID(entity.getUniqueId());
            if (shooter != null) {
                Player player = (Player) event.getHitEntity();
                MetaDungeonPlayer target = GConfig.playerManager.getFromID(player.getUniqueId());

                MetaDungeonAttack attack = new MetaDungeonAttack(shooter, target);
                attack.resolve();

            } else {
                System.out.println("[WARN] unregister projectile hit caused by " + entity.getName());
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            MetaDungeonPlayer player = GConfig.playerManager.getFromID(event.getEntity().getUniqueId());
            plugin.projectiles.put(
                    event.getProjectile().getUniqueId(),
                    new MetaDungeonProjectile(event.getEntity().getUniqueId(), player.getDamage())
            );
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        MetaDungeonPlayer playerConfig = GConfig.playerManager.getFromID(event.getPlayer().getUniqueId());
        if (playerConfig.getInputDelay() <= 0) {
            playerConfig.resetInputDelay();

            String action = event.getAction().toString();
            if (playerConfig.isDead() && action.startsWith("LEFT_CLICK")
                    && GConfig.playerManager.getAll().size() > 1
                    && playerConfig.getLives() > 1
            ) {
                playerConfig.respawn();

            } else if (action.startsWith("RIGHT_CLICK")) {
                MetaDungeonItem item = playerConfig.getGear().get("mainHand");
                if (item != null) {
                    item.getController().onRightClick(playerConfig);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        System.out.println("[WARN] Uncaught player death");
        MetaDungeonPlayer playerConfig = GConfig.playerManager.getFromID(event.getEntity().getUniqueId());
        if (playerConfig.isInGame()) playerConfig.kill();

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        try {
            GConfig.itemManager.getAdvancedItem(event.getItem()).consume(GConfig.playerManager.getFromID(event.getPlayer().getUniqueId()));
            event.setCancelled(true);
        } catch (NullPointerException ignored) {System.out.println("could not find item on consume");}

    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
            GConfig.playerManager.getFromID(event.getWhoClicked().getUniqueId()).updateInventory();
            }, 5L));
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
            GConfig.playerManager.getFromID(event.getPlayer().getUniqueId()).updateInventory();
        }, 5L));
    }




//    @EventHandler
//    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
//        AdvancedItem item = plugin.players.get(event.getPlayer().getUniqueId()).getGear().get("mainHand");
//        System.out.println("check");
//        System.out.println(item.getDurability());
//
//        if (item != null) {
//            if (item.durability != null && item.durability != -1) {
//                System.out.println(item.getDurability());
//                item.currentDurability -= 1;
//                //System.out.println(this.currentDurability);
//                //System.out.println(round(this.maxInternalDurability - ((float) this.currentDurability / (float)  this.durability) * (float)  this.maxInternalDurability));
//            }
//        }
//    }
}