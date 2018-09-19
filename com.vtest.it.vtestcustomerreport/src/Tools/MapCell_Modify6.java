package Tools;

public class MapCell_Modify6 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return "Z";
		}
		if (Value.matches("[0-9]{1,}")) {
			Integer Temp_Value=Integer.valueOf(Value);
			if (Temp_Value>9) {
				return ""+(char)(55+Temp_Value);
			}else {
				if (Value.equals("0")) 
					return "5";
				else 
					return Value;
				
			}
		}else
			return null;
	}
}
