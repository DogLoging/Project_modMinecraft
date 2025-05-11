package com.forgecraft.modding.proxy;

import com.forgecraft.modding.proxy.render.ModRenderingBlocks;
import com.forgecraft.modding.proxy.render.ModRenderingItems;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		registerEvent(new ModRenderingBlocks());
		registerEvent(new ModRenderingItems());
		
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
