package Tools;

public class MapCell_Modify13 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return " ";
		}
		if (Value.equals("S")) {
			return "-";
		}
		if (Value.equals("M")) {
			return "-";
		}
		if (Value.equals("1")){
			return "1";
		}
		else
		{
				Integer Temp_Value=Integer.valueOf(Value);
				if (Temp_Value>35) {
					return "#";
				}
				if (Temp_Value>9) {
					return ""+(char)(55+Temp_Value);
				}else {
					return Value;
				}
		}
	}
}
