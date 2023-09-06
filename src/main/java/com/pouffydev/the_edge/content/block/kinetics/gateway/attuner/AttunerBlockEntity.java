package com.pouffydev.the_edge.content.block.kinetics.gateway.attuner;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Objects;

public class AttunerBlockEntity extends KineticBlockEntity {
    private static final int MAX_RANGE = 20;
    private static final int DELAY_TICKS = 20 * 20; // 20 seconds delay
    
    private boolean isTriggered = false;
    private int delayCounter = 0;
    private long linkedBlockPos;
    private boolean isLinked = false;
    public AttunerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        linkedBlockPos = compound.getLong("LinkedBlockPos");
        super.read(compound, clientPacket);
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putLong("LinkedBlockPos", linkedBlockPos);
        super.write(compound, clientPacket);
    }
    @Override
    public void tick() {
        super.tick();
        //checkForPairedAttuner();
        //if (isTriggered) {
        //    if (delayCounter < DELAY_TICKS) {
        //        delayCounter++;
        //    } else {
        //        delayCounter = 0;
        //        isTriggered = false;
        //
        //        // Place a block at the center point between the two block entities
        //    }
        //    tear();
        //}
    }
    private boolean hasCertainBlockState() {
        // Implement your logic to check if the block has a certain block state
        // Return true if it has the desired state, false otherwise
        return true; // Modify this according to your needs
    }
    private boolean areAttunersReadyToTear() {
        // Check if the block entities are linked
        if (isLinked) {
            return true;
        }
        
        return false;
    }
    private void tear() {
        if (isTriggered) {
            placeBlockAtCenter();
        }
    }
    public void checkForPairedAttuner() {
        // check if the block is facing up
        if (level != null && !level.isClientSide && this.getBlockState().getValue(BlockStateProperties.FACING) == Direction.UP) {
            BlockPos blockEntityPos = this.getBlockPos();
            Direction up = Direction.UP;
            
            // Iterate upwards within the specified range
            for (int i = 1; i <= MAX_RANGE; i++) {
                BlockPos checkPos = BlockPos.of(blockEntityPos.offset(i, up));
                BlockEntity blockEntity = level.getBlockEntity(checkPos);
                
                // Check if the block entity exists and is of the same type
                if (blockEntity instanceof AttunerBlockEntity) {
                    AttunerBlockEntity otherBlockEntity = (AttunerBlockEntity) blockEntity;
                    
                    // Check if there are no obstacles in the way
                    if (isPathClear(blockEntityPos, otherBlockEntity.getBlockPos())) {
                        // Set the "linked" block state to true for both block entities
                        this.setLinked(true);
                        otherBlockEntity.setLinked(true);
                        
                        // Save the location of the linked block in NBT data
                        this.setLinkedBlockLocation(otherBlockEntity.getBlockPos());
                        otherBlockEntity.setLinkedBlockLocation(this.getBlockPos());
                    }
                } else {
                    // Stop searching if a different block entity is encountered
                    break;
                }
            }
        }
    }
    private boolean isPathClear(BlockPos startPos, BlockPos endPos) {
        // Define the bounding box for the 3x3x3 area
        int minX = Math.min(startPos.getX(), endPos.getX()) - 1;
        int minY = Math.min(startPos.getY(), endPos.getY()) - 1;
        int minZ = Math.min(startPos.getZ(), endPos.getZ()) - 1;
        int maxX = Math.max(startPos.getX(), endPos.getX()) + 1;
        int maxY = Math.max(startPos.getY(), endPos.getY()) + 1;
        int maxZ = Math.max(startPos.getZ(), endPos.getZ()) + 1;
        
        // Iterate through the 3x3x3 area
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    if (!checkPos.equals(startPos) && !checkPos.equals(endPos)) {
                        BlockState state = level.getBlockState(checkPos);
                        
                        // Check if the block at the checkPos is an obstacle (modify this condition)
                        if (!state.isAir()) {
                            return false; // Path is not clear
                        }
                    }
                }
            }
        }
        
        return true; // Path is clear
    }
    private void placeBlockAtCenter() {
        // Calculate the center position between the two block entities
        BlockPos thisPos = this.getBlockPos();
        BlockPos otherPos = thisPos.offset(Direction.UP.getNormal());
        BlockPos centerPos = thisPos.offset(otherPos).offset(0, 1, 0);
        
        // Decide whether to place the block at the lower or higher position
        if (thisPos.getY() < otherPos.getY()) {
            // Place the block at thisPos
            if (level != null) {
                BlockState blockStateToPlace = Blocks.DIRT.defaultBlockState();
                level.setBlockAndUpdate(thisPos, blockStateToPlace);
            }
        } else {
            // Place the block at otherPos
            if (level != null) {
                BlockState blockStateToPlace = Blocks.DIRT.defaultBlockState();
                level.setBlockAndUpdate(otherPos, blockStateToPlace);
            }
        }
        
    }
    private void setLinked(boolean linked) {
        // Modify the block state to set the "linked" property
        BlockState state = Objects.requireNonNull(this.getLevel()).getBlockState(this.getBlockPos());
        this.getLevel().setBlockAndUpdate(this.getBlockPos(), state.setValue(Attuner.linked, linked));
    }
    
    private void setLinkedBlockLocation(BlockPos linkedPos) {
        linkedBlockPos = linkedPos.asLong();
    }
}
