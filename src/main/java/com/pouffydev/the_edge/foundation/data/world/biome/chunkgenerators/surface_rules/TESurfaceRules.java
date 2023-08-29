package com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.surface_rules;

import com.google.common.collect.ImmutableList;
import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.TEFluids;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class TESurfaceRules {
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource starfilledEdgestone = makeStateRule(TEBlocks.STARFILLED_EDGESTONE.get());
    private static final SurfaceRules.RuleSource edgestone = makeStateRule(TEBlocks.EDGESTONE.get());
    private static final SurfaceRules.RuleSource glitchedNylium = makeStateRule(TEBlocks.GLITCHED_NYLIUM.get());
    private static final SurfaceRules.RuleSource frostedNylium = makeStateRule(TEBlocks.frostedNylium.get());
    private static final SurfaceRules.RuleSource ice = makeStateRule(Blocks.ICE);
    private static final SurfaceRules.RuleSource packedIce = makeStateRule(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource snow = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource starfall = makeStateRule(TEFluids.STARFALL.get());
    private static final SurfaceRules.RuleSource lava = makeStateRule(Blocks.LAVA);
    
    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
    private static SurfaceRules.RuleSource makeStateRule(ForgeFlowingFluid.Flowing fluid) {
        return SurfaceRules.state(fluid.defaultFluidState().createLegacyBlock());
    }
    public static SurfaceRules.RuleSource basicOverworldGen() {
        SurfaceRules.ConditionSource y62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.RuleSource sandPlacer = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, starfilledEdgestone), edgestone);
        
        SurfaceRules.RuleSource overworldLike = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        SurfaceRules.isBiome(BiomeKeys.starfallSwamp),
                                        SurfaceRules.ifTrue(y62,
                                                SurfaceRules.ifTrue(
                                                        SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), starfall))),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.isBiome(BiomeKeys.overgrownLake, BiomeKeys.starryStream), sandPlacer),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.waterBlockCheck(-1, 0), glitchedNylium), edgestone)),
                SurfaceRules.ifTrue(
                        SurfaceRules.waterStartCheck(-6, -1),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        SurfaceRules.UNDER_FLOOR,
                                        edgestone))));
        
        
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        SurfaceRules.RuleSource surfacerules$rulesource9 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), overworldLike);
        builder.add(surfacerules$rulesource9);
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }
    
    public static SurfaceRules.RuleSource teSurface() {
        //surface is a normal overworld surface as the base
        //snowy forest is all snow on the top layers
        //glacier has 1 ice layer, 30 packed ice layers, gravel for a few layers, then stone
        //highlands has a noise-based mixture of podzol and coarse dirt
        //thornlands/plateau has no caves and deadrock instead of stone
        
        SurfaceRules.RuleSource swampNoise = SurfaceRules.sequence(
                //check if we're in the highlands
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(BiomeKeys.starfallSwamp),
                        SurfaceRules.ifTrue(
                                //check if we're on the surface
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        //mix coarse dirt and podzol like we used to
                                        glitchedNylium,
                                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), edgestone),
                                        SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), glitchedNylium)))));
        SurfaceRules.RuleSource frozenWastes = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        //check if we're in the snowy forest
                        SurfaceRules.isBiome(BiomeKeys.frozenWastes),
                        //surface is snow
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, snow),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.waterStartCheck(-6, -1),
                                        SurfaceRules.sequence(
                                                SurfaceRules.ifTrue(
                                                        SurfaceRules.UNDER_FLOOR,
                                                        snow))))));
        
        SurfaceRules.RuleSource glacialPeaks = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        //check if we're in the glacier
                        SurfaceRules.isBiome(BiomeKeys.glacialPeaks),
                        SurfaceRules.sequence(
                                //surface and under is gravel
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, edgestone),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.waterStartCheck(-6, -1),
                                        SurfaceRules.sequence(
                                                SurfaceRules.ifTrue(
                                                        SurfaceRules.UNDER_FLOOR,
                                                        edgestone))))));
        
        
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder
                .add(swampNoise)
                .add(frozenWastes)
                .add(glacialPeaks)
                //overworld generation last
                .add(basicOverworldGen());
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }
    
    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
    }
}
