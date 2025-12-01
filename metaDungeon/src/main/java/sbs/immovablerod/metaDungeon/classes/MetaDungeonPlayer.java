package sbs.immovablerod.metaDungeon.classes;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.util.ItemUtil;

import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;


public class MetaDungeonPlayer extends MetaDungeonEntity {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final int inputDelayMax;
    private final int maxInvincibilityFrames;
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

        this.player = player;
        this.stamina = 100.0;
        this.baseStamina = 100.0;
        this.maxStamina = 100.0;
        this.staminaRecoveryTime = 20;
        this.staminaRecovery = this.staminaRecoveryTime;
        this.maxHealth = 100;
        this.lives = 4;



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

    public void onTargetHit() {
        if (this.gear.get("mainHand") != null) {
            this.gear.get("mainHand").onTargetHit(this);
            // note that the weapon durability is changed during the attack
            // this just updates the item ui
            this.gear.get("mainHand").changeDurability(1, this.getPlayer().getInventory().getItemInMainHand(), this);
        }
        ;

        if (this.gear.get("mainHand") != null && this.gear.get("mainHand").getAttackSpeed() >= 1) {
            this.attackCoolDown = ((float) 10 /((float) this.gear.get("mainHand").getAttackSpeed() /10));
            this.attackCoolDownPeak = this.attackCoolDown;
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
                this.player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, (2 * (int) this.attackCoolDown), 255));
            }, 4L));

        }


    }

    public boolean getInGame() {return this.inGame;}
    public void setInGame(boolean value) {this.inGame = value;}
    public int getInputDelay() {return this.currentInputDelay;}
    public void resetInputDelay() {this.currentInputDelay = this.inputDelayMax;}
    public boolean isInvincible() {return this.invincibilityFrames > 0;}
    public void resetInvincibility() {this.invincibilityFrames = this.maxInvincibilityFrames;}
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
        this.baseStamina = amount;
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
        this.baseHealth = amount;
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
        this.armorPierce = this.baseArmorPierce;
        this.maxHealth = this.baseHealth;
        this.maxStamina = this.baseStamina;

        float damagePercentBoost = 1;
        for (MetaDungeonItem item : this.gear.values()) {
            if (item != null) {
                damagePercentBoost += (float) item.getDamagePercent() / 100;
                this.defence += item.defence;
                this.damage  += item.damage;
                this.movementSpeed += item.movement;
                this.knockback += item.getKnockback();
                this.armorPierce += item.getArmorPierce();

                this.maxHealth  += item.getHealth();
                this.maxStamina += item.getStamina();
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

    public void kill() {
        this.dead = true;
        this.player.setGameMode(GameMode.SPECTATOR);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));

    }

    @Override
    public void receiveAttack(MetaDungeonEntity attacker) {
        super.receiveAttack(attacker);
        System.out.println("attack received");
        this.invincibilityFrames = this.maxInvincibilityFrames;

        for (String key: this.gear.keySet()) {
            MetaDungeonItem item = this.gear.get(key);
            if (item != null && item.category.equals("armor")) {
                if (key.equals("helmet")) item.changeDurability(1, this.getPlayer().getInventory().getHelmet(), this);
                if (key.equals("chestplate")) item.changeDurability(1, this.getPlayer().getInventory().getChestplate(), this);
                if (key.equals("leggings")) item.changeDurability(1, this.getPlayer().getInventory().getLeggings(), this);
                if (key.equals("boots")) item.changeDurability(1, this.getPlayer().getInventory().getBoots(), this);


            }
        }
    }
}
