package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel44 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel44 model15=new TextReportModel44();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getHardBinTestDiesDimensionalArray();
			String Flat_R=null;
			String notch=properties.get("Notch");
			if (notch.equals("0-Degree")) {
				Flat_R="0(UP)";
			}else if (notch.equals("90-Degree")) {
				Flat_R="90(RIGHT)";
			}else if (notch.equals("180-Degree")) {
				Flat_R="180(DOWN)";
			}else {
				Flat_R="270(LEFT)";
			}
			String PassDie_R=(properties.get("Pass Die"));	
			String grossDie=(properties.get("Gross Die"));	
			String yeild=properties.get("Wafer Yield");
			yeild=yeild.substring(0, yeild.length()-1);
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

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
			out.print("Customer :"+CustomerCode+"\r\n");
			out.print("Device :"+Device+"\r\n");
			out.print("Cust Lot No :"+Lot+"\r\n");
			out.print("Flat Orientation :"+Flat_R+"\r\n");
			out.print("Wafer Num :"+RightID_R+"\r\n");
			out.print("Good bin code: 1\r\n");
			out.print("Bad bin code:X\r\n");
			out.print("TOTAL DIE\r\n");
			out.print("TESTED      GOOD DIE    YIELD       REMARK\r\n");
			out.print("-------------   ------------    ---------   ----------\r\n");
			out.print(String.format("%8s", grossDie)+String.format("%8s", PassDie_R)+String.format("%9s", yeild)+"\r\n");
			out.print("MAPSTART\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j =0 ; j < Col_R; j++) {
					out.print(MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
