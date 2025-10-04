package sbs.immovablerod.metaDungeon.commands;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.util.SQL;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DebugSQLQuery implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SQL database = null;

        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            String string = String.join(" ", args);
            Player player = (Player) sender;
            ResultSet query = database.execute_query(string);
            try {
                while (query.next()) {
                    int columnCount = query.getMetaData().getColumnCount();
                    for(int i=1;i<columnCount;i++) {
                        player.sendMessage(query.getString(i));
                    }
                    player.sendMessage("-----");
                }
            } catch (SQLException e) {  e.printStackTrace(System.err);}


        } finally {
            database.Close();

        }
        return true;
    }
}