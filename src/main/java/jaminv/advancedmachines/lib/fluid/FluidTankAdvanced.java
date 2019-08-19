package jaminv.advancedmachines.lib.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidTankAdvanced implements IFluidTankAdvanced {
	
    @Nullable
    protected FluidStack fluid;
    protected int capacity;
    protected boolean canFill = true, canDrain = true;
    protected int maxFill, maxDrain;
    protected IFluidTankProperties[] tankProperties;

    public FluidTankAdvanced(@Nullable FluidStack fluidStack, int capacity, int maxFill, int maxDrain) {
        this.fluid = fluidStack;
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
    }

    public FluidTankAdvanced(FluidStack fluidStack, int capacity, int maxTransfer) { this(null, capacity, maxTransfer, maxTransfer); }
    public FluidTankAdvanced(int capacity, int maxTransfer) { this(null, capacity, maxTransfer, maxTransfer); }
    public FluidTankAdvanced(int capacity, int maxFill, int maxDrain) { this(null, capacity, maxFill, maxDrain); }
    
    @Override // IFluidTankAdvanced
	public boolean isEmpty() { return fluid == null; } 
    
	public void setMaxTransfer(int maxTransfer) {
		setMaxFill(maxTransfer);
		setMaxDrain(maxTransfer);
	}	
	public void setMaxFill(int maxFill) { this.maxFill = maxFill; }	
	public void setMaxDrain(int maxDrain) { this.maxDrain = maxDrain; }    

    public boolean canFill() { return canFill; }
    public boolean canDrain() { return canDrain; }
    public void setCanFill(boolean canFill) { this.canFill = canFill; }
    public void setCanDrain(boolean canDrain) { this.canDrain = canDrain; }
	
	private List<IObserver> observers = new ArrayList<>();
	public void addObserver(IObserver obv) { observers.add(obv); }
	
	protected void onContentsChanged() {
		for (IObserver obv : observers) {
			obv.onTankContentsChanged();
		}
	}    

    @Override // IFluidTank
    @Nullable
    public FluidStack getFluid() { return fluid; }

    @Override // IFluidTankAdvanced
    public void setFluid(@Nullable FluidStack fluid) { this.fluid = fluid; }    

    @Override // IFluidTank
    public int getFluidAmount() {
        if (fluid == null) { return 0; }
        return fluid.amount;
    }

    @Override // IFluidTank
    public int getCapacity() { return capacity; }

    @Override // IFluidTankAdvanced
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public FluidTankInfo getInfo() { return new FluidTankInfo(this); }

    @Override // IFluidHandler
    public IFluidTankProperties[] getTankProperties() {
        if (this.tankProperties == null) {
            this.tankProperties = new IFluidTankProperties[] { new FluidTankProperties(this, canFill, canDrain) };
        }
        return this.tankProperties;
    }

    @Override // IFluidTank
    public int fill(@Nullable FluidStack resource, boolean doFill) {
    	if (!canFill || resource == null) { return 0; }
        return fillInternal(new FluidStack(resource, Math.min(resource.amount, maxFill)), doFill);
    }
    
    @Override // IFluidTankInternal
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

    @Override // IFluidTankInternal
    @Nullable
    public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(getFluid())) { return null; }
        return drainInternal(resource.amount, doDrain);
    }

    @Override // IFluidTankInternal
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
	public FluidTankAdvanced copy() {
		FluidTankAdvanced ret = new FluidTankAdvanced(fluid, capacity, maxFill, maxDrain);
		ret.canFill = canFill; ret.canDrain = canDrain;
		return ret;
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
