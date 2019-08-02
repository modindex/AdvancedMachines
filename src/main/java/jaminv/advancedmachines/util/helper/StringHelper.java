package jaminv.advancedmachines.util.helper;

public class StringHelper {
	public static String buildString(String...strings) {
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
