package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parseRawdata.parseRawdata;

public class TextReportModel5 extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel5 model1=new TextReportModel5();
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
			Flat_R=CP.equals("CP1")?"Left":((CP.equals("CP2")||CP.equals("CP3"))?"Right":Flat_R);
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			String Program=properties.get("Tester Program");
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");
			String TestEndTime_R=properties.get("Test End Time");
			String Tester_R=properties.get("Tester ID");
			String ProberCard_R=properties.get("Prober Card ID");
			Integer gross_die=Integer.parseInt(properties.get("Gross Die"));

			ArrayList<String> Bin_Defination_Array = null;
			try {
				Bin_Defination_Array = GetSoftBinDefination.CallserviceForDoc(Device);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] Bin_Defination=new String[32];
			for (int i = 0; i < Bin_Defination.length; i++) {
				Bin_Defination[i]="Fail";
			}
			for (String BinIdInformation : Bin_Defination_Array) {
				String Value=BinIdInformation.split("&")[3];
				if (!Value.equals("VERSION")) 
				{
					Integer id=Integer.valueOf(BinIdInformation.split("&")[2]);
					Bin_Defination[id-1]=Value;
				}
			}
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
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);

			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(CP.equals("CP1"))
			{
				MapCell_R=TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);
			}else if (CP.equals("CP2")||CP.equals("CP3")) {
				MapCell_R=TurnNighteenDegree.Turn(MapCell_R, Row_R, Col_R);
			}else {
				MapCell_R=TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);
			}
			int tempNumber=Row_R;
			Row_R=Col_R;
			Col_R=tempNumber;
			
			out.print("[BOF]"+"\r\n");
			out.print("PRODUCT ID      : "+Device+"\r\n");
			out.print("LOT ID          : "+Lot+"\r\n");
			out.print("WAFER ID        : "+FinalID+"\r\n");
			out.print("FLOW ID         : "+CP+"\r\n");
			out.print("START TIME      : "+ModifyTime.Modify2(TestStartTime_R)+"\r\n");
			out.print("STOP TIME       : "+ModifyTime.Modify2(TestEndTime_R)+"\r\n");
			out.print("SUBCON          : VT"+"\r\n");
			out.print("TESTER NAME     : "+Tester_R+"\r\n");
			out.print("TEST PROGRAM    : "+(Program.endsWith("()")?Program.substring(0, Program.length()-2):Program)+"\r\n");
			out.print("LOAD BOARD ID   : "+"\r\n");
			out.print("PROBE CARD ID   : "+ProberCard_R+"\r\n");
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
			out.print("MAP BIN LENGTH  : 2"+"\r\n");
			out.print("SHIP            : YES"+"\r\n");
			out.print(""+"\r\n");
			out.print("[HARD BIN]"+"\r\n");
			out.print("        BINNAME, DIENUM,  YIELD, DESCRIPTION"+"\r\n");
			for (int i = 1; i < 64; i++) {
				Integer value=0;
				if (Bin_Summary_R.containsKey(i)) {
					value=Bin_Summary_R.get(i);
				}
				double yeild=(double)value*100/gross_die;
				out.printf("%7S", "Bin");
				out.print(",");
				out.printf("%7S", i);
				out.print(",");
				out.printf("%7S", value);
				out.print(",");
				out.printf("%7S", String.format("%.2f",yeild)+"%");
				out.print(",");
				try {
					out.printf(" {["+Bin_Defination[i-1]+"]}");
				} catch (Exception e) {
					// TODO: handle exception
					out.printf(" {[Fail]}");
				}
				out.print("\r\n");
			}
			out.print(""+"\r\n");
			out.print("[HARD BIN MAP]"+"\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print(String.format("%3s",i/100));
			}
			out.print("\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print(String.format("%3s",(i/10)%10));
			}
			out.print("\r\n");
			out.print("    ");
			for(int i=1;i<=Col_R;i++)
			{
				out.print(String.format("%3s",i%10));
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				if ((i+1)<10) {
					out.print("00"+(i+1)+" ");
				}else if ((i+1)>9&&(i+1)<100) {
					out.print("0"+(i+1)+" ");
				}else {
					out.print((i+1)+" ");
				}
				for (int j = 0; j < Col_R; j++) {					
					try {
						if (MapCell_R[i][j]==null) {
							out.print(String.format("%3s","."));
						}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
							out.print(String.format("%3s","."));						
						}else {
							out.print(String.format("%3s",MapCell_R[i][j].length()>1?MapCell_R[i][j]:"0"+MapCell_R[i][j]));
						}	
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}					
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("[EXTENSION]"+"\r\n");
			out.print("\r\n");
			out.print("[EOF]"+"\r\n");
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
////		File MailReleaseDirectory=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
////		if (!MailReleaseDirectory.exists()) {
////			MailReleaseDirectory.mkdirs();
////		}
////		File MailReleaseFile=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
////		if (MailReleaseFile.exists()) {
////			MailReleaseFile.delete();
////		}
////		try {
////			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
}
