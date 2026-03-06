package net.matteo.playerbounty.internal.provider;

import net.minecraft.world.entity.Entity;

public interface BountyHunterProvider {
    
    boolean hasPointsBounty(Entity entity, double amount);

    double getBountyHunter(Entity actor);
    boolean updateBountyHunter(Entity actor, Entity target);

    boolean setBountyHunter(Entity actor, double amount);
    boolean addBountyHunter(Entity actor, double amount);
    boolean removeBountyHunter(Entity actor, double amount);

    default int getBountyHunterInt(Entity actor) {
        return (int) getBountyHunter(actor);
    }
    
    default boolean setBountyHunterInt(Entity actor, int amount) {
        return setBountyHunter(actor, amount);
    }
    default boolean addBountyHunterInt(Entity actor, int amount) {
        return addBountyHunter(actor, amount);
    }
    default boolean removeBountyHunterInt(Entity actor, int amount) {
        return removeBountyHunter(actor, amount);
    }

}
