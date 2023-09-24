package com.pouffydev.the_edge.foundation.client;

import com.pouffydev.the_edge.content.block.dungeons.heart_door.HeartDoorBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class TEBuilderTransformers {
    
    public static <B extends HeartDoorBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> heartDoor() {
        return b -> b.properties(p -> p.sound(SoundType.GLASS)
                        .strength(50.0F, 1200.0F)
                        .noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(HeartDoorBlock.stateDependentCT())))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, HeartDoorBlock.stateDependentCT())))
                .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
                .item()
                .build();
    }
    public static <B extends Block> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> ctBlock(
            Supplier<CTSpriteShiftEntry> ct) {
        return b -> b
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(ct.get())))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, ct.get())))
                .item()
                .build();
    }
}
