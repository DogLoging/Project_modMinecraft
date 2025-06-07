package com.forgecraft.modding.blocks;

import java.util.Random;

import com.forgecraft.modding.Main;
import com.forgecraft.modding.blocks.tileentity.TileEntityFusionFurnace;
import com.forgecraft.modding.init.ModBlocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFusionFurnace extends BlockContainer implements ITileEntityProvider
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool PROCESS = PropertyBool.create("process");
	
	public BlockFusionFurnace()
	{
		super(Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PROCESS, false));
	}
	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setDefaultFacing(worldIn, pos, state);
    }
	
	private void setDefaultFacing(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
		{
			IBlockState north = world.getBlockState(pos.north());
			IBlockState south = world.getBlockState(pos.south());
			IBlockState west = world.getBlockState(pos.west());
			IBlockState east = world.getBlockState(pos.east());
			EnumFacing facing = (EnumFacing)state.getValue(FACING);
			
			if(facing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock())
			{
				facing = EnumFacing.SOUTH;
			}
			else if(facing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock())
			{
				facing = EnumFacing.NORTH;
			}
			else if(facing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock())
			{
				facing = EnumFacing.EAST;
			}
			else if(facing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock())
			{
				facing = EnumFacing.WEST;
			}
			
			world.setBlockState(pos, state.withProperty(FACING, facing), 2);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if(state.getValue(PROCESS))
		{
			EnumFacing facing = (EnumFacing)state.getValue(FACING);
			double x = (double)pos.getX() + 0.5D;
			double y = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double z = (double)pos.getZ() + 0.5D;
			double d = rand.nextDouble() * 0.6D - 0.3D;
			
			if(rand.nextDouble() < 0.5D)
			{
				world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			
			switch(facing)
			{
			case NORTH:
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y + 1.0D, z, 0.0D, 0.10D, 0.0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, x + d, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
				break;
			case SOUTH:
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y + 1.0D, z, 0.0D, 0.10D, 0.0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, x + d, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
				break;
			case WEST:
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y + 1.0D, z, 0.0D, 0.10D, 0.0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, x - 0.52D, y, z + d, 0.0D, 0.0D, 0.0D);
				break;
			case EAST:
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y + 1.0D, z, 0.0D, 0.10D, 0.0D);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, x + 0.52D, y, z + d, 0.0D, 0.0D, 0.0D);
				break;
				default:
					break;
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
	{
		if(!world.isRemote)
		{
			player.openGui(Main.instance, Main.ENUM_GUI.FUSION_FURNACE.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		}
		
		return true;
	}
	
	public static void setState(boolean bool, World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);
		
		world.setBlockState(pos, ModBlocks.fusion_furnace.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(PROCESS, bool), 3);
		
		if(tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
		
		world.checkLight(pos);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(state.getValue(PROCESS))
		{
			return 15;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFusionFurnace();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if(tileEntity instanceof TileEntityFusionFurnace)
		{
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityFusionFurnace)tileEntity);
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation)
	{
		return state.withProperty(FACING, rotation.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {PROCESS, FACING});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing facing = EnumFacing.getFront(meta);
		
		if(facing.getAxis() == EnumFacing.Axis.Y)
		{
			facing = EnumFacing.NORTH;
		}
		
		return this.getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.fusion_furnace);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModBlocks.fusion_furnace);
	}
}
