package com.forgecraft.modding.init;

import com.forgecraft.modding.Main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{
	public static Item copper_ingot, steel_ingot, titanium_ingot, adamantium_ingot;
	public static IForgeRegistry<Item> register;
	
	public static void initialization()
	{
		copper_ingot = register("copper_ingot", new Item()).setCreativeTab(CreativeTabs.MATERIALS);
		steel_ingot = register("steel_ingot", new Item()).setCreativeTab(CreativeTabs.MATERIALS);
		titanium_ingot = register("titanium_ingot", new Item()).setCreativeTab(CreativeTabs.MATERIALS);
		adamantium_ingot = register("adamantium_ingot", new Item()).setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	private static Item register(String name, Item item)
	{
		item.setUnlocalizedName(name);
		register.register(item.setRegistryName(Main.MODID + ":" + name));
		
		return item;
	}
}
