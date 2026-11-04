package dev.olhovarch.extragenerators.menu;

import dev.olhovarch.extragenerators.GeneratorBlockEntity;
import dev.olhovarch.extragenerators.GeneratorRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public final class GeneratorMenu extends AbstractContainerMenu {
    private final GeneratorBlockEntity generator;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public GeneratorMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, getGenerator(playerInventory, buffer), new net.minecraft.world.inventory.SimpleContainerData(4));
    }

    private static GeneratorBlockEntity getGenerator(Inventory inventory, FriendlyByteBuf buffer) {
        BlockEntity be = inventory.player.level().getBlockEntity(buffer.readBlockPos());
        if (be instanceof GeneratorBlockEntity generator) return generator;
        throw new IllegalStateException("Generator block entity not found while opening menu");
    }

    public GeneratorMenu(int id, Inventory playerInventory, GeneratorBlockEntity generator, ContainerData data) {
        super(GeneratorRegistries.GENERATOR_MENU.get(), id);
        this.generator = generator;
        this.access = ContainerLevelAccess.create(generator.getLevel(), generator.getBlockPos());
        this.data = data;
        checkContainerDataCount(data, 4);
        addSlot(new SlotItemHandler(generator.getInventory(), 0, 80, 35));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        addDataSlots(data);
    }

    public int energy() { return data.get(0); }
    public int capacity() { return Math.max(1, data.get(1)); }
    public int burnRemaining() { return data.get(2); }

    @Override public boolean stillValid(Player player) {
        return stillValid(access, player, generator.getBlockState().getBlock());
    }

    @Override public net.minecraft.world.item.ItemStack quickMoveStack(Player player, int index) {
        net.minecraft.world.item.ItemStack result = net.minecraft.world.item.ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            net.minecraft.world.item.ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index == 0) {
                if (!moveItemStackTo(stack, 1, slots.size(), true)) return net.minecraft.world.item.ItemStack.EMPTY;
            } else if (!moveItemStackTo(stack, 0, 1, false)) {
                return net.minecraft.world.item.ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(net.minecraft.world.item.ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }
}