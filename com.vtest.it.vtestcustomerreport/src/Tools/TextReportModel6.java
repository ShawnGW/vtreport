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

public class TextReportModel6 extends FTPRealseModel implements Text_Report{

	@Override
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
				Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
				Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
				String FailDie_R=properties.get("Fail Die");
				String FinalID=RightID_R.toString();
				String Wafer_Load_Time_R=properties.get("Test Start Time");
				TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
				Integer gross_die=Integer.parseInt(properties.get("Gross Die"));


				Integer[] Bininfor=new Integer[32];
				for (int i = 0; i < Bininfor.length; i++) {
					Bininfor[i]=0;
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
				TextReportModel3 model1=new TextReportModel3();
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
				
				File directory=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-"+CP);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-"+CP+"/"+FinalName);
				PrintWriter out=null;
				try {
					out=new PrintWriter(new FileWriter(Result_Text));
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}	
				
				out.print("PRODUCT       ="+Device+"\r\n");
				out.print("LOT           ="+Lot+"\r\n");
				out.print("WAFER ID      ="+FinalID+"\r\n");
				out.print("X QUANTUM     ="+Col_R+"\r\n");
				out.print("Y QUANTUM     ="+Row_R+"\r\n");
				out.print("FLAT/NOTCH    ="+Flat_R+"\r\n");
				out.print("\r\n");
				out.print("[ WAFER MAP]"+"\r\n");
				for (int i = -3; i < Row_R+2; i++) {
					for (int j = 0-3; j < Col_R+3; j++) {					
						try {
							if (MapCell_R[i][j]==null) {
								out.print(".");
							}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")||MapCell_R[i][j].equals("99")) {
								out.print(".");
							}else {
								out.print(MapCell_Modify.Modify(MapCell_R[i][j]));
							}
						} catch (Exception e) {
							// TODO: handle exception
							out.print(".");
						}						
					}
					out.print("\r\n");
				}
				out.print("[ BIN SUMMARY]"+"\r\n");
				out.print("BIN No.    Quan. Yield%"+"\r\n");
				out.print("========================================================"+"\r\n");
				for (int i = 0; i < Bininfor.length; i++) {
					out.print("BIN");
					out.print(String.format("%4s",(i+1)));
					out.print(" =");
					out.print(String.format("%7s",Bininfor[i]));
					out.print(String.format("%6.2f", (double)Bininfor[i]*100/gross_die)+"%"+"\r\n");
				}
				out.print("========================================================"+"\r\n");
				out.print("PassDie ="+String.format("%7s", PassDie_R)+String.format("%6.2f", (double)PassDie_R*100/gross_die)+"%"+"\r\n");
				out.print("FailDie ="+String.format("%7s",FailDie_R)+String.format("%6.2f", (double)Integer.parseInt(FailDie_R)*100/gross_die)+"%"+"\r\n");
				out.print("TotalDie="+String.format("%7s",gross_die)+"\r\n");
				out.flush();
				out.close();
				FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
