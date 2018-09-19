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

public class TextReportModel9TurnN90 extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {					
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String waferid=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			String Flat_R=null;
			String notch=properties.get("Notch");
			if (notch.equals("0-Degree")) {
				Flat_R="Up";
			}else if (notch.equals("90-Degree")) {
				Flat_R="Right";
			}else if (notch.equals("180-Degree")) {
				Flat_R="Down";
			}else {
				Flat_R="Left";
			}
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));
			
			MapCell_R=TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);		
			Integer temp=Row_R;
			Row_R=Col_R;
			Col_R=temp;
			
			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");
			String TestEndTime_R=properties.get("Test End Time");
			Integer gross_die=Integer.parseInt(properties.get("Gross Die"));
			String waferSize_R=properties.get("WF_Size");
			String slotId=properties.get("Slot");
			TextReportModel9TurnN90 model1=new TextReportModel9TurnN90();
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
			out.print("    ");
			for(int i = 1;i<Col_R;i++)
			{
				if (i<10) {
					out.print(" 0"+i);
				}else
				{
					out.print(" "+i);
				}
				
			}
			out.print("\r\n");
			out.print("   ");
			for(int i = 1;i<Col_R;i++)
			{
				out.print("++-");
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				if (i<10) {
					out.print("00"+i+"|");
				}else if (i>9&&i<100) {
					out.print("0"+i+"|");
				}else {
					out.print(i+"|");
				}
				for (int j = 0; j < Col_R; j++) {
					if (MapCell_R[i][j]==null) {
						out.print(String.format("%3s"," "));
					}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
						out.print(String.format("%3s"," "));
					}else {
						if (Integer.valueOf(MapCell_R[i][j])>9) {
							out.print(String.format("%3s", MapCell_R[i][j]));
						}else {
							out.print(String.format("%3s", "0"+MapCell_R[i][j]));
						}
					}
				}
				out.print("\r\n");
			}
			
			out.print("============ Wafer Information () ==========="+"\r\n");
			out.print("  Device: "+Device+"\r\n");
			out.print("  Lot NO: "+Lot+"\r\n");
			out.print("  Slot No: "+(slotId.length()==1?"0"+slotId:slotId)+"\r\n");
			out.print("  Wafer ID: "+waferid+"\r\n");
			out.print("  Operater: "+OPerater_R+"\r\n");
			out.print("  Wafer Size: "+waferSize_R+" Inch"+"\r\n");
			out.print("  Flat Dir: "+Flat_R+"\r\n");
//			if (Flat_R.equals("LEFT")) {
//				out.print("  Flat Dir: "+270+"-Degree"+"\r\n");
//			}
//			if (Flat_R.equals("RIGHT")) {
//				out.print("  Flat Dir: "+90+"-Degree"+"\r\n");
//			}
//			if (Flat_R.equals("UP")) {
//				out.print("  Flat Dir: "+0+"-Degree"+"\r\n");
//			}
//			if (Flat_R.equals("DOWN")) {
//				out.print("  Flat Dir: "+180+"-Degree"+"\r\n");
//			}
			out.print("  Wafer Test Start Time: "+TestStartTime_R+"\r\n");
			out.print("  Wafer Test Finish Time: "+TestEndTime_R+"\r\n");
			out.print("  Wafer Load Time: "+TestStartTime_R+"\r\n");
			out.print("  Wafer Unload Time: "+TestEndTime_R+"\r\n");
			out.print("  Total test die: "+gross_die+"\r\n");
			out.print("  Pass Die: "+PassDie_R+"\r\n");
			out.print("  Fail Die: "+FailDie_R+"\r\n");
			out.print("  Yield: "+Yeild_R+"\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("             Bin (0~63) Data Deatil Summary"+"\r\n");
			out.print("======================================================================="+"\r\n");
		 
			String Bin_Sum="";
			String Bin_yield_percent="";
			for (int i = 0; i < 64; i++) {
				String Every_Bininfor="";
				Integer Sum=0;
				if (Bin_Summary_R.containsKey(i+1)) {
					Sum=Bin_Summary_R.get(i+1);
				}
				for(int j=0;j<5-(""+Sum).length();j++)
				{
					Every_Bininfor+="0";
				}
				Bin_Sum+=Every_Bininfor+Sum+"  | ";
				String percent=String.format("%.2f", ((double)Sum*100/gross_die));
				if (percent.length()!=5) {		
					percent="0"+percent;
				}
				Bin_yield_percent+= percent+"% | ";
				
				if ((i+1)>9) {
					out.print("Bin "+(i+1)+" | ");
				}else
				{
					out.print("Bin "+(i+1)+"  | ");
				}
				if ((i+1)%8==0) {
			
					out.print("\r\n");		
					out.print(Bin_Sum);
					Bin_Sum="";	
					out.print("\r\n");
					out.print(Bin_yield_percent);
					Bin_yield_percent="";
				}			
				if ((i+1)%8==0) {
					out.print("\r\n");
					out.print("======================================================================="+"\r\n");
				}
			}	
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}

	}
}
