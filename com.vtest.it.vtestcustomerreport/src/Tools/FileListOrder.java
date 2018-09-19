package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileListOrder {
	public static File[]  GetList(File[] files) throws IOException {
		HashMap<Integer, File> FileMap=new HashMap<>();
		String regex="[A-Z]{1,}";
		Pattern pattern=Pattern.compile(regex);
		for (File file : files) {
			if (file.getName().equals(".raw")) {
				file.delete();
			}else {
				String FileName=file.getName().substring(0,file.getName().length()-4);
				Integer Length=FileName.length();
				String RightID=FileName.substring(Length-2, Length);
				Matcher matcher=pattern.matcher(RightID);
				if (matcher.find()) {
					RightID=FileName.substring(Length-4, Length-2);
					if (RightID.contains("-")) 
						RightID=FileName.substring(Length-5, Length-3);
				}
				FileReader out=new FileReader(file);
				BufferedReader bReader=new BufferedReader(out);
				String Content;
				while((Content=bReader.readLine())!=null)
				{
					if (Content.contains("RightID:")) {
						RightID=Content.split(":")[1];
						break;
					}
				}
				bReader.close();
				Integer ID=Integer.valueOf(RightID);
				FileMap.put(ID, file);
			}
		}
		TreeSet<Integer> treeSet= new TreeSet<>(FileMap.keySet());
		ArrayList<File> filesArray=new ArrayList<>();
		for (Integer ID : treeSet) {
			filesArray.add(FileMap.get(ID));
		}
		return filesArray.toArray(new File[filesArray.size()]);
	}
	public  void sort(Integer data[]) {  
        for (int i = 0; i < data.length -1; i++) {  
            for (int j = 0; j < data.length - i - 1; j++) {  
                if (data[j] > data[j + 1]) {  
                    int temp = data[j];  
                    data[j] = data[j + 1];  
                    data[j + 1] = temp;  
                }  
  
            }  
        }  
    } 
}
