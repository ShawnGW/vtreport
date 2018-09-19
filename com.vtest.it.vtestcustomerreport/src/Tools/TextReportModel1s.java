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

public class TextReportModel1s extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName) throws IOException {
		// TODO Auto-generated method stub
		TextReportModel1 model1=new TextReportModel1();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
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

			String FinalID=RightID_R.toString();
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			String Wafer_Unload_Time_R=properties.get("Test End Time");
			String Device_R=properties.get("Device Name");
			String Lot_R=properties.get("Lot ID");
			Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));
			TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();

			if (Bin_Summary_Map_R.containsKey(0)) {
				Integer Bin0=Bin_Summary_Map_R.get(0);
				Bin_Summary_Map_R.remove(0);
				if (Bin_Summary_Map_R.containsKey(5)) {
					Bin0+=Bin_Summary_Map_R.get(5);
				}
				Bin_Summary_Map_R.put(5, Bin0);
				PassDie_R=PassDie_R-Bin0;
				Total_Fail_Die_R=Total_Fail_Die_R+Bin0;
			}
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			String VERSION="NA";
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
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
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print(MapCell_Modify6.Modify(MapCell_R[i][j]));
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("[ Product Information ]"+"\r\n");
			out.print("\r\n");
			out.print("Product name = "+Device_R+"\r\n");
			out.print("Lot     name = "+Lot_R+"\r\n");
			out.print("Wafer-ID     = "+Wafer_ID_R+"\r\n");
			out.print("WF Start time= "+Wafer_Load_Time_R+"\r\n");
			out.print("WF End   time= "+Wafer_Unload_Time_R+"\r\n");
			out.print("X max coor.  = "+Row_R+"\r\n");
			out.print("Y max coor.  = "+Col_R+"\r\n");
			out.print("Flat         = "+Flat_R+"\r\n");
			out.print("\r\n");
			out.print("[ Wafer Bin Summary ]"+"\r\n");
			out.print("\r\n");	
			Set<Integer> KeySet=Bin_Summary_Map_R.keySet();
			for (Integer key : KeySet) {
				if (key>9) {
					out.print("bin  ");
					out.printf("%5s", (key)+"("+(char)(key+55)+")\t");
					out.printf("%6s", Bin_Summary_Map_R.get(key));
					float persent=((float)Bin_Summary_Map_R.get(key)/(Total_Fail_Die_R+PassDie_R))*100;
					out.printf("%6.2f", persent);
					out.print("%");
					out.print("\r\n");
				}else
				{
					out.print("bin  ");
					out.printf("%5s", (key)+"\t" );
					out.printf("%6s", Bin_Summary_Map_R.get(key));
					float persent=((float)Bin_Summary_Map_R.get(key)/(Total_Fail_Die_R+PassDie_R))*100;
					out.printf("%6.2f", persent);
					out.print("%");
					out.print("\r\n");
				}
			}
			out.print("pass die :"+PassDie_R+"\r\n");
			out.print("fail_die :"+Total_Fail_Die_R+"\r\n");
			out.print("total die:"+(Total_Fail_Die_R+PassDie_R)+"\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
