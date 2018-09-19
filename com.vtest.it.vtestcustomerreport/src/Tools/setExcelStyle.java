package Tools;

import java.util.HashMap;
import java.util.Set;

public class setExcelStyle {
	private  Integer StartValue=22;
	private  HashMap<String, Integer> styleMap=new HashMap<>();
	public void initMap()
	{
		for (int i = 0; i < 40; i++) {
			styleMap.put(StartValue+","+(StartValue+26), 67-26*(i+1));
			StartValue=StartValue+26;
		}
	}
	public String set(Integer J)
	{
		String formula=null;
		Set<String> set=styleMap.keySet();
		for (String range : set) {
			Integer minInteger=Integer.valueOf(range.split(",")[0]);
			Integer maxInteger=Integer.valueOf(range.split(",")[1]);
			if (J>minInteger&&J<=maxInteger) {
				formula="SUM("+(char)((minInteger-22)/26+65)+(char)(J+1+styleMap.get(range))+"8:"+(char)((minInteger-22)/26+65)+(char)(J+1+styleMap.get(range))+"32)";
			}
		}
		return formula;
	}
}
