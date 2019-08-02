package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BakedModelExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.TileEntityMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.model.BakedModelMultiblockMachine;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.material.MaterialExpansion;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMachineMultiblock extends BlockMachineBase {
	
	public static final PropertyBool BORDER_TOP = PropertyBool.create("border_top");
	public static final PropertyBool BORDER_BOTTOM = PropertyBool.create("border_bottom");
	public static final PropertyBool BORDER_NORTH = PropertyBool.create("border_north");
	public static final PropertyBool BORDER_SOUTH = PropertyBool.create("border_south");
	public static final PropertyBool BORDER_EAST = PropertyBool.create("border_east");
	public static final PropertyBool BORDER_WEST = PropertyBool.create("border_west");	

	public BlockMachineMultiblock(String name) {
		super(name);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		scanMultiblock(worldIn, pos, false);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		scanMultiblock(worldIn, pos, true);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }
	
	protected void scanMultiblock(World worldIn, BlockPos pos, boolean destroy) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileEntityMachineMultiblock)) { return; }
		
		((TileEntityMachineMultiblock)te).scanMultiblock(destroy);		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder.add(VARIANT)
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

        if (tileentity instanceof TileEntityMachineMultiblock) {
        	TileEntityMachineMultiblock te = (TileEntityMachineMultiblock)tileentity;
        	facing = te.getFacing();
        	active = te.isProcessing();
        	borders = te.getBorders();
        	face = te.getMachineFace();
        	type = te.getMachineType();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.MACHINE_FACE, face).withProperty(Properties.MACHINE_TYPE, type)
            	.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active)
            	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
            	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
            	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}
	
	
	
	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.MULTIPLY;
	}

	@Override
	public int getUpgradeQty(World world, BlockPos pos) {
		return MaterialExpansion.byMetadata(this.getMetaFromState(world.getBlockState(pos))).getMultiplier();
	}
	
	@Override
	public void setMultiblock(World world, BlockPos pos, BlockPos parent, MultiblockBorders borders) {
		BlockHelper.setBorders(world, pos, borders);
	}

	public static final String BAKEDMODEL_MACHINE = "bakedmodel_machine";
	
	@Override
	public void registerModels() {
		registerCustomModel(BAKEDMODEL_MACHINE, BakedModelMultiblockMachine.class);
		registerVariantModels();
	}	
}
