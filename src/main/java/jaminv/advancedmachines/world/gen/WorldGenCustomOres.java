package jaminv.advancedmachines.world.gen;

import java.util.Random;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.objects.variant.MaterialMod;
import jaminv.advancedmachines.util.ModConfig;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCustomOres implements IWorldGenerator {

	private WorldGenerator ore_titanium;

	public WorldGenCustomOres() {
		ore_titanium = new WorldGenMinable(BlockInit.ORE.getDefaultState().withProperty(BlockInit.ORE.VARIANT, MaterialMod.TITANIUM), ModConfig.worldgen.titaniumVeinSize, BlockMatcher.forBlock(Blocks.STONE));	
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {

		switch(world.provider.getDimension()) {
			case 0:
				if (ModConfig.worldgen.doGenerateTitanium) {
					runGenerator(ore_titanium, world, random, chunkX, chunkZ, ModConfig.worldgen.titaniumChance, ModConfig.worldgen.titaniumMinHeight, ModConfig.worldgen.titaniumMaxHeight);
				}
				break;
		}		
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
		if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256) {
			throw new IllegalArgumentException("Ore generated out of bounds");
		}
		
		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chance; i++) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			
			gen.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
