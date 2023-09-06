package com.pouffydev.the_edge.content.block.kinetics.cryo_fan;

import com.pouffydev.the_edge.TEBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class CryoFanBlockEntity extends KineticBlockEntity {
    public CryoFanBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    private boolean isRunning() {
        return isSpeedRequirementFulfilled() && !isOverStressed();
    }
    private boolean hasSpaceBelow() {
        BlockState checkState = level.getBlockState(worldPosition.below());
        boolean hasLayerProperty = checkState.hasProperty(BlockStateProperties.LAYERS);
        int snowLayer = checkState.getBlock().defaultBlockState().getValue(BlockStateProperties.LAYERS);
        if (hasLayerProperty) {
            return snowLayer < 8;
        }
        return level.getBlockState(worldPosition.below()).is(AllTags.AllBlockTags.FAN_TRANSPARENT.tag);
    }
    public void explodeWithSnow() {
        level.addParticle(ParticleTypes.SNOWFLAKE, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 0.0D, 0.0D, 0.0D);
        level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 1.0f, false, Explosion.BlockInteraction.BREAK);
    }
    public void spawnSuctionParticles() {
        Random random = level.random;
        for(Direction direction : Direction.values()) {
            BlockPos blockpos = worldPosition.relative(direction);
            ParticleOptions modeParticles = ParticleTypes.SNOWFLAKE;
            Direction.Axis direction$axis = direction.getAxis();
            double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.25625D * (double)direction.getStepX() : (double)random.nextFloat();
            double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.25625D * (double)direction.getStepY() : (double)random.nextFloat();
            double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.25625D * (double)direction.getStepZ() : (double)random.nextFloat();
            level.addParticle(modeParticles, d1, d2, d3, 0, 0, 0);
            Vec3 baseMotion = new Vec3(.25, 0.1, 0);
            Vec3 baseVec = VecHelper.getCenterOf(worldPosition);
            for (int i = 0; i < 360; i += 10) {
                Vec3 m = VecHelper.rotate(baseMotion, i, Direction.Axis.Y);
                Vec3 v = baseVec.add(m.normalize()
                        .scale(.25f));
                
                level.addParticle(ParticleTypes.SNOWFLAKE, v.x, v.y, v.z, m.x, m.y, m.z);
            }
        }
    }
    private boolean isInColdBiome() {
        Biome biome = level.getBiome(worldPosition).value();
        return biome.coldEnoughToSnow(worldPosition);
    }
    @Override
    public void tick() {
        super.tick();
        if (!isRunning()) {
            level.setBlock(worldPosition, TEBlocks.areaHeater.getDefaultState().setValue(BlockStateProperties.LIT, false), 1);
        }
        //if (!hasSpaceBelow() && isInColdBiome()) {
        //    this.explodeWithSnow();
        //}
        if (isRunning() && isInColdBiome() /*&& hasSpaceBelow()*/) {
            level.setBlock(worldPosition, TEBlocks.areaHeater.getDefaultState().setValue(BlockStateProperties.LIT, true), 1);
            this.spawnSuctionParticles();
        }
    }
    public void onSpeedChanged(float previousSpeed) {
       
        setChanged();
    }
   
}
