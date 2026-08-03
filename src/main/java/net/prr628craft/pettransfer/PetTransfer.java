package net.prr628craft.pettransfer;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import static net.prr628craft.pettransfer.transferLogic.transferFunc;

public class PetTransfer implements ModInitializer {
	public static final String MOD_ID = "pettransfer";

	// Logger setup
	public static final Logger LOGGER = LoggerFactory.getLogger("PetTransfer");

	// Prefix helper to maintain consistency and prevent style bleeding
	private static Component getPrefix() {
		return Component.literal("[PetTransfer] ").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD);
	}

	// Initialize commands
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("pettransfer").executes(context -> {
				context.getSource().sendSuccess(() -> Component.empty()
								.append(getPrefix())
								.append(Component.translatable("commands.pettransfer.main", Component.literal("/pettransfer help").withStyle(ChatFormatting.GRAY)))
						, false);
				return 1;
			}).then(Commands.literal("help").executes(helpdialog -> {
				helpdialog.getSource().sendSuccess(() -> Component.empty()
								.append(getPrefix())
								.append(Component.translatable("commands.pettransfer.help.main")).append("\n")

								.append(Component.literal("help").withStyle(ChatFormatting.YELLOW))
								.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
								.append(Component.translatable("commands.pettransfer.help.help").withStyle(ChatFormatting.GRAY)).append("\n")

								.append(Component.literal("transfer").withStyle(ChatFormatting.YELLOW))
								.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
								.append(Component.translatable("commands.pettransfer.help.transfer").withStyle(ChatFormatting.GRAY)).append("\n")

								.append(Component.literal("confirm").withStyle(ChatFormatting.YELLOW))
								.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
								.append(Component.translatable("commands.pettransfer.help.confirm").withStyle(ChatFormatting.GRAY)).append("\n")

								.append(Component.literal("accept").withStyle(ChatFormatting.YELLOW))
								.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
								.append(Component.translatable("commands.pettransfer.help.accept").withStyle(ChatFormatting.GRAY)).append("\n")

								.append(Component.literal("version").withStyle(ChatFormatting.YELLOW))
								.append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
								.append(Component.translatable("commands.pettransfer.help.version").withStyle(ChatFormatting.GRAY)).append("\n")
						, false);
				return 1;
			})).then(Commands.literal("transfer").then(Commands.argument("recipient", EntityArgument.player())
					.executes(transferdialog -> {
						return transferFunc(transferdialog, false);
					}).then(Commands.argument("autoconfirm", BoolArgumentType.bool())
							.executes(transferdialog -> {
								return transferFunc(transferdialog, true);
							})
					))).then(Commands.literal("confirm").executes(context -> {
				context.getSource().sendSuccess(() -> Component.empty()
								.append(getPrefix())
								.append(Component.translatable("commands.pettransfer.confirm.confirmed", Component.translatable("placeholder.pettransfer.pet"), Component.translatable("placeholder.pettransfer.recipient")))
						, false);
				return 1;

			})).then(Commands.literal("accept").executes(context -> {
				context.getSource().sendSuccess(() -> Component.empty()
								.append(getPrefix())
								.append(Component.translatable("commands.pettransfer.accept.recipient", Component.translatable("placeholder.pettransfer.pet"), Component.translatable("placeholder.pettransfer.recipient")))
						, false);
				return 1;

			})).then(Commands.literal("version").executes(context -> {
						context.getSource().sendSuccess(() -> Component.empty()
										.append(getPrefix())
										.append(Component.translatable("commands.pettransfer.version"))
										.append(Component.literal(" 1.0.0"))
										.append(Component.literal("-").withStyle(ChatFormatting.GRAY))
										.append(Component.literal("alpha.1").withStyle(ChatFormatting.RED))
								, false);
						return 1;

					})
			));

		});
		LOGGER.info("Successfully loaded PetTransfer by PRR628! :3");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
