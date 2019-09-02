package jaminv.advancedmachines.lib.recipe;

public interface RecipeInternal extends Recipe {
	int getInputCount();
	int getOutputCount();
	int getCatalystCount();
	int getSecondaryCount();

	RecipeInput getInput(int index);
	RecipeOutput getOutput(int index);
	RecipeInput getCatalyst(int index);
}