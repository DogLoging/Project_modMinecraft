package com.forgecraft.modding.inventory.slot;

import com.forgecraft.modding.blocks.tileentity.TileEntityFusionFurnace;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFusionFurnaceFuel extends Slot
{
	public SlotFusionFurnaceFuel(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityFusionFurnace.isItemFuel(stack) || isBucket(stack);
	}

	public static boolean isBucket(ItemStack stack)
	{
		return stack.getItem() == Items.BUCKET;
	}
	
	public int getItemStackLimit(ItemStack stack)
	{
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}
}
