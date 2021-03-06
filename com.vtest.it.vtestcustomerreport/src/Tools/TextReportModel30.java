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

public class TextReportModel30 extends FTPRealseModel implements Text_Report {

	private static final Integer[] Bin_Array={1, 21, 22, 23, 24, 30, 31, 40, 41, 42, 43, 44, 45, 46, 50, 60, 61, 62};
	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel30 model1=new TextReportModel30();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {				
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			String Nocth=properties.get("Notch");
			if (Nocth.equals("0-Degree")) {
				Nocth="UP";
			}else if (Nocth.equals("90-Degree")) {
				Nocth="RIGHT";
			}else if (Nocth.equals("180-Degree")) {
				Nocth="DOWN";
			}else {
				Nocth="LEFT";
			}
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			Integer gross_die=Integer.valueOf(properties.get("Gross Die"));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			String Program=properties.get("Tester Program");
			String Tester_R=properties.get("Tester ID");
			String ProberCard_R=properties.get("Prober Card ID");
			
			String TestEndTime_R=properties.get("Test End Time");
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");

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
			String[] Bin_Defination=new String[64];
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
			
			out.print("[BOF]"+"\r\n");
			out.print("PRODUCT ID      : "+Device+"\r\n");
			out.print("LOT ID          : "+Lot+"\r\n");
			out.print("WAFER ID        : "+FinalID+"\r\n");
			out.print("FLOW ID         : "+CP+"\r\n");
			out.print("START TIME      : "+ModifyTime.Modify2(TestStartTime_R)+"\r\n");
			out.print("STOP TIME       : "+ModifyTime.Modify2(TestEndTime_R)+"\r\n");
			out.print("SUBCON          : VT"+"\r\n");
			out.print("TESTER NAME     : "+Tester_R+"\r\n");
			out.print("TEST PROGRAM    : "+Program+"\r\n");
			out.print("LOAD BOARD ID   : "+"\r\n");
			out.print("PROBE CARD ID   : "+ProberCard_R+"\r\n");
			out.print("SITE NUM        : "+"\r\n");
			out.print("DUT ID          : "+"\r\n");
			out.print("DUT DIFF NUM    : "+"\r\n");
			out.print("OPERATOR ID     : "+OPerater_R+"\r\n");
			out.print("TESTED DIE      : "+gross_die+"\r\n");
			out.print("PASS DIE        : "+PassDie_R+"\r\n");
			out.print("YIELD           : "+Yeild_R+"\r\n");
			out.print("SOURCE NOTCH    : "+Nocth+"\r\n");
			out.print("MAP ROW         : "+Row_R+"\r\n");
			out.print("MAP COLUMN      : "+Col_R+"\r\n");
			out.print("MAP BIN LENGTH  : 2"+"\r\n");
			out.print("SHIP            : YES"+"\r\n");
			out.print(""+"\r\n");
			out.print("[SOFT BIN]"+"\r\n");
			out.print("        BINNAME, DIENUM,  YIELD, DESCRIPTION"+"\r\n");
			for (int i = 0; i < Bin_Array.length; i++) {
				Integer value=0;
				if (Bin_Summary_R.containsKey(Bin_Array[i])) {
					value=Bin_Summary_R.get(Bin_Array[i]);
				}
				double yeild=(double)value*100/gross_die;
				out.printf("%7S", "Bin");
				out.print(",");
				out.printf("%7S", Bin_Array[i]);
				out.print(",");
				out.printf("%7S", value);
				out.print(",");
				out.printf("%7S", String.format("%.2f",yeild)+"%");
				out.print(",");
				out.printf(" {["+Bin_Defination[Bin_Array[i]]+"]}");
				out.print("\r\n");
			}
			out.print(""+"\r\n");
			out.print("[SOFT BIN MAP]"+"\r\n");
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
			out.print("\r\n");
			for (int i =0 ; i < Row_R; i++) {
				if (i<10) {
					out.print("00"+i+" ");
				}else if (i>9&&i<100) {
					out.print("0"+i+" ");
				}else {
					out.print(i+" ");
				}
				for (int j = 0; j < Col_R; j++) {					
					if (MapCell_R[i][j]==null) {
						out.print(String.format("%3s"," "));
					}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
						out.print(String.format("%3s"," "));						
					}else {
						out.print(String.format("%3s",MapCell_R[i][j]));
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
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
