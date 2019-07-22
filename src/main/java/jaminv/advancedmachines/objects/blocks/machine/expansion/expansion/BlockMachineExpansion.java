package jaminv.advancedmachines.objects.blocks.machine.expansion.expansion;

import jaminv.advancedmachines.client.BakedModelMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.TileEntityMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.PropertyMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.PropertyMachineParent;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMachineExpansion extends BlockMachineExpansionBase {
	
    public static final PropertyMachineParent MACHINE_PARENT = PropertyMachineParent.create("parent");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMachineExpansion(String name) {
		super(name);
	}
	
/*	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(worldIn, pos, playerIn, getGuiId());
	}*/
	
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
		return new BlockStateContainer(this, new IProperty[] { VARIANT, ICanHaveMachineFace.MACHINE_FACE, MACHINE_PARENT, FACING, BORDER_TOP, BORDER_BOTTOM, BORDER_NORTH, BORDER_SOUTH, BORDER_EAST, BORDER_WEST });
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        MachineFace face = MachineFace.NONE;
        MachineParent parent = MachineParent.NONE;
        EnumFacing facing = EnumFacing.NORTH;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineExpansion) {
        	TileEntityMachineExpansion te = (TileEntityMachineExpansion)tileentity;
        	face = te.getMachineFace();
        	parent = te.getMachineParent();
        	facing = te.getFacing();
        	borders = te.getBorders();
        }
        
        return state.withProperty(ICanHaveMachineFace.MACHINE_FACE, face).withProperty(MACHINE_PARENT, parent).withProperty(FACING, facing)
        	.withProperty(BORDER_TOP, borders.getTop()).withProperty(BORDER_BOTTOM, borders.getBottom())
        	.withProperty(BORDER_NORTH, borders.getNorth()).withProperty(BORDER_SOUTH, borders.getSouth())
        	.withProperty(BORDER_EAST, borders.getEast()).withProperty(BORDER_WEST, borders.getWest());
	}

	@Override
	public void registerModels() {
		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return BakedModelMultiblock.BAKED_MODEL_MULTIBLOCK;
			}
		};
		ModelLoader.setCustomStateMapper(this, ignoreState);
	}
}
