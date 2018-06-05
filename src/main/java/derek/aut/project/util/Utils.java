package derek.aut.project.util;

public class Utils {
	public static String dataProcessing(String str, boolean split) {
		if(split) {
			if(str.split("[A-Z]+").length > 1) {
				str = str.replaceAll("([A-Z])", " $1").toLowerCase();
			}
		}
		
		str = str.replaceAll("\\W", " ").replaceAll("_", " ").trim().toLowerCase();

		while (str.indexOf("  ") > -1) {
			str = str.replaceAll("  ", " ");
		}

		return str;
	}
}
