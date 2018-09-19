package Tools;

import java.util.ArrayList;

public class MapCell_Modify5 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return ".";
		}
		if (Value.equals("1"))
			return "A";
		else
			return "X";
	}
	public static String Modify2(String Value,ArrayList<String> passDies) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return ".";
		}
		if (passDies.contains(Value))
			return "A";
		else
			return "X";
	}
	public static String Modify3(String Value,ArrayList<String> passDies) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return ".";
		}
		if (passDies.contains(Value))
			return Value;
		else
			return "X";
	}
	public static String Modify4(String Value,ArrayList<String> passDies) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return ".";
		}
		if (passDies.contains(Value))
			return "A";
		else
			return "X";
	}
}
