package Tools;

import java.io.File;
import java.io.IOException;

public interface Text_Report {
//	HashMap<String, String> NameMap=new HashMap<>();
//	public HashMap<String, String> InitMap(String LOT,String ID,String CPPROC,String TIME,String DEVICE,String WAFER,String VERSION);
	public void Write_text(String CustomerCode,String Device,String Lot,String CP,File DataSorce,String FileName)throws IOException;
	public void FTP_Release(String CustomerCode, String Device, String Lot, String CP,File file);
}
