package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel18 extends FTPRealseModel implements Text_Report{


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel18 model17=new TextReportModel18();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
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
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String FinalID=RightID_R.toString();

			String OPerater_R=properties.get("Operator");
			String Program_R=properties.get("Tester Program");
			String Tester_R=properties.get("Tester ID");
			String TestEndTime_R=properties.get("Test End Time");
			HashMap<String, String> softBinMap=parseRawdata.getSoftBinTestDie();
			
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String TestStartTime_R=properties.get("Test Start Time");
			String VERSION="NA";
			HashMap<String, String> NameMap=model17.InitMap(Lot, FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION);
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
			out.print("SMIC Prod ID:"+Device.substring(1,Device.length())+"\r\n");
			out.print("Test Program:"+Program_R+"\r\n");
			out.print("Tester ID:"+Tester_R+"\r\n");
			out.print("Operator ID:"+OPerater_R+"\r\n");
			out.print("Start Time:20"+TestStartTime_R.substring(0, 2)+"/"+TestStartTime_R.substring(2, 4)+"/"+TestStartTime_R.substring(4, 6)+" "+TestStartTime_R.substring(6, 8)+":"+TestStartTime_R.substring(8, 10)+":00"+"\r\n");
			out.print("End Time:20"+TestEndTime_R.substring(0, 2)+"/"+TestEndTime_R.substring(2, 4)+"/"+TestEndTime_R.substring(4, 6)+" "+TestEndTime_R.substring(6, 8)+":"+TestEndTime_R.substring(8, 10)+":00"+"\r\n");
			out.print("Notch Side:"+Flat_R.substring(0, 1)+"\r\n");
			out.print("Sort ID:1"+"\r\n");
			out.print("Bin Definition File:7516f-1-1\r\n");
			out.print("Grid Xmax:83\r\n");
			out.print("Grid Ymax:152\r\n");
			out.print("Fab Site:SMICB2"+"\r\n");
			out.print("Test Site:V-Test"+"\r\n");
			out.print("Probe Card ID:"+"\r\n");
			out.print("Load Board ID:"+"\r\n");
			out.print("ROM Code:CHK|8|"+Program_R+".xls|"+Program_R+"||Production|||||||||"+Wafer_ID_R+"\r\n");
			out.print("X Die Size:3599.00um"+"\r\n");
			out.print("Y Die Size:1961.00um"+"\r\n");
			out.print("X Street:"+"\r\n");
			out.print("Y Street:"+"\r\n");
			out.print("Customer Code:U958-C01"+"\r\n");
			out.print("Customer PartID:"+"\r\n");
			out.print("RawData"+"\r\n");
			Set<String> coordinates=softBinMap.keySet();
			for (String coordinate : coordinates) {
				String[] coordinateToken=coordinate.split(":");
				String value=softBinMap.get(coordinate);
				out.print(String.format("%4s", coordinateToken[1])+String.format("%4s", coordinateToken[0])+String.format("%4s", value)+String.format("%4s", value)+String.format("%4s", "0")+"\r\n");
			}
			out.print("DataEnd"+"\r\n");
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
