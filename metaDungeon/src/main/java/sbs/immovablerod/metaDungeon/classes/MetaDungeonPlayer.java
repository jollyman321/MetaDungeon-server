package sbs.immovablerod.metaDungeon.classes;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.util.ItemUtil;

import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;


public class MetaDungeonPlayer extends MetaDungeonEntity {
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

        this.player = player;
        this.stamina = 100.0;
        this.maxStamina = 100.0;
        this.staminaRecoveryTime = 20;
        this.staminaRecovery = this.staminaRecoveryTime;
        this.maxHealth = 100;
        this.lives = 3;



        // modified by equipment and buffs
        this.gear = new HashMap<>();


        this.attackCoolDown = 0;
        this.attackCoolDownPeak = 0;

        // remove 1.9+ weapon effect e.g sweep attack
        this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        this.player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(-20);



    }

    public void onTargetHit() {
        for (MetaDungeonItem item : this.gear.values()) {
            if (item != null) item.onTargetHit(this);
        }
        if (this.gear.get("mainHand") != null && this.gear.get("mainHand").getAttackSpeed() >= 1) {
            this.attackCoolDown = ((float) 10 /((float) this.gear.get("mainHand").getAttackSpeed() /10));
            this.attackCoolDownPeak = this.attackCoolDown;
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2 * (int) this.attackCoolDown, 249));
        }


    }

    public boolean getInGame() {return this.inGame;}
    public void setInGame(boolean value) {this.inGame = value;}
    public boolean isDead() {return this.dead;}

    public boolean canAttack() {return this.attackCoolDown <= 0 &&
            this.gear.get("mainHand") != null
            && this.gear.get("mainHand").getAttackSpeed() >= 1
            && this.gear.get("mainHand").getStaminaCost() <= this.stamina;

    }

    public Player getPlayer() {return this.player;}

    public void setPlayer(Player player) {
        // resetting the player is required in the event of a disconnect
        this.player = player;
    }

    public Double getStamina() {return stamina;}

    public Integer getMaxHealth() {return this.maxHealth;}

    public Integer getLives() {return this.lives;}

    public HashMap<String, MetaDungeonItem> getGear() {return this.gear;}

    public void setStamina(Double amount) {
        this.stamina = Math.min(amount, this.maxStamina);
        this.player.setFoodLevel((int) Math.min( Math.floor(this.stamina / this.maxStamina * 19), 19));
        this.displayActionBar();
    }
    public void changeStamina(Double amount) {
        if (amount < 0) {
            this.staminaRecovery = 0;
        }
        this.setStamina(this.stamina + amount);
    }
    public double getMaxStamina() {
        return this.maxStamina;
    }
    public void setMaxStamina(double amount) {
        this.maxStamina = amount;
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

    public void setMaxHealth(int amount) {
        this.maxHealth = amount;
    }

    @Override
    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
        this.player.setWalkSpeed(value / Constants.PLAYER_SPEED_MODIFIER.value());
    }
    public void changeHealth(int amount) {
        this.setHealth(Math.min(this.health + amount, this.maxHealth));
    }

    public void updateStats() {
        this.defence = this.baseDefence;
        this.damage = this.baseDamage;
        this.movementSpeed = this.baseMovementSpeed;
        this.knockback = this.baseKnockback;

        float damagePercentBoost = 1;
        for (MetaDungeonItem item : this.gear.values()) {
            if (item != null) {
                damagePercentBoost += (float) item.getDamagePercent() / 100;
                this.defence += item.defence;
                this.damage  += item.damage;
                this.movementSpeed += item.movement;
                this.knockback += item.getKnockback();
            }
        }

        this.damage = round(this.damage * damagePercentBoost);



        this.setMovementSpeed(this.movementSpeed);
        this.displayActionBar();
    }

    public void updateInventory() {
        this.gear.put("mainHand", ItemUtil.getAdvancedItem(this.player.getInventory().getItemInMainHand()));
        this.gear.put("helmet",ItemUtil.getAdvancedItem(this.player.getInventory().getHelmet()));
        this.gear.put("chestplate",ItemUtil.getAdvancedItem(this.player.getInventory().getChestplate()));
        this.gear.put("leggings", ItemUtil.getAdvancedItem(this.player.getInventory().getLeggings()));
        this.gear.put("boots",ItemUtil.getAdvancedItem(this.player.getInventory().getBoots()));
    }

    public void displayActionBar() {

        if (!this.dead) {
            this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("@HEALTH/@MAX_HEALTH   |   @DAMAGE   |   @DEFENCE   |   @STAMINA/@MAX_STAMINA".replace(
                            "@HEALTH", Symbols.HEART.color().code() + String.valueOf(this.health)
                    ).replace("@MAX_HEALTH", this.maxHealth.toString() + Symbols.HEART).replace(
                            "@DAMAGE", Symbols.DAMAGE.color().code() + String.valueOf(this.damage) + Symbols.DAMAGE).replace(
                            "@DEFENCE", Symbols.DEFENCE.color().code() + String.valueOf(this.defence) + Symbols.DEFENCE).replace(
                            "@STAMINA", Symbols.STAMINA.color().code() + round(this.stamina)).replace(
                            "@MAX_STAMINA", String.valueOf(round(this.maxStamina)) + Symbols.STAMINA)
            ));
        } else {
            this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    "§eYou are §cDEAD§e left click to respawn @COLOR(@LIVES lives remaining)".replace
                            ("@COLOR", Symbols.LIFE.color().code()).replace
                            ("@LIVES", String.valueOf(this.lives - 1))
            ));
        }
    }

    public void update() {
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
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 4, 249));
        }

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

    public void kill() {
        this.dead = true;
        this.player.setGameMode(GameMode.SPECTATOR);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));

    }
}
