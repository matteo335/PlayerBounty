package net.matteo.playerbounty.internal;

import net.matteo.playerbounty.configs.ServerConfig;
import net.matteo.playerbounty.internal.provider.BountyHunterProvider;
import net.matteo.playerbounty.repositories.DataBountyRepository;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.sirgrantd.sg_economy.api.EconomyEventProvider;
import net.sirgrantd.sg_economy.api.SGEconomyApi;

public class BountyHunterServiceImpl implements BountyHunterProvider {

    private final EconomyEventProvider economy = SGEconomyApi.get();

    private long convertToInternal(double amount) {
        if (economy.isDecimalSystem()) {
            return Math.round(amount * 100);
        }
        return (long) amount;
    }

    @Override
    public boolean hasPointsBounty(Entity entity, double amount) {
        return getBountyHunter(entity) >= amount;
    }

    @Override
    public double getBountyHunter(Entity actor) {
        long amount = DataBountyRepository.getPointsBounty(actor);
        if (economy.isDecimalSystem()) {
            return amount / 100.0;
        }
        return amount;
    }

    @Override
    public boolean updateBountyHunter(Entity actor, Entity target) {
        if (actor.level().isClientSide()) {
            return false;
        }

        double actorPoints = getBountyHunter(actor);
        double targetPoints = getBountyHunter(target);

        double baseRate = ServerConfig.baseRateBountyHunter;
        double configPercentage = ServerConfig.percentageRewardBountyHunter;
        double percentageReward = targetPoints * (configPercentage / 100.0);
        double bountyLossOnDeath = baseRate + percentageReward;

        transferHuntRewardBalance(actor, target);

        if (bountyLossOnDeath > 0) {
            removeBountyHunter(target, bountyLossOnDeath);
        }

        if (actorPoints > 0 && targetPoints == 0) {
            return true;
        }
        if (actorPoints == 0 && targetPoints == 0) {
            addBountyHunter(actor, baseRate);
            return true;
        }
        if (actorPoints == 0 && targetPoints > 0 || actorPoints > 0 && targetPoints > 0) {
            double reward = baseRate + percentageReward;
            
            addBountyHunter(actor, reward);
            return true;
        }

        return false;
    }

    private void transferHuntRewardBalance(Entity actor, Entity target) {
        if (!(actor instanceof ServerPlayer killer)) {
            return;
        }
        if (!(target instanceof ServerPlayer victim)) {
            return;
        }

        if (!economy.hasCoinsBag(killer) || !economy.hasCoinsBag(victim)) {
            return;
        }

        if (!economy.balanceLostOnDeath(victim)) {
            return;
        }

        int percentageSaved = economy.getPercentageBalanceSaveOnDeath();
        double percentageLost = (100.0 - percentageSaved) / 100.0;
        if (percentageLost <= 0) {
            return;
        }

        if (economy.isDecimalSystem()) {
            double victimBalance = economy.getBalance(victim);
            double reward = victimBalance * percentageLost;
            if (reward <= 0) {
                return;
            }
            economy.transferBalance(victim, killer, reward);
            return;
        }

        int victimBalance = economy.getBalanceAsInt(victim);
        int reward = (int) Math.floor(victimBalance * percentageLost);
        if (reward <= 0) {
            return;
        }
        economy.transferBalanceAsInt(victim, killer, reward);
    }

    @Override
    public boolean setBountyHunter(Entity actor, double amount) {
        DataBountyRepository.setPointsBounty(actor, convertToInternal(amount));
        return true;
    }

    @Override
    public boolean addBountyHunter(Entity actor, double amount) {
        DataBountyRepository.addPointsBounty(actor, convertToInternal(amount));
        return true;
    }

    @Override
    public boolean removeBountyHunter(Entity actor, double amount) {
        DataBountyRepository.removePointsBounty(actor, convertToInternal(amount));
        return true;
    }

}
