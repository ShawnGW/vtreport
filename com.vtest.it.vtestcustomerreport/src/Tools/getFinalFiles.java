package Tools;

import java.io.File;
import java.util.ArrayList;

public class getFinalFiles {
	public static File[] classfy(File[] files)
	{
		ArrayList<File> tempArray=new ArrayList<>();
		for (File file : files) {		
			try {
				file=getFile(file, files);
				//System.out.println(file.getName());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (!tempArray.contains(file)) {
				tempArray.add(file);
				System.out.println(file.getName());
			}
			
		}
		return tempArray.toArray(new File[tempArray.size()]);
	}
	public static File getFile(File file,File[] files)
	{
		File finFile=null;
		String fileName=file.getName();
		String waferid=fileName.substring(0, fileName.length()-4);
		String lastChar=waferid.substring(waferid.length()-1);
		Integer number=Integer.valueOf(lastChar)+1;
		String higerFileName=waferid.substring(0, waferid.length()-1)+number+".raw";
		boolean flag=false;
		File tempfile=null;
		for (File file2 : files) {
			if (file2.getName().equals(higerFileName)) {
				flag=true;
				tempfile=file2;
				break;
			}
		}
		if (flag) {
			finFile= getFile(tempfile, files);
		}else {
			finFile= file;
		}
		return finFile;
	}
}
