package org.mesdag.map_wormhole;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(MapWormhole.MODID)
public class MapWormhole {
    public static final String MODID = "map_wormhole";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> WORMHOLE_POTION = ITEMS.register("wormhole_potion", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public MapWormhole(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::buildCreativeModeTabContents);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            WormholeToPlayerPacketC2S.CHANNEL.registerMessage(0, WormholeToPlayerPacketC2S.class, WormholeToPlayerPacketC2S::encode, WormholeToPlayerPacketC2S::decode, WormholeToPlayerPacketC2S::handle);
        });
    }

    private void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(WORMHOLE_POTION.get().getDefaultInstance());
        }
    }
}
