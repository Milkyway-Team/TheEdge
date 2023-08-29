package com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.traits;

import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.BigContext;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.context.Context;
import com.pouffydev.the_edge.foundation.data.world.biome.gen_layer.layer.area.Area;

public interface CastleTransformer extends AreaTransformer1, DimensionOffset1Transformer {
    int apply(Context context, int p_77060_, int p_77061_, int p_77062_, int p_77063_, int p_77064_);
    
    default int applyPixel(BigContext<?> context, Area area, int p_77057_, int p_77058_) {
        return this.apply(context, area.get(this.getParentX(p_77057_ + 1), this.getParentY(p_77058_)), area.get(this.getParentX(p_77057_ + 2), this.getParentY(p_77058_ + 1)), area.get(this.getParentX(p_77057_ + 1), this.getParentY(p_77058_ + 2)), area.get(this.getParentX(p_77057_), this.getParentY(p_77058_ + 1)), area.get(this.getParentX(p_77057_ + 1), this.getParentY(p_77058_ + 1)));
    }
}
