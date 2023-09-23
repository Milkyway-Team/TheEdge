package com.pouffydev.the_edge.content.block.dungeons.completion_monolith;

import com.mojang.math.Vector3f;
import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.content.block.dungeons.heart_door.DungeonDifficulty;
import com.pouffydev.the_edge.content.block.dungeons.heart_door.LockedHeartDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;
import java.util.Random;

public class CompletionMonolithBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> half = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty deactivated = BooleanProperty.create("deactivated");
    
    public CompletionMonolithBlock(Properties p) {
        super(p);
        this.registerDefaultState(this.stateDefinition.any().setValue(deactivated, false).setValue(half, DoubleBlockHalf.LOWER));
    }
    private void resetMaxHealth(LivingEntity entity, int amount) {
        AttributeModifier modifier = new AttributeModifier("Completion Reward", +amount, AttributeModifier.Operation.ADDITION);
        Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
        entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1, false, false, false));
    }
    protected static Integer getHearts(DungeonDifficulty dungeonDifficulty) {
        return switch (dungeonDifficulty) {
            case EASY -> 4;
            case NORMAL -> 6;
            case HARD -> 10;
            case EXTREME -> 14;
            case IMPOSSIBLE -> 18;
            default -> 0;
        };
    }
    @Override
    @Deprecated
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
    }
    private boolean isDeactivated(BlockState state) {
        return state.hasProperty(deactivated) && state.getValue(deactivated);
    }
    @Override
    @Deprecated
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (world.isClientSide) {
            return;
        }
        if (!isDeactivated(state) && !state.getValue(deactivated) && world.hasNeighborSignal(pos)) {
            world.setBlockAndUpdate(pos, TEBlocks.completionMonolith.get().defaultBlockState().setValue(deactivated, true));
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (state.getValue(deactivated)) {
            this.sparkle(world, pos);
        }
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(deactivated, half);
    }
    public void sparkle(Level worldIn, BlockPos pos) {
        Random random = worldIn.random;
        double d0 = 0.0625D;
        
        for (int i = 0; i < 6; ++i) {
            double d1 = (float) pos.getX() + random.nextFloat();
            double d2 = (float) pos.getY() + random.nextFloat();
            double d3 = (float) pos.getZ() + random.nextFloat();
            
            if (i == 0 && !worldIn.getBlockState(pos.above()).isSolidRender(worldIn, pos)) {
                d2 = (double) pos.getY() + d0 + 1.0D;
            }
            
            if (i == 1 && !worldIn.getBlockState(pos.below()).isSolidRender(worldIn, pos)) {
                d2 = (double) pos.getY() - d0;
            }
            
            if (i == 2 && !worldIn.getBlockState(pos.south()).isSolidRender(worldIn, pos)) {
                d3 = (double) pos.getZ() + d0 + 1.0D;
            }
            
            if (i == 3 && !worldIn.getBlockState(pos.north()).isSolidRender(worldIn, pos)) {
                d3 = (double) pos.getZ() - d0;
            }
            
            if (i == 4 && !worldIn.getBlockState(pos.east()).isSolidRender(worldIn, pos)) {
                d1 = (double) pos.getX() + d0 + 1.0D;
            }
            
            if (i == 5 && !worldIn.getBlockState(pos.west()).isSolidRender(worldIn, pos)) {
                d1 = (double) pos.getX() - d0;
            }
            
            float f1 = 0.6F + 0.4F;
            float f2 = Math.max(0.0F, 1.0F * 1.0F * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, 1.0F * 1.0F * 0.6F - 0.7F);
            if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
                worldIn.addParticle(new DustParticleOptions(new Vector3f(f1, f2, f3), 1.0F), d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    @Override
    @Deprecated
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int radius = 50; // Change this to your desired radius.
        // Loop through the blocks in the specified radius.
        if (!isDeactivated(state)) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos targetPos = pos.offset(x, y, z);
                        Block targetBlock = world.getBlockState(targetPos).getBlock();
                        
                        if (targetBlock == TEBlocks.lockedHeartDoor.get()) {
                            DungeonDifficulty difficulty = world.getBlockState(targetPos).getValue(LockedHeartDoorBlock.difficulty);
                            resetMaxHealth(player, getHearts(difficulty));
                            world.setBlockAndUpdate(targetPos, TEBlocks.heartDoor.getDefaultState());
                            world.setBlockAndUpdate(pos, TEBlocks.completionMonolith.get().defaultBlockState().setValue(deactivated, true));
                            
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.PASS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
    //int radius = 5; // Change this to your desired radius.
    //
    //// Loop through the blocks in the specified radius.
    //    for (int x = -radius; x <= radius; x++) {
    //    for (int y = -radius; y <= radius; y++) {
    //        for (int z = -radius; z <= radius; z++) {
    //            BlockPos targetPos = pos.add(x, y, z);
    //            Block targetBlock = world.getBlockState(targetPos).getBlock();
    //
    //            // Check if the block is a diamond block.
    //            if (targetBlock == Blocks.DIAMOND_BLOCK) {
    //                // Replace the diamond block with dirt.
    //                world.setBlockState(targetPos, Blocks.DIRT.getDefaultState());
    //
    //                // Optionally, you can play a sound or perform other actions here.
    //            }
    //        }
    //    }
    //}
}
