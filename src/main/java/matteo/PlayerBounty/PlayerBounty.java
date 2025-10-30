package matteo.PlayerBounty;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

import static matteo.PlayerBounty.BountyConfig.*;
import static matteo.PlayerBounty.BountyConfig.BountyDisplay1;
import static matteo.PlayerBounty.BountyConfig.BountyDisplay2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PlayerBounty.MOD_ID)
public class PlayerBounty {

    public static final String MOD_ID = "playerbounty";
    public static final Logger LOGGER = LogManager.getLogger("PlayerBounty");

    public static final BountyDisplays.Type<BountyDisplays> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PlayerBounty.MOD_ID, "bountydisplays"));

    public static StreamCodec<RegistryFriendlyByteBuf, BountyDisplays> STREAM = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BountyDisplays::bountyDisplay1,
            ByteBufCodecs.INT, BountyDisplays::bounty,
            ByteBufCodecs.STRING_UTF8, BountyDisplays::bountyDisplay2,
            ByteBufCodecs.INT, BountyDisplays::entityID,
            ByteBufCodecs.BOOL, BountyDisplays::deleteDisplay,

            BountyDisplays::new
    );

    public PlayerBounty(ModContainer modContainer) {
        PlayerBounty.init();
        modContainer.getEventBus().addListener(this::modSetup);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(DisplayEvents.class);

        modContainer.registerConfig(ModConfig.Type.SERVER, CONFIG_SPEC);
    }

    public static void init() {
        LOGGER.warn("\nTHIS WARN IS FOR PEOPLE WHO FORGOT TO READ THE MOD DESCRIPTION, WHICH YOU SHOULD ALWAYS DO\nIN ORDER FOR THE MOD TO WORK AS YOU WISH, YOU NEED TO CHANGE THE CONFIG AND RESTART THE GAME");
    }

    public void modSetup(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar payload = event.registrar("1.0.0");
        payload.playToClient(TYPE, STREAM, BountyDisplays::payload);
    }

    public static void packets(ServerPlayer player, String bountydisplay1, int bounty, String bountydisplay2, boolean deleteDisplay) {
        bountyTags(player, bountydisplay1, bounty, bountydisplay2, deleteDisplay);
        PacketDistributor.sendToAllPlayers(new BountyDisplays(bountydisplay1, bounty, bountydisplay2, player.getId(), deleteDisplay));
    }

    public static void bountyTags(Player player, String bountydisplay1, int Bounty, String bountydisplay2, boolean deleteDisplay) {
        CompoundTag tag = player.getPersistentData();

        if (!deleteDisplay) {
            tag.putString("bountydisplay1", bountydisplay1);
            tag.putInt("bounty", Bounty);
            tag.putString("bountydisplay2", bountydisplay2);
            player.refreshDisplayName();
        }
    }

    @SubscribeEvent
    public void deathEvent(LivingDeathEvent event) {
        Entity target = event.getEntity();
        Entity killer = event.getSource().getEntity();

        if (killer instanceof ServerPlayer serverKiller && target instanceof ServerPlayer serverTarget) {
            if (serverKiller.getName() != serverTarget.getName()) {
                if (serverKiller.gameMode.isSurvival() && serverTarget.gameMode.isSurvival()) {
                    DelayHelper.runLater(1, () -> {
                        //KILLER
                        CompoundTag killerTag = killer.getPersistentData();
                        CompoundTag targetTag = target.getPersistentData();
                        double BountyKiller = killerTag.getInt("bounty").orElse(0);
                        double BountyTarget = targetTag.getInt("bounty").orElse(0);

                        boolean wasPositive;
                        wasPositive = BountyKiller >= 0;
                        double RandomGain = (RandomSource.create().nextDouble() * (RandomGainMax.get() - RandomGainMin.get()) + RandomGainMin.get());
                        double RandomGainMultiplier = (RandomSource.create().nextDouble() * (RandomGainMultiplierMax.get() - RandomGainMultiplierMin.get()) + RandomGainMultiplierMin.get());

                        BountyKiller = ((BountyTarget * MultiplierOfGainOverClaimedBounty.get()) + (BountyKiller * (MultiplierOfGainOverKillerBounty.get() + RandomGainMultiplier)) + GainOnKilling.get() + RandomGain);
                        if (BountyKiller < 0 && wasPositive) { BountyKiller = (BountyKiller - 0.01); }
                        else if (BountyKiller >= 0 && !wasPositive) { BountyKiller = (BountyKiller + 0.1); }
                        if (BountyKiller > BountyMaximumValue.get()) { BountyKiller = BountyMaximumValue.get(); }
                        else if (BountyKiller < BountyMinimumValue.get()) { BountyKiller = BountyMinimumValue.get(); }

                        String bountydisplay1 = killer.getName().getString() + BountyDisplay1.get();
                        String bountydisplay2 = BountyDisplay2.get();

                        killerTag.putString("bountydisplay1", bountydisplay1);
                        killerTag.putInt("bounty", (int) BountyKiller);
                        killerTag.putString("bountydisplay2", bountydisplay2);

                        PlayerBounty.packets(serverKiller, bountydisplay1, (int) BountyKiller, bountydisplay2, false);

                        //TARGET
                        double RandomLoss = (RandomSource.create().nextDouble() * (RandomLossMax.get() - RandomLossMin.get()) + RandomLossMin.get());
                        double RandomLossMultiplier = (RandomSource.create().nextDouble() * (RandomLossMultiplierMax.get() - RandomLossMultiplierMin.get()) + RandomLossMultiplierMin.get());

                        wasPositive = BountyTarget >= 0;

                        if (!LoseCompleteBountyOnDeath.get()) { BountyTarget = (int) (BountyTarget * (MultiplierOfLossOverTargetBounty.get() + RandomLossMultiplier) - (LossOnDeath.get() + RandomLoss)); }
                        else { BountyTarget = ((int) -RandomLoss - LossOnDeath.get()) + (BountyTarget * (MultiplierOfLossOverTargetBounty.get() + RandomLossMultiplier)) - BountyTarget; }

                        if (BountyTarget < 0 && wasPositive) { BountyTarget = (BountyTarget - 0.1); }
                        else if (BountyTarget >= 0 && !wasPositive) { BountyTarget = (BountyTarget + 0.1); }
                        if (BountyTarget > BountyMaximumValue.get()) { BountyTarget = BountyMaximumValue.get(); }
                        if (BountyTarget < BountyMinimumValue.get()) { BountyTarget = BountyMinimumValue.get(); }

                        bountydisplay1 = target.getName().getString() + bountydisplay1;

                        targetTag.putString("bountydisplay1", bountydisplay1);
                        targetTag.putInt("bounty", (int) BountyTarget);
                        targetTag.putString("bountydisplay2", bountydisplay2);

                        PlayerBounty.packets(serverTarget, bountydisplay1, (int) BountyTarget, bountydisplay2, false);
                    });
                }
            }
        }
    }
}