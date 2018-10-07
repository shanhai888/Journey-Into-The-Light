package net.slayer.api.block;

import java.util.Random;

import net.journey.JourneyTabs;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slayer.api.EnumMaterialTypes;

public class BlockModBush extends BlockMod implements IPlantable, IGrowable {

	private boolean isNether;
	private ItemStack berry;

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);

	public BlockModBush(String name, String finalName, ItemStack berry, boolean isNether) {
		super(EnumMaterialTypes.LEAVES, name, finalName, 1.0F);
		this.berry = berry;
		this.isNether = isNether;
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setLightOpacity(-100000);
		this.setCreativeTab(JourneyTabs.crops);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
		float f = 0.3F;
		if (access.getBlockState(pos).getValue(AGE) == 0) {
			return new AxisAlignedBB(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 1.0F, 0.5F + f);
		}

		if (access.getBlockState(pos).getValue(AGE) == 1) {
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if (access.getBlockState(pos).getValue(AGE) == 2) {
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		return null;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}
	
	@Override
	public boolean canPlaceBlockAt(World w, BlockPos pos) {
		Block block = w.getBlockState(pos.down()).getBlock();
		if (isNether) {
			return block == Blocks.NETHERRACK;
		}
		if (!isNether) {
			return block == Blocks.GRASS || block == Blocks.DIRT;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AGE).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}
	

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return null;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.getDefaultState();
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(state.getValue(AGE).intValue() + 1)), 2);
	}

	@Override
	public void updateTick(World w, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(w, pos, state, rand);
		if (w.rand.nextInt(5) == 0) {
			int age = state.getValue(AGE).intValue();
			if (age < 2) {
				w.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + 1)), 2);
			}	
		}
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		if (plantable instanceof BlockModBush && state.getPropertyKeys().contains(AGE)) {
			return (state.getValue(AGE) > 2);
		}

		return super.canSustainPlant(state, world, pos, direction, plantable);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		int age = state.getValue(AGE).intValue();

		if (age == 0) {
			return false;
		}
		if (age == 1) {
			return false;
		}
		if (age == 2) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
    
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (worldIn.isRemote) {
			return;
		}

		int age = worldIn.getBlockState(pos).getValue(AGE).intValue();

		if (age == 2) {
			worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(2)), 2);

			ItemStack itemDrop = new ItemStack(this.berry.getItem(), 1, this.berry.getItemDamage());
			EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY - 1.0D, playerIn.posZ,
					itemDrop);

			worldIn.spawnEntity(entityitem);

			if (!(playerIn instanceof FakePlayer)) {
				entityitem.onCollideWithPlayer(playerIn);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		int age = state.getValue(AGE).intValue();

		if (age == 2) {
			if (worldIn.isRemote) {
				return true;
			}

			worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(2)), 2);

			ItemStack itemDrop = new ItemStack(this.berry.getItem(), 1, this.berry.getItemDamage());
			EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY - 1.0D, playerIn.posZ,
					itemDrop);

			worldIn.spawnEntity(entityitem);

			if (!(playerIn instanceof FakePlayer)) {
				entityitem.onCollideWithPlayer(playerIn);
			}
			return true;
		}
		return false;
	}
}