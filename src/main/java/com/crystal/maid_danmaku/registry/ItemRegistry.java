package com.crystal.maid_danmaku.registry;

import com.crystal.maid_danmaku.MaidDanmaku;
import com.crystal.maid_danmaku.item.DanmakuControllerItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MaidDanmaku.MODID);
    public static final DeferredItem<Item> DANMAKU_CONTROLLER = ITEMS.register("danmaku_controller", DanmakuControllerItem::new);
}
