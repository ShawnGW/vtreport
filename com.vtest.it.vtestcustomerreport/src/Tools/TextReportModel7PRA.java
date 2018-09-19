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

public class TextReportModel7PRA extends FTPRealseModel implements Text_Report {

	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {					
			try {
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
				String FinalID=RightID_R.toString();
				String Wafer_Load_Time_R=properties.get("Test Start Time");
				Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
				Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
				TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
				Integer[] Bininfor=new Integer[32];
				for (int i = 0; i < Bininfor.length; i++) {
					Bininfor[i]=0;
				}
				String mapReferenceDie=properties.get("Map Reference");
				if (!mapReferenceDie.equals("NA")) {
					Integer CoordinateX=Integer.parseInt(mapReferenceDie.split(",")[0].substring(1).trim())-Integer.valueOf(properties.get("TestDieleft"));
					Integer CoordinateY=Integer.parseInt(mapReferenceDie.split(",")[1].substring(0, mapReferenceDie.split(",")[1].length()-1))-Integer.valueOf(properties.get("TestDieup"));
					MapCell_R[CoordinateY][CoordinateX]="#";
				}
				Set<Integer> keyset=Bin_Summary_R.keySet();
				for (Integer key : keyset) {
					if(key<32)
					{
						Bininfor[key-1]=Bin_Summary_R.get(key);
					}else {
						Bininfor[31]=Bininfor[31]+Bin_Summary_R.get(key);
					}
				}
				TextReportModel7PRA model1=new TextReportModel7PRA();
				String VERSION="NA";
				if (RightID_R<10) {
					FinalID="0"+RightID_R.toString();
				}
				HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
				Set<String> keyset1=NameMap.keySet();
				String FinalName=FileName;
				for (String key : keyset1) {
					if (FinalName.contains(key)) {
						FinalName=FinalName.replace(key, NameMap.get(key));
					}
				}
				File directory=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-"+CP+"-A");
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-"+CP+"-A"+"/"+FinalName);
				PrintWriter out=null;
				try {
					out=new PrintWriter(new FileWriter(Result_Text));
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				out.print("Lot ID:  "+Lot+"\r\n");
				out.print("Wafer ID: "+FinalID+"\r\n");
				out.print("ProductID:  "+Device+"\r\n");
				out.print("Customer Code: "+CustomerCode+"\r\n");
				out.print("Mapping File: "+Lot+"."+FinalID+".wf"+"\r\n");
				out.print("Notch Side: "+Flat_R+"\r\n");
				for (int i = 0-2; i <Row_R+5; i++) {
					for (int j = 0-4; j < Col_R+4; j++) {				
						try {
							if (MapCell_R[i][j]==null) {
								out.print(".");
							}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
								out.print(".");
							}else {
								if (MapCell_R[i][j].equals("#")) {
									out.print("#");
								}else if (MapCell_R[i][j].equals("1")) {
									out.print("A");
								}else {
									out.print("X");
								}
								
							}	
						} catch (Exception e) {
							// TODO: handle exception
							out.print(".");
						}					
					}
					out.print("\r\n");
				}
				out.print("A - Good die"+String.format("%13s", PassDie_R)+"\r\n");
				out.print("B - Good die        0\r\n");
				out.print("X - Bad die"+"\r\n");
				out.print("\r\n");
				out.flush();
				out.close();
				FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
