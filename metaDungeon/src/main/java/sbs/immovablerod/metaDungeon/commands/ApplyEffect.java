package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.enums.Effects;

public class ApplyEffect implements CommandExecutor {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        Entity target = (Entity) sender;

        Effects effect = Effects.valueOf(args[0].toString());
        int duration = Integer.parseInt(args[1]);
        int level = Integer.parseInt(args[2]);


        plugin.players.get(target.getUniqueId()).addEffect(new MetaDungeonEffect(effect, duration, level));

        return true;
    }
}
