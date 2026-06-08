package net.matteo.playerbounty.compats;

import static net.matteo.playerbounty.configs.CreateNumismaticsConfig.*;
import static net.matteo.playerbounty.utils.mods.CreateNumismaticsUtils.*;

import dev.ithundxr.createnumismatics.content.backend.BankSavedData;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public class CreateNumismatics {

    //This part is particularly confusing... Good luck!
    public static void deathEvent(Player killer, Player target) {
        double killerBalance = spurValue(killer, true);
        double targetBalance = BankSavedData.load(target.getServer()).getAccounts().get(target.getUUID()).getBalance();
        int moveInventoryToBank = 0;

        killerBalance += (Gain.get() + randomGain())
                + (killerBalance * (randomGainMultiplier() + KillerMultiplier.get()))
                + (spurValue(target, true) * ClaimMultiplier.get());

        BankSavedData.load(killer.getServer()).getAccounts().get(killer.getUUID()).setBalance((int) killerBalance);

        if (CompleteLoss.get()) {
            targetBalance = (-Loss.get() - randomLoss()) - (targetBalance * (TargetMultiplier.get() + randomLossMultiplier()));
        } else {
            targetBalance -= (Loss.get() + randomLoss()) + (targetBalance * (TargetMultiplier.get() + randomLossMultiplier()));
        }

        if (InventoryCoinsMove.get()) for (ItemStack stack : target.getInventory().items) {
            ResourceLocation registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

            if ("numismatics".equals(registry.getNamespace())) {
                switch (registry.getPath()) {
                    case "sun" -> {
                        moveInventoryToBank += 4096 * stack.getCount();
                        stack.setCount(0);
                    }

                    case "crown" -> {
                        moveInventoryToBank += 512 * stack.getCount();
                        stack.setCount(0);
                    }

                    case "cog" -> {
                        moveInventoryToBank += 64 * stack.getCount();
                        stack.setCount(0);
                    }

                    case "sprocket" -> {
                        moveInventoryToBank += 16 * stack.getCount();
                        stack.setCount(0);
                    }

                    case "bevel" -> {
                        moveInventoryToBank += 8 * stack.getCount();
                        stack.setCount(0);
                    }

                    case "spur" -> {
                        moveInventoryToBank += stack.getCount();
                        stack.setCount(0);
                    }
                }
            }
        }

        if (targetBalance + moveInventoryToBank < 0) {
            String message = "";

            for (ItemStack stack : target.getEnderChestInventory().getItems()) {
                ResourceLocation registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

                if ("numismatics".equals(registry.getNamespace())) {
                    switch (registry.getPath()) {
                        case "sun" -> {
                            int stackValue = 4096 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 4096);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "crown" -> {
                            int stackValue = 512 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 512);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "cog" -> {
                            int stackValue = 64 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 64);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "sprocket" -> {
                            int stackValue = 16 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 64);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "bevel" -> {
                            int stackValue = 8 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 8);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "spur" -> {
                            stack.setCount(stack.getCount() - moveInventoryToBank);
                            moveInventoryToBank = (stack.getCount() - moveInventoryToBank) / (stack.getCount() / moveInventoryToBank);
                        }
                    }

                    if (moveInventoryToBank + targetBalance >= 0) {
                        target.displayClientMessage(Component.literal("§ePlayerBounty§r: Some of your Create Numismatics coins were moved from your ender chest to your bank, otherwise your bank would have been less than 0 which is breaking the game"), false);
                        BankSavedData.load(killer.getServer()).getAccounts().get(target.getUUID()).setBalance((int) targetBalance + moveInventoryToBank);
                        return;
                    } else if (moveInventoryToBank != 0 && InventoryCoinsMove.get()) message = "§ePlayerBounty§r: Some of your Create Numismatics coins were moved from both your inventory and ender chest to your bank, otherwise your bank would have been less than 0 which is breaking the game";
                }
            }

            if (!InventoryCoinsMove.get()) for (ItemStack stack : target.getInventory().items) {
                ResourceLocation registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

                if ("numismatics".equals(registry.getNamespace())) {
                    switch (registry.getPath()) {
                        case "sun" -> {
                            int stackValue = 4096 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 4096);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "crown" -> {
                            int stackValue = 512 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 512);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "cog" -> {
                            int stackValue = 64 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 64);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "sprocket" -> {
                            int stackValue = 16 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 16);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "bevel" -> {
                            int stackValue = 8 * stack.getCount();

                            if (stackValue >= moveInventoryToBank) {
                                stack.setCount((stackValue - moveInventoryToBank) / 6);
                                moveInventoryToBank = (stackValue - moveInventoryToBank) / (stackValue / moveInventoryToBank);
                            }
                        }

                        case "spur" -> {
                            stack.setCount(stack.getCount() - moveInventoryToBank);
                            moveInventoryToBank = (stack.getCount() - moveInventoryToBank) / (stack.getCount() / moveInventoryToBank);
                        }
                    }
                }

                if (moveInventoryToBank + targetBalance >= 0) {
                    target.displayClientMessage(Component.literal(message.isEmpty() ? "§ePlayerBounty§r: Some of your Create Numismatics coins were moved from your inventory to your bank to avoid the bank being less than 0, which breaks the game" : message), false);
                    BankSavedData.load(target.getServer()).getAccounts().get(target.getUUID()).setBalance((int) targetBalance + moveInventoryToBank);
                    return;
                }
            }
        }

        BankSavedData.load(target.getServer()).getAccounts().get(target.getUUID()).setBalance(targetBalance > 0 ? (int) targetBalance + moveInventoryToBank : 0);
    }
}
