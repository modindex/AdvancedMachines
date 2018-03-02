package jaminv.advancedmachines.util.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level; 

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.material.MaterialBase;

public class ModelMaker {
	static protected final String start = "{\r\n" + 
		"    \"parent\": \"block/cube_all\",\r\n" + 
		"    \"textures\": {\r\n";
	static protected final String end = "    }\r\n}";
	
	static protected final String tab = "        ";
	
	static public void make(String folder, String name, MaterialBase.MaterialType type) throws IOException {
		for (MaterialBase material : MaterialBase.values(type)) {
			makeType(folder, name, material.getName());
		}
	}
	
	static protected void makeType(String folder, String name, String type) throws IOException {
		String model_dir = "..\\src\\main\\resources\\assets\\advmach\\models\\block\\" + folder + "\\" + type + "\\";
		String texture_dir = "advmach:blocks/" + folder.replace("\\", "/") + "/" + type + "/";
		String abbr = name + "_" + type;
		
		makeNone(model_dir, texture_dir, abbr);			// 0,1
		makeEdge(model_dir, texture_dir, abbr);			// 2
		makeCorner(model_dir, texture_dir, abbr);		// 3 (all 3 axes)
		makeTunnel(model_dir, texture_dir, abbr);		// 3 (2 on same axis)
		makeTube(model_dir, texture_dir, abbr);			// 4
		makeTubeEnd(model_dir, texture_dir, abbr);		// 5
		makeAll(model_dir, texture_dir, abbr);			// 6
	}

	// 0, 1
	static protected void makeNone(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_none.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_none\"\r\n" +
			end);
		
		writer.close();		
	}

	// 2
	static protected void makeEdge(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_edge.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_none\",\r\n" +
			tab + "\"north\": \"" + texture_dir + abbr + "_top\",\r\n" +
			tab + "\"up\": \"" + texture_dir + abbr + "_top\"\r\n" +
			end);
		
		writer.close();
	}

	// 3 (all 3 axes)
	static protected void makeCorner(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_corner.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_none\",\r\n" +
			tab + "\"north\": \"" + texture_dir + abbr + "_top_right\",\r\n" +
			tab + "\"west\": \"" + texture_dir + abbr + "_top_left\",\r\n" +
			tab + "\"up\": \"" + texture_dir + abbr + "_top_left\"\r\n" +
			end);
		
		writer.close();		
	}
	
	// 3 (2 on same axis)
	static protected void makeTunnel(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_tunnel.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_none\",\r\n" +
			tab + "\"north\": \"" + texture_dir + abbr + "_top\",\r\n" +
			tab + "\"south\": \"" + texture_dir + abbr + "_top\",\r\n" +
			tab + "\"up\": \"" + texture_dir + abbr + "_left_right\"\r\n" +
			end);
		
		writer.close();		
	}
	
	// 4
	// This should always be 2 edges each on 2 different axes.
	// A shape with 4 edges on 3 different axes would be a convex shape and not a cuboid.
	static protected void makeTube(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_tube.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_none\",\r\n" +
			tab + "\"north\": \"" + texture_dir + abbr + "_top_bottom\",\r\n" +
			tab + "\"south\": \"" + texture_dir + abbr + "_top_bottom\",\r\n" +
			tab + "\"up\": \"" + texture_dir + abbr + "_left_right\"\r\n" +
			tab + "\"down\": \"" + texture_dir + abbr + "_left_right\"\r\n" +
			end);
		
		writer.close();		
	}

	// 5
	static protected void makeTubeEnd(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_tube_end.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"down\": \"" + texture_dir + abbr + "_none\",\r\n" +
			tab + "\"north\": \"" + texture_dir + abbr + "_top_left_right\",\r\n" +
			tab + "\"south\": \"" + texture_dir + abbr + "_top_left_right\",\r\n" +
			tab + "\"east\": \"" + texture_dir + abbr + "_top_left_right\",\r\n" +
			tab + "\"west\": \"" + texture_dir + abbr + "_top_left_right\",\r\n" +
			tab + "\"up\": \"" + texture_dir + abbr + "_all\"\r\n" +
			end);
		
		writer.close();		
	}

	
	// 6
	static protected void makeAll(String model_dir, String texture_dir, String abbr) throws IOException {
		File file = new File(model_dir + abbr + "_all.json");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, false);
		
		writer.write(start + 
			tab + "\"all\": \"" + texture_dir + abbr + "_all\"\r\n" +
			end);
		
		writer.close();		
	}
	
	
	
	
}
