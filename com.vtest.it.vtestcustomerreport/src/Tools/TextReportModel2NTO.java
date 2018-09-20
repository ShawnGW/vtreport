package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel2NTO extends FTPRealseModel  implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		TextReportModel2NTO model2=new TextReportModel2NTO();
		// TODO Auto-generated method stub
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
			Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));

			String Program=properties.get("Tester Program");
			String Yeild=properties.get("Wafer Yield");

			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			String VERSION="NA";
			HashMap<String, String> NameMap=model2.InitMap(Lot, FinalID.substring(0, 2), CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
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
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat formatFormer=new SimpleDateFormat("yyyyMMddhhmmss");
			out.print("DEVICE_NAME="+Device+"\r\n");
			out.print("PROGRAMME NAME="+(Program.endsWith("()")?Program.substring(0, Program.length()-2):Program)+"\r\n");
			out.print("ALIGN MARK=(110,120)\r\n");
			out.print("WAFER_ID="+Wafer_ID_R+"\r\n");
			try {
				out.print("Start Test Time:"+(format.format(formatFormer.parse(Wafer_Load_Time_R)))+"\r\n");
				out.print("End   Test Time:"+(format.format(formatFormer.parse(Wafer_Unload_Time_R)))+"\r\n");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("Wafer Flat="+Flat_R+"\r\n");
			out.print("ROW=133\r\n");
			out.print("COL="+Col_R+"\r\n");
			out.print("TOTAL_TEST="+(Total_Fail_Die_R+PassDie_R)+"\r\n");
			out.print("TOTAL_PASS="+PassDie_R+"\r\n");
			out.print("TOTAL_FAIL="+(Total_Fail_Die_R)+"\r\n");
			out.print("TEST_YIELD="+Yeild+"\r\n");
			out.print("\r\n");
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					if (MapCell_R[i][j]==null) {
						out.print(" ");
					}else
					{
						if (MapCell_R[i][j].equals("M")) {
							out.print(" ");
						}else if (MapCell_R[i][j].equals("S")) {
							out.print(" ");
						}else {
							if (MapCell_R[i][j].equals("1")) {
								out.print("1");
							}else {
								out.print("x");
							}
						}
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
