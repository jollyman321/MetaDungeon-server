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
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

import static sbs.immovablerod.metaDungeon.util.ItemUtil.getAdvancedItem;

public class ServerListener implements Listener {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Welcome to the server - niggers are halve price today so get them while they last!");

        if (!plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            System.out.println("player " + event.getPlayer().getUniqueId() + " added to player config");
            plugin.players.put(event.getPlayer().getUniqueId(), new MetaDungeonPlayer(event.getPlayer()));
        }

        if (plugin.players.get(event.getPlayer().getUniqueId()).getInGame()) {
            plugin.players.get(event.getPlayer().getUniqueId()).setPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            MetaDungeonPlayer playerConfig = plugin.players.get(player.getUniqueId());

            MetaDungeonMonster attacker = plugin.entities.get(event.getDamager().getUniqueId());
            event.setDamage(0);
            try {
                playerConfig.receiveAttack(attacker);
            } catch (NullPointerException e) {
                System.out.println("[WARN] could not find entity profile for " + event.getDamager().getUniqueId());
            }

        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            MetaDungeonPlayer playerConfig = plugin.players.get(player.getUniqueId());
            event.setDamage(0);
            if (!playerConfig.canAttack()) {
                event.setCancelled(true);}

            else {

                MetaDungeonMonster target = plugin.entities.get(event.getEntity().getUniqueId());

                int damage = playerConfig.getDamage();

                playerConfig.onTargetHit();

                if (damage < 1) event.setCancelled(true);
                try {
                    target.getEntity().setVelocity(playerConfig.getPlayer().getLocation().getDirection().setY(0).normalize().multiply(
                            (float) playerConfig.getKnockback()/30)
                    );
                    target.receiveAttack(playerConfig);
                } catch (NullPointerException e) {
                    System.out.println("[WARN] could not find entity profile for " + event.getEntity().getUniqueId());
                }
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
            MetaDungeonPlayer playerConfig = plugin.players.get(player.getUniqueId());
            playerConfig.setHealth(playerConfig.getHealth() - (int) event.getDamage() * 5);
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof Player) {
            Entity entity = (Entity) event.getEntity().getShooter();
            MetaDungeonMonster shooter = plugin.entities.get(entity.getUniqueId());
            if (shooter != null) {
                Player player = (Player) event.getHitEntity();
                MetaDungeonPlayer target = plugin.players.get(player.getUniqueId());

                target.receiveAttack(shooter);

            } else {
                System.out.println("[WARN] unregister projectile hit caused by " + entity.getName());
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {}

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        MetaDungeonPlayer playerConfig = plugin.players.get(event.getPlayer().getUniqueId());

        String action = event.getAction().toString();
        if (playerConfig.isDead() && action.startsWith("LEFT_CLICK")
                && plugin.players.size() > 1
                && playerConfig.getLives() > 1
                ) {
            playerConfig.respawn();

        } else if (action.startsWith("RIGHT_CLICK")) {
            MetaDungeonItem item = playerConfig.getGear().get("mainHand");
            if (item != null) {
                item.getInterface().onRightClick(playerConfig);

            }
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        System.out.println("[WARN] Uncaught player death");
        MetaDungeonPlayer playerConfig = plugin.players.get(event.getEntity().getUniqueId());
        if (playerConfig.getInGame()) playerConfig.kill();

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        try {
            getAdvancedItem(event.getItem()).consume(plugin.players.get(event.getPlayer().getUniqueId()));
            event.setCancelled(true);
        } catch (NullPointerException ignored) {System.out.println("could not find item on consume");}

    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.players.get(event.getWhoClicked().getUniqueId()).updateInventory();
            plugin.players.get(event.getWhoClicked().getUniqueId()).updateStats();
            }, 5L));
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.players.get(event.getPlayer().getUniqueId()).updateInventory();
            plugin.players.get(event.getPlayer().getUniqueId()).updateStats();
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