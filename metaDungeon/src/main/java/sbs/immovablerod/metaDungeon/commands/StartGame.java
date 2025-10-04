package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.game.Game;
import sbs.immovablerod.metaDungeon.game.DungeonMap;

public class StartGame implements CommandExecutor {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (plugin.game != null)  plugin.game.stopGame();
        if (args.length != 1) return false;

        plugin.game = new Game();
        if (plugin.map == null) {
            plugin.map = new DungeonMap(args[0]);
        }
        plugin.game.startGame(args[0]);
        return true;
    }
}