package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import com.mojang.math.Vector3f;
import com.pouffydev.the_edge.TEBlocks;
import com.pouffydev.the_edge.TESpriteShifts;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;


@SuppressWarnings("ALL")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HeartDoorBlock extends Block {
    public static final BooleanProperty active = BooleanProperty.create("active");
    public static final BooleanProperty vanished = BooleanProperty.create("vanished");
    public static final DamageSource heartOffering = (new DamageSource("the_edge.heart_offering")).bypassArmor().bypassMagic();
    
    
    public static CTSpriteShiftEntry stateDependentCT() {
        BlockState state = TEBlocks.heartDoor.get().defaultBlockState();
        if (state.getValue(vanished))
            return TESpriteShifts.heartDoorVanished;
        else
            return TESpriteShifts.heartDoor;
    }
    public HeartDoorBlock(Properties p) {
        super(p);
        this.registerDefaultState(this.stateDefinition.any().setValue(active, false).setValue(vanished, false));
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(active, vanished);
    }
    private boolean isVanished(BlockState state) {
        return state.hasProperty(vanished) && state.getValue(vanished);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }
    @Override
    @Deprecated
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!isVanished(state) && !state.getValue(active)) {
            if (areBlocksLocked(world, pos)) {
                world.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 0.3F);
            } else {
                activate(world, pos);
            }
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
    private static boolean areBlocksLocked(BlockGetter world, BlockPos start) {
        int limit = 512;
        Deque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> checked = new HashSet<>();
        queue.offer(start);
        
        for (int iter = 0; !queue.isEmpty() && iter < limit; iter++) {
            BlockPos cur = queue.pop();
            BlockState state = world.getBlockState(cur);
            if (state.getBlock() == TEBlocks.lockedHeartDoor.get() && state.getValue(LockedHeartDoorBlock.locked)) {
                return true;
            }
            
            checked.add(cur);
            
            if (state.getBlock() instanceof HeartDoorBlock) {
                for (Direction facing : Direction.values()) {
                    BlockPos neighbor = cur.relative(facing);
                    if (!checked.contains(neighbor)) {
                        queue.offer(neighbor);
                    }
                }
            }
        }
        
        return false;
    }
    @Override
    @Deprecated
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (world.isClientSide) {
            return;
        }
        
        if (!isVanished(state) && !state.getValue(active) && world.hasNeighborSignal(pos) && !areBlocksLocked(world, pos)) {
            activate(world, pos);
        }
    }
    @Override
    @Deprecated
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (world.isClientSide) {
            return;
        }
        
        if (isVanished(state)) {
            if (state.getValue(active)) {
                world.setBlockAndUpdate(pos, state.setValue(vanished, false).setValue(active, false));
            } else {
                world.setBlockAndUpdate(pos, state.setValue(active, true));
                world.scheduleTick(pos, this, 15);
            }
            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.3F, 0.6F);
        } else {
            if (state.getValue(active)) {
                if (state.hasProperty(vanished)) {
                    world.setBlockAndUpdate(pos, state.setValue(active, false).setValue(vanished, true));
                    world.scheduleTick(pos, this, 80);
                } else {
                    world.removeBlock(pos, false);
                }
                
                world.playSound(null, pos, state.getBlock() instanceof HeartDoorBlock ? SoundEvents.AXE_WAX_OFF : SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 0.3F, 0.5F);
                
                for (Direction e : Direction.values()) {
                    activate(world, pos.relative(e));
                }
            }
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (state.getValue(active)) {
            this.sparkle(world, pos);
        }
    }
    private void activate(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof HeartDoorBlock && !isVanished(state) && !state.getValue(active)) {
            world.setBlockAndUpdate(pos, state.setValue(active, true));
            world.scheduleTick(pos, state.getBlock(), 2 + world.random.nextInt(5));
        }
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return isVanished(state) ? Shapes.empty() : super.getCollisionShape(state, world, pos, ctx);
    }
   
}
