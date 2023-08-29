package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits;

import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.AreaFactory;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;

public interface AreaTransformer1 extends DimensionTransformer {
    default <R extends Area> AreaFactory<R> run(BigContext<R> context, AreaFactory<R> factory) {
        return () -> {
            R r = factory.make();
            return context.createResult((p_164647_, p_164648_) -> {
                context.initRandom(p_164647_, p_164648_);
                return this.applyPixel(context, r, p_164647_, p_164648_);
            }, r);
        };
    }
    
    int applyPixel(BigContext<?> context, Area area, int p_77000_, int p_77001_);
}
