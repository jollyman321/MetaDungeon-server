package sbs.immovablerod.metaDungeon.commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.util.SQL;
import sbs.immovablerod.metaDungeon.util.Serialize;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadItemFromDB implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SQL database = null;

        try {
            try {
                database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

                Player player = (Player) sender;
                String itemName = args[0];

                ItemStack item;
                try (ResultSet query = database.execute_query("SELECT * FROM items WHERE '" + itemName + "' IS name")) {
                    item = Serialize.deserializeItem(query.getString("item"));
                }

                player.getInventory().addItem(item);

            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }

        } finally {
            database.Close();

        }
        return true;
    }
}