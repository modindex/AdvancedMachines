package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BakedModelExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.TileEntityMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.model.LayeredTextureMultiblockBase;
import jaminv.advancedmachines.util.enums.EnumGui;
import jaminv.advancedmachines.util.helper.BlockHelper;
import jaminv.advancedmachines.util.interfaces.IHasTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockMachineInventory extends BlockMachineExpansionBase implements ITileEntityProvider, IHasTileEntity {
	
    public static final PropertyBool INPUT = PropertyBool.create("input");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMachineInventory(String name) {
		super(name);
	}
	
	protected int getGuiId() { return EnumGui.MACHINE_INVENTORY.getId(); }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		BlockHelper.setDirectional(worldIn, pos, placer);
	}		 
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return BlockHelper.openGui(worldIn, pos, playerIn, getGuiId());
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMachineInventory();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMachineInventory.class;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		return builder.add(VARIANT)
			.add(Properties.INPUT, Properties.FACING)
			.add(Properties.BORDER_TOP, Properties.BORDER_BOTTOM) 
			.add(Properties.BORDER_NORTH, Properties.BORDER_SOUTH)
			.add(Properties.BORDER_EAST, Properties.BORDER_WEST)
			.add(Properties.MACHINE_FACE, Properties.MACHINE_TYPE)
			.build();
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		TileEntity tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);

        EnumFacing facing = EnumFacing.NORTH;
        boolean input = true;
        MultiblockBorders borders = MultiblockBorders.DEFAULT;

        if (tileentity instanceof TileEntityMachineInventory) {
        	TileEntityMachineInventory te = (TileEntityMachineInventory)tileentity;
        	facing = te.getFacing();
        	input = te.getInputState();
        	borders = te.getBorders();
        }
        
        return (IExtendedBlockState) ext.withProperty(Properties.FACING, facing).withProperty(Properties.INPUT, input)
            	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
            	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
            	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}
	
	@Override
	public void registerModels() {
		registerCustomModel("bakedmodel_inventory", BakedModelInventory.class);
		registerVariantModels();
	}
}
