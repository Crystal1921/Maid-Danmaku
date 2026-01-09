package com.crystal.maid_danmaku.datagen;

import com.crystal.maid_danmaku.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItem extends ItemModelProvider {
    public ModItem(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ItemRegistry.DANMAKU_CONTROLLER.get());
    }
}
