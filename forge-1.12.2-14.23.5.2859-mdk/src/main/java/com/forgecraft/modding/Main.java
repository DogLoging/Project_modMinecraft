package com.forgecraft.modding;

import com.forgecraft.modding.blocks.tileentity.ModTileEntity;
import com.forgecraft.modding.init.ModBlocks;
import com.forgecraft.modding.inventory.GUIHandler;
import com.forgecraft.modding.proxy.CommonProxy;
import com.forgecraft.modding.proxy.register.ModRegistryEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME)
public class Main
{
	public static final String MODID = "forgecraft";
	public static final String VERSION = "1.0.0";
	public static final String NAME = "ForgeCraft";
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = "com.forgecraft.modding.proxy.ClientProxy", serverSide = "com.forgecraft.modding.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ModBlocks.initialization();
		ModTileEntity.initialization();
		CommonProxy.registerEvent(new ModRegistryEvent());
		proxy.preInit(event);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GUIHandler());
		proxy.init(event);
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
	
	public enum ENUM_GUI
	{
		FUSION_FURNACE
	}
}
