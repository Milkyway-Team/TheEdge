package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface Context {
        int nextRandom(int p_76516_);
        ImprovedNoise getBiomeNoise();
}
