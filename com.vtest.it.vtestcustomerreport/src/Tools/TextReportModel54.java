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

public class TextReportModel54 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel54 model15=new TextReportModel54();
		File[] Filelist=DataSorce.listFiles();		
		File summaryFile=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"L1.LIS");
		PrintWriter printWriter=new PrintWriter(summaryFile);
		printWriter.print("Terapower\r\n");
		printWriter.print("FTS\r\n");
		printWriter.print(Device+"\r\n");
		printWriter.print(Lot+"\r\n");
		printWriter.print(Filelist.length+"\r\n");
		printWriter.print("MAP_FILE                    	WAFER_ID        BIN 1    BIN X\r\n");	
		TreeMap<Integer, String> fileSummary=new TreeMap<>();
		Integer allPassDies=0;
		Integer allFailDies=0;
		String finNotch="NA";
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getHardBinTestDiesDimensionalArray();

			//Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			Integer passdie=Integer.valueOf(properties.get("Pass Die"));
			Integer faildie=Integer.valueOf(properties.get("Fail Die"));
			
			allPassDies+=passdie;
			allFailDies+=faildie;
			
			String notch=properties.get("Notch");
			if (k==0) {
				if (notch.equals("180-Degree")) {
					finNotch="DOWN";
				}else if (notch.equals("90-Degree")) {
					finNotch="RIGHT";
				}else if (notch.equals("270-Degree")) {
					finNotch="LEFT";
				}else {
					finNotch="UP";
				}
			}
			if (notch.equals("180-Degree")) {
				notch="180(DOWN)";
			}else if (notch.equals("90-Degree")) {
				notch="90(RIGHT)";
			}else if (notch.equals("270-Degree")) {
				notch="270(LEFT)";
			}else {
				notch="0(UP)";
			}
			StringBuffer sBuffer=new StringBuffer();
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
			out.print("Customer :FTS\r\n");
			out.print("Cust Lot No :"+Lot+"\r\n");
			out.print("Wafer_ID :"+Wafer_ID_R+"\r\n");
			out.print("Mapping File Name :"+Lot+"                                                   "+RightID_R+".ASC\r\n");
			out.print("Flat Orientation :"+notch+"\r\n");
			out.print("R_wafer_ID :\r\n");
			out.print("Wafer Num :"+RightID_R+"\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print( MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			sBuffer.append(String.format("%-32s", Lot+RightID_R+".ASC")+String.format("%-16s", Wafer_ID_R));
			sBuffer.append(String.format("%-12s", passdie));
			sBuffer.append(faildie);
			fileSummary.put(RightID_R, sBuffer.toString());
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
			
		}
		Set<Integer> idSet=fileSummary.keySet();
		for (Integer id : idSet) {
			printWriter.print(fileSummary.get(id)+"\r\n");
		}
		printWriter.print("                             TOTAL(BIN 1)"+allPassDies+"\r\n");
		printWriter.print("                                  (BIN X)"+allFailDies+"\r\n");
		printWriter.print("\r\n");
		printWriter.print("Notch : "+finNotch+"\r\n");
		printWriter.flush();
		printWriter.close();
		FTP_Release(CustomerCode, Device, Lot, CP, summaryFile);
	}
}
