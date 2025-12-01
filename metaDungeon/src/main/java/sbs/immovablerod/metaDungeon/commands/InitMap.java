package sbs.immovablerod.metaDungeon.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sbs.immovablerod.metaDungeon.lootbox.Generate;
import sbs.immovablerod.metaDungeon.util.SQL;

import java.io.File;
import java.util.List;


public class InitMap implements CommandExecutor {
    private final ObjectMapper chest_locations_object_mapper = new ObjectMapper();
    private String chest_locations;
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /init_map <name> <x> <y> <z> <xdelta> <ydelta> <zdelta> <spawnx> <spawny> <spawnz>
        if (args.length == 10) {
            List<List<Integer>> valid_points = Generate.create_lot_box_map(Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),
                    Integer.parseInt(args[5]),
                    Integer.parseInt(args[6]));

            SQL database = new SQL("plugins" + File.separator + "metaDungeon" + File.separator + "database.sqlite");


            try {
                chest_locations = chest_locations_object_mapper.writeValueAsString(valid_points);
            } catch (JsonProcessingException e) {
                e.printStackTrace(System.err);
                return false;
            }

            try {
                String sql_command = "('" +
                        args[0] +
                        "'," + args[1] + "," + args[2] + "," + args[3] + "," + args[4] + "," + args[5] + "," + args[6] +
                        ",'" +
                        chest_locations +
                        "','" +
                        "test" +
                        "'," +
                        args[7] + "," + args[8] + "," + args[9] +
                        ")";
                database.execute_write(
                        "INSERT INTO maps values" + sql_command
                );

            } finally {
                database.Close();
            }

        }
        return true;
    }
}