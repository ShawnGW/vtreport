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

public class TextReportModel29ZSW extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel29ZSW model15=new TextReportModel29ZSW();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			String Flat_R="DOWN";
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();		
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
			out.print("Product name = "+Device+"\r\n");
			out.print("Lot     name = "+Lot+"\r\n");
			out.print("Wafer-ID     = "+FinalID+"\r\n");
			out.print("X max coor.  = "+Col_R+"\r\n");
			out.print("Y max coor.  = "+Row_R+"\r\n");
			out.print("Flat         = "+Flat_R.toUpperCase()+"\r\n");
			out.print("\r\n");
//			out.print("[ Wafer Bin Summary ]\r\n");
			Set<Integer> SumSet=new TreeSet<>(Bin_Summary_R.keySet());
			for (Integer integer : SumSet) {
				out.print("BIN");
				out.print(String.format("%3s", integer));
				out.print("  : ");
				out.print(Bin_Summary_R.get(integer)+"\r\n");
			}
			out.print("PASS  Die:"+PassDie_R+"\r\n");
			out.print("Fail  Die:"+FailDie_R+"\r\n");
			out.print("ToTel Die:"+GrossDie_R+"\r\n");
			for (int i = 0; i < Row_R+3; i++) {
				for (int j = 0; j < Col_R; j++) {
				try {
					out.print(MapCell_Modify8.Modify(MapCell_R[i][j]));		
				} catch (Exception e) {
					// TODO: handle exception
					out.print(".");		
				}
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
