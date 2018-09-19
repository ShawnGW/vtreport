package Tools;

public class MapCell_Modify11 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}
		else if (Value.equals("S")) {
			return ".";
		}
		else if (Value.equals("M")) {
			return ".";
		}
		else {
			if (Value.equals("1")||Value.equals("9")) {
				return Value;
			}else {
				return "2";
			}	
		}
	}
}
