package dev.olhovarch.extragenerators;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public final class ClientRenderRegistration {
    public static final ResourceLocation COG_MODEL = ResourceLocation.fromNamespaceAndPath(ExtraGenerators.MOD_ID, "block/cog_wheels");
    private ClientRenderRegistration() {}
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(COG_MODEL));
    }
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GeneratorRegistries.GENERATOR_BE.get(), GeneratorBlockEntityRenderer::new);
    }
}
