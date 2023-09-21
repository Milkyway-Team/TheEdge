package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import com.simibubi.create.content.legacy.ChromaticCompoundItem;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class HeartDoorBlock extends HorizontalDirectionalBlock implements IBE<HeartDoorBlockEntity> {
    public static final DamageSource heartOffering = (new DamageSource("the_edge.heart_offering")).bypassArmor().bypassMagic();
    public static final DamageSource playerDestruction = (new DamageSource("the_edge.player_destruction")).bypassArmor().bypassMagic().bypassInvul();
    public static final IntegerProperty difficulty = IntegerProperty.create("difficulty", 0, 4);
    public HeartDoorBlock(Properties p, int difficultyLvl) {
        super(p);
        this.registerDefaultState(this.stateDefinition.any().setValue(difficulty, difficultyLvl));
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(difficulty, FACING);
    }
    
    protected static Integer getHearts(HeartDoorBlockEntity be) {
        return switch (HeartDoorBlockEntity.getDifficulty(be)) {
            case 0 -> 2;
            case 1 -> 4;
            case 2 -> 6;
            case 3 -> 8;
            case 4 -> 10;
            default -> 0;
        };
    }
    
    @Override
    public Class<HeartDoorBlockEntity> getBlockEntityClass() {
        return HeartDoorBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends HeartDoorBlockEntity> getBlockEntityType() {
        return null;
    }
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        LivingEntity entity = player;
        if (player.getHealth() > getHearts(this.getBlockEntity(level, pos))) {
            decreaseMaxHealth(entity, getHearts(this.getBlockEntity(level, pos)));
            return InteractionResult.SUCCESS;
        }
        else if (player.getHealth() <= getHearts(this.getBlockEntity(level, pos))) {
            player.hurt(playerDestruction, player.getHealth());
            spawnParticles(entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
    private void decreaseMaxHealth(LivingEntity entity, int amount) {
        AttributeModifier modifier = new AttributeModifier("Heart Door Offering", -amount, AttributeModifier.Operation.ADDITION);
        Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
    }
    private void spawnParticles(LivingEntity entity) {
        Level world = entity.level;
        Vec3 positionVec = entity.position();
        Vec3 start = VecHelper.offsetRandomly(positionVec, world.random, 3);
        Vec3 motion = positionVec.subtract(start)
                .normalize()
                .scale(.2f);
        world.addParticle(ParticleTypes.END_ROD, start.x, start.y, start.z, motion.x, motion.y, motion.z);
    }
}
