package com.forgecraft.modding.blocks.tileentity;

import com.forgecraft.modding.blocks.BlockFusionFurnace;
import com.forgecraft.modding.blocks.container.ContainerFusionFurnace;
import com.forgecraft.modding.crafting.FusionFurnaceRecipes;
import com.forgecraft.modding.inventory.slot.SlotFusionFurnaceFuel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFusionFurnace extends TileEntityLockable implements IInventory, ITickable
{
	private NonNullList<ItemStack> fusionItemStack = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	private int fuel, burnProcess, timeProcess, maxTime, timeFusion, maxFusion;
	private String tileEntityName;
	
	@Override
	public int getSizeInventory()
	{
		return fusionItemStack.size();
	}

	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : fusionItemStack)
		{
			if(!stack.isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return fusionItemStack.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(fusionItemStack, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(fusionItemStack, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack item = this.fusionItemStack.get(index);
		boolean isAlreadyInSlot = stack != null && stack.isItemEqual(item) && ItemStack.areItemStackTagsEqual(stack, item);
		fusionItemStack.set(index, stack);
		
		if(stack != null && stack.getCount() > this.getInventoryStackLimit()) stack.setCount(this.getInventoryStackLimit());
		
		if(index == 0 && index + 1 == 1 && !isAlreadyInSlot)
		{
			maxFusion = getTimeFusion(this.fuel);
			timeFusion = 0;
			maxTime = getTimeProcess(this.fusionItemStack.get(0), this.fusionItemStack.get(1));
			timeProcess = 0;
			burnProcess = 0;
			markDirty();
		}
	}

	private int getTimeProcess(ItemStack slot1, ItemStack slot2)
	{
		return 200;
	}

	private int getTimeFusion(int level)
	{
		int speed;
		if(level > getFuelPercentage(90))
		{
			speed = 35;
		}
		else if(level > getFuelPercentage(60))
		{
			speed = 85;
		}
		else if(level > getFuelPercentage(30))
		{
			speed = 150;
		}
		else
		{
			speed = 200;
		}
		
		return speed;
	}
	
	private int getFuelPercentage(int var)
	{
		return var * (getMaxFuel() / 100);
	}

	public int getMaxFuel()
	{
		return 5000;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.fusionItemStack = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, fusionItemStack);
		
		this.burnProcess = compound.getInteger("BurnTProcess");
		this.timeFusion = compound.getInteger("FusionTime");
		this.maxFusion = compound.getInteger("MaxFusionTime");
		this.timeProcess = compound.getInteger("ProcessTime");
		this.maxTime = compound.getInteger("MaxProcessTime");
		this.fuel = getItemFuel(this.fusionItemStack.get(2));
		this.fuel = compound.getInteger("Fuel");
		
		if(compound.hasKey("CustomName", 8)) this.tileEntityName = compound.getString("CustomName");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnProcess", (short)burnProcess);
		compound.setInteger("FusionTime", (short)timeFusion);
		compound.setInteger("MaxFusionTime", (short)maxFusion);
		compound.setInteger("ProcessTime", (short)timeProcess);
		compound.setInteger("MaxProcessTime", (short)maxTime);
		compound.setInteger("Fuel", (short)fuel);
		ItemStackHelper.saveAllItems(compound, this.fusionItemStack);
		
		if(this.hasCustomName()) compound.setString("CustomName", tileEntityName);
		
		return compound;
	}

	private static int getItemFuel(ItemStack stack)
	{
		if(stack.isEmpty())
		{
			return 0;
		}
		else
		{
			Item fuel = stack.getItem(); 
			
			if(fuel == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
			{
				return 1;
			}
			else if(fuel == Item.getItemFromBlock(Blocks.WOOL))
			{
				return 4;
			}
			else if(fuel == Item.getItemFromBlock(Blocks.CARPET))
			{
				return 2;
			}
			else if(fuel == Item.getItemFromBlock(Blocks.LADDER))
			{
				return 3;
			}
			else if(fuel == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
			{
				return 1;
			}
			else if(Block.getBlockFromItem(fuel).getDefaultState().getMaterial() == Material.WOOD)
			{
				return 10;
			}
			else if(fuel == Item.getItemFromBlock(Blocks.COAL_BLOCK))
			{
				return 25;
			}
			else if(fuel instanceof ItemTool && "WOOD".equals(((ItemTool)fuel).getToolMaterialName()))
			{
				return 1;
			}
			else if(fuel instanceof ItemSword && "WOOD".equals(((ItemSword)fuel).getToolMaterialName()))
			{
				return 1;
			}
			else if(fuel instanceof ItemHoe && "WOOD".equals(((ItemHoe)fuel).getMaterialName()))
			{
				return 1;
			}
			else if(fuel == Items.STICK)
			{
				return 1;
			}
			else if(fuel != Items.BOW && fuel != Items.FISHING_ROD)
			{
				if(fuel == Items.SIGN)
				{
					return 10;
				}
				else if(fuel == Items.COAL)
				{
					return 15;
				}
				else if(fuel == Items.LAVA_BUCKET)
				{
					return 35;
				}
				else if(fuel != Item.getItemFromBlock(Blocks.SAPLING) && fuel != Items.BOW)
				{
					if(fuel == Items.BLAZE_ROD)
					{
						return 50;
					}
					else if(fuel instanceof ItemDoor && fuel != Items.IRON_DOOR)
					{
						return 15;
					}
					else
					{
						return fuel instanceof ItemBoat ? 15 : 0;
					}
				}
				else
				{
					return 15;
				}
			}
			else
			{
				return 1;
			}
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(index == 3)
		{
			return false;
		}
		else if(index != 2)
		{
			return true;
		}
		else
		{
			ItemStack item = this.fusionItemStack.get(2);
			return isItemFuel(stack) || SlotFusionFurnaceFuel.isBucket(stack) && item.getItem() != Items.BUCKET;
		}
	}

	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemFuel(stack) > 0;
	}

	@Override
	public int getField(int id)
	{
		switch(id)
		{
		case 0:
			return timeFusion;
		case 1:
			return timeProcess;
		case 2:
			return maxFusion;
		case 3:
			return maxTime;
		case 4:
			return burnProcess;
		case 5:
			return fuel;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch(id)
		{
		case 0:
			timeFusion = value;
			break;
		case 1:
			timeProcess = value;
			break;
		case 2:
			maxFusion = value;
			break;
		case 3:
			maxTime = value;
			break;
		case 4:
			burnProcess = value;
			break;
		case 5:
			fuel = value;
			break;
			default:
				break;
		}
	}

	@Override
	public int getFieldCount()
	{
		return 6;
	}

	@Override
	public void clear()
	{
		this.fusionItemStack.clear();
	}

	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.tileEntityName : "container.name";
	}
	
	public void setName(String name)
	{
		this.tileEntityName = name;
	}

	@Override
	public boolean hasCustomName()
	{
		return tileEntityName != null && !tileEntityName.isEmpty();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFusionFurnace(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return "forgecraft:fusion_furnace";
	}

	@Override
	public void update()
	{
		boolean flag = isFuel();
		boolean flag_1 = false;
		
		if(!world.isRemote)
		{
			if(this.fuel < getMaxFuel())
			{
				ItemStack stack = this.fusionItemStack.get(2);
				
				if(this.fuel < 0)
				{
					this.fuel = 0;
					BlockFusionFurnace.setState(false, world, pos);
				}
				
					fuel += getItemFuel(stack);
					
				if(this.isFuel())
				{
					flag_1 = true;
					
					if(!stack.isEmpty() && stack.getItem() != Items.BUCKET)
					{
						Item item = stack.getItem();
						stack.shrink(1);
						
						if(stack.isEmpty())
						{
							ItemStack item_1 = item.getContainerItem(stack);
							this.fusionItemStack.set(2, item_1);
						}
					}
				}
			}
				
			if(this.isFuel())
			{
				if(this.fuel > getMaxFuel())
				{
					this.fuel = getMaxFuel();
				}
				
				BlockFusionFurnace.setState(true, world, pos);
				
				if(canFusion())
				{
					timeFusion += 1;
					
					if(timeFusion >= maxFusion)
					{
						burnProcess += 1;
						timeFusion = 0;
						maxFusion = getTimeFusion(fuel);
						
						if(burnProcess >= 8)
						{
							timeProcess += 10;
							fuel -= 50;
							burnProcess = 0;
							
							if(timeProcess >= maxTime)
							{
								fusionItem();
								maxTime = getProcessTime(this.fusionItemStack.get(0), this.fusionItemStack.get(1));
								timeProcess = 0;
								flag_1 = true;
							}
						}
					}
				}
				else
				{
					timeProcess = 0;
					timeFusion = 0;
					burnProcess = 0;
				}
			}
			if(flag != isFuel()) flag_1 = true;
		}
		if(flag_1) markDirty();
	}

	private void fusionItem()
	{
		if(canFusion())
		{
			ItemStack input_1 = this.fusionItemStack.get(0);
			ItemStack input_2 = this.fusionItemStack.get(1);
			ItemStack result = FusionFurnaceRecipes.instance().getFusionResult(input_1, input_2);
			ItemStack output = this.fusionItemStack.get(3);
			
			if(output.isEmpty())
			{
				this.fusionItemStack.set(3, result.copy());
			}
			else if(output.isItemEqual(result))
			{
				output.grow(result.getCount());
			}
			
			input_1.shrink(1);
			input_2.shrink(1);
		}
	}

	private int getProcessTime(ItemStack slot_1, ItemStack slot_2)
	{
		return 200;
	}

	private boolean canFusion()
	{
		if(((ItemStack)this.fusionItemStack.get(0)).isEmpty() && ((ItemStack)this.fusionItemStack.get(1)).isEmpty())
		{
			return false;
		}
		else
		{
			ItemStack stack = FusionFurnaceRecipes.instance().getFusionResult(this.fusionItemStack.get(0), this.fusionItemStack.get(1));
			
			if(stack.isEmpty())
			{
				return false;
			}
			else
			{
				ItemStack result = this.fusionItemStack.get(3);
				
				if(result.isEmpty())
				{
					return true;
				}
				else if(!result.isItemEqual(stack))
				{
					return false;
				}
				
				int res = result.getCount() + result.getCount();
				return res <= this.getInventoryStackLimit() && res <= result.getMaxStackSize();
			}
		}
	}

	private boolean isFuel()
	{
		return fuel > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isFuel(IInventory inventory)
	{
		return inventory.getField(5) > 0;
	}
}
