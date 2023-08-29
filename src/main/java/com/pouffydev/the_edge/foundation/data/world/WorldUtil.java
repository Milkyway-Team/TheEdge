package com.pouffydev.the_edge.foundation.data.world;

import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TEGenerationSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Random;

public final class WorldUtil {
    private WorldUtil() {}
    
    public static Biome voidFallback() {
        return OverworldBiomes.theVoid();
    }
    
    /**
     * Inclusive of edges
     */
    public static Iterable<BlockPos> getAllAround(BlockPos center, int range) {
        return BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range));
    }
    
    /**
     * Floors both corners of the bounding box to integers
     * Inclusive of edges
     */
    public static Iterable<BlockPos> getAllInBB(AABB bb) {
        return BlockPos.betweenClosed((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
    }
    
    public static BlockPos randomOffset(Random random, BlockPos pos, int range) {
        return randomOffset(random, pos, range, range, range);
    }
    
    public static BlockPos randomOffset(Random random, BlockPos pos, int rx, int ry, int rz) {
        int dx = random.nextInt(rx * 2 + 1) - rx;
        int dy = random.nextInt(ry * 2 + 1) - ry;
        int dz = random.nextInt(rz * 2 + 1) - rz;
        return pos.offset(dx, dy, dz);
    }
    
    @Nullable
    public static ChunkGeneratorEdge getChunkGenerator(LevelAccessor level) {
        if (level.getChunkSource() instanceof ServerChunkCache chunkSource && chunkSource.chunkMap.generator() instanceof ChunkGeneratorEdge chunkGenerator)
            return chunkGenerator;
        
        return null;
    }
    
    public static int getSeaLevel(ChunkGenerator generator) {
        if (generator instanceof ChunkGeneratorEdge) {
            return generator.getSeaLevel();
        } else return TEGenerationSettings.SEALEVEL;
    }
    
    public static int getBaseHeight(LevelAccessor level, int x, int z, Heightmap.Types type) {
        if (level.getChunkSource() instanceof ServerChunkCache chunkSource) {
            return chunkSource.chunkMap.generator().getBaseHeight(x, z, type, level);
        } else {
            return level.getHeight(type, x, z);
        }
    }
}
