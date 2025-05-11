package com.forgecraft.modding.proxy.render;

import com.forgecraft.modding.Main;
import com.forgecraft.modding.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModRenderingBlocks
{
	@SubscribeEvent
	public void onModelRegisterEvent(ModelRegistryEvent event)
	{
		register(ModBlocks.copper_block, "copper_block");
		register(ModBlocks.steel_block, "steel_block");
		register(ModBlocks.titanium_block, "titanium_block");
		register(ModBlocks.adamantium_block, "adamantium_block");
	}
	
	private static void register(Block block, String name)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Main.MODID + ":" + name, "inventory"));
	}
}
