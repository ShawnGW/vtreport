package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel11 extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		File[] Filelist=DataSorce.listFiles();
		String FabDevice="NA";
		String MESprogram="NA";
		try {
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Filelist[0]).getProperties();
			FabDevice=propertiesFirst.get("Fab Device");
			MESprogram=propertiesFirst.get("Tester Program");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for (int k = 0; k < Filelist.length; k++) {				
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArraySoftBin();
			String Program_R=properties.get("Tester Program");
			if (Program_R.equals("NA")) {
				Program_R=MESprogram;
			}
			String ProberCard_R=properties.get("Prober Card ID");

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
			Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
			Integer Row_R=Integer.parseInt(properties.get("Map Rows"));
			String waferid= properties.get("Wafer ID");
			String TestStartTime_R=properties.get("Test Start Time");
			String TestEndTime_R=properties.get("Test End Time");
			String OPerater_R=properties.get("Operator");
			String Tester_R=properties.get("Tester ID");
			
			TextReportModel11 model1=new TextReportModel11();
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			String VERSION="NA";
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));

			String FinalID=RightID_R.toString();
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, waferid, VERSION);
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
			out.print(String.format("%-12s", Lot));
			out.print(waferid.split("-")[1].substring(0, 2));
			out.print(String.format("%-42s", Device));
			out.print(String.format("%-8s", Tester_R));
			out.print(String.format("%-8s", OPerater_R));
			out.print(String.format("%-30s", Program_R));
			out.print(String.format("%-38s", ModifyTime.Modify(TestStartTime_R)+ModifyTime.Modify(TestEndTime_R)));
			out.print(String.format("%-12s", ProberCard_R));
			out.print(String.format("%-12s", "L/B01"));
			out.print(String.format("%-20s", FabDevice+"_JCET_1_1"));
			out.print(Flat_R.substring(0, 1));
			out.print(String.format("%-29s", "1V-Test"));
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					if (MapCell_R[i][j]!=null&&!MapCell_R[i][j].equals("S")&&!MapCell_R[i][j].equals("M")) {
						out.print(String.format("%4s", j));
						out.print(String.format("%4s", i));
						out.print(String.format("%4s", MapCell_R[i][j]));
						out.print(String.format("%4s","0")+"\r\n");
					}
				}
			}			
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
