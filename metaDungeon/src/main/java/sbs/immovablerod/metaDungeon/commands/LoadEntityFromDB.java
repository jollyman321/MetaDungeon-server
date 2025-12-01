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
import sbs.immovablerod.metaDungeon.util.SpawnEntity;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadEntityFromDB implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        } else if (args.length != 1) {
            return false;
        }
        Player player = (Player) sender;

        SpawnEntity.spawn(args[0], player.getLocation());
        return true;
    }
}