package com.forgecraft.modding.proxy.render;

import com.forgecraft.modding.Main;
import com.forgecraft.modding.init.ModItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModRenderingItems
{
	@SubscribeEvent
	public void onModelRegisterEvent(ModelRegistryEvent event)
	{
		register(ModItems.copper_ingot, "copper_ingot");
		register(ModItems.steel_ingot, "steel_ingot");
		register(ModItems.titanium_ingot, "titanium_ingot");
		register(ModItems.adamantium_ingot, "adamantium_ingot");
	}
	
	private static void register(Item item, String name)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Main.MODID + ":" + name, "inventory"));
	}
}
