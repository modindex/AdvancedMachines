package jaminv.advancedmachines.objects.blocks;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class DataBlock extends Block implements IHasTileEntity, IHasModel {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public DataBlock() {
		super(Material.IRON);
		setUnlocalizedName(Reference.MODID + ".datablock");
		setRegistryName("datablock");
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));		
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return DataTileEntity.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new DataTileEntity();
	}
	
	private DataTileEntity getTileEntity(World world, BlockPos pos) {
		return (DataTileEntity)world.getTileEntity(pos);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (!worldIn.isRemote) {
			if (facing == state.getValue(FACING)) {
				int counter;
				if (hitY < 0.5f) {
					counter = getTileEntity(worldIn, pos).decrement();
				} else {
					counter = getTileEntity(worldIn, pos).increment();
				}
				
				TextComponentTranslation component = new TextComponentTranslation("message.advmach.counter", counter);
				component.getStyle().setColor(TextFormatting.GREEN);
				playerIn.sendStatusMessage(component, false);
			}
		}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront((meta & 3) + 2));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex()-2;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
