package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel23 extends FTPRealseModel  implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel23 model15=new TextReportModel23();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");		

			String mapReferenceDie=properties.get("Map Reference");
			if (!mapReferenceDie.toUpperCase().equals("NA")) {
				Integer CoordinateX=Integer.parseInt(mapReferenceDie.split(",")[0].substring(1).trim());
				Integer CoordinateY=Integer.parseInt(mapReferenceDie.split(",")[1].substring(0, mapReferenceDie.split(",")[1].length()-1));
				MapCell_R[CoordinateY][CoordinateX]="#";
			}
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
			out.print("Device Name: "+Device+"\r\n");
			out.print("Lot No: "+Lot+"\r\n");
			out.print("Wafer ID: "+FinalID+"\r\n");
			out.print("Pass Die: "+PassDie_R+"\r\n");
			out.print("Total Die: "+GrossDie_R+"\r\n");
			out.print("Yield:"+String.format("%2.2f",(double)PassDie_R*100/GrossDie_R)+"%\r\n");
			for (int i = -2; i < Row_R+4; i++) {
				for (int j =-2; j < Col_R+3; j++) {
					try {
						out.print( MapCell_Modify7.Modify(MapCell_R[i][j]));	
					} catch (Exception e) {
						// TODO: handle exception
						out.print(".");
					}	
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
