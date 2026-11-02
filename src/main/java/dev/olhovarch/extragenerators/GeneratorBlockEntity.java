package dev.olhovarch.extragenerators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class GeneratorBlockEntity extends BlockEntity {
    private static final int CAPACITY = 100_000;
    private static final int OUTPUT_PER_TICK = 40;
    private final EnergyStorage energy = new EnergyStorage(CAPACITY, 0, 2_000);
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };
    private int burnTime;
    public GeneratorBlockEntity(BlockPos pos, BlockState state) { super(GeneratorRegistries.GENERATOR_BE.get(), pos, state); }
    public static void serverTick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity entity) {
        if (level.isClientSide) return;
        if (entity.burnTime <= 0 && entity.energy.getEnergyStored() < entity.energy.getMaxEnergyStored()) {
            ItemStack fuel = entity.inventory.getStackInSlot(0);
            int duration = fuel.isEmpty() ? 0 : fuel.getBurnTime(RecipeType.SMELTING);
            if (duration > 0) {
                entity.burnTime = duration;
                ItemStack remainder = fuel.getCraftingRemainingItem();
                fuel.shrink(1);
                if (fuel.isEmpty() && !remainder.isEmpty()) entity.inventory.setStackInSlot(0, remainder);
            }
        }
        if (entity.burnTime > 0 && entity.energy.getEnergyStored() < entity.energy.getMaxEnergyStored()) {
            int accepted = entity.energy.receiveEnergy(OUTPUT_PER_TICK, false);
            if (accepted > 0) entity.burnTime--;
            entity.pushEnergy();
            entity.setChanged();
        }
    }
    private void pushEnergy() {
        if (level == null) return;
        for (Direction direction : Direction.values()) {
            var target = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition.relative(direction), direction.getOpposite());
            if (target != null && target.canReceive() && energy.getEnergyStored() > 0) {
                int moved = target.receiveEnergy(Math.min(energy.getEnergyStored(), 2_000), false);
                energy.extractEnergy(moved, false);
            }
        }
    }
    public EnergyStorage getEnergyStorage() { return energy; }
    public ItemStackHandler getInventory() { return inventory; }
    @Override protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("energy", energy.serializeNBT(registries));
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("burnTime", burnTime);
    }
    @Override protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("energy")) energy.deserializeNBT(registries, tag.get("energy"));
        if (tag.contains("inventory")) inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        burnTime = tag.getInt("burnTime");
    }
}
