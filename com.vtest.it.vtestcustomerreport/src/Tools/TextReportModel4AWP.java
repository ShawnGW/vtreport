package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import parseRawdata.parseRawdata;

public class TextReportModel4AWP extends FTPRealseModel implements Text_Report{

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel4AWP model1=new TextReportModel4AWP();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {			
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArraySoftBin();
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

			Integer gross_die=Integer.valueOf(properties.get("Gross Die"));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			String Program=properties.get("Tester Program");
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");
			String TestEndTime_R=properties.get("Test End Time");
			String[] Bin_Defination=new String[32];
			for (int i = 0; i < Bin_Defination.length; i++) {
				Bin_Defination[i]="Fail";
			}
			String Version="NA";
			String Report_Dev="NA";
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			if (Report_Dev.equals("NA")) {
				Report_Dev=Device;
			}
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Report_Dev, Wafer_ID_R, Version);
			Set<String> keyset1=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset1) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			File[] filelist=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/").listFiles();
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i].getName().contains(Lot+FinalID)) {
						filelist[i].delete();
					}
				}
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
			String startTime=TestStartTime_R.substring(0, 4)+"/"+TestStartTime_R.substring(4, 6)+"/"+TestStartTime_R.substring(6, 8)+" "+TestStartTime_R.substring(8, 10)+":"+TestStartTime_R.substring(10, 12)+":"+TestStartTime_R.substring(12, 14);
			String endTime=TestEndTime_R.substring(0, 4)+"/"+TestEndTime_R.substring(4, 6)+"/"+TestEndTime_R.substring(6, 8)+" "+TestEndTime_R.substring(8, 10)+":"+TestEndTime_R.substring(10, 12)+":"+TestEndTime_R.substring(12, 14);
			out.print("[BOF]"+"\r\n");
			out.print("PRODUCT ID      : "+Device+"\r\n");
			out.print("LOT ID          : "+Lot+"\r\n");
			out.print("WAFER ID        : "+FinalID+"\r\n");
			out.print("FLOW ID         : "+CP+"\r\n");
			out.print("START TIME      : "+startTime+"\r\n");
			out.print("STOP TIME       : "+endTime+"\r\n");
			out.print("SUBCON          : VT"+"\r\n");
			out.print("TESTER NAME     : "+properties.get("Tester ID")+"\r\n");
			out.print("TEST PROGRAM    : "+Program+"\r\n");
			out.print("LOAD BOARD ID   : "+"\r\n");
			out.print("PROBE CARD ID   : "+"\r\n");
			out.print("SITE NUM        : "+properties.get("Sites")+"\r\n");
			out.print("DUT ID          : "+"\r\n");
			out.print("DUT DIFF NUM    : "+"\r\n");
			out.print("OPERATOR ID     : "+OPerater_R+"\r\n");
			out.print("TESTED DIE      : "+gross_die+"\r\n");
			out.print("PASS DIE        : "+PassDie_R+"\r\n");
			out.print("YIELD           : "+Yeild_R+"\r\n");
			out.print("SOURCE NOTCH    : "+Flat_R+"\r\n");
			out.print("MAP ROW         : "+Row_R+"\r\n");
			out.print("MAP COLUMN      : "+Col_R+"\r\n");
			out.print("MAP BIN LENGTH  : 1"+"\r\n");
			out.print("SHIP            : "+"\r\n");
			out.print(""+"\r\n");
			out.print("[SOFT BIN]"+"\r\n");
			out.print("        BINNAME, DIENUM,  YIELD, DESCRIPTION"+"\r\n");
			
			for (int i = 1; i < 31; i++) {
				Integer Value=0;
				if (Bin_Summary_R.containsKey(i)) {
					Value=Bin_Summary_R.get(i);
				}
				double yeild=(double)Value*100/gross_die;
				out.printf("%7S", "Bin");
				out.print(",");
				out.printf("%7S", i);
				out.print(",");
				out.printf("%7S", Value);
				out.print(",");
				out.printf("%7S", String.format("%.2f",yeild)+"%");
				out.print(",");
				out.printf(" {[       ]}");
				out.print("\r\n");
			}
			TreeSet<Integer> keySet=new TreeSet<>(Bin_Summary_R.keySet());
			for (Integer bin : keySet) {
				if (bin>30) {
					Integer Value=0;
					Value=Bin_Summary_R.get(bin);
					double yeild=(double)Value*100/gross_die;
					out.printf("%7S", "Bin");
					out.print(",");
					out.printf("%7S", bin);
					out.print(",");
					out.printf("%7S", Value);
					out.print(",");
					out.printf("%7S", String.format("%.2f",yeild)+"%");
					out.print(",");
					out.printf(" {[       ]}");
					out.print("\r\n");
				}
			}
			
			out.print(""+"\r\n");
			out.print("[SOFT BIN MAP]"+"\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print(i/100);
			}
			out.print("\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print((i/10)%10);
			}
			out.print("\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print(i%10);
			}
			out.print("\r\n");
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				if ((i+1)<10) {
					out.print("00"+(i+1));
				}else if ((i+1)>9&&(i+1)<100) {
					out.print("0"+(i+1));
				}else {
					out.print((i+1));
				}
				for (int j = 0; j < Col_R; j++) {					
					if (MapCell_R[i][j]==null) {
						out.print(" ");
					}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
						out.print(".");
					}else {
						out.print(MapCell_Modify.Modify(MapCell_R[i][j]));	
					}						
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("[EXTENSION]\r\n");
			out.print("\r\n");
			out.print("[EOF]\r\n");
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
