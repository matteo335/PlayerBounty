package net.matteo.playerbounty.internal;

import net.matteo.playerbounty.internal.provider.BountyHunterProvider;

public final class RulesGame {
    
    private static final BountyHunterProvider INSTANCE = new BountyHunterServiceImpl();

    private RulesGame() {
    }

    public static BountyHunterProvider get() {
        return INSTANCE;
    }
}
