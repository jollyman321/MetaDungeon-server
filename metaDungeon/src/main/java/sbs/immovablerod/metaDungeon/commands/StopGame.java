package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.MetaDungeon;

public class StopGame implements CommandExecutor {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.game != null) {
            plugin.game.stopGame();
        }
        return true;
    }
}