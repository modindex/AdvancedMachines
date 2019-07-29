package jaminv.advancedmachines.objects.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidBase extends Fluid {
	
	protected int mapColor = 0xFFFFFFFF;
	protected float overlayAlpha = 0.2F;
	protected SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
	protected SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
	protected Material material = Material.WATER;

	public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
	}
	
	public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) {
		super(fluidName, still, flowing);
		setColor(mapColor);
	}
	
	public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor, float overlayAlpha) {
		this(fluidName, still, flowing, mapColor);
		setAlpha(overlayAlpha);
	}

	@Override
	public int getColor() {	return mapColor; }
	
	public FluidBase setColor(int color) {
		mapColor = color;
		return this;
	}
	
	public float getAlpha() { return overlayAlpha; }
	
	public FluidBase setAlpha(float alpha) {
		overlayAlpha = alpha;
		return this;
	}

	@Override
	public SoundEvent getFillSound() {
		return fillSound;
	}

	@Override
	public SoundEvent getEmptySound() {
		return emptySound;
	}

	@Override
	public Fluid setFillSound(SoundEvent sound) {
		fillSound = sound;
		return this;
	}

	@Override
	public Fluid setEmptySound(SoundEvent sound) {
		emptySound = sound;
		return this;
	}
	
	public FluidBase setMaterial(Material mat) {
		material = mat;
		return this;
	}
	
	public Material getMaterial() { return material; }

	@Override
	public boolean doesVaporize(FluidStack fluidStack) {
		if (this.block == null) { return false; }
		return this.block.getDefaultState().getMaterial() == getMaterial();
	}
	
	
}
