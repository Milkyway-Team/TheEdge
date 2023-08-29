package com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators;

import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.ChunkGeneratorEdge;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TEGenerationSettings {
    
    @Deprecated // Used in places where we can't access the sea level FIXME Resolve
    public static final int SEALEVEL = 0;
    
    public static final ResourceLocation DIMENSION = TheEdge.asResource("the_edge");
    public static final ResourceKey<LevelStem> WORLDGEN_KEY = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, DIMENSION);
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, DIMENSION);
    
    
    
    // Checks if the world is a qualified Edge world by checking against its namespace or if it's a portal destination
    @OnlyIn(Dist.CLIENT)
    public static boolean isTwilightWorldOnClient(Level world) {
        return TheEdge.ID.equals(Minecraft.getInstance().level.dimension().location().getNamespace());
    }
    
    // Checks if the world is *a* Twilight world on the Server side.
    public static boolean usesTwilightChunkGenerator(ServerLevel world) {
        return world.getChunkSource().getGenerator() instanceof ChunkGeneratorEdge;
    }
}
