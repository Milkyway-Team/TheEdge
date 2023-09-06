package com.pouffydev.the_edge.content.block.kinetics.groundbreaker.drill;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pouffydev.the_edge.content.block.kinetics.groundbreaker.json.ExposedBlock;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

import static com.pouffydev.the_edge.content.block.kinetics.groundbreaker.json.JsonListener.mineableBlocks;

public class GBDrillBlockEntity extends KineticBlockEntity {
    public boolean running;
    public int runningTicks;
    private boolean isRunning() {
        return running;
    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        super.read(compound, clientPacket);
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        super.write(compound, clientPacket);
    }
    public GBDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        super.tick();
        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
        }
        if (runningTicks <= 20) {
            running = true;
            runningTicks = 0;
        }
        int randomWaitTime = Create.RANDOM.nextInt(20);
        if (running) {
            if (level.getGameTime() % 20 == 0) {
                if (Create.RANDOM.nextInt(100) < randomWaitTime) {
                    randomlyPopLootFromSurface(worldPosition, (ServerLevel) level);
                    spawnBlockBreakParticles(worldPosition, (ServerLevel) level);
                }
            }
        }
        if (runningTicks != 20)
            runningTicks++;
    }
    public boolean getBlockBelow(BlockPos pos, ServerLevel world, Block block) {
        //Check if a 5x1x5 platform below the block is made of the same block specified in the json file excluding the block directly below the drill
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (world.getBlockState(pos.below().offset(x, -1, z)).getBlock() == block) {
                    return false;
                }
            }
        }
        return true;
    }
    @SuppressWarnings({"deprecation"})
    public void spawnBlockBreakParticles(BlockPos pos, ServerLevel world) {
        if (!world.isClientSide)
            return;
        //Spawn particles based on the block type being mined around the block
        for (JsonElement jsonElement : mineableBlocks) {
            JsonObject json = jsonElement.getAsJsonObject();
            String platformBlock = json.get("platformBlock").getAsString();
            BlockPos randomPosAroundBlock = pos.offset(Create.RANDOM.nextInt(3) - 1, Create.RANDOM.nextInt(3) - 1, Create.RANDOM.nextInt(3) - 1);
            if (world.getBlockState(pos).is(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(platformBlock))).defaultBlockState().getBlock())) {
                ParticleOptions blockParticles = new BlockParticleOption(ParticleTypes.BLOCK, Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(platformBlock))).defaultBlockState());
                world.addParticle(blockParticles, randomPosAroundBlock.getX(), randomPosAroundBlock.getY(), randomPosAroundBlock.getZ(), 0, 0, 0);
            }
        }
    }
    @SuppressWarnings({"deprecation"})
    public void randomlyPopLootFromSurface(BlockPos pos, ServerLevel world) {
        
        BlockPos randomPosAroundBlock = pos.offset(Create.RANDOM.nextInt(3) - 1, Create.RANDOM.nextInt(3) - 1, Create.RANDOM.nextInt(3) - 1);
        //Spawn items based on the block type being mined around the block
        for (JsonElement jsonElement : mineableBlocks) {
            JsonObject json = jsonElement.getAsJsonObject();
            String requiredBlock = json.get("requiredBlock").getAsString();
            String platformBlock = json.get("platformBlock").getAsString();
            Vec3 vec3d = new Vec3(randomPosAroundBlock.getX(), randomPosAroundBlock.getY(), randomPosAroundBlock.getZ());
            if (world.getBlockState(pos.below()).is(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(requiredBlock))).defaultBlockState().getBlock()) && getBlockBelow(pos, world, Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(platformBlock))).defaultBlockState().getBlock())) {
                LootTable loottable = world.getServer().getLootTables().get(new ResourceLocation(json.get("lootTable").getAsString()));
                List<ItemStack> list = loottable.getRandomItems(new LootContext.Builder(world)
                        .withParameter(LootContextParams.ORIGIN, vec3d)
                        .withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(worldPosition))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .create(LootContextParamSets.BLOCK));
                for (ItemStack item : list) {
                        Entity itemEntity = new ItemEntity(world, randomPosAroundBlock.getX(), randomPosAroundBlock.getY(), randomPosAroundBlock.getZ(), item);
                        itemEntity.setDeltaMovement(0, 0.1, 0);
                        world.addFreshEntity(itemEntity);
                }
            }
        }
    }
}
