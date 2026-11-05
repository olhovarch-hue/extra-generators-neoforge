package dev.olhovarch.extragenerators.client;

import dev.olhovarch.extragenerators.menu.GeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("extragenerators", "textures/gui/item_generator.png");
    public GeneratorScreen(GeneratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = 72;
    }
    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        int barWidth = (int)(64.0f * Math.min(1.0f, (float)menu.energy() / menu.capacity()));
        graphics.fill(x + 105, y + 35, x + 169, y + 42, 0xFF171717);
        if (barWidth > 0) graphics.fill(x + 105, y + 35, x + 105 + barWidth, y + 42, 0xFF36B86B);
        if (menu.burnRemaining() > 0) {
            int burnHeight = Math.min(14, menu.burnRemaining() / 2 + 1);
            graphics.fill(x + 102, y + 52 - burnHeight, x + 106, y + 52, 0xFFFF9C32);
        }
    }
    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0xFFFFFF, false);
        graphics.drawString(font, Component.translatable("container.inventory"), 8, inventoryLabelY, 0xFFFFFF, false);
        graphics.drawString(font, Component.literal(menu.energy() + " / " + menu.capacity() + " FE"), 105, 25, 0xFFFFFF, false);
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}