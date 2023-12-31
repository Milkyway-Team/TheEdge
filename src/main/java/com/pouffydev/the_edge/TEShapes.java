package com.pouffydev.the_edge;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static com.simibubi.create.AllShapes.SIX_VOXEL_POLE;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

public class TEShapes {
    public static final VoxelShaper
            
            drillHead = shape(3, 0, 3, 13, 4, 13).add(2, 4, 2, 14, 8, 14).add(2, 8, 2, 14.5, 14, 14.5).add(4, 14, 4, 12, 16, 12).forDirectional();
    public static final VoxelShape largeGear = cuboid(0, 6, 0, 16, 10, 16);
    public static final VoxelShaper largeGearDirectional = shape(largeGear).add(SIX_VOXEL_POLE.get(Direction.Axis.Y))
            .forDirectional()
            ;
    
    private static Builder shape(VoxelShape shape) {
        return new Builder(shape);
    }
    
    private static Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }
    
    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
    
    public static class Builder {
        
        private VoxelShape shape;
        
        public Builder(VoxelShape shape) {
            this.shape = shape;
        }
        
        public Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }
        
        public Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }
        
        public Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }
        
        public VoxelShape build() {
            return shape;
        }
        
        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }
        
        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }
        
        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }
        
        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }
        
        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }
        
        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }
        
        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }
        
    }
}
