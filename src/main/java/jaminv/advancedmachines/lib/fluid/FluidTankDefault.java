package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidTankDefault implements FluidTank, IFluidHandler {
	
    @Nullable
    protected FluidStack fluid;
    protected int capacity;
    protected boolean canFill = true, canDrain = true;
    protected int maxFill, maxDrain;
    protected IFluidTankProperties[] tankProperties;

    public FluidTankDefault(@Nullable FluidStack fluidStack, int capacity, int maxFill, int maxDrain) {
        this.fluid = fluidStack;
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
    }

    public FluidTankDefault(FluidStack fluidStack, int capacity, int maxTransfer) { this(null, capacity, maxTransfer, maxTransfer); }
    public FluidTankDefault(int capacity, int maxTransfer) { this(null, capacity, maxTransfer, maxTransfer); }
    public FluidTankDefault(int capacity, int maxFill, int maxDrain) { this(null, capacity, maxFill, maxDrain); }
    
    @Override // IFluidTankAdvanced
	public boolean isEmpty() { return fluid == null; } 
    
	public void setMaxTransfer(int maxTransfer) {
		setMaxFill(maxTransfer);
		setMaxDrain(maxTransfer);
	}	
	public void setMaxFill(int maxFill) { this.maxFill = maxFill; }	
	public void setMaxDrain(int maxDrain) { this.maxDrain = maxDrain; }    

	@Override // FluidTank
    public boolean canFill() { return canFill; }
	@Override // FluidTank
    public boolean canDrain() { return canDrain; }
    public void setCanFill(boolean canFill) { this.canFill = canFill; }
    public void setCanDrain(boolean canDrain) { this.canDrain = canDrain; }
	
	private List<Observer> observers = new ArrayList<>();
	public void addObserver(Observer obv) { observers.add(obv); }
	
	protected void onContentsChanged() {
		for (Observer obv : observers) {
			obv.onTankContentsChanged();
		}
	}    

    @Override // IFluidTank
    @Nullable
    public FluidStack getFluid() { return fluid; }

    public void setFluid(@Nullable FluidStack fluid) { this.fluid = fluid; }    

    @Override // IFluidTank
    public int getFluidAmount() {
        if (fluid == null) { return 0; }
        return fluid.amount;
    }

    @Override // IFluidTank
    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public FluidTankInfo getInfo() { return new FluidTankInfo(this); }

    @Override // IFluidHandler
    public IFluidTankProperties[] getTankProperties() {
        if (this.tankProperties == null) {
            this.tankProperties = new IFluidTankProperties[] { new FluidTankProperties(this) };
        }
        return this.tankProperties;
    }

    @Override // IFluidTank
    public int fill(@Nullable FluidStack resource, boolean doFill) {
    	if (!canFill || resource == null) { return 0; }
        return fillInternal(new FluidStack(resource, Math.min(resource.amount, maxFill)), doFill);
    }
    
    @Override // FluidTank
    public int fillInternal(FluidStack resource, boolean doFill) {
        if (resource == null || resource.amount <= 0) { return 0; }

        if (!doFill) {
        	if (fluid == null) { return Math.min(capacity, resource.amount); }
        	if (!fluid.isFluidEqual(resource)) { return 0; }
        	return Math.min(capacity - fluid.amount, resource.amount);
        }

        if (fluid == null) {
            fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
            onContentsChanged();
            return fluid.amount;
        }

        if (!fluid.isFluidEqual(resource)) { return 0; }
        
        int filled = capacity - fluid.amount;
        if (resource.amount < filled) {
            fluid.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluid.amount = capacity;
        }

        onContentsChanged();
        return filled;
    }

    @Override // IFluidTank
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (!canDrain) { return null; }
        return drainInternal(new FluidStack(resource, Math.min(resource.amount, maxDrain)), doDrain);
    }

    @Override // IFluidHandler
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (!canDrain) { return null; }
        return drainInternal(Math.min(maxDrain, this.maxDrain), doDrain);
    }

    @Override // FluidTank
    @Nullable
    public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(getFluid())) { return null; }
        return drainInternal(resource.amount, doDrain);
    }

    @Override // FluidTank
    @Nullable
    public FluidStack drainInternal(int maxDrain, boolean doDrain)   {
        if (fluid == null || maxDrain <= 0) { return null; }

        int drained = maxDrain;
        if (fluid.amount < drained) { drained = fluid.amount; }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            fluid.amount -= drained;
            if (fluid.amount <= 0) { fluid = null; }
            onContentsChanged();
        }
        return stack;
    }

	@Override
	public FluidTankDefault copy() {
		FluidTankDefault ret = new FluidTankDefault(fluid, capacity, maxFill, maxDrain);
		ret.canFill = canFill; ret.canDrain = canDrain;
		return ret;
	}
	
	@SideOnly (Side.CLIENT)
	public void addInformation(List<String> tooltip, ITooltipFlag advanced) {
		if (fluid != null) {
			tooltip.add(String.format("%dmB %s", fluid.amount, fluid.getLocalizedName()));
		}
	}	

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (fluid != null) {
			fluid.writeToNBT(nbt);
		} else {
			nbt.setString("empty", "");
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("empty")) { setFluid(null); return; }
		setFluid(FluidStack.loadFluidStackFromNBT(nbt));
	}
}
