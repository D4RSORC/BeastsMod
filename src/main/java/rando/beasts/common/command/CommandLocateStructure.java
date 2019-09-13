package rando.beasts.common.command;

import javax.annotation.Nonnull;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandLocateStructure {

	static String structures[] = { "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple",
			"Mineshaft", "RabbitVillage" };

	@Nonnull
	public String getName() {
		return "locate";
	}

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("locateVillage")
				.requires((player) -> player.hasPermissionLevel(2));
		for (String s : structures) {
			literalargumentbuilder.then(Commands.literal(s).executes((sender) -> findStructure(sender, s)));
		}

		dispatcher.register(literalargumentbuilder);
	}

	private static int findStructure(CommandContext<CommandSource> sender, String s) throws CommandSyntaxException {
		BlockPos blockpos = sender.getSource().getWorld().findNearestStructure(s,
				sender.getSource().asPlayer().getPosition(), 1000, false);
		if (blockpos != null) {
			sender.getSource().sendFeedback(
					new TranslationTextComponent("commands.locate.success", s, blockpos.getX(), blockpos.getZ()), true);
		} else {
			throw new CommandException(new TranslationTextComponent("commands.locate.failure", s));
		}
		return 1;
	}
}
