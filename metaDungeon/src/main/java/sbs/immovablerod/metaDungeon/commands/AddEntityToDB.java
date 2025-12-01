package sbs.immovablerod.metaDungeon.commands;

import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.util.SQL;
import sbs.immovablerod.metaDungeon.util.Serialize;

import java.io.File;
import java.util.List;

public class AddEntityToDB implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /add_entity_to_db <targetName> <name> <tier> <groupMin> <groupMax>
        World world = Bukkit.getWorld("world");
        Player player = (Player) sender;

        List<Entity> entities = world.getEntities();
        Entity targetEntity = null;

        for (Entity entity : entities) {
            if (entity.getName().equals(args[0])) {
                targetEntity = entity;

            }
        }

        if (targetEntity == null) {
            player.sendMessage("could not find entity '" + args[0] + "'");
            return false;

        }
        ReadWriteNBT targetEntityNbt = Serialize.serializeEntity(targetEntity);
        //targetEntityNbt.removeKey("CustomName");
        targetEntityNbt.removeKey("UUID");
        targetEntityNbt.removeKey("Pos");
        targetEntityNbt.removeKey("Motion");
        targetEntityNbt.removeKey("Rotation");

        SQL database = null;
        try {
            // handle saving to db
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            String sql_command = "('" +
                    args[1] +
                    "'," +
                    args[2] +
                    "," +
                    args[3] +
                    "," +
                    args[4] +
                    ",'" +
                    targetEntity.getType().toString() +
                    "','" +
                    targetEntityNbt.toString().replace("'","''") +
                    "')";

            database.execute_write(
                    "CREATE TABLE IF NOT EXISTS entities (name string, tier integer, groupMin integer, groupMax integer, type string, entity string)",
                    "DELETE FROM entities WHERE '" + args[1] + "' IS name",
                    "INSERT INTO entities values" + sql_command
            );

            targetEntity.remove();
        } finally {
            database.Close();

        }
        return true;
    }
}
