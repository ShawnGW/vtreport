package Tools;

public class MapCell_Modify12 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return " ";
		}else if (Value.equals("M")) {
			return "M";
		}else if (Value.equals("1")){
			return "1";
		}else{
			return "X";
		}
	}
}
