package sbs.immovablerod.metaDungeon.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.game.GConfig;

public class TriggerEvent implements CommandExecutor {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("Usage: <name> <level>");
            return false;
        }
        GConfig.eventManager.add(args[0], Integer.parseInt(args[1]));
        return true;
    }
}
