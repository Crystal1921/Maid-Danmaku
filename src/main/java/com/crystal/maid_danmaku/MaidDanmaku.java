package com.crystal.maid_danmaku;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(MaidDanmaku.MODID)
public class MaidDanmaku {
    public static final String MODID = "maid_danmaku";

    public MaidDanmaku(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
