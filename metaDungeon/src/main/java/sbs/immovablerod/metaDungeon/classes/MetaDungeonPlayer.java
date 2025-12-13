package sbs.immovablerod.metaDungeon.classes;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.game.GConfig;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;


public class MetaDungeonPlayer extends MetaDungeonEntity {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final int inputDelayMax;
    private final int maxInvincibilityFrames;
    private final String team;
    private double range;
    private int invincibilityFrames;
    private int currentInputDelay;
    private double baseStamina;
    private Player player;
    private final int staminaRecoveryTime;
    private int lives;
    private boolean dead;
    private float attackCoolDownPeak;
    private float attackCoolDown;
    private HashMap<String, MetaDungeonItem> gear;



    private int staminaRecovery;
    private Double stamina;
    private Double maxStamina;
    private boolean inGame;
    private Integer maxHealth;

    public MetaDungeonPlayer(Player player) {

        super(100, 0, 10, 0, 0, 0);

        this.inGame = false;
        this.dead = false;

        this.team = "survivers";
        this.player = player;
        this.stamina = 100.0;
        this.baseStamina = 100.0;
        this.maxStamina = 100.0;
        this.staminaRecoveryTime = 20;
        this.staminaRecovery = this.staminaRecoveryTime;
        this.maxHealth = 100;
        this.lives = 4;
        this.range = 0.0;



        // modified by equipment and buffs
        this.gear = new HashMap<>();


        this.attackCoolDown = 0;
        this.attackCoolDownPeak = 0;

        this.inputDelayMax = 4;
        this.currentInputDelay = 0;

        this.maxInvincibilityFrames = 10;
        this.invincibilityFrames = 0;
        // remove 1.9+ weapon effect e.g sweep attack
        this.player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(16);
        this.player.getAttribute(Attribute.ARMOR).setBaseValue(-20);
        this.player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).setBaseValue(0);

