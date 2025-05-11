package com.forgecraft.modding.proxy.register;

import com.forgecraft.modding.init.ModBlocks;
import com.forgecraft.modding.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModRegistryEvent
{
	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event)
	{
		ModBlocks.registerBlock(event.getRegistry());
	}
	
	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		ModBlocks.registerItem(event.getRegistry());
		ModItems.register = event.getRegistry();
		ModItems.initialization();
	}
}
