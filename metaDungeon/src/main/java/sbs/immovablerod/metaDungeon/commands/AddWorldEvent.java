package sbs.immovablerod.metaDungeon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.util.SQL;

import java.io.File;

public class AddWorldEvent implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /add_world_event <name> <category> <tier> <rarity>

        SQL database = null;
        try {
            // handle saving to db
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            String sql_command = "('" +
                    args[0] +
                    "','" +
                    args[1] +
                    "'," +
                    args[2] +
                    "," +
                    args[3] +
                    ")";

            database.execute_write(
                    "CREATE TABLE IF NOT EXISTS world_events (name string, category string, tier integer, rarity integer)",
                    "DELETE FROM world_events WHERE '" + args[0] + "' IS name",
                    "INSERT INTO world_events values" + sql_command
            );

        } finally {
            database.Close();

        }
        return true;
    }
}
