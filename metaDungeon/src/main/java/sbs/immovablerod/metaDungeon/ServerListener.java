package sbs.immovablerod.metaDungeon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

import static sbs.immovablerod.metaDungeon.util.ItemUtil.getAdvancedItem;

public class ServerListener implements Listener {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Welcome to the server!");

        if (!plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            System.out.println("player " + event.getPlayer().getUniqueId() + " added to player config");
            plugin.players.put(event.getPlayer().getUniqueId(), new MetaDungeonPlayer(event.getPlayer()));
        }

        if (plugin.players.get(event.getPlayer().getUniqueId()).getInGame()) {
            System.out.println("test");
            plugin.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, () -> plugin.players.get(event.getPlayer().getUniqueId()).update(), 2L, 2L));
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        MetaDungeonPlayer playerConfig = plugin.players.get(event.getPlayer().getUniqueId());
        if (playerConfig.isDead() && event.getAction().name().equals("LEFT_CLICK_AIR")
                && plugin.players.size() > 1
                && playerConfig.getLives() > 1
                ) {
            playerConfig.respawn();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        MetaDungeonPlayer playerConfig = plugin.players.get(event.getEntity().getUniqueId());
        if (playerConfig.getInGame()) playerConfig.kill();

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        try {
            plugin.players.get(event.getPlayer().getUniqueId()).changeHealth(getAdvancedItem(event.getItem()).heal_life);
            plugin.players.get(event.getPlayer().getUniqueId()).changeStamina((double) getAdvancedItem(event.getItem()).heal_stamina);
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