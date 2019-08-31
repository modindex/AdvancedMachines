package jaminv.advancedmachines.lib.util.logger;

import java.io.*;

import org.apache.logging.log4j.Level;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

public class Logger {
	private final Writer writer;
	protected String name = null;
	
	public Logger(String filename, String name, boolean append) {
		this.name = name;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(new File(filename), append), "utf-8");
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(I18n.format("error.logger.unsupported_encoding", "utf-8", e.getMessage()));
		} catch(FileNotFoundException e) {
			throw new RuntimeException(I18n.format("error.logger.error_loading_file", filename, e.getMessage()));
		}		
	}
	
	public Logger(String filename, String name) {
		this(filename, name, true);
	}

	public void close() {
		try {
			writer.close();
		} catch(IOException e) {
			throw new RuntimeException(e);			
		}
	}
	
	protected Logger(Writer writer, String name) {
		this.writer = writer;
		this.name = name;
	}
	public Logger getLogger(String name) {
		return new Logger(writer, this.name + "." + name);
	}
	
	public void log(Level level, String message) {
		try {
			writer.write("[" + level.toString() + "] [" + name + "] " + message + "\n");
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void log(Level level, String section, String message) {
		log(level, "[" + section + "] " + message);
	}
	
	public void info(String message) { log(Level.INFO, message); }
	public void trace(String message) { log(Level.TRACE, message); }
	public void debug(String message) { log(Level.DEBUG, message); }
	public void error(String message) { log(Level.ERROR, message); }
	public void fatal(String message) { log(Level.FATAL, message); }
	public void warn(String message) { log(Level.WARN, message); }
	
	public void logBlank() {
		try {
			writer.write("\n");
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
