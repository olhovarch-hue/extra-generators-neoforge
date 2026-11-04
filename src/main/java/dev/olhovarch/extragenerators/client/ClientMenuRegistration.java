package dev.olhovarch.extragenerators.client;

import dev.olhovarch.extragenerators.GeneratorRegistries;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ClientMenuRegistration {
    private ClientMenuRegistration() {}
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GeneratorRegistries.GENERATOR_MENU.get(), GeneratorScreen::new);
    }
}