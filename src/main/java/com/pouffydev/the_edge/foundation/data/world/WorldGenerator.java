package com.pouffydev.the_edge.foundation.data.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.pouffydev.the_edge.TheEdge;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeKeys;
import com.pouffydev.the_edge.foundation.data.world.biome.BiomeMaker;
import com.pouffydev.the_edge.foundation.data.world.biome.TEBiomeProvider;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TEGenerationSettings;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TENoiseGenerationSettings;
import com.pouffydev.the_edge.foundation.data.world.biome.chunkgenerators.TerrainPoint;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record WorldGenerator(DataGenerator generator) implements DataProvider {
    private static final Logger LOGGER = TheEdge.LOGGER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        RegistryAccess registryaccess = BuiltinRegistries.ACCESS;
        DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, registryaccess);
        
        Registry<LevelStem> edge = this.registerTESettings(registryaccess);
        //Registry<LevelStem> skylight = this.registerSkylightSettings(registryaccess);
        //TODO void world
        
        WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
        Map<ResourceLocation, Biome> biomes = this.getBiomes();
        biomes.forEach((rl, biome) -> biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental()));
        
        StreamSupport.stream(RegistryAccess.knownRegistries().spliterator(), false)
                .filter(r -> registryaccess.ownedRegistry(r.key()).isPresent() && !r.key().equals(Registry.BIOME_REGISTRY))
                .forEach((data) -> dumpRegistryCap(cache, path, registryaccess, dynamicops, data));
        
        LOGGER.info("Dumping real BIOME_REGISTRY");
        dumpRegistry(path, cache, dynamicops, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);
        
        LOGGER.info("Dumping real LEVEL_STEM_REGISTRY");
        dumpRegistry(path, cache, dynamicops, Registry.LEVEL_STEM_REGISTRY, edge, LevelStem.CODEC);
    }
    private static <T> void dumpRegistryCap(HashCache cache, Path path, RegistryAccess access, DynamicOps<JsonElement> ops, RegistryAccess.RegistryData<T> data) {
        LOGGER.info("Dumping: {}");
        dumpRegistry(path, cache, ops, data.key(), access.ownedRegistryOrThrow(data.key()), data.codec());
    }
    private Registry<LevelStem> registerTESettings(RegistryAccess access) {
        WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
        Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
        Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrCreateHolder(Objects.requireNonNull(TENoiseGenerationSettings.FLOATING_ISLANDS.getKey()));
        
        NoiseBasedChunkGenerator forestChunkGen =
                new NoiseBasedChunkGenerator(
                        access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                        access.registryOrThrow(Registry.NOISE_REGISTRY),
                        new TEBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), -1.25F, 2.5F),
                        0L,
                        noiseGenSettings
                );
        
        writableregistry.register(TEGenerationSettings.WORLDGEN_KEY, new LevelStem(Holder.direct(this.edgeDimType()), new ChunkGeneratorEdge(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), noiseGenSettings, true, true, Optional.of(12), true), true), Lifecycle.experimental());
        return writableregistry;
    }
    private Registry<LevelStem> registerSkylightSettings(RegistryAccess access) {
        WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
        Registry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
        Holder<NoiseGeneratorSettings> noiseGenSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrCreateHolder(Objects.requireNonNull(TENoiseGenerationSettings.SKYLIGHT_NOISE_GEN.getKey()));
        
        NoiseBasedChunkGenerator forestChunkGen =
                new NoiseBasedChunkGenerator(
                        access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                        access.registryOrThrow(Registry.NOISE_REGISTRY),
                        new TEBiomeProvider(0L, biomeRegistry, makeBiomeList(biomeRegistry), -1.25F, 2.5F),
                        4L, //drull had it set like this so its staying until he changes it
                        noiseGenSettings
                );
        
        writableregistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, TheEdge.asResource("skylight_edge")), new LevelStem(Holder.direct(this.edgeDimType()), new ChunkGeneratorEdge(forestChunkGen, access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), noiseGenSettings, true, true, Optional.of(12), true)), Lifecycle.stable());
        return writableregistry;
    }
    private static <E, T extends Registry<E>> void dumpRegistry(Path path, HashCache cache, DynamicOps<JsonElement> ops, ResourceKey<? extends T> key, T registry, Encoder<E> encoder) {
        for (Map.Entry<ResourceKey<E>, E> entry : registry.entrySet()) {
            if (entry.getKey().location().getNamespace().equals(TheEdge.ID)) {
                LOGGER.info("\t\t{}", entry.getKey().location().getPath());
                Path otherPath = createPath(path, key.location(), entry.getKey().location());
                dumpValue(otherPath, cache, ops, encoder, entry.getValue());
            }
        }
        
    }
    private static <E> void dumpValue(Path path, HashCache cache, DynamicOps<JsonElement> ops, Encoder<E> encoder, E entry) {
        try {
            Optional<JsonElement> optional = encoder.encodeStart(ops, entry).resultOrPartial((p_206405_) -> {
                LOGGER.error("Couldn't serialize element {}: {}", path, p_206405_);
            });
            if (optional.isPresent()) {
                if (optional.get().isJsonObject()) {
                    JsonObject object = optional.get().getAsJsonObject();
                    if (object.has("generator") && object.get("generator").isJsonObject()) {
                        JsonObject generator = object.getAsJsonObject("generator");
                        if (generator.has("use_overworld_seed")) {
                            generator.remove("use_overworld_seed");
                            generator.addProperty("use_overworld_seed", true);
                        }
                        if (generator.has("wrapped_generator")) {
                            JsonObject wrapped_generator = generator.getAsJsonObject("wrapped_generator");
                            if (wrapped_generator.has("biome_source"))
                                wrapped_generator.getAsJsonObject("biome_source").remove("seed");
                        }
                    }
                }
                DataProvider.save(GSON, cache, optional.get(), path);
            }
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save element {}", path, ioexception);
        }
        
    }
    
    private static Path createPath(Path path, ResourceLocation registry, ResourceLocation entry) {
        return path.resolve("data").resolve(entry.getNamespace()).resolve(registry.getPath()).resolve(entry.getPath() + ".json");
    }
    
    public String getName() {
        return "Worldgen";
    }
    private DimensionType edgeDimType() {
        return DimensionType.create(
                OptionalLong.of(13000L), //fixed time TODO Kill the celestial bodies
                false, //skylight
                false, //ceiling
                false, //ultrawarm
                false, //natural
                0.125D, //coordinate scale
                false, //dragon fight
                false, //piglin safe
                false, //bed works
                true, //respawn anchor works
                true, //raids
                -64, // Minimum Y Level
                64 + 384, // Height + Min Y = Max Y
                64 + 256, // Logical Height
                BlockTags.INFINIBURN_OVERWORLD, //infiburn
                TheEdge.asResource("renderer"), // DimensionRenderInfo
                0f // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
        );
    }
    private Map<ResourceLocation, Biome> getBiomes() {
        return BiomeMaker
                .BIOMES
                .entrySet()
                .stream()
                .peek(registryKeyBiomeEntry -> registryKeyBiomeEntry.getValue().setRegistryName(registryKeyBiomeEntry.getKey().location()))
                .collect(Collectors.toMap(entry -> entry.getKey().location(), Map.Entry::getValue));
    }
    private List<Pair<TerrainPoint, Holder<Biome>>> makeBiomeList(Registry<Biome> registry) {
        return List.of(
                pairBiome(registry, 0.025F, 0.05F, BiomeKeys.theEdge),
                pairBiome(registry, 0.1F, 0.2F, BiomeKeys.glitchwoodForest),
                pairBiome(registry, 0.0625F, 0.05F, BiomeKeys.glacialPeaks),
                pairBiome(registry, 0.005F, 0.005F, BiomeKeys.starfallSwamp)
        );
    }
    
    private Pair<TerrainPoint, Holder<Biome>> pairBiome(Registry<Biome> registry, float depth, float scale, ResourceKey<Biome> key) {
        return Pair.of(new TerrainPoint(depth, scale), Holder.Reference.createStandAlone(registry, key));
    }
    
}
