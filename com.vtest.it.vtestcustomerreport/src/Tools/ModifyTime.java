package Tools;

public class ModifyTime {
	public static String Modify(String Time) {
		StringBuffer final_time=new StringBuffer();
		String Year=Time.substring(0,4);
		String Mouth=Time.substring(4,6);
		String Day=Time.substring(6,8);
		String Hour=Time.substring(8,10);
		String Minute=Time.substring(10,12);
		String second=Time.substring(12,14);
		final_time.append(Year+"-");
		final_time.append(Mouth);
		final_time.append("-");
		final_time.append(Day);
		final_time.append(" ");
		final_time.append(Hour);
		final_time.append(":");
		final_time.append(Minute);
		final_time.append(":"+second);
		return final_time.toString();
	}
	public static String Modify2(String Time) {
		StringBuffer final_time=new StringBuffer();
		String Year=Time.substring(0,4);
		String Mouth=Time.substring(4,6);
		String Day=Time.substring(6,8);
		String Hour=Time.substring(8,10);
		String Minute=Time.substring(10,12);
		String second=Time.substring(12,14);
		final_time.append(Year+"/");
		final_time.append(Mouth);
		final_time.append("/");
		final_time.append(Day);
		final_time.append(" ");
		final_time.append(Hour);
		final_time.append(":");
		final_time.append(Minute);
		final_time.append(":"+second);
		return final_time.toString();
	}
}
