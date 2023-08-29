package com.pouffydev.the_edge.foundation.data.recipe;

import com.google.common.base.Supplier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pouffydev.the_edge.TEItems;
import com.pouffydev.the_edge.TheEdge;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class TEStandardRecipeGen extends TheEdgeRecipeProvider {
    public TEStandardRecipeGen(DataGenerator generator) {
        super(generator);
    }
    /**
     * Recipes are added through fields, so one can navigate to the right one easily
     *
     * (Ctrl-o) in Eclipse
     */
    
    private Marker MATERIALS = enterFolder("materials");
    
    GeneratedRecipe
    
    platinumHelmet = create(TEItems.platinumHelmet).unlockedBy(TEItems.platinumIngot::get)
    .viaShaped(b -> b
            .define('p', I.platinum())
            .define('h', AllItems.NETHERITE_DIVING_HELMET.get())
            .define('g', Items.TINTED_GLASS)
            .define('s', I.platinumSheet())
            .define('t', I.electronTube())
            .pattern("ppp")
            .pattern("ghg")
            .pattern("sts")),
    
    platinumBacktank = create(TEItems.platinumBacktank).unlockedBy(TEItems.platinumIngot::get)
            .viaShaped(b -> b
                    .define('f', AllItems.PROPELLER.get())
                    .define('h', AllItems.NETHERITE_BACKTANK.get())
                    .define('p', I.platinum())
                    .define('s', I.platinumSheet())
                    .define('t', AllBlocks.FLUID_TANK.get())
                    .pattern("sfs")
                    .pattern("php")
                    .pattern("ptp")),
    
    platinumLeggings = create(TEItems.platinumLeggings).unlockedBy(TEItems.platinumIngot::get)
            .viaShaped(b -> b
                    .define('p', I.platinum())
                    .define('h', Items.NETHERITE_LEGGINGS)
                    .define('s', I.platinumSheet())
                    .define('k',Items.DRIED_KELP)
                    .pattern("pkp")
                    .pattern("shs")
                    .pattern("p p")),
    
    platinumBoots = create(TEItems.platinumBoots).unlockedBy(TEItems.platinumIngot::get)
            .viaShaped(b -> b
                    .define('p', I.platinum())
                    .define('h', AllItems.NETHERITE_DIVING_BOOTS.get())
                    .define('s', I.platinumSheet())
                    .define('k',Items.DRIED_KELP)
                    .pattern(" k ")
                    .pattern("php")
                    .pattern("sks")),
    
    platinumOre = create(TEItems.platinumIngot).withSuffix("_from_ore")
            .viaCookingTag(I::platinumOre)
            .rewardXP(1)
            .inBlastFurnace(),
    
    rawPlatinumOre = create(TEItems.platinumIngot).withSuffix("_from_raw_ore")
            .viaCooking(TEItems.rawPlatinum::get)
            .rewardXP(.7f)
            .inBlastFurnace(),
    
    bismuthOre = create(TEItems.bismuthIngot).withSuffix("_from_ore")
            .viaCookingTag(I::bismuthOre)
            .rewardXP(1)
            .inBlastFurnace(),
    
    rawBismuthOre = create(TEItems.bismuthIngot).withSuffix("_from_raw_ore")
            .viaCooking(TEItems.rawBismuth::get)
            .rewardXP(.7f)
            .inBlastFurnace(),
    
    glacioniteOre = create(TEItems.glacioniteIngot).withSuffix("_from_ore")
            .viaCookingTag(I::glacioniteOre)
            .rewardXP(1)
            .inBlastFurnace(),
    
    rawGlacioniteOre = create(TEItems.glacioniteIngot).withSuffix("_from_raw_ore")
            .viaCooking(TEItems.rawGlacionite::get)
            .rewardXP(.7f)
            .inBlastFurnace()
            
    ;
    String currentFolder = "";
    
    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }
    
    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }
    
    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }
    
    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }
    
    GeneratedRecipe createSpecial(Supplier<? extends SimpleRecipeSerializer<?>> serializer, String recipeType,
                                                       String path) {
        ResourceLocation location = TheEdge.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(serializer.get());
            b.save(consumer, location.toString());
        });
    }
    
    GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_crushed")
                .viaCooking(ingredient::get)
                .rewardXP(.1f)
                .inBlastFurnace();
    }
    GeneratedRecipe metalCompacting(List<ItemProviderEntry<? extends ItemLike>> variants,
                                                         List<Supplier<TagKey<Item>>> ingredients) {
        GeneratedRecipe result = null;
        for (int i = 0; i + 1 < variants.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = variants.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = variants.get(i + 1);
            Supplier<TagKey<Item>> currentIngredient = ingredients.get(i);
            Supplier<TagKey<Item>> nextIngredient = ingredients.get(i + 1);
            
            result = create(nextEntry).withSuffix("_from_compacting")
                    .unlockedBy(currentEntry::get)
                    .viaShaped(b -> b.pattern("###")
                            .pattern("###")
                            .pattern("###")
                            .define('#', currentIngredient.get()));
            
            result = create(currentEntry).returns(9)
                    .withSuffix("_from_decompacting")
                    .unlockedBy(nextEntry::get)
                    .viaShapeless(b -> b.requires(nextIngredient.get()));
        }
        return result;
    }
    
    GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike>> cycle) {
        GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("from_conversion")
                    .unlockedBy(currentEntry::get)
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }
    
    GeneratedRecipe clearData(ItemProviderEntry<? extends ItemLike> item) {
        return create(item).withSuffix("_clear")
                .unlockedBy(item::get)
                .viaShapeless(b -> b.requires(item.get()));
    }
    
    class GeneratedRecipeBuilder {
        
        private String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;
        
        private Supplier<ItemPredicate> unlockedBy;
        private int amount;
        
        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }
        
        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }
        
        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }
        
        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }
        
        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }
        
        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }
        
        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }
        
        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }
        
        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }
        
        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }
        
        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(consumer -> {
                ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        GeneratedRecipe viaSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                UpgradeRecipeBuilder b =
                        UpgradeRecipeBuilder.smithing(Ingredient.of(base.get()), upgradeMaterial.get(), result.get()
                                .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        private ResourceLocation createSimpleLocation(String recipeType) {
            return TheEdge.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }
        
        private ResourceLocation createLocation(String recipeType) {
            return TheEdge.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }
        
        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }
        
        class GeneratedCookingRecipeBuilder {
            
            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;
            
            private final SimpleCookingSerializer<?> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                    SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                    CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
            
            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }
            
            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }
            
            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }
            
            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }
            
            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(FURNACE, builder, 1);
            }
            
            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }
            
            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, .5f);
            }
            
            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }
            
            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                return create(BLAST, builder, .5f);
            }
            
            private GeneratedRecipe create(SimpleCookingSerializer<?> serializer,
                                                                UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return register(consumer -> {
                    boolean isOtherMod = compatDatagenOutput != null;
                    
                    SimpleCookingRecipeBuilder b = builder.apply(
                            SimpleCookingRecipeBuilder.cooking(ingredient.get(), isOtherMod ? Items.DIRT : result.get(),
                                    exp, (int) (cookingTime * cookingTimeModifier), serializer));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                    b.save(result -> {
                        consumer.accept(
                                isOtherMod ? new ModdedCookingRecipeResult(result, compatDatagenOutput, recipeConditions)
                                        : result);
                    }, createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer)
                            .getPath()));
                });
            }
        }
    }
    
    @Override
    public String getName() {
        return "The Edge's Standard Recipes";
    }
    
    private static class ModdedCookingRecipeResult implements FinishedRecipe {
        
        private FinishedRecipe wrapped;
        private ResourceLocation outputOverride;
        private List<ICondition> conditions;
        
        public ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride,
                                         List<ICondition> conditions) {
            this.wrapped = wrapped;
            this.outputOverride = outputOverride;
            this.conditions = conditions;
        }
        
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }
        
        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }
        
        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }
        
        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }
        
        @Override
        public void serializeRecipeData(JsonObject object) {
            wrapped.serializeRecipeData(object);
            object.addProperty("result", outputOverride.toString());
            
            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }
        
    }
    
}
