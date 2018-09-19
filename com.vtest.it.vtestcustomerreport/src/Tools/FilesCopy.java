package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilesCopy {
	public static void copyfile(File file_input,String path) throws IOException
	{
			File file_output=new File(path+"/"+file_input.getName());
			FileInputStream fileInputStream=new FileInputStream(file_input);
			FileOutputStream fileOutputStream=new FileOutputStream(file_output);
			byte[] bs=new byte[1024];
			int length=0;
			while((length=fileInputStream.read(bs))!=-1)
			{
				fileOutputStream.write(bs, 0, length);
			}
			fileOutputStream.close();
			fileInputStream.close();
	}
	public void DirectoryCopy(File file_input,String path) throws IOException
	{
		if(file_input.isDirectory())
		{
			String directoryName=file_input.getName();
			File file=new File(path+"/"+directoryName);
			file.mkdirs();
			File[] filelist=file_input.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isFile()) {
					copyfile(filelist[i], file.getPath());
				}else if (filelist[i].isDirectory()) {
					DirectoryCopy(filelist[i], file.getPath());
				}
				
			}
		}	
	}
	public void Copy(File file_input,String path) throws IOException
	{
		FilesCopy filesCopy=new FilesCopy();
		if(file_input.isFile())
		{
			FilesCopy.copyfile(file_input, path);
		}else if (file_input.isDirectory()) {
			filesCopy.DirectoryCopy(file_input, path);
		}
	}
}
