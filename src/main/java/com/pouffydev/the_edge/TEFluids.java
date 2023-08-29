package com.pouffydev.the_edge;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class TEFluids {
    private static final CreateRegistrate REGISTRATE = TheEdge.registrate().creativeModeTab(
            () -> TheEdge.itemGroup
    );
    public static final FluidEntry<ForgeFlowingFluid.Flowing> STARFALL =
            REGISTRATE.standardFluid("starfall", NoColorFluidAttributes::new)
                    .lang("StarFall")
                    .tag(AllTags.forgeFluidTag("starfall"))
                    .attributes(b -> b
                            .viscosity(1500)
                            .density(1400)
                            .luminosity(15)
                    )
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(5)
                            .explosionResistance(100f)
                    )
                    .register();

    public static void register() {}

    private static class NoColorFluidAttributes extends FluidAttributes {

        protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }
}
