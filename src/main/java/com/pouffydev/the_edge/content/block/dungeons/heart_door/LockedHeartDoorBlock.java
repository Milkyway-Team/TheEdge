package com.pouffydev.the_edge.content.block.dungeons.heart_door;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
@SuppressWarnings("ALL")
public class LockedHeartDoorBlock extends HeartDoorBlock {
    public static final DamageSource playerDestruction = (new DamageSource("the_edge.player_destruction")).bypassArmor().bypassMagic().bypassInvul();
    public static final EnumProperty<DungeonDifficulty> difficulty = EnumProperty.create("difficulty", DungeonDifficulty.class);
    public static final BooleanProperty locked = BooleanProperty.create("locked");
    public LockedHeartDoorBlock(Properties p) {
        super(p);
        this.registerDefaultState(defaultBlockState().setValue(locked, true).setValue(difficulty, DungeonDifficulty.EASY));
    }
    protected static Integer getHearts(BlockState state) {
        DungeonDifficulty dungeonDifficulty = state.getValue(difficulty);
        return switch (dungeonDifficulty) {
            case EASY -> 4;
            case NORMAL -> 6;
            case HARD -> 10;
            case EXTREME -> 14;
            case IMPOSSIBLE -> 18;
            default -> 0;
        };
    }
    private void decreaseMaxHealth(LivingEntity entity, int amount) {
        AttributeModifier modifier = new AttributeModifier("Heart Door Offering", -amount, AttributeModifier.Operation.ADDITION);
        Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
        entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1, false, false, false));
    }
    private void spawnParticles(LivingEntity entity, ParticleOptions particle, int amount) {
        Level world = entity.level;
        Vec3 positionVec = entity.position();
        Vec3 start = VecHelper.offsetRandomly(positionVec, world.random, 3);
        Vec3 motion = positionVec.subtract(start)
                .normalize()
                .scale(.2f);
        int n = amount;
        while (n > 0) {
            System.out.println(n);
            n = n - 1;
        }
        world.addParticle(particle, start.x, start.y, start.z, motion.x, motion.y, motion.z);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(locked, difficulty);
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getHealth() > getHearts(state) && state.getValue(locked)) {
            decreaseMaxHealth(player, getHearts(state));
            spawnParticles(player, ParticleTypes.HEART, getHearts(state));
            world.setBlockAndUpdate(pos, state.setValue(locked, false));
            return InteractionResult.SUCCESS;
        }
        else if (player.getHealth() <= getHearts(state) && state.getValue(locked)) {
            player.hurt(playerDestruction, player.getHealth());
            int healthParticles = Math.round(player.getHealth());
            spawnParticles(player, ParticleTypes.HEART, healthParticles);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}
