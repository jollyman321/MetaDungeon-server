package sbs.immovablerod.metaDungeon.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.util.BuildMap;

import java.io.IOException;

public class CreateMap {
    private static final Logger log = LogManager.getLogger(CreateMap.class);

    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName) {
        return Commands.literal(commandName)
                // Require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.argument("name", StringArgumentType.word())
                    .then(Commands.argument("pos1", ArgumentTypes.blockPosition())
                            .then(Commands.argument("pos2", ArgumentTypes.blockPosition())
                                    .then(Commands.argument("playerSpawnPos", ArgumentTypes.blockPosition())
                                            .executes(CreateMap::executeCommandLogic)
                                    )
                            )
                    )
                )
                .build();

    }

    private static int executeCommandLogic(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            return Command.SINGLE_SUCCESS;
        }

        final String name = ctx.getArgument("name", String.class);
        final BlockPosition pos1Raw = ctx.getArgument("pos1", BlockPositionResolver.class).resolve(ctx.getSource());
        final BlockPosition pos2Raw = ctx.getArgument("pos2", BlockPositionResolver.class).resolve(ctx.getSource());
        final BlockPosition playerSpawnPosRaw = ctx.getArgument("playerSpawnPos", BlockPositionResolver.class).resolve(ctx.getSource());

        final Location pos1 = new Location(player.getWorld(), pos1Raw.x(), pos1Raw.y(), pos1Raw.z());
        final Location pos2 = new Location(player.getWorld(), pos2Raw.x(), pos2Raw.y(), pos2Raw.z());
        final Location playerSpawnPos = new Location(player.getWorld(), playerSpawnPosRaw.x(), playerSpawnPosRaw.y(), playerSpawnPosRaw.z());


        try {
            new BuildMap(player.getWorld(), name,  pos1,  pos2,  playerSpawnPos);
        } catch (IOException e) {
            log.error("e: ", e);
        }
        ctx.getSource().getSender().sendPlainMessage("map created at" + pos1Raw + " to " + pos2Raw);

        return Command.SINGLE_SUCCESS;
    }
}