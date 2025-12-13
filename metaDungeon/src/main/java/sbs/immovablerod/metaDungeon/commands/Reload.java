package sbs.immovablerod.metaDungeon.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.util.BuildMap;

import java.io.IOException;

public class Reload {
    private static final Logger log = LogManager.getLogger(Reload.class);
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName) {
        return Commands.literal(commandName).executes(ignored -> {
            try {
                plugin.jsonLoader.reload();
            } catch (IOException e) {
                log.error("e: ", e);
            }
            return Command.SINGLE_SUCCESS;
        }).build();

    }

}