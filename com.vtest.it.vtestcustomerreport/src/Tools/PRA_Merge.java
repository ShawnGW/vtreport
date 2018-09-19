package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PRA_Merge {
	public HashMap<String, String> GetCPoneInfor(File file) throws IOException {
		HashMap<String, String> FailDieMap=new HashMap<>();
		FileReader in=new FileReader(file);
		BufferedReader bufferedReader=new BufferedReader(in);
		String content;
		boolean flag=false;
		while((content=bufferedReader.readLine())!=null)
		{
			if (content.contains("RawData")) {
				flag=true;
				continue;
			}
			if (content.contains("DataEnd")) {
				flag=false;
				break;
			}
			if (flag) {
				if (!content.substring(8, 12).trim().equals("1")) {
					String CoordinateX=content.substring(0, 4).trim();
					String CoordianetY=content.substring(4, 8).trim();
					FailDieMap.put(CoordianetY+":"+CoordinateX, content.substring(8, 12).trim());
				}
				
			}
		}
		bufferedReader.close();
		return FailDieMap;
	}
}
