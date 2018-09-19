package Tools;

public class MapCell_Modify3 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}else if (Value.equals("S")||Value.equals("M")) {
			return "M";
		}
		else {
			if (Value.equals("1")) {
				return "P";
			}
			else
			{
				return "F";
			}
		}
			
	}
}
