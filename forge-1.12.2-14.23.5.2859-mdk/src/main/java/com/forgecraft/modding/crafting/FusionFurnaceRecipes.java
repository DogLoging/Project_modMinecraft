package com.forgecraft.modding.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.forgecraft.modding.init.ModItems;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FusionFurnaceRecipes
{
	private static final FusionFurnaceRecipes fusionRecipes = new FusionFurnaceRecipes();
	private final Table<ItemStack, ItemStack, ItemStack> fusionRecipesList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();
	
	public static FusionFurnaceRecipes instance()
	{
		return fusionRecipes;
	}
	
	public FusionFurnaceRecipes()
	{
		addFusionRecipes(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(ModItems.copper_ingot), 10.0F);
		addFusionRecipes(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.COAL), new ItemStack(ModItems.steel_ingot), 10.0F);
		addFusionRecipes(new ItemStack(ModItems.copper_ingot), new ItemStack(ModItems.steel_ingot), new ItemStack(ModItems.titanium_ingot), 25.0F);
		addFusionRecipes(new ItemStack(ModItems.titanium_ingot), new ItemStack(Blocks.OBSIDIAN), new ItemStack(ModItems.adamantium_ingot), 30.0F);
	}
	
	public void addFusionRecipes(ItemStack slot_1, ItemStack slot_2, ItemStack result, float exp)
	{
		if(this.getFusionResult(slot_1, slot_2) != ItemStack.EMPTY) return;
		
		this.fusionRecipesList.put(slot_1, slot_2, result);
		this.experienceList.put(result, Float.valueOf(exp));
	}

	public ItemStack getFusionResult(ItemStack slot_1, ItemStack slot_2)
	{
		if(slot_1.isEmpty() && slot_2.isEmpty()) return ItemStack.EMPTY;
		
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.fusionRecipesList.columnMap().entrySet())
		{
			if(comparationItem(slot_1, entry.getKey()))
			{
				for(Entry<ItemStack, ItemStack> input : entry.getValue().entrySet())
				{
					if(comparationItem(slot_2, input.getKey())) return input.getValue();
				}
			}
			
			if(comparationItem(slot_2, entry.getKey()))
			{
				for(Entry<ItemStack, ItemStack> input : entry.getValue().entrySet())
				{
					if(comparationItem(slot_1, input.getKey())) return input.getValue();
				}
			}
		}
		
		return ItemStack.EMPTY;
	}

	private boolean comparationItem(ItemStack result, ItemStack input)
	{
		return input.getItem() == result.getItem() && (input.getMetadata() == 32767 || input.getMetadata() == result.getMetadata());
	}
	
	public float getFusionExperience(ItemStack stack)
	{
		for(Entry<ItemStack, Float> entry : this.experienceList.entrySet())
		{
			if(comparationItem(stack, entry.getKey())) return ((Float)entry.getValue()).floatValue();
		}
		
		return 0.0F;
	}

	public boolean isPatialIngredient(ItemStack item)
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : fusionRecipesList.columnMap().entrySet())
		{
			if(comparationItem(item, entry.getKey())) return true;
			
			for(ItemStack input : entry.getValue().keySet())
			{
				if(comparationItem(item, input)) return true;
			}
		}
		
		return false;
	}
}
