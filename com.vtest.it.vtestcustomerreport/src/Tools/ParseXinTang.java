package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

public class ParseXinTang {
	private  final File file=new File("/server212/Manmap/IncomingMap/XNT");
	private  final File fileBack=new File("/server212/Manmap/IncomingMapBackup/XNT");
	public void init()
	{
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!fileBack.exists()) {
			fileBack.mkdirs();
		}
		File[] mappings=file.listFiles();
		try {
			if (mappings.length>0) {
				for (int i = 0; i < mappings.length; i++) {
					String fileName=mappings[i].getName();
					File backupFile=new File(fileBack.getPath()+"/"+fileName);
					if (backupFile.exists()) {
						backupFile.delete();
					}
					FileUtils.copyFile(mappings[i], backupFile);
					mappings[i].delete();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public String[][] parse(String waferId,Integer row,Integer col)
	{
		init();
		String[][] resultMapping=new String[row][col];
		File mergeMapping=null;
		File[] mappings=fileBack.listFiles();
		for (File file : mappings) {
			if (file.getName().contains(waferId)) {
				mergeMapping=file;
			}
		}
		if (mergeMapping!=null) {
			try {
				boolean flag=false;
				FileReader reader=new FileReader(mergeMapping);
				BufferedReader bufferedReader=new BufferedReader(reader);
				String content;
				Integer mergeRow=0;
				while((content=bufferedReader.readLine())!=null)
				{
					if (content.trim().length()==0) {
						continue;
					}
					if (flag) {
						Integer length=content.length();
						for(int i=0;i<length;i++)
						{
							String value=content.substring(i, i+1);
							if (!value.equals("_")) {
								resultMapping[mergeRow][i]=value;
							}
						}
						mergeRow++;
					}
					if (content.trim().equals("WAFER MAP:")) {
						flag=true;
						continue;
					}
				}
				bufferedReader.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return resultMapping;
	}
}
