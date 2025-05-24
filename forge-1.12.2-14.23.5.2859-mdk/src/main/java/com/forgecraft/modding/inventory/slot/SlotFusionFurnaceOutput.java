package com.forgecraft.modding.inventory.slot;

import com.forgecraft.modding.crafting.FusionFurnaceRecipes;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotFusionFurnaceOutput extends Slot
{
	private final EntityPlayer player;
	private int removeCount;
	
	public SlotFusionFurnaceOutput(EntityPlayer player, IInventory inventory, int index, int xPosition, int yPosition)
	{
		super(inventory, index, xPosition, yPosition);
		this.player = player;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public ItemStack decrStackSize(int amout)
	{
		if(this.getHasStack()) removeCount += Math.min(amout, getStack().getCount());
		return super.decrStackSize(amout);
	}
	
	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack)
	{
		this.onCrafting(stack);
		super.onTake(player, stack);
		return stack;
	}
	
	protected void onCreafting(ItemStack stack, int var)
	{
		removeCount += var;
		onCrafting(stack);
	}
	
	@Override
	protected void onCrafting(ItemStack stack)
	{
		if(!player.world.isRemote)
		{
			int expEarned = removeCount;
			float expFactor = FusionFurnaceRecipes.instance().getFusionExperience(stack);
			
			if(expFactor == 0.0F)
			{
				expEarned = 0;
			}
			else if(expFactor < 1.0F)
			{
				int possibleExpEarned = MathHelper.floor(expEarned * expFactor);	
                if (possibleExpEarned < MathHelper.ceil(expEarned * expFactor) && Math.random() < expEarned * expFactor - possibleExpEarned) ++possibleExpEarned;
                expEarned = possibleExpEarned;
			}
			
			int expInOrb;
			while(expEarned > 0)
			{
				expInOrb = EntityXPOrb.getXPSplit(expEarned);
				expEarned -= expInOrb;
				player.world.spawnEntity(new EntityXPOrb(player.world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, expInOrb));
			}
		}
		
		removeCount = 0;
	}
}
