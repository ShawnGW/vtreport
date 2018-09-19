package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel21 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel21 model15=new TextReportModel21();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			TreeMap<Integer, Integer> binSummaryMap=parseRawdata.getBinSummary();
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
			//Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String GrossDie_R=properties.get("Gross Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			
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
			out.print("Customer Device ID:"+Device+"\r\n");
			out.print("Customer Code:"+"\r\n");
			out.print("Mapping file:"+Lot+FinalID+".MAP\r\n");
			out.print("Notch side:"+Flat_R.toLowerCase()+"\r\n");
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print( MapCell_Modify2.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("1 - Good  die(bin 1)    "+String.format("%-4s", (binSummaryMap.containsKey(1)?binSummaryMap.get(1):0))+"EA\r\n");
			out.print("3 - Good  die(bin 3)    "+String.format("%-4s", (binSummaryMap.containsKey(3)?binSummaryMap.get(3):0))+"EA\r\n");
			out.print("4 - Good  die(bin 4)    "+String.format("%-4s", (binSummaryMap.containsKey(4)?binSummaryMap.get(4):0))+"EA\r\n");
			out.print("5 - Good  die(bin 5)    "+String.format("%-4s", (binSummaryMap.containsKey(5)?binSummaryMap.get(5):0))+"EA\r\n");
//			out.print("A - Fail  die(bin 10)   "+String.format("%-4s", (binSummaryMap.containsKey(10)?binSummaryMap.get(10):0))+"EA\r\n");
//			out.print("B - Fail  die(bin 11)   "+String.format("%-4s", (binSummaryMap.containsKey(11)?binSummaryMap.get(11):0))+"EA\r\n");
			out.print("Gross die:                                  "+GrossDie_R+"\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
