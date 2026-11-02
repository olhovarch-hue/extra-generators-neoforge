package dev.olhovarch.extragenerators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import java.util.Map;
import java.util.Set;

public final class GeneratorBlockEntity extends BlockEntity {
    private static final int[][] TIERS={{0,0},{32000,64},{64000,128},{128000,256},{256000,512},{2048000,4096},{102400000,204800}};
    private static final Map<String,Integer> TIER=Map.ofEntries(
        Map.entry("thermoelectric_generator",1),Map.entry("burnable_generator",1),Map.entry("icy_generator",1),Map.entry("colorful_generator",1),
        Map.entry("sludgy_generator",2),Map.entry("teleport_generator",2),Map.entry("scalding_generator",2),Map.entry("steam_generator",2),
        Map.entry("gluttony_generator",3),Map.entry("brew_generator",3),Map.entry("redstone_generator",3),Map.entry("blast_generator",3),
        Map.entry("enchanted_generator",4),Map.entry("demise_generator",4),Map.entry("dragon_generator",5),Map.entry("withered_generator",5),
        Map.entry("heavenly_generator",6),Map.entry("infernal_generator",6));
    private static final Set<String> DYES=Set.of("white_dye","orange_dye","magenta_dye","light_blue_dye","yellow_dye","lime_dye","pink_dye","gray_dye","light_gray_dye","cyan_dye","purple_dye","blue_dye","brown_dye","green_dye","red_dye","black_dye");
    private static final Set<String> FOODS=Set.of("cooked_chicken","cooked_beef","cooked_porkchop","cooked_mutton","cooked_rabbit","baked_potato","bread","golden_carrot","golden_apple");
    private final EnergyStorage energy;
    private final ItemStackHandler inventory=new ItemStackHandler(1){@Override protected void onContentsChanged(int slot){setChanged();}};
    private int burnRemaining, burnEnergy;
    public GeneratorBlockEntity(BlockPos pos, BlockState state){super(GeneratorRegistries.GENERATOR_BE.get(),pos,state);int[] t=tier();energy=new EnergyStorage(t[0],t[1],t[1]);}
    private String id(){return BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath();}
    private int[] tier(){return TIERS[TIER.getOrDefault(id(),1)];}
    public static void serverTick(Level level,BlockPos pos,BlockState state,GeneratorBlockEntity e){
        if(level.isClientSide)return;String id=e.id();int rate=e.tier()[1];
        if(id.equals("heavenly_generator")||id.equals("infernal_generator"))e.energy.receiveEnergy(rate,false);
        else if(id.equals("thermoelectric_generator"))e.energy.receiveEnergy(Math.min(rate,e.heatDelta(level)),false);
        else {if(e.burnRemaining<=0)e.startFuel(id);if(e.burnRemaining>0&&e.energy.getEnergyStored()+e.burnEnergy<=e.energy.getMaxEnergyStored()){e.energy.receiveEnergy(e.burnEnergy,false);e.burnRemaining--;}}
        e.pushEnergy();e.setChanged();
    }
    private void startFuel(String id){
        ItemStack s=inventory.getStackInSlot(0); if(s.isEmpty()) return;
        String item=BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        int duration=0, perTick=0;
        if(id.equals("burnable_generator")) {
            int ticks=s.getBurnTime(RecipeType.SMELTING);
            duration=Math.max(1,Math.round(ticks/4.0f)); perTick=32;
        } else if(id.equals("icy_generator")) {
            duration=switch(item){case "ice"->40;case "packed_ice"->360;case "blue_ice"->3240;case "snowball"->5;case "snow_block"->20;case "snow"->10;default->0;}; perTick=32;
        } else if(id.equals("sludgy_generator")) {
            duration=switch(item){case "slime_ball"->200;case "slime_block"->1800;default->0;}; perTick=64;
        } else if(id.equals("teleport_generator")) {
            duration=switch(item){case "ender_pearl"->800;case "ender_eye"->1000;case "chorus_fruit"->20;default->0;}; perTick=64;
        } else if(id.equals("blast_generator")) {
            duration=switch(item){case "gunpowder"->100;case "tnt"->600;default->0;}; perTick=128;
        } else if(id.equals("demise_generator")) {
            duration=switch(item){case "rotten_flesh","bone"->10;case "spider_eye"->20;case "bone_block"->90;case "zombie_head","creeper_head","skeleton_skull"->1800;case "wither_skeleton_skull"->3600;default->0;}; perTick=256;
        } else if(id.equals("dragon_generator")) {
            duration=switch(item){case "dragon_breath"->20;case "dragon_head"->2000;case "dragon_egg"->24000;default->0;}; perTick=2034;
        } else if(id.equals("withered_generator")) {
            duration=switch(item){case "nether_star"->6000;case "wither_skeleton_skull"->450;case "wither_rose"->20;default->0;}; perTick=2048;
        } else if(id.equals("colorful_generator")) {
            duration=DYES.contains(item)?40:0; perTick=16;
        } else if(id.equals("gluttony_generator")) {
            duration=FOODS.contains(item)?100:0; perTick=64;
        } else if(id.equals("brew_generator")) {
            duration=(s.is(Items.POTION)||s.is(Items.SPLASH_POTION)||s.is(Items.LINGERING_POTION))?200:0; perTick=8;
        } else if(id.equals("steam_generator")&&s.is(Items.WATER_BUCKET)) {
            duration=100; perTick=64;
        } else if((id.equals("scalding_generator")||id.equals("redstone_generator"))&&s.is(Items.LAVA_BUCKET)) {
            duration=100; perTick=id.equals("scalding_generator")?64:128;
        }
        if(duration<=0||perTick<=0) return;
        burnRemaining=duration; burnEnergy=perTick; s.shrink(1);
        if(s.isEmpty()&&(item.equals("water_bucket")||item.equals("lava_bucket"))) inventory.setStackInSlot(0,new ItemStack(Items.BUCKET));
        setChanged();
    }
    private int heatDelta(Level l){
        int x=Math.abs(temp(l.getBlockState(worldPosition.east()).getBlock())-temp(l.getBlockState(worldPosition.west()).getBlock()));
        int y=Math.abs(temp(l.getBlockState(worldPosition.above()).getBlock())-temp(l.getBlockState(worldPosition.below()).getBlock()));
        int z=Math.abs(temp(l.getBlockState(worldPosition.north()).getBlock())-temp(l.getBlockState(worldPosition.south()).getBlock()));
        return (x+y+z)/100;
    }
    private int temp(net.minecraft.world.level.block.Block b){
        if(b==Blocks.LAVA)return 1200;
        if(b==Blocks.MAGMA_BLOCK||b==Blocks.SOUL_FIRE||b==Blocks.SOUL_CAMPFIRE||b==Blocks.SOUL_TORCH||b==Blocks.SOUL_WALL_TORCH)return 600;
        if(b==Blocks.FIRE||b==Blocks.CAMPFIRE||b==Blocks.TORCH||b==Blocks.WALL_TORCH||b==Blocks.LANTERN)return 300;
        if(b==Blocks.WATER)return 30;if(b==Blocks.ICE)return -30;if(b==Blocks.PACKED_ICE)return -40;if(b==Blocks.BLUE_ICE)return -50;return 0;
    }
    private void pushEnergy(){if(level==null)return;for(Direction d:Direction.values()){var target=level.getCapability(Capabilities.EnergyStorage.BLOCK,worldPosition.relative(d),d.getOpposite());if(target!=null&&target.canReceive()&&energy.getEnergyStored()>0){int moved=target.receiveEnergy(Math.min(energy.getEnergyStored(),tier()[1]),false);energy.extractEnergy(moved,false);}}}
    public EnergyStorage getEnergyStorage(){return energy;}
    public ItemStackHandler getInventory(){return inventory;}
    @Override protected void saveAdditional(CompoundTag tag,net.minecraft.core.HolderLookup.Provider registries){super.saveAdditional(tag,registries);tag.put("energy",energy.serializeNBT(registries));tag.put("inventory",inventory.serializeNBT(registries));tag.putInt("burnRemaining",burnRemaining);tag.putInt("burnEnergy",burnEnergy);}
    @Override protected void loadAdditional(CompoundTag tag,net.minecraft.core.HolderLookup.Provider registries){super.loadAdditional(tag,registries);if(tag.contains("energy"))energy.deserializeNBT(registries,tag.get("energy"));if(tag.contains("inventory"))inventory.deserializeNBT(registries,tag.getCompound("inventory"));burnRemaining=tag.getInt("burnRemaining");burnEnergy=tag.getInt("burnEnergy");}
}
