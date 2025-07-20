package org.mesdag.xaero_wormhole;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(XaeroWormhole.MODID)
public class XaeroWormhole {
    public static final String MODID = "xaero_wormhole";
    public static final Logger LOGGER = LoggerFactory.getLogger("XaeroWormhole");

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<Item> WORMHOLE_POTION = ITEMS.register("wormhole_potion", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public XaeroWormhole(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::registerPayloadHandlers);
        modEventBus.addListener(this::buildCreativeModeTabContents);
    }

    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(WormholeToPlayerPacketC2S.TYPE, WormholeToPlayerPacketC2S.STREAM_CODEC, WormholeToPlayerPacketC2S::handle);
    }

    private void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(WORMHOLE_POTION.toStack());
        }
    }
}
