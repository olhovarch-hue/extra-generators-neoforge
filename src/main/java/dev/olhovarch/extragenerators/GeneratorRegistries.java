package dev.olhovarch.extragenerators;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GeneratorRegistries {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExtraGenerators.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraGenerators.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExtraGenerators.MOD_ID);

    public static final Map<String, DeferredHolder<Block, GeneratorBlock>> GENERATORS = new LinkedHashMap<>();

    private static DeferredHolder<Block, GeneratorBlock> generator(String id) {
        DeferredHolder<Block, GeneratorBlock> block = BLOCKS.register(id,
                () -> new GeneratorBlock(BlockBehaviour.Properties.of().strength(3.5F).requiresCorrectToolForDrops()));
        GENERATORS.put(id, block);
        ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static final DeferredHolder<Block, GeneratorBlock> THERMOELECTRIC = generator("thermoelectric_generator");
    public static final DeferredHolder<Block, GeneratorBlock> BURNABLE = generator("burnable_generator");
    public static final DeferredHolder<Block, GeneratorBlock> ICY = generator("icy_generator");
    public static final DeferredHolder<Block, GeneratorBlock> COLORFUL = generator("colorful_generator");
    public static final DeferredHolder<Block, GeneratorBlock> SLUDGY = generator("sludgy_generator");
    public static final DeferredHolder<Block, GeneratorBlock> TELEPORT = generator("teleport_generator");
    public static final DeferredHolder<Block, GeneratorBlock> SCALDING = generator("scalding_generator");
    public static final DeferredHolder<Block, GeneratorBlock> STEAM = generator("steam_generator");
    public static final DeferredHolder<Block, GeneratorBlock> GLUTTONY = generator("gluttony_generator");
    public static final DeferredHolder<Block, GeneratorBlock> BREW = generator("brew_generator");
    public static final DeferredHolder<Block, GeneratorBlock> REDSTONE = generator("redstone_generator");
    public static final DeferredHolder<Block, GeneratorBlock> BLAST = generator("blast_generator");
    public static final DeferredHolder<Block, GeneratorBlock> ENCHANTED = generator("enchanted_generator");
    public static final DeferredHolder<Block, GeneratorBlock> DEMISE = generator("demise_generator");
    public static final DeferredHolder<Block, GeneratorBlock> DRAGON = generator("dragon_generator");
    public static final DeferredHolder<Block, GeneratorBlock> WITHERED = generator("withered_generator");
    public static final DeferredHolder<Block, GeneratorBlock> HEAVENLY = generator("heavenly_generator");
    public static final DeferredHolder<Block, GeneratorBlock> INFERNAL = generator("infernal_generator");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorBlockEntity>> GENERATOR_BE =
            BLOCK_ENTITY_TYPES.register("generator", () -> BlockEntityType.Builder.of(
                    GeneratorBlockEntity::new,
                    GENERATORS.values().stream().map(DeferredHolder::get).toArray(Block[]::new)
            ).build(null));

    private GeneratorRegistries() {}

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);
    }
}
