package net.matteo.playerbounty;

import net.matteo.playerbounty.utils.GetValues;

import net.sirgrantd.sg_economy.api.SGEconomyApi;

import tallestred.numismaticoverhaul.cap.CurrencyHolder;

import dev.ithundxr.createnumismatics.content.backend.BankSavedData;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static net.minecraft.network.chat.Component.literal;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

import com.mojang.brigadier.arguments.IntegerArgumentType;

@EventBusSubscriber
public class ModCommands {

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("playerbounty").requires(source -> source.hasPermission(4))
                .then(Commands.literal("mod")
                        .then(Commands.literal("playerbounty")
                                .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(command -> {
                                            Player player = EntityArgument.getPlayer(command, "player");
                                            int value = IntegerArgumentType.getInteger(command, "value");
                                            player.getPersistentData().putDouble("bounty", value);

                                            command.getSource().sendSystemMessage(literal(player.getName().getString() + "'s bounty set to " + value));
                                            return 0;
                                        })))))

                        .then(Commands.literal("sg_economy").requires(command -> GetValues.sg_economy_system)
                                .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(command -> {
                                            Player player = EntityArgument.getPlayer(command, "player");
                                            int value = IntegerArgumentType.getInteger(command, "value");
                                            SGEconomyApi.setBalance(player, value);

                                            command.getSource().sendSystemMessage(literal(player.getName().getString() + "'s balance set to " + value));
                                            return 0;
                                        })))))

                        .then(Commands.literal("numismatic_overhaul").requires(command -> GetValues.numismatic_overhaul_system)
                                .then(Commands.literal("set")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                                        .executes(command -> {
                                                            Player player = EntityArgument.getPlayer(command, "player");
                                                            int value = IntegerArgumentType.getInteger(command, "value");
                                                            CurrencyHolder.setValue(player, value);

                                                            command.getSource().sendSystemMessage(literal(player.getName().getString() + "'s purse set to " + value + " bronze coins"));
                                                            return 0;
                                                        })))))

                                .then(Commands.literal("create_numismatics").requires(command -> GetValues.create_numismatics_system)
                                        .then(Commands.literal("set")
                                                .then(Commands.literal("spurs")
                                                .then(Commands.argument("player", EntityArgument.player())
                                                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                                        .executes(command -> {
                                                            Player player = EntityArgument.getPlayer(command, "player");
                                                            int value = IntegerArgumentType.getInteger(command, "value");
                                                            //noinspection DataFlowIssue
                                                            BankSavedData.load(player.getServer()).getAccounts().get(player.getUUID()).setBalance(value);

                                                            command.getSource().sendSystemMessage(literal(player.getName().getString() + "'s bank set to " + value + " spurs (" + value / 64 + " cogs)" +
                                                                    "\nNote: This will not remove items from the inventory, if the display seems wrong it's likely the player have coins inside the inventory."));
                                                            return 0;
                                                        }))))
                                                .then(Commands.literal("cogs")
                                                        .then(Commands.argument("player", EntityArgument.player())
                                                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                                                        .executes(command -> {
                                                                            Player player = EntityArgument.getPlayer(command, "player");
                                                                            int value = IntegerArgumentType.getInteger(command, "value");
                                                                           //noinspection DataFlowIssue
                                                                            BankSavedData.load(player.getServer()).getAccounts().get(player.getUUID()).setBalance(value * 64);

                                                                            command.getSource().sendSystemMessage(literal(player.getName().getString() + "'s bank set to " + value + " cogs" +
                                                                                    "\nNote: This will not remove items from the inventory, if the display seems wrong it's likely the player have coins inside the inventory."));
                                                                            return 0;
                                                                        }))))
                                        ))
                )
        );
    }

}
