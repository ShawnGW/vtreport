package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class GetMarkDie {
	private final File DataSource=new File("D:/TestReport/Q9FMJJG.raw");
	public HashSet<String> Get() throws IOException
	{
		HashSet<String> Markset=new HashSet<>();
		FileReader in=new FileReader(DataSource);
		BufferedReader bReader=new BufferedReader(in);
		String content;
		boolean flag=false;
		while((content=bReader.readLine())!=null)
		{
			if (content.contains("SkipAndMarkStart")) {
				flag=true;
				continue;
			}
			if (content.contains("SkipAndMarkEnd")) {
				flag=false;
				continue;		
			}
			if (flag) {
				String Coordinate_X=(content.substring(0, 4).trim());
				String Coordinate_Y=(content.substring(4,8).trim());
				Markset.add(Coordinate_Y+"&"+Coordinate_X);
			}
		}
		bReader.close();
		return Markset;
	}
}
