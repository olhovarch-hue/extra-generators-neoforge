package dev.olhovarch.extragenerators;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(ExtraGenerators.MOD_ID)
public final class ExtraGenerators {
    public static final String MOD_ID = "extragenerators";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtraGenerators() {
        LOGGER.info("Extra Generators NeoForge port bootstrap loaded.");
    }
}
