package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.util.SQL;
import sbs.immovablerod.metaDungeon.util.Serialize;

import java.io.File;

public class AddItemToDB implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /add_item_to_db <name> <category> <cost> <rarity> <tier> <consumable> <randomGen>
        //                 string string     int    int<0-6> int    bool

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        String item_serialize = Serialize.serializeItem(item);

        StringBuilder sql_command = getStringBuilder(args, item_serialize);
        SQL database = null;

        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            database.execute_write(
                    "CREATE TABLE IF NOT EXISTS items (name string, category string, cost integer, rarity integer, tier integer, consumable integer, randomGen integer, item string)",
                    "DELETE FROM items WHERE '" + args[0] + "' IS name",
                    "INSERT INTO items values" + sql_command
            );

        } finally {
            try {
                database.Close();
            } catch (NullPointerException e) {
                e.printStackTrace(System.err);
            }
        }
        return true;
    }

    private static StringBuilder getStringBuilder(String[] args, String item) {
        String name = args[0];
        String category = args[1];
        int cost = Integer.parseInt(args[2]);
        int rarity = Integer.parseInt(args[3]);
        int tier = Integer.parseInt(args[4]);
        int consumable = Integer.parseInt(args[5]);
        int randomGen = Integer.parseInt(args[6]);

        StringBuilder sql_command = new StringBuilder();
        sql_command.append("('");
        sql_command.append(name);
        sql_command.append("','");
        sql_command.append(category);
        sql_command.append("',");
        sql_command.append(cost);
        sql_command.append(",");
        sql_command.append(rarity);
        sql_command.append(",");
        sql_command.append(tier);
        sql_command.append(",");
        sql_command.append(consumable);
        sql_command.append(",");
        sql_command.append(randomGen);
        sql_command.append(",'");
        sql_command.append(item.replace("'", "''"));
        sql_command.append("')");
        return sql_command;
    }
}
