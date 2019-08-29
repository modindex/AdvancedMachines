package jaminv.advancedmachines.proxy;

import jaminv.advancedmachines.machine.expansion.energy.DialogMachineEnergy;
import jaminv.advancedmachines.machine.expansion.energy.TileMachineEnergy;
import jaminv.advancedmachines.machine.expansion.inventory.DialogMachineInventory;
import jaminv.advancedmachines.machine.expansion.inventory.TileMachineInventory;
import jaminv.advancedmachines.machine.expansion.tank.DialogMachineTank;
import jaminv.advancedmachines.machine.expansion.tank.TileMachineTank;
import jaminv.advancedmachines.machine.instance.alloy.DialogMachineAlloy;
import jaminv.advancedmachines.machine.instance.alloy.TileMachineAlloy;
import jaminv.advancedmachines.machine.instance.furnace.DialogMachineFurnace;
import jaminv.advancedmachines.machine.instance.furnace.TileMachineFurnace;
import jaminv.advancedmachines.machine.instance.grinder.DialogMachineGrinder;
import jaminv.advancedmachines.machine.instance.grinder.TileMachineGrinder;
import jaminv.advancedmachines.machine.instance.injector.DialogMachineInjector;
import jaminv.advancedmachines.machine.instance.injector.TileMachineInjector;
import jaminv.advancedmachines.machine.instance.melter.DialogMachineMelter;
import jaminv.advancedmachines.machine.instance.melter.TileMachineMelter;
import jaminv.advancedmachines.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.machine.instance.purifier.TileMachinePurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.DialogMachineStabilizer;
import jaminv.advancedmachines.machine.instance.stabilizer.TileMachineStabilizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	public static final int FURNACE = 1;
	public static final int GRINDER = 2;
	public static final int ALLOY = 3;
	public static final int PURIFIER = 4;
	public static final int PRESS = 5;
	public static final int MELTER = 11;
	public static final int STABILIZER = 12;
	public static final int INJECTOR = 13;
	public static final int MACHINE_INVENTORY = 31;
	public static final int MACHINE_ENERGY = 32;
	public static final int MACHINE_TANK = 33;


    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof HasGui) {
        	return ((HasGui)te).createContainer(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        
        switch(ID) {
        case FURNACE:
        	if (!(te instanceof TileMachineFurnace)) { return null; }
        	return new DialogMachineFurnace(((TileMachineFurnace)te).createContainer(player.inventory), (TileMachineFurnace)te);
        case GRINDER:
        	if (!(te instanceof TileMachineGrinder)) { return null; }
        	return new DialogMachineGrinder(((TileMachineGrinder)te).createContainer(player.inventory), (TileMachineGrinder)te);
        case ALLOY:
        	if (!(te instanceof TileMachineAlloy)) { return null; }
        	return new DialogMachineAlloy(((TileMachineAlloy)te).createContainer(player.inventory), (TileMachineAlloy)te);
        case PURIFIER:
        	if (!(te instanceof TileMachinePurifier)) { return null; }
        	return new DialogMachinePurifier(((TileMachinePurifier)te).createContainer(player.inventory), (TileMachinePurifier)te);
        case MELTER:
        	if (!(te instanceof TileMachineMelter)) { return null; }
        	return new DialogMachineMelter(((TileMachineMelter)te).createContainer(player.inventory), (TileMachineMelter)te);
        case STABILIZER:
        	if (!(te instanceof TileMachineStabilizer)) { return null; }
        	return new DialogMachineStabilizer(((TileMachineStabilizer)te).createContainer(player.inventory), (TileMachineStabilizer)te);
        case INJECTOR:
        	if (!(te instanceof TileMachineInjector)) { return null; }
        	return new DialogMachineInjector(((TileMachineInjector)te).createContainer(player.inventory), (TileMachineInjector)te);
        case MACHINE_INVENTORY:
        	if (!(te instanceof TileMachineInventory)) { return null; }
        	return new DialogMachineInventory(((TileMachineInventory)te).createContainer(player.inventory), (TileMachineInventory)te);
        case MACHINE_ENERGY:
        	if (!(te instanceof TileMachineEnergy)) { return null; }
    		return new DialogMachineEnergy(((TileMachineEnergy)te).createContainer(player.inventory), (TileMachineEnergy)te);        	
        case MACHINE_TANK:
        	if (!(te instanceof TileMachineTank)) { return null; }
        	return new DialogMachineTank(((TileMachineTank)te).createContainer(player.inventory), (TileMachineTank)te);
        }
        return null;
    }

}