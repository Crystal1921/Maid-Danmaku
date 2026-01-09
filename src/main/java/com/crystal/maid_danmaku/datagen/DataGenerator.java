package com.crystal.maid_danmaku.datagen;

import com.crystal.maid_danmaku.MaidDanmaku;
import net.minecraft.core.HolderLookup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MaidDanmaku.MODID, value = Dist.CLIENT)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var efh = event.getExistingFileHelper();
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();
        var vanillaPack = generator.getVanillaPack(true);
        var existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();
        //Language
        generator.addProvider(
                event.includeClient(), new ModLanguage(output, "zh_cn"));
        generator.addProvider(
                event.includeClient(), new ModLanguage(output, "en_us"));
        generator.addProvider(
                event.includeServer(), new ModItem(output, MaidDanmaku.MODID, existingFileHelper));
    }
}
