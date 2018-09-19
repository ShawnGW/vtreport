package Tools;

public class MapCell_Modify9 {
	public static String Modify(String Value) {
		if (Value==null) {	
			return "___ ";
		}
		else if (Value.equals("S")) {
			return "___ ";
		}
		else if (Value.equals("M")) {
			return "___ ";
		}
		else {
			if (Value.length()==1)
				{return "00"+Value+" ";}
			else if (Value.length()==2) {
				return "0"+Value+" ";
			}else {
				return Value+" ";
			}
				
		}
	}
}
