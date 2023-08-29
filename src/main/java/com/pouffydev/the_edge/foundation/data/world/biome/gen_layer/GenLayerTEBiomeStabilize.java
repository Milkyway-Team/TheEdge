package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer;

import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits.AreaTransformer1;

public enum GenLayerTEBiomeStabilize implements AreaTransformer1 {
    
    INSTANCE;
    
    GenLayerTEBiomeStabilize() { }
    
    @Override
    public int getParentX(int x) {
        return x & 3;
    }
    
    @Override
    public int getParentY(int z) {
        return z & 3;
    }
    
    
    @Override
    public int applyPixel(BigContext<?> iExtendedNoiseRandom, Area iArea, int x, int z) {
        int offX = getParentX(x << 4);
        int offZ = getParentY(z << 4);
        int centerX = ((x + offX + 1) & -4) - offX;
        int centerZ = ((z + offZ + 1) & -4) - offZ;
        if (x <= centerX + 1 && x >= centerX - 1 && z <= centerZ + 1 && z >= centerZ - 1) {
            return iArea.get(centerX, centerZ);
        } else {
            return iArea.get(x, z);
        }
    }
}
