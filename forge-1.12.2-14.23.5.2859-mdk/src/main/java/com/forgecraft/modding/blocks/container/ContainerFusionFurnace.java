package com.forgecraft.modding.blocks.container;

import com.forgecraft.modding.blocks.tileentity.TileEntityFusionFurnace;
import com.forgecraft.modding.crafting.FusionFurnaceRecipes;
import com.forgecraft.modding.inventory.slot.SlotFusionFurnaceFuel;
import com.forgecraft.modding.inventory.slot.SlotFusionFurnaceOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFusionFurnace extends Container
{
	private final IInventory tileFusionFurnace;
	private int fuel, burnProcess, timeProcess, maxTime, timeFusion, maxFusion;
	
	public ContainerFusionFurnace(InventoryPlayer playerInventory, IInventory inventory)
	{
		tileFusionFurnace = inventory;
		
		this.addSlotToContainer(new Slot(tileFusionFurnace, 0, 26, 22));
		this.addSlotToContainer(new Slot(tileFusionFurnace, 1, 67, 22));
		this.addSlotToContainer(new SlotFusionFurnaceFuel(tileFusionFurnace, 2, 26, 53));
		this.addSlotToContainer(new SlotFusionFurnaceOutput(playerInventory.player, tileFusionFurnace, 3, 127, 22));
		
		int i;
		for(i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public void addListener(IContainerListener listaner)
	{
		super.addListener(listaner);
		listaner.sendAllWindowProperties(this, tileFusionFurnace);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.listeners.size(); i++)
		{
			IContainerListener containerListener = this.listeners.get(i);
			
			if(timeFusion != tileFusionFurnace.getField(0))
			{
				containerListener.sendWindowProperty(this, 0, tileFusionFurnace.getField(0));
			}
			
			if(timeProcess != tileFusionFurnace.getField(1))
			{
				containerListener.sendWindowProperty(this, 1, tileFusionFurnace.getField(1));
			}
			
			if(maxFusion != tileFusionFurnace.getField(2))
			{
				containerListener.sendWindowProperty(this, 2, tileFusionFurnace.getField(2));
			}
			
			if(maxTime != tileFusionFurnace.getField(3))
			{
				containerListener.sendWindowProperty(this, 3, tileFusionFurnace.getField(3));
			}
			
			if(burnProcess != tileFusionFurnace.getField(4))
			{
				containerListener.sendWindowProperty(this, 4, tileFusionFurnace.getField(4));
			}
			
			if(fuel != tileFusionFurnace.getField(5))
			{
				containerListener.sendWindowProperty(this, 5, tileFusionFurnace.getField(5));
			}
			
			timeFusion = tileFusionFurnace.getField(0);
			timeProcess = tileFusionFurnace.getField(1);
			maxFusion = tileFusionFurnace.getField(2);
			maxTime = tileFusionFurnace.getField(3);
			burnProcess = tileFusionFurnace.getField(4);
			fuel = tileFusionFurnace.getField(5);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		tileFusionFurnace.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tileFusionFurnace.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack_1 = slot.getStack();
			stack = stack_1.copy();
			
			if(index == 3)
			{
				if(!this.mergeItemStack(stack_1, 4, 40, true)) return ItemStack.EMPTY;
				slot.onSlotChange(stack_1, stack);
			}
			else if(index != 2 && index != 1 && index != 0)
			{
				Slot slot_0 = inventorySlots.get(0);
				boolean handled = false;
				
				if(FusionFurnaceRecipes.instance().isPatialIngredient(stack_1))
				{
					if(this.mergeItemStack(stack_1, 0, 2, false)) handled = true;
				}
				
				if(!handled && !FusionFurnaceRecipes.instance().getFusionResult(slot_0.getStack(), stack_1).isEmpty())
				{
					if(this.mergeItemStack(stack_1, 1, 2, false)) handled = true;
				}
				
				if(!handled && TileEntityFusionFurnace.isItemFuel(stack_1))
				{
					if(this.mergeItemStack(stack_1, 2, 3, false)) handled = true;	
				}
				
				if(!handled)
				{
					if(index >= 4 && index < 31)
					{
						if(!this.mergeItemStack(stack_1, 31, 40, false)) return ItemStack.EMPTY;
					}
					else if(index >= 31 && index < 40)
					{
						if(!this.mergeItemStack(stack_1, 4, 31, false)) return ItemStack.EMPTY;
					}
				}
			}
			else if(!this.mergeItemStack(stack_1, 4, 40, false))
			{
				return ItemStack.EMPTY;
			}
			
			if(stack_1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if(stack_1.getCount() == stack.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, stack_1);
		}
		
		return stack;
	}
}
