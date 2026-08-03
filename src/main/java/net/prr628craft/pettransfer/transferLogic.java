package net.prr628craft.pettransfer;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;

import static net.prr628craft.pettransfer.PetTransfer.LOGGER;

public class transferLogic {
    // Prefix helper to maintain consistency and prevent style bleeding
    private static Component getPrefix() {
        return Component.literal("[PetTransfer] ").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD);
    }

    public static int transferFunc(CommandContext<CommandSourceStack> transferdialog, Boolean autoconfirm) throws CommandSyntaxException {
        ServerPlayer recipient = EntityArgument.getPlayer(transferdialog, "recipient"); // Fetches the recipient from the cmd argument
        String recipientName = recipient.getGameProfile().name();
        ServerPlayer gifter = transferdialog.getSource().getPlayerOrException(); // fetches the gifter from the command source
        String gifterName = gifter.getGameProfile().name();
        Entity entity = PetCast.getLookedAtEntity(gifter, 8); // fetches the entity from the raycast helpurr

        if (entity != null) {
            String entityName = entity.getDisplayName().getString();
            if (entity instanceof TamableAnimal pet) {
                if (pet.isTame()) {
                    if (pet.isOwnedBy(gifter)) {
                        if (!recipient.equals(gifter)) {
                            if (autoconfirm) {
                                confirmLogic.confirmFunc(gifter, pet, recipient, Boolean.TRUE);
                                transferdialog.getSource().sendSuccess(() -> Component.empty()

                                        , false);
                                return 1;
                            }

                            LOGGER.info("{} is preparing to transfer {} to {}.", gifterName, entityName, recipientName);
                            transferdialog.getSource().sendSuccess(() -> Component.empty()
                                            .append(getPrefix())
                                            .append(Component.translatable("commands.pettransfer.transfer.confirm",
                                                    entityName,
                                                    recipientName,
                                                    Component.literal("/pettransfer confirm").withStyle(ChatFormatting.GRAY),
                                                    Component.translatable("commands.pettransfer.transfer.confirm.click").withStyle(ChatFormatting.GREEN)
                                                            .withStyle(Style.EMPTY.withClickEvent(new ClickEvent.RunCommand("/pettransfer confirm")))
                            )), false);
                            return 1;
                        } else {
                            transferdialog.getSource().sendFailure(Component.empty()
                                    .append(getPrefix())
                                    .append(Component.translatable("commands.pettransfer.transfer.fail.self").withStyle(ChatFormatting.RED))
                            );
                            return 0; // this basically stops you from gifting stuff to yourself. seriously, don't do that.
                        }
                    } else {
                        transferdialog.getSource().sendFailure(Component.empty()
                                .append(getPrefix())
                                .append(Component.translatable("commands.pettransfer.transfer.fail.notowned", entityName).withStyle(ChatFormatting.RED))
                        );
                        return 0; // stops you from taking other peoples pets away!
                    }
                } else {
                    transferdialog.getSource().sendFailure(Component.empty()
                            .append(getPrefix())
                            .append(Component.translatable("commands.pettransfer.transfer.fail.untamed", entityName).withStyle(ChatFormatting.RED))
                    );
                    return 0; // cant give a pet that isn't tamed.
                }
            } else {
                transferdialog.getSource().sendFailure(Component.empty()
                        .append(getPrefix())
                        .append(Component.translatable("commands.pettransfer.transfer.fail.untameable", entityName).withStyle(ChatFormatting.RED))
                );
                return 0; // stops you from transferring a pet that ISN'T A PET
            }
        } else {
            transferdialog.getSource().sendFailure(Component.empty()
                    .append(getPrefix())
                    .append(Component.translatable("commands.pettransfer.transfer.fail.noentity").withStyle(ChatFormatting.RED))
            );
            return 0; // stops you from transferring air
        }
    }
}
