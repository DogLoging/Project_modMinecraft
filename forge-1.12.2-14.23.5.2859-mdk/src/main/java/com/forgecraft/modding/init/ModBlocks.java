package com.forgecraft.modding.init;

import com.forgecraft.modding.Main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{
	public static Block copper_block, steel_block, titanium_block, adamantium_block;
	
	public static Block[] listBlock = new Block[4];
	public static Item[] listItem = new Item[4];
	private static int id;
	
	public static void registerBlock(IForgeRegistry<Block> event)
	{
		event.registerAll(listBlock);
	}
	
	public static void registerItem(IForgeRegistry<Item> event)
	{
		event.registerAll(listItem);
	}
	
	public static void initialization()
	{
		copper_block = register("copper_block", new Block(Material.IRON)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		steel_block = register("steel_block", new Block(Material.IRON)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		titanium_block = register("titanium_block", new Block(Material.IRON)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		adamantium_block = register("adamantium_block", new Block(Material.IRON)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	private static Block register(String name, Block block)
	{
		block.setUnlocalizedName(name);
		listBlock[id] = block.setRegistryName(Main.MODID + ":" + name);
		listItem[id] = new ItemBlock(block).setRegistryName(Main.MODID + ":" + name);
		id++;
		
		return block;
	}
}
