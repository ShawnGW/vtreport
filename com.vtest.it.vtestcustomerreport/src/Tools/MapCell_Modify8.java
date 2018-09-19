package Tools;

public class MapCell_Modify8 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")) {
			return ".";
		}
		if (Value.equals("M")) {
			return "N";
		}
		if (Value.equals("1"))
			return "1";
		else
			return "X";
	}
}
