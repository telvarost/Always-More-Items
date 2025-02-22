package net.glasslauncher.mods.alwaysmoreitems.testmod.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.event.ActionButtonRegisterEvent;
import net.glasslauncher.mods.alwaysmoreitems.testmod.CheatyDummyActionButton;
import net.glasslauncher.mods.alwaysmoreitems.testmod.DummyActionButton;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.util.Namespace;

public class ActionButtonListener {
    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    @EventListener
    public void registerActionButtons(ActionButtonRegisterEvent event){
        for (int i = 0; i < 15; i++) {
            // Not registering these on server to test handling of actions missing on server
            if (i > 10 && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                continue;
            }
            event.add(NAMESPACE.id("dummy" + i), new DummyActionButton());
        }
        
        event.add(NAMESPACE.id("cheat_dummy"), new CheatyDummyActionButton());
    }
}
