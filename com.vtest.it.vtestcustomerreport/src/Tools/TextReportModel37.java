package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel37 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel37 model15=new TextReportModel37();
		File[] Filelist=DataSorce.listFiles();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String GrossDie_R=properties.get("Gross Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			String Program=properties.get("Tester Program");
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
			Integer TestDie_MaxX_R=Integer.valueOf(properties.get("TestDieright"));
			Integer TestDie_MaxY_R=Integer.valueOf(properties.get("TestDiedown"));
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");
			String TestEndTime_R=properties.get("Test End Time");
			String Tester_R=properties.get("Tester ID");
			String ProberCard_R=properties.get("Prober Card ID");

			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String tempTime=TestEndTime_R.substring(0, 8)+"_"+TestEndTime_R.substring(8);
			String VERSION="NA";
			HashMap<String, String> NameMap=model15.InitMap(Lot, FinalID, CP, tempTime, Device, Wafer_ID_R, VERSION);
			Set<String> keyset=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			try {
				File[] textReports=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP).listFiles();
				if (textReports.length>0) {
					for (int i = 0; i < textReports.length; i++) {
						if (textReports[i].getName().contains("_"+FinalID+"_")) {
							textReports[i].delete();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("\r\n");
			out.print("Filename: "+Wafer_ID_R.split("-")[0]+"_"+FinalID+"_"+TestStartTime_R.substring(0, 8)+"_"+TestStartTime_R.substring(8)+".map\r\n");
			out.print("\r\n");
			out.print("                              LOT INFORMATION\r\n");
			out.print("\r\n");
			try {
				out.print("Start Date/Time: "+(format.parse(TestStartTime_R).toString().replace("CST ", ""))+"\r\n");
				out.print("Stop Date/Time: "+format.parse(TestEndTime_R).toString().replace("CST ", "")+"\r\n");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("Tester Station: "+Tester_R+"\r\n");
			out.print("Program Name: "+Program+"\r\n");
			out.print("Lot ID: "+Lot+"\r\n");
			out.print("Wafer ID: "+FinalID+"\r\n");
			out.print("Operator: "+OPerater_R.toUpperCase()+"\r\n");
			out.print("Handler/Prober Type: TSK UF Series\r\n");
			out.print("Test OS Version: \r\n");
			out.print("Test Conditions: \r\n");
			out.print("Device ID: "+Device+"\r\n");
			out.print("Site number: 3\r\n");
			out.print("Tested Dice: "+GrossDie_R+"\r\n");
			out.print("Map Row: "+(TestDie_MaxY_R-TestDie_MinY_R+1)+"\r\n");
			out.print("Map Column: "+(TestDie_MaxX_R-TestDie_MinX_R+1)+"\r\n");
			out.print("Map bin length: 2\r\n");
			out.print("pass dice: "+PassDie_R+"\r\n");
			out.print("yield: "+Yeild_R+"\r\n");
			out.print("P/C number: "+ProberCard_R+"\r\n");
			out.print("Wafer OCR_ID: "+Wafer_ID_R+"\r\n");
			out.print("Wafermap Generation Program by Credence-Spirox Integration Corporation\r\n");
			out.print("\r\n");
			out.print("     ");
			for (int i = 10; i < TestDie_MaxX_R-TestDie_MinX_R+11; i++) {
				out.print(String.format("%3s", i/10));
			}
			out.print("\r\n");
			out.print("     ");
			for (int i = 10; i < TestDie_MaxX_R-TestDie_MinX_R+11; i++) {
				out.print(String.format("%3s", i%10));
			}
			out.print("\r\n");
			out.print("     ");
			for (int i = 10; i < TestDie_MaxX_R-TestDie_MinX_R+11; i++) {
				out.print("___");
			}
			out.print("\r\n");
			for (int i = TestDie_MinY_R; i < TestDie_MaxY_R+1; i++) {
				out.print(String.format("%4s", i-TestDie_MinY_R+10));
				out.print("|");
				for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++) {
					out.print(MapCell_Modify10.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			out.print("Total: "+GrossDie_R+"\r\n");
			out.print("\r\n");
			Set<Integer> keyset_Sum=Bin_Summary_R.keySet();
			for (Integer bin : keyset_Sum) {
				out.print("Bin"+String.format("%4s", bin)+" ="+String.format("%5s", Bin_Summary_R.get(bin))+",     ");				
				out.print(String.format("%.4f", (double)Bin_Summary_R.get(bin)*100/(Integer.valueOf(GrossDie_R)))+"%\r\n");
			}
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
		
	}
//	@Override
//	public void FTP_Release(String CustomerCode, String Device, String Lot, String CP, File file) {
//		// TODO Auto-generated method stub
//		File ReleaseDirectory=new File("/home/UploadReport/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping");
//		if (!ReleaseDirectory.exists()) {
//			ReleaseDirectory.mkdirs();
//		}
//		File ReleaseFile=new File("/home/UploadReport/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/"+file.getName());
//		if (ReleaseFile.exists()) {
//			ReleaseFile.delete();
//		}
//		try {
//			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		File MailReleaseDirectory=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping");
//		if (!MailReleaseDirectory.exists()) {
//			MailReleaseDirectory.mkdirs();
//		}
//		File MailReleaseFile=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/"+file.getName());
//		if (MailReleaseFile.exists()) {
//			MailReleaseFile.delete();
//		}
//		try {
//			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
