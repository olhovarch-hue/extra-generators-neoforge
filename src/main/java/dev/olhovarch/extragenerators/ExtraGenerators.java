package dev.olhovarch.extragenerators;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.slf4j.Logger;

@Mod(ExtraGenerators.MOD_ID)
public final class ExtraGenerators {
    public static final String MOD_ID = "extragenerators";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtraGenerators(IEventBus modBus) {
        GeneratorRegistries.register(modBus);
        modBus.addListener(this::registerCapabilities);
        LOGGER.info("Extra Generators NeoForge port bootstrap loaded.");
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, GeneratorRegistries.GENERATOR_BE.get(),
                (blockEntity, side) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GeneratorRegistries.GENERATOR_BE.get(),
                (blockEntity, side) -> blockEntity.getInventory());
    }
}