        this.setHealth(100);
        this.setStamina(100.0);

    }

    @Override
    public void onDealtAttack(MetaDungeonEntity victum) {
        if (this.gear.get("mainHand") != null) {
            this.gear.get("mainHand").onTargetHit(this);
            // note that the weapon durability is changed during the attack
            // this just updates the item ui
            this.gear.get("mainHand").changeDurability(1, this.getPlayer().getInventory().getItemInMainHand(), this);
        }

        if (this.gear.get("mainHand") != null && this.gear.get("mainHand").getAttackSpeed() >= 1) {
            this.attackCoolDown = ((float) 10 /((float) this.gear.get("mainHand").getAttackSpeed() /10));
            this.attackCoolDownPeak = this.attackCoolDown;
            taskManager.runTaskLater(() -> this.player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, (2 * (int) this.attackCoolDown), 255)), 4L);
        }
    }

    @Override
    public void receiveAttack(MetaDungeonEntity attacker) {
        super.receiveAttack(attacker);

        this.invincibilityFrames = this.maxInvincibilityFrames;
        for (String key: this.gear.keySet()) {
            MetaDungeonItem item = this.gear.get(key);
            if (item != null && item.category.equals("armor")) {
                if (key.equals("helmet")) item.changeDurability(1, this.getPlayer().getInventory().getHelmet(), this);
                if (key.equals("chestplate")) item.changeDurability(1, this.getPlayer().getInventory().getChestplate(), this);
                if (key.equals("leggings")) item.changeDurability(1, this.getPlayer().getInventory().getLeggings(), this);
                if (key.equals("boots")) item.changeDurability(1, this.getPlayer().getInventory().getBoots(), this);

                item.getController().onReceiveAttack(attacker, this);
            }
        }

    }


    public boolean canAttack() {
        return this.attackCoolDown <= 0 &&
            this.gear.get("mainHand") != null
            && this.gear.get("mainHand").getAttackSpeed() >= 1
            && this.gear.get("mainHand").getStaminaCost() <= this.stamina;

    }
    public void updateInventory() {
        this.gear.put("mainHand", GConfig.itemManager.getAdvancedItem(this.player.getInventory().getItemInMainHand()));
        this.gear.put("helmet",GConfig.itemManager.getAdvancedItem(this.player.getInventory().getHelmet()));
        this.gear.put("chestplate",GConfig.itemManager.getAdvancedItem(this.player.getInventory().getChestplate()));
        this.gear.put("leggings", GConfig.itemManager.getAdvancedItem(this.player.getInventory().getLeggings()));
        this.gear.put("boots",GConfig.itemManager.getAdvancedItem(this.player.getInventory().getBoots()));

        this.player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).setBaseValue(this.getRange());
    }

    public void displayActionBar() {
        if (this.isInGame()) {
            if (!this.dead) {
                Component actionBar = Component
                        .text(this.getHealth() + "/" + this.getMaxHealth() + Symbols.HEART, Symbols.HEART.color())
                        .append(Component.text("  |  ", NamedTextColor.WHITE))
                        .append(Component.text(this.getDamage() + "" + Symbols.DAMAGE, Symbols.DAMAGE.color()))
                        .append(Component.text("  |  ", NamedTextColor.WHITE))
                        .append(Component.text(this.getDefence() + "" + Symbols.DEFENCE, Symbols.DEFENCE.color()))
                        .append(Component.text("  |  ", NamedTextColor.WHITE))
                        .append(Component.text(round(this.getStamina()) + "/" + round(this.getMaxStamina()) + Symbols.STAMINA, Symbols.STAMINA.color()));
                this.player.sendActionBar(actionBar);
            } else {
                this.player.sendActionBar(
                        GConfig.messageManager.getMM().deserialize(plugin.jsonLoader.messages.at("/player/deadActionBar").asText(),
                                Placeholder.component("lives", Component.text(String.valueOf(this.getLives() - 1), Symbols.LIFE.color())))
                );
            }

            final Component header = Component.text("Meta Dungeon v1.1", NamedTextColor.BLUE);
            final Component footer = Component.text("Lives Remaining: ", NamedTextColor.WHITE)
                    .append(Component.text(String.valueOf(this.getLives() - 1), NamedTextColor.DARK_AQUA))
                    .append(Component.text("  |  Round: ", NamedTextColor.WHITE))
                    .append(Component.text(plugin.game.getCurrentRound(), NamedTextColor.YELLOW));
            this.player.sendPlayerListHeaderAndFooter(header, footer);
        }

    }
    public void update() {

        if (this.gear.get("mainHand") != null ) {

        }

        // player updates should happen 10 times a second
        if (this.player.isSprinting() && this.stamina > 0) {
            this.changeStamina(-0.5);
        } else if (!this.player.isSprinting() && this.stamina < this.maxStamina && this.staminaRecovery >= this.staminaRecoveryTime) {
            this.changeStamina(2.0);
        }
        if (this.staminaRecovery < this.staminaRecoveryTime) this.staminaRecovery += 1;

        if (this.player.getFoodLevel() > 19) {this.player.setFoodLevel(19); }

        if (this.attackCoolDown > 0) {
            try {
                this.player.setExp(this.attackCoolDown / this.attackCoolDownPeak);
            } catch (IllegalArgumentException ignored) {}
            this.attackCoolDown -= 1;

            this.player.setLevel((int) this.attackCoolDown);

        }

        if (this.gear.get("mainHand") != null && this.stamina < this.gear.get("mainHand").getStaminaCost()) {
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 4, 255));
        }

        if (this.currentInputDelay > 0) this.currentInputDelay -= 1;
        if (this.invincibilityFrames > 0) this.invincibilityFrames -= 1;
        this.displayActionBar();
    }

    public void respawn() {
        List<Entity> near = this.player.getNearbyEntities(150.0D, 150.0D, 150.0D);
        for(Entity entity : near) {
            if(entity instanceof Player) {
                Player nearPlayer = (Player) entity;
                if (nearPlayer.getGameMode() == GameMode.SPECTATOR) continue;
                this.player.teleport(nearPlayer.getLocation());
                this.player.setGameMode(GameMode.ADVENTURE);
                this.setHealth(this.getMaxHealth());
                this.lives -= 1;
                this.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));
                this.dead = false;

            }
        }
    }

    @Override
    public void kill() {
        GConfig.messageManager.messageAll(this.player.getDisplayName() + " was slain...", Colors.DARK_RED);

        this.dead = true;
        this.player.setGameMode(GameMode.SPECTATOR);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));

        plugin.game.onPlayerDeath();
    }

    @Override
    public UUID getId() {
        return this.player.getUniqueId();
    }

    public String getTeam() {
        return this.team;
    }

    @Override
    public @NotNull Vector getKnockback() {
        float value = this.knockback;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            value += item.getKnockback();
        }
        return this.player.getLocation().getDirection().setY(0).normalize().multiply(
                (value)/30);
    }

    public boolean isInGame() {return this.inGame;}
    public void setInGame(boolean value) {this.inGame = value;}
    public int getInputDelay() {return this.currentInputDelay;}
    public void resetInputDelay() {this.currentInputDelay = this.inputDelayMax;}
    public boolean isInvincible() {return this.invincibilityFrames > 0;}
    public void resetInvincibility() {this.invincibilityFrames = this.maxInvincibilityFrames;}
    public boolean isDead() {return this.dead;}


    public void setPlayer(Player player) {
        // resetting the player is required in the event of a disconnect
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getName() {
        return this.player.getName();
    }

    public Integer getMaxHealth() {
        int value = this.maxHealth;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            value += item.getHealth();
        }
        return value;
    }

    public double getMaxStamina() {
        double value = this.maxStamina;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            value += item.getStamina();
        }
        return value;
    }

    public Double getStamina() {
        return this.stamina;
    }

    public Integer getLives() {
        return this.lives;
    }

    @Override
    public int getDamage() {
        int damage = this.damage;
        float damagePercentBoost = 1;

        for (MetaDungeonItem item : this.getEquipment().values()) {
            damage += item.getDamage();
            damagePercentBoost += (float) item.getDamagePercent() / 100;
        }

        return round(damage * damagePercentBoost);
    }

    @Override
    public int getDefence() {
        int defence = this.defence;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            defence += item.getDefence();
        }
        return defence;
    }

    @Override
    public int getArmorPierce() {
        int value = this.armorPierce;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            value += item.getArmorPierce();
        }
        return value;
    }

    public double getRange() {
        double value = this.range;
        for (MetaDungeonItem item : this.getEquipment().values()) {
            value += item.getRange();
        }
        return value;
    }

    @Deprecated
    public HashMap<String, MetaDungeonItem> getGear() {
        return this.gear;
    }

    public HashMap<String, MetaDungeonItem> getEquipment() {
        // returns only gear the player has equipped
        HashMap<String, MetaDungeonItem> output = new HashMap<>();

        for (String key :  this.gear.keySet()) {
            if (this.gear.get(key) != null) {
                output.put(key, this.gear.get(key));
            }
        }
        return output;
    }

    public void setMaxHealth(int amount) {
        this.maxHealth = amount;
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(Math.min(health, this.maxHealth));

        if (this.health <= 0) {
            this.kill();
        } else {
            this.player.setHealth(Math.min(Math.max(((double) this.health / this.maxHealth) * 20, 1), 20));
        }
        this.displayActionBar();
    }

    @Override
    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
        this.player.setWalkSpeed(value / Constants.PLAYER_SPEED_MODIFIER.value());
    }

    public void setLives(int value) {
        this.lives = value;
    }

    public void setStamina(Double amount) {
        this.stamina = Math.min(amount, this.maxStamina);
        this.player.setFoodLevel((int) Math.min( Math.floor(this.stamina / this.maxStamina * 19), 19));
        this.displayActionBar();
    }

    @Override
    public void setVelocity(@NotNull Vector knockback) {
        this.player.setVelocity(knockback);
    }

    public void changeMaxHealth(int amount) {
        this.setMaxHealth(this.maxHealth + amount);
    }

    public void changeStamina(Double amount) {
        if (amount < 0) {
            this.staminaRecovery = 0;
        }
        this.setStamina(this.stamina + amount);
    }

    @Override
    public void changeHealth(int amount) {
        this.setHealth(Math.min(this.health + amount, this.maxHealth));
    }

}
