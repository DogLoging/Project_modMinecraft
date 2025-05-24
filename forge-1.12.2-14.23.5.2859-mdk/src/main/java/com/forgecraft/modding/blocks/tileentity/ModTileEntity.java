package com.forgecraft.modding.blocks.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntity
{
	@SuppressWarnings("deprecation")
	public static void initialization()
	{
		GameRegistry.registerTileEntity(TileEntityFusionFurnace.class, "tileFusionFurnace");
	}
}
