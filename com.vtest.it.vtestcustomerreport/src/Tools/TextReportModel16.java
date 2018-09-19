package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel16 extends FTPRealseModel implements Text_Report{


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel16 model16=new TextReportModel16();
		File[] Filelist=DataSorce.listFiles();
		FileWriter writer=new FileWriter(new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+".smic"));
		PrintWriter Lot_Smic=new PrintWriter(writer);
		Lot_Smic.print("SMIC Wafer Sort Summary Report\r\n");
		Lot_Smic.print("Lot ID: "+Lot+"\r\n");
		Lot_Smic.print("\r\n");
		Lot_Smic.print("\r\n");
		Lot_Smic.print("MAPPING            WAFER_ID      GOODDIE\r\n");
		Integer Total_PassDie=0;
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
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
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));
			String fabDevice=properties.get("Fab Device");

			String FinalID=RightID_R.toString();

			Total_PassDie+=PassDie_R;
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String TestStartTime_R=properties.get("Test Start Time");;
			String VERSION="NA";
			HashMap<String, String> NameMap=model16.InitMap(Lot, FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION);
			Set<String> keyset=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			Lot_Smic.print(String.format("%-19s", FileName));
			Lot_Smic.print(String.format("%-14s", Lot+"-"+FinalID));
			Lot_Smic.print(String.format("%-7s", PassDie_R)+"\r\n");
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("Lot ID:"+Lot+"\r\n");
			out.print("Wafer ID:"+Wafer_ID_R+"\r\n");
			out.print("Product ID: "+fabDevice+"\r\n");
			out.print("Customer Code: "+fabDevice+"\r\n");
			out.print("Mapping file:"+Wafer_ID_R+".smic\r\n");
			out.print("Notch side:"+Flat_R+"\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print(MapCell_Modify5.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("A - Good die        "+PassDie_R+"\r\n");
			out.print("X - Bad die     "+"\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
		Lot_Smic.print("\r\n");
		Lot_Smic.print("\r\n");
		Lot_Smic.print("\r\n");
		Lot_Smic.print("Wafer Count: "+Filelist.length+"\r\n");
		Lot_Smic.print("Total Good Dice:   "+Total_PassDie+"\r\n");
		Lot_Smic.close();
	}
}
