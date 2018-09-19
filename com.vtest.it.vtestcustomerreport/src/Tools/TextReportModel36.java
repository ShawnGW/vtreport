package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel36 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel36 model15=new TextReportModel36();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Index_X_R=properties.get("Index X(mm)");
			String Index_Y_R=properties.get("Index Y(mm)");
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			String Flat_R=null;
			String notch=properties.get("Notch");
			if (notch.equals("0-Degree")) {
				Flat_R="UP";
			}else if (notch.equals("90-Degree")) {
				Flat_R="RIGHT";
			}else if (notch.equals("180-Degree")) {
				Flat_R="DOWN";
			}else {
				Flat_R="LEFT";
			}
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
			Integer TestDie_MaxX_R=Integer.valueOf(properties.get("TestDieright"));
			Integer TestDie_MaxY_R=Integer.valueOf(properties.get("TestDiedown"));
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String VERSION="NA";
			HashMap<String, String> NameMap=model15.InitMap(Lot, FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION);
			Set<String> keyset=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("DEVICE:"+Device+"\r\n");
			out.print("LOT:"+Lot+"\r\n");
			out.print("WAFER:"+FinalID+"\r\n");			
			if (Flat_R.equals("DOWN")) {
				out.print("FNLOC:180\r\n");
			}else if (Flat_R.equals("UP")) {
				out.print("FNLOC:0\r\n");
			}else if (Flat_R.equals("LEFT")) {
				out.print("FNLOC:270\r\n");
			}else {
				out.print("FNLOC:90\r\n");
			}			
			out.print("ROWCT:"+(TestDie_MaxY_R+1-TestDie_MinY_R)+"\r\n");
			out.print("COLCT:"+(TestDie_MaxX_R+1-TestDie_MinX_R)+"\r\n");
			out.print("BCEQU:000"+"\r\n");
			out.print("REFPX:0"+"\r\n");
			out.print("REFPY:0"+"\r\n");
			out.print("DUTMS:mm"+"\r\n");
			out.print("XDIES:"+Index_X_R+"\r\n");
			out.print("YDIES:"+Index_Y_R+"\r\n");
			for (int i = TestDie_MinY_R; i < TestDie_MaxY_R+1; i++) {
				out.print("RowData:");
				for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++){
					out.print(MapCell_Modify9.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
