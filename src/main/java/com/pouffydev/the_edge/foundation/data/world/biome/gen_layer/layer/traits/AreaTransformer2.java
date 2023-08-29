package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits;

import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.AreaFactory;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;

public interface AreaTransformer2 extends DimensionTransformer {
    default <R extends Area> AreaFactory<R> run(BigContext<R> context, AreaFactory<R> factory, AreaFactory<R> factory2) {
        return () -> {
            R r = factory.make();
            R r1 = factory2.make();
            return context.createResult((p_164653_, p_164654_) -> {
                context.initRandom(p_164653_, p_164654_);
                return this.applyPixel(context, r, r1, p_164653_, p_164654_);
            }, r, r1);
        };
    }
    
    int applyPixel(Context context, Area area, Area area2, int p_77027_, int p_77028_);
}
