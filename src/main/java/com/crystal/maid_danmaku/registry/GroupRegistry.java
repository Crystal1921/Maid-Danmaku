package com.crystal.maid_danmaku.registry;

import com.crystal.maid_danmaku.MaidDanmaku;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GroupRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MaidDanmaku.MODID);

    @SuppressWarnings("all")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAID_DANMAKU = TABS.register("blocks", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup.maid_danmaku"))
            .icon(() -> new ItemStack(ItemRegistry.DANMAKU_CONTROLLER.get()))
            .displayItems((parameters, output) -> {
                ItemRegistry.DANMAKU_CONTROLLER.asItem();
            }).build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
