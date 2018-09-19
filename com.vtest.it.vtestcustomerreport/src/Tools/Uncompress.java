package Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Uncompress {

	public void Ucompress(File zipfile) throws IOException
	{
		FileInputStream fileInputStream=new FileInputStream(zipfile);
		ZipInputStream zipInputStream=new ZipInputStream(fileInputStream);
		BufferedInputStream bufferedInputStream=new BufferedInputStream(zipInputStream);
		ZipEntry zipEntry=null;
		while((zipEntry=zipInputStream.getNextEntry())!=null)
		{
			File unzipfile=new File("E:/test/"+zipEntry.getName());
			System.out.println(unzipfile.getPath());
			System.out.println(zipEntry.getName());
			FileOutputStream fileOutputStream=new FileOutputStream(unzipfile);
			BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
			int b;
			while((b=bufferedInputStream.read())!=-1)
			{
				bufferedOutputStream.write(b);
			}
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		}
		bufferedInputStream.close();
	}
}
