package com.pouffydev.the_edge.foundation.client;

import com.pouffydev.the_edge.content.block.foliage.BearTrapBlock;
import com.pouffydev.the_edge.content.block.foliage.EdgeBushBlock;
import com.pouffydev.the_edge.content.block.foliage.SpineCurlBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TEBlockstateGen {
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> axisBlockProvider(
            boolean customItem) {
        return (c, p) -> axisBlock(c, p, getBlockModel(customItem, c, p));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalBlockProvider(
            boolean customItem) {
        return (c, p) -> p.directionalBlock(c.get(), getBlockModel(customItem, c, p));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalBlockProviderIgnoresWaterlogged(
            boolean customItem) {
        return (c, p) -> directionalBlockIgnoresWaterlogged(c, p, getBlockModel(customItem, c, p));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalBlockProvider(
            boolean customItem) {
        return (c, p) -> p.horizontalBlock(c.get(), getBlockModel(customItem, c, p));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalAxisBlockProvider(
            boolean customItem) {
        return (c, p) -> horizontalAxisBlock(c, p, getBlockModel(customItem, c, p));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleCubeAll(
            String path) {
        return (c, p) -> p.simpleBlock(c.get(), p.models()
                .cubeAll(c.getName(), p.modLoc("block/" + path)));
    }
    
    public static <T extends DirectionalAxisKineticBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalAxisBlockProvider() {
        return (c, p) -> directionalAxisBlock(c, p, ($, vertical) -> p.models()
                .getExistingFile(p.modLoc("block/" + c.getName() + "/" + (vertical ? "vertical" : "horizontal"))));
    }
    
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalWheelProvider(
            boolean customItem) {
        return (c, p) -> horizontalWheel(c, p, getBlockModel(customItem, c, p));
    }
    
    // Utility
    
    private static <T extends Block> Function<BlockState, ModelFile> getBlockModel(boolean customItem,
                                                                                   DataGenContext<Block, T> c, RegistrateBlockstateProvider p) {
        return $ -> customItem ? AssetLookup.partialBaseModel(c, p) : AssetLookup.standardModel(c, p);
    }
    public static <T extends Block> void directionalBlockIgnoresWaterlogged(DataGenContext<Block, T> ctx,
                                                                            RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStatesExcept(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180
                                    : dir.getAxis()
                                    .isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis()
                                    .isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }
    public static <T extends Block> void axisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                   Function<BlockState, ModelFile> modelFunc) {
        axisBlock(ctx, prov, modelFunc, false);
    }
    
    public static <T extends Block> void axisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                   Function<BlockState, ModelFile> modelFunc, boolean uvLock) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStatesExcept(state -> {
                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .uvLock(uvLock)
                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                            .rotationY(axis == Direction.Axis.X ? 90 : axis == Direction.Axis.Z ? 180 : 0)
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }
    
    public static <T extends Block> void simpleBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                     Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStatesExcept(state -> {
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }
    
    public static <T extends Block> void horizontalAxisBlock(DataGenContext<Block, T> ctx,
                                                             RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStates(state -> {
                    Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                            .build();
                });
    }
    
    public static <T extends DirectionalAxisKineticBlock> void directionalAxisBlock(DataGenContext<Block, T> ctx,
                                                                                    RegistrateBlockstateProvider prov, BiFunction<BlockState, Boolean, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStates(state -> {
                    
                    boolean alongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
                    Direction direction = state.getValue(DirectionalAxisKineticBlock.FACING);
                    boolean vertical = direction.getAxis()
                            .isHorizontal() && (direction.getAxis() == Direction.Axis.X) == alongFirst;
                    int xRot = direction == Direction.DOWN ? 270 : direction == Direction.UP ? 90 : 0;
                    int yRot = direction.getAxis()
                            .isVertical() ? alongFirst ? 0 : 90 : (int) direction.toYRot();
                    
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state, vertical))
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .build();
                });
    }
    
    public static <T extends Block> void horizontalWheel(DataGenContext<Block, T> ctx,
                                                         RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationX(90)
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING)
                                .toYRot() + 180) % 360)
                        .build());
    }
    
    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                 String textureSubDir) {
        cubeAll(ctx, prov, textureSubDir, ctx.getName());
    }
    
    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                 String textureSubDir, String name) {
        String texturePath = "block/" + textureSubDir + name;
        prov.simpleBlock(ctx.get(), prov.models()
                .cubeAll(ctx.getName(), prov.modLoc(texturePath)));
    }
    
    public static <B extends DoublePlantBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> existingDoubleFoliage() {
        return (c, p) -> {
            p.getVariantBuilder(c.get())
                    .forAllStatesExcept(state -> {
                        return ConfiguredModel.builder()
                                .modelFile(p.models()
                                        .getExistingFile(p.modLoc("block/" + c.getName().replace("tall_", "") + "/" + state.getValue(DoublePlantBlock.HALF)
                                                .name()
                                                .toLowerCase(Locale.ROOT))))
                                .build();
                    });
        };
    }
    public static <B extends SpineCurlBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> spineCurl() {
        return (c, p) -> {
            p.getVariantBuilder(c.get())
                    .forAllStatesExcept(state -> {
                        return ConfiguredModel.builder()
                                .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/upper")))
                                .build();
                    });
        };
    }
    public static <B extends EdgeBushBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> existingEdgeBush() {
        return (c, p) -> {
            p.getVariantBuilder(c.get())
                    .forAllStatesExcept(state -> {
                        return ConfiguredModel.builder()
                                .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/block")))
                                .build();
                    });
        };
    }
    public static <B extends BearTrapBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> bearTrap() {
        return (c, p) -> {
            p.getVariantBuilder(c.get())
                    .forAllStatesExcept(state -> {
                        return ConfiguredModel.builder()
                                .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/triggered_" + state.getValue(BearTrapBlock.triggered))))
                                .build();
                    });
        };
    }
}