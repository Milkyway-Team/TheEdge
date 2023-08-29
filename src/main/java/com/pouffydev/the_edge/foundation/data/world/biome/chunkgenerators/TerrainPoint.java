package com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TerrainPoint(float depth, float scale) {
    public static final Codec<TerrainPoint> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("depth").forGetter((o) -> o.depth),
                    Codec.FLOAT.fieldOf("scale").forGetter((o) -> o.scale)
            ).apply(instance, TerrainPoint::new));
}
