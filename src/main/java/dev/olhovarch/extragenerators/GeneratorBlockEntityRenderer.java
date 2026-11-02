package dev.olhovarch.extragenerators;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.data.ModelData;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class GeneratorBlockEntityRenderer implements BlockEntityRenderer<GeneratorBlockEntity> {
    public GeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(GeneratorBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (!entity.isRunning()) return;
        BakedModel model = Minecraft.getInstance().getModelManager()
                .getModel(ModelResourceLocation.standalone(ClientRenderRegistration.COG_MODEL));
        if (model == Minecraft.getInstance().getModelManager().getMissingModel()) return;
        Direction facing = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        RandomSource random = RandomSource.create(42L);
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            default -> { }
        }
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        float angle = entity.getInterpolatedRotation(partialTick);
        poseStack.translate(8.5D / 16.0D, 6.0D / 16.0D, 6.0D / 16.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(angle));
        poseStack.translate(-8.5D / 16.0D, -6.0D / 16.0D, -6.0D / 16.0D);
        List<BakedQuad> quads = new ArrayList<>();
        for (Direction direction : Direction.values()) quads.addAll(model.getQuads(null, direction, random, ModelData.EMPTY, null));
        quads.addAll(model.getQuads(null, null, random, ModelData.EMPTY, null));
        float red=1.0F, green=1.0F, blue=1.0F;
        String id=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(entity.getBlockState().getBlock()).getPath();
        if(id.equals("heavenly_generator")||id.equals("infernal_generator")){
            int rgb=Color.HSBtoRGB((System.currentTimeMillis()%2000L)/2000.0F,0.8F,0.95F);
            red=((rgb>>16)&255)/255.0F;green=((rgb>>8)&255)/255.0F;blue=(rgb&255)/255.0F;
        }
        var consumer=buffers.getBuffer(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
        for(BakedQuad quad:quads)consumer.putBulkData(poseStack.last(),quad,red,green,blue,1.0F,packedLight,packedOverlay);
        poseStack.popPose();
    }
}
