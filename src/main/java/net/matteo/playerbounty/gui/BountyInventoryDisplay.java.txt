package net.matteo.playerbounty.gui;

import net.matteo.playerbounty.gui.components.ImageDisplayRender;
import net.matteo.playerbounty.gui.components.TextDisplayRender;
import net.matteo.playerbounty.internal.RulesGame;
import net.matteo.playerbounty.internal.provider.BountyHunterProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.sirgrantd.sg_economy.api.EconomyEventProvider;
import net.sirgrantd.sg_economy.api.SGEconomyApi;

@EventBusSubscriber({ Dist.CLIENT })
public class BountyInventoryDisplay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();

        boolean isInventory = screen instanceof InventoryScreen;
        boolean isCreative = screen instanceof CreativeModeInventoryScreen;
        boolean isCurios = false;

        if (ModList.get().isLoaded("curios")) {
            try {
                Class<?> curiosScreenClass = Class.forName("top.theillusivec4.curios.api.client.ICuriosScreen");
                isCurios = curiosScreenClass.isInstance(screen);
            } catch (ClassNotFoundException ignored) {
            }
        }

        if (isInventory || isCreative || isCurios) {

            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            isCreative = screen instanceof CreativeModeInventoryScreen;

            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }

            String pointsBounty = "";

            EconomyEventProvider economy = SGEconomyApi.get();
            BountyHunterProvider events = RulesGame.get();

            if (economy.isDecimalSystem()) {
                double points = events.getBountyHunter(player);
                pointsBounty = String.format("%.2f", points);
            } else {
                int points = events.getBountyHunterInt(player);
                pointsBounty = Integer.toString(points);
            }

            int xOffsetImage = isCreative ? 0 : 0;
            int yOffsetImage = isCreative ? -75 : -28;

            int xOffsetText = 90;
            int yOffsetText = isCreative ? -67 : -20;
            
            ResourceLocation DisplayImage = ImageDisplayRender.DISPLAY_VIEW_DEFAULT;
            
            ImageDisplayRender displayRender = new ImageDisplayRender(gui, xOffsetImage, yOffsetImage, DisplayImage);
            displayRender.renderWidget(event.getGuiGraphics(), 96, 23);

            TextDisplayRender textRender = new TextDisplayRender(gui, xOffsetText, yOffsetText, pointsBounty);
            textRender.renderText(event.getGuiGraphics());
        }
    }
}