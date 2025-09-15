package matteo.PlayerBounty;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import static matteo.PlayerBounty.BountyConfig.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PlayerBounty.MOD_ID)
public class PlayerBounty {

    public static SimpleChannel network;
    public static IModInfo info;

    public static final String MOD_ID = "playerbounty";
    public static final Logger LOGGER = LogManager.getLogger("PlayerBounty");

    public PlayerBounty() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (c, b) -> true));
        info = ModLoadingContext.get().getActiveContainer().getModInfo();

        PlayerBounty.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ModSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
    }

    public static void init() { LOGGER.warn("\nTHIS WARN IS FOR PEOPLE WHO FORGOT TO READ THE MOD DESCRIPTION, WHICH YOU SHOULD ALWAYS DO\nIN ORDER FOR THE MOD TO WORK AS YOU WISH, YOU NEED TO CHANGE THE CONFIG AND RESTART THE GAME"); }

    private void ModSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PlayerBounty.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "playerbounty"), () -> PlayerBounty.info.getVersion().toString(), string -> true, string -> true);
            PlayerBounty.network.registerMessage(0, BountyDisplays.class, BountyDisplays::toBytes, BountyDisplays::new, BountyDisplays::handle);
        });
    }

    public static void sendPacket(Player player, String bountydisplay1, int bounty, String bountydisplay2, int operation) {
        bountyTags(player, bountydisplay1, bounty, bountydisplay2, operation);
        PlayerBounty.network.send(PacketDistributor.ALL.noArg(), new BountyDisplays(bountydisplay1, bounty, bountydisplay2, player.getId(), operation));
    }

    public static void bountyTags(Player player, String bountydisplay1, int Bounty, String bountydisplay2, int operation) {
        CompoundTag tag = player.getPersistentData();

        if (operation == 0) {
            tag.putString("bountydisplay1", bountydisplay1);
            tag.putInt("bounty", Bounty);
            tag.putString("bountydisplay2", bountydisplay2);
            player.refreshDisplayName();
        }
    }

    ///Current bugs: The value may differ of 1 unit compared to the correct result when going from negative to positive
    @SubscribeEvent
    public void onTarget(LivingHurtEvent event) {
        Entity target = event.getEntity();
        Entity killer = event.getSource().getEntity();

        if (target instanceof ServerPlayer serverTarget && killer instanceof ServerPlayer serverKiller) {
            if (serverKiller != serverTarget) {
                if (serverTarget.gameMode.isSurvival() && serverKiller.gameMode.isSurvival()) {
                    DelayHelper.runLater(2, () -> {
                        if (!serverTarget.isAlive()) {

                            CompoundTag tag = serverTarget.getPersistentData();
                            int Bounty = tag.getInt("bounty");

                            double RandomLoss = (RandomSource.create().nextDouble() * (RandomLossMax.get() + RandomLossMin.get()));
                            double RandomLossMultiplier = (RandomSource.create().nextDouble() * (RandomLossMultiplierMax.get() + RandomLossMultiplierMin.get()));

                            double CorrectValue;
                            if (Bounty >= 0) { CorrectValue = 0.001; } else { CorrectValue = -0.001; }

                            if (LoseCompleteBountyOnDeath.get() == false) { Bounty = (int) (Bounty * (MultiplierOfLossOverTargetBounty.get() + (RandomLossMultiplier + CorrectValue)) - (LossOnDeath.get() + (RandomLoss + CorrectValue))); }
                            else { Bounty = (int) ((int) (-RandomLoss - LossOnDeath.get()) + (Bounty * (MultiplierOfLossOverTargetBounty.get() + (RandomLossMultiplier - 0.001))) - Bounty); }
                            if (Bounty < BountyMinimumValue.get()) { Bounty = BountyMinimumValue.get(); }
                            if (Bounty > BountyMaximumValue.get()) { Bounty = BountyMaximumValue.get(); }

                            String bountydisplay1 = serverTarget.getName().getString() + BountyDisplay1.get();
                            String bountydisplay2 = BountyDisplay2.get();

                            tag.putString("bountydisplay1", bountydisplay1);
                            tag.putInt("bounty", Bounty);
                            tag.putString("bountydisplay2", bountydisplay2);


                            PlayerBounty.sendPacket(serverTarget, bountydisplay1, Bounty, bountydisplay2, 0);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public void onKiller(AttackEntityEvent event) {
        Entity killer = event.getEntity();
        Entity target = event.getTarget();

        if (killer instanceof ServerPlayer serverKiller && target instanceof ServerPlayer serverTarget) {
            if (serverKiller != serverTarget) {
                if (serverKiller.gameMode.isSurvival() && serverTarget.gameMode.isSurvival()) {
                    DelayHelper.runLater(1, () -> {
                        if (!serverTarget.isAlive()) {
                            CompoundTag killerTag = killer.getPersistentData();
                            CompoundTag targetTag = target.getPersistentData();
                            int BountyKiller = killerTag.getInt("bounty");
                            int BountyTarget = targetTag.getInt("bounty");

                            double CorrectValue;
                            if (BountyKiller < 0) { CorrectValue = -0.001; } else { CorrectValue = 0.001; }

                            double RandomGain = (RandomSource.create().nextDouble() * (RandomGainMin.get() + RandomGainMax.get()));
                            double RandomGainMultiplier = (RandomSource.create().nextDouble() * (RandomGainMultiplierMax.get() + RandomGainMultiplierMin.get()));

                            BountyKiller = (int) ((BountyTarget * MultiplierOfGainOverClaimedBounty.get()) + (BountyKiller * (MultiplierOfGainOverKillerBounty.get() + (RandomGainMultiplier + CorrectValue))) + GainOnKilling.get() + (RandomGain + CorrectValue));
                            if (BountyKiller > BountyMaximumValue.get()) { BountyKiller = BountyMaximumValue.get(); }
                            if (BountyKiller < BountyMinimumValue.get()) { BountyKiller = BountyMinimumValue.get(); }

                            String bountydisplay1 = serverKiller.getName().getString() + BountyDisplay1.get();
                            String bountydisplay2 = BountyDisplay2.get();

                            killerTag.putString("bountydisplay1", bountydisplay1);
                            killerTag.putInt("bounty", BountyKiller);
                            killerTag.putString("bountydisplay2", bountydisplay2);


                            PlayerBounty.sendPacket(serverKiller, bountydisplay1, BountyKiller, bountydisplay2, 0);
                        }
                    });
                }
            }
        }
    }
}