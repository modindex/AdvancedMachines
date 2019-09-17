package jaminv.advancedmachines.machine;

import jaminv.advancedmachines.AdvancedMachines;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.ModelBakeryProvider;
import jaminv.advancedmachines.lib.util.blocks.BlockProperties;
import jaminv.advancedmachines.lib.util.helper.BlockHelper;
import jaminv.advancedmachines.machine.expansion.MachineUpgrade;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.model.ModelBakeryMultiblockMachine;
import jaminv.advancedmachines.objects.blocks.BlockPropertiesMod;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMachine extends Block implements VariantExpansion.HasVariant, MachineUpgrade, ModelBakeryProvider {
	
	public static final PropertyBool BORDER_TOP = PropertyBool.create("border_top");
	public static final PropertyBool BORDER_BOTTOM = PropertyBool.create("border_bottom");
	public static final PropertyBool BORDER_NORTH = PropertyBool.create("border_north");
	public static final PropertyBool BORDER_SOUTH = PropertyBool.create("border_south");
	public static final PropertyBool BORDER_EAST = PropertyBool.create("border_east");
	public static final PropertyBool BORDER_WEST = PropertyBool.create("border_west");	
	
    protected VariantExpansion variant;
    
    protected static final BlockProperties props = BlockPropertiesMod.MACHINE;

	public BlockMachine(VariantExpansion variant) {
		super(props.getMaterial());
		props.apply(this);
		setSoundType(props.getSoundType());		
		this.variant = variant;
	}

	protected abstract int getGuiId();
	
	public VariantExpansion getVariant() { return variant; }
		
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileMachine) {
			if (((TileMachine)te).onBlockActivated(playerIn, hand)) {
				return true;
			}
		}
		
		return BlockHelper.openGui(AdvancedMachines.instance, worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		BlockHelper.setDirectional(worldIn, pos, placer, false);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileMachine) {
			((TileMachine)te).setVariant(variant);
		}
		
		scanMultiblock(worldIn, pos, false);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder
			.add(Properties.EXPANSION_VARIANT)
			.add(Properties.BORDER_TOP, Properties.BORDER_BOTTOM) 
			.add(Properties.BORDER_NORTH, Properties.BORDER_SOUTH)
			.add(Properties.BORDER_EAST, Properties.BORDER_WEST)
			.add(Properties.MACHINE_FACE, Properties.MACHINE_TYPE)
			.add(Properties.FACING, Properties.ACTIVE)
			.build();
	}	
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean active = false;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;
        MachineFace face = MachineFace.NONE;
        MachineType type = MachineType.NONE;

        if (tileentity instanceof TileMachine) {
        	TileMachine te = (TileMachine)tileentity;
        	facing = te.getFacing();
        	active = te.isProcessing();
        	borders = te.getBorders();
        	face = te.getMachineFace();
        	type = te.getMachineType();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.MACHINE_FACE, face).withProperty(Properties.MACHINE_TYPE, type)
        		.withProperty(Properties.EXPANSION_VARIANT, variant)
            	.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active)
            	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
            	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
            	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = BlockHelper.getTileEntity(world, pos);
		if (te instanceof TileMachine) {
			return ((TileMachine)te).isProcessing() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof TileMachine) {
			BlockHelper.dropInventoryItems(worldIn, pos, ((TileMachine)te).getInventory());
		}
		
		scanMultiblock(worldIn, pos, true);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }
	
	protected void scanMultiblock(World worldIn, BlockPos pos, boolean destroy) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileMachine)) { return; }
		
		((TileMachine)te).scanMultiblock(destroy ? pos : null);
	}
		
	public abstract MachineType getMachineType();
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.MULTIPLY;
	}

	@Override
	public int getUpgradeQty(World world, BlockPos pos) {
		return getVariant().getMultiplier();
	}

	@Override @SideOnly(Side.CLIENT) public ModelBakery getModelBakery() { return new ModelBakeryMultiblockMachine(variant); }

	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
}
