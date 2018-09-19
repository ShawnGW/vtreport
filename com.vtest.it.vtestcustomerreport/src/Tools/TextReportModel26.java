package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel26 extends FTPRealseModel  implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel26 model15=new TextReportModel26();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			String Notch_R=properties.get("Notch");
			if (Notch_R.equals("0-Degree")) {
				Notch_R="UP";
			}else if (Notch_R.equals("90-Degree")) {
				Notch_R="RIGHT";
			}else if (Notch_R.equals("180-Degree")) {
				Notch_R="DOWN";
			}else {
				Notch_R="LEFT";
			}
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");

			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));

			String mapReferenceDie=properties.get("Map Reference");
			if (!mapReferenceDie.equals("NA")) {
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
			out.print("DEVICE NAME 	 : "+Device+"\r\n");
			out.print("LOT ID     	   : "+Lot+"\r\n");
			out.print("WAFER ID   	   : "+Wafer_ID_R+"\r\n");
			out.print("NOTCH       	 : "+Notch_R.substring(0, 1).toUpperCase()+Notch_R.substring(1).toLowerCase()+"\r\n");
			out.print("GOOD BIN    	 : "+PassDie_R+"\r\n");
			for (int i = TestDie_MinY_R-1; i < Row_R+TestDie_MinY_R; i++) {
				for (int j =TestDie_MinX_R; j < Col_R+TestDie_MinX_R; j++) {
					out.print( MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}

}
