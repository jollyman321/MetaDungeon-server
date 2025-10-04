package sbs.immovablerod.metaDungeon.commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.util.SQL;
import java.io.File;

public class DebugSQLWrite implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SQL database = null;

        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            database.execute_write(args);

        } finally {
            database.Close();

        }
        return true;
    }
}