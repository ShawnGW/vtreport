package Tools;

public class MapCell_Modify10 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return String.format("%3s", ".");
		}
		else if (Value.equals("S")||Value.equals("M")) {
			return String.format("%3s", ".");
		}
		else {
			return String.format("%3s", Value);
		}
	}
}
