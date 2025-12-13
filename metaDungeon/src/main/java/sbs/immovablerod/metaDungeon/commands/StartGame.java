package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.game.Runtime;

public class StartGame implements CommandExecutor {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (plugin.game != null)  plugin.game.stopGame();
        if (args.length != 1) return false;

        this.plugin.world = Bukkit.getWorld("world");

        plugin.game = new Runtime();
        plugin.game.startGame(args[0]);
        return true;
    }
}