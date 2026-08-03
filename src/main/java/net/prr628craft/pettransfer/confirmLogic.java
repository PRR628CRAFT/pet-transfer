package net.prr628craft.pettransfer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import static net.prr628craft.pettransfer.PetTransfer.LOGGER;

public class confirmLogic {
    private static Component getPrefix() {
        return Component.literal("[PetTransfer] ").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD);
    }
    public static int confirmFunc(ServerPlayer gifter, Entity pet, ServerPlayer recipient, Boolean auto) {
        String gifterName = gifter.getGameProfile().name();
        String petName = pet.getDisplayName().getString();
        String recipientName = recipient.getGameProfile().name();
        LOGGER.info("{} is attempting to transfer {} to {}.", gifterName, petName, recipientName);
        if (auto) {
            gifter.sendSystemMessage(
                    Component.empty()
                            .append(getPrefix())
                            .append(Component.translatable("commands.pettransfer.confirm.autoconfirmed", recipientName, petName))
            );
        } else {
            gifter.sendSystemMessage(
                    Component.empty()
                            .append(getPrefix())
                            .append(Component.translatable("commands.pettransfer.confirm.confirmed", recipientName, petName))
            );
        }
        recipient.sendSystemMessage(
                Component.empty()
                        .append(getPrefix())
                        .append(Component.translatable("commands.pettransfer.confirm.ask",
                                gifterName,
                                petName,
                                Component.literal("/pettransfer accept").withStyle(ChatFormatting.GRAY),
                                Component.translatable("commands.pettransfer.confirm.ask.click").withStyle(ChatFormatting.GREEN)
                                        .withStyle(Style.EMPTY.withClickEvent(new ClickEvent.RunCommand("/pettransfer accept")))
                        ))
        );
        return 1;
    }
}