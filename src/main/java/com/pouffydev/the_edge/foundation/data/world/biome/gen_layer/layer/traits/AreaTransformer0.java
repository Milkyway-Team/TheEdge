package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits;

import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.AreaFactory;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;

public interface AreaTransformer0 {
    default <R extends Area> AreaFactory<R> run(BigContext<R> context) {
        return () -> context.createResult((p_164642_, p_164643_) -> {
            context.initRandom(p_164642_, p_164643_);
            return this.applyPixel(context, p_164642_, p_164643_);
        });
    }
    
    int applyPixel(Context context, int p_76991_, int p_76992_);
}
