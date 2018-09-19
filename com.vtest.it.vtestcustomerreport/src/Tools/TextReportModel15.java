package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel15 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel15 model15=new TextReportModel15();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
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
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();

			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String TestStartTime_R=properties.get("Test Start Time");
//			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
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
			out.print("Lot ID:"+Lot+"\r\n");
			out.print("Wafer ID:"+FinalID+"\r\n");
			out.print("Product ID:"+"\r\n");
			out.print(Lot+FinalID+".MAP\r\n");
			if (Device.equals("P7516G")) {
				out.print("Customer Device ID:7516G\r\n");
			}else {
				out.print("Customer Device ID:"+Device+"\r\n");
			}
			out.print("Customer Code:"+"\r\n");
			out.print("Mapping file:"+Lot+FinalID+".MAP\r\n");
			out.print("Notch side:"+Flat_R.toLowerCase()+"\r\n");
			out.print("\r\n");
			out.print("\r\n");
			for (int j = 0; j < Col_R; j++) {
				out.print(".");
			}
			out.print("\r\n");
			for (int j = 0; j < Col_R; j++) {
				out.print(".");
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print(MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("1 - Good  die(bin 1)    "+PassDie_R+"EA\r\n");
			out.print("X - Bad die     "+FailDie_R+"\r\n");
			out.print("Gross die:                                  "+GrossDie_R+"\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
