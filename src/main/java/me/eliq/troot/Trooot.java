package me.eliq.troot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import me.eliq.troot.block.HornBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Trooot.MODID)
public class Trooot {
    public static final String MODID = "trooot";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final DeferredBlock<HornBlock> FOG_HORN_BLOCK = BLOCKS.registerBlock("fog_horn", HornBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
    public static final DeferredItem<BlockItem> FOG_HORN_ITEM = ITEMS.registerSimpleBlockItem("fog_horn", FOG_HORN_BLOCK);
    public static final DeferredHolder<SoundEvent, SoundEvent> FOG_HORN_BLARE_SOUND =
            SOUNDS.register("fog_horn_blare",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(MODID, "fog_horn_blare")));

    public Trooot(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("TRÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖT");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(FOG_HORN_ITEM);
        }
    }
}
