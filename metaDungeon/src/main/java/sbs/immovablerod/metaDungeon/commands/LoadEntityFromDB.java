package sbs.immovablerod.metaDungeon.commands;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.util.SQL;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadEntityFromDB implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        //player.getWorld().spawnEntity(player.getLocation());
        SQL database = null;
        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");


            try (ResultSet query = database.execute_query("SELECT * FROM entities WHERE '" + args[0] + "' IS name")) {

                System.out.println(query.getString("type"));
                System.out.println(query.getString("entity"));

                EntityType entityType = EntityType.valueOf(query.getString("type"));
                Entity entity = player.getWorld().spawnEntity(player.getLocation(), entityType);
                ReadWriteNBT nbtData = NBT.parseNBT(query.getString("entity"));
                //nbtData.setString("CustomName", query.getString("name"));
                NBT.modify(entity, nbt -> {
                    nbt.mergeCompound(nbtData);
                });

            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        } finally {
            database.Close();
        }

        return true;
    }
}