package sbs.immovablerod.metaDungeon.commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.util.ItemUtil;
import sbs.immovablerod.metaDungeon.util.SQL;
import sbs.immovablerod.metaDungeon.util.Serialize;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadItemFromDB implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        else if (args.length != 1) {
            return false;
        }

        MetaDungeonItem item = ItemUtil.createItem(args[0]);

        ((Player) sender).getInventory().addItem(item);
        return true;
    }
}