package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel56 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel56 model15=new TextReportModel56();
		File[] Filelist=DataSorce.listFiles();
		File summaryFile=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+".lot");
		PrintWriter printWriter=new PrintWriter(summaryFile);
		printWriter.print("Assembly Lot Summary Report\r\n");
		printWriter.print("\r\n");
		printWriter.print("\r\n");
		printWriter.print("Wafer Q'ty   :"+Filelist.length+"\r\n");
		printWriter.print("Summary Date :"+(new SimpleDateFormat("MM/dd/yyyy").format(new Date()))+"\r\n");
		printWriter.print("-------------------------------------------------------------------------\r\n");
		printWriter.print("     FILE          WAFERID          NOTCH     BINA    BINX   TOTAL\r\n");
		printWriter.print("-------------------------------------------------------------------------\r\n");
		printWriter.print("\r\n");
		TreeMap<String, String> sumMap=new TreeMap<>();
		int totalPassDie=0;
		int totalFailDie=0;
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			String[] passDieArray=properties.get("Pass Bins").split(",");
			ArrayList<String> passList=new ArrayList<>();
			for (int i = 0; i < passDieArray.length; i++) {
				passList.add(passDieArray[i]);
			}
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
			String passDies=properties.get("Pass Die");
			String failDies=properties.get("Fail Die");

			totalPassDie=totalPassDie+Integer.valueOf(passDies);
			totalFailDie=totalFailDie+Integer.valueOf(failDies);
			sumMap.put(Wafer_ID_R, passDies+"&"+failDies);
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
			out.print("Wafer ID : "+Wafer_ID_R+"\r\n");
			out.print("Flat_Notch : Bottom\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j =0 ; j < Col_R; j++) {
					out.print(MapCell_Modify5.Modify4(MapCell_R[i][j],passList));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
			//Fab_Mapping
		}
		Set<String> waferidSet=sumMap.keySet();
		for (String waferId : waferidSet) {
			String passDie=sumMap.get(waferId).split("&")[0];
			String failDie=sumMap.get(waferId).split("&")[1];//Bottom
			printWriter.print(String.format("%-18s", waferId)+String.format("%-18s", waferId)+String.format("%-10s","Bottom")+String.format("%-9s",passDie )+failDie+"\r\n");
			printWriter.print("-------------------------------------------------------------------------\r\n");
		}
		printWriter.print("                                              "+String.format("%-9s", totalPassDie)+String.format("%-7s", totalFailDie)+(totalPassDie+totalFailDie)+"\r\n");
		printWriter.flush();
		printWriter.close();
	}
}
