package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
	public void zip(File in,File out) throws IOException
	{
		FileInputStream fins=new FileInputStream(in);
		FileOutputStream fous=new FileOutputStream(out);
		ZipOutputStream zipous=new ZipOutputStream(fous);
		zipous.setLevel(9);
		ZipEntry zipEntry=new ZipEntry(in.getName());
		zipous.putNextEntry(zipEntry);
		byte[] bt=new byte[1024];
		int length=0;
		while((length=fins.read(bt))!=-1)
		{
			zipous.write(bt, 0, length);
		}
		zipous.closeEntry();
		zipous.close();
		fins.close();
	}
}
