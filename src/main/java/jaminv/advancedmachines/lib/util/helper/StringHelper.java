package jaminv.advancedmachines.lib.util.helper;

public class StringHelper {
	public static String buildString(String...strings) {
		return buildString(".", strings);
	}
	
	public static String buildPath(String...strings) {
		return buildString("/", strings);
	}
	
	protected static String buildString(String separator, String...strings) {
		String ret = "";
		
		boolean first = true;
		for (String s : strings) {
			if (!first) { ret += "."; }
			first = false;
			
			ret += s;
		}
		
		return ret;
	}
}
