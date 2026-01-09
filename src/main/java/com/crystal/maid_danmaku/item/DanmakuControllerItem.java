package com.crystal.maid_danmaku.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class DanmakuControllerItem extends Item {
    public DanmakuControllerItem() {
        super(new  Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }
}
