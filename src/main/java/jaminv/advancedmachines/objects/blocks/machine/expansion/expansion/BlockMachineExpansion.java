package jaminv.advancedmachines.objects.blocks.machine.expansion.expansion;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockMachineExpansion extends BlockMachineExpansionBase {
	
	public BlockMachineExpansion(String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineExpansion();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineExpansion.class;
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

        MachineFace face = MachineFace.NONE;
        MachineType parent = MachineType.NONE;
        EnumFacing facing = EnumFacing.UP;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;
        boolean active = false;

        if (tileentity instanceof TileEntityMachineExpansion) {
        	TileEntityMachineExpansion te = (TileEntityMachineExpansion)tileentity;
        	face = te.getMachineFace();
        	parent = te.getMachineParent();
        	facing = te.getFacing();
        	active = te.isActive();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.MACHINE_FACE, face).withProperty(Properties.MACHINE_TYPE, parent)
        	.withProperty(Properties.FACING, facing).withProperty(Properties.ACTIVE, active)
        	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
        	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
        	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}
	
	public static final String MODEL_EXPANSION = "bakedmodel_expansion";
	@Override
	public void registerModels() {
		registerCustomModel(MODEL_EXPANSION, BakedModelExpansion.class);
		registerVariantModels();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityMachineExpansion) {
			TileEntityMachineExpansion exp = (TileEntityMachineExpansion)te;
			if (exp.getMachineFace() == MachineFace.NONE) { return false; }

			BlockPos machine = exp.getParentPos();
			if (worldIn.isRemote) { return true; }
			playerIn.openGui(Main.instance, exp.getMachineParent().getGuiId(), worldIn, machine.getX(), machine.getY(), machine.getZ());
			return true;
		}
		
		return false;
	}	
}
