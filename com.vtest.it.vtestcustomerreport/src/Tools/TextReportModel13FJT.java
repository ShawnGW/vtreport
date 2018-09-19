package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel13FJT extends FTPRealseModel implements Text_Report {
	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel13FJT model13=new TextReportModel13FJT();
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
			String Wafer_Load_Time_R=properties.get("Test End Time");
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String waferSize_R=properties.get("WF_Size");
			
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String Yeild_R=properties.get("Wafer Yield");
			String FinalID=RightID_R.toString();

			String VERSION="NA";
			HashMap<String, String> NameMap=model13.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
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
			out.print("Device Name       : "+Device+"\r\n");
			out.print("Lot No.           : "+Lot+"\r\n");
			out.print("Wafer ID          : "+Wafer_ID_R+"\r\n");
			out.print("Wafer Size        : "+waferSize_R+"\r\n");
			out.print("Flat/Notch        : "+Flat_R.substring(0, 1)+Flat_R.substring(1, Flat_R.length()).toLowerCase()+"\r\n");
			out.print("Map Column        : "+Col_R+"\r\n");
			out.print("Map Row           : "+Row_R+"\r\n");
			out.print("Total Tested      : "+GrossDie_R+"\r\n");
			out.print("Total Pass        : "+PassDie_R+"\r\n");
			out.print("Total Fail        : "+FailDie_R+"\r\n");
			out.print("Yield             : "+Yeild_R+"\r\n");
			for (int j = 0; j < Col_R; j++)
			{
				out.print(".");
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					if (MapCell_R[i][j]==null) {
						out.print(".");	
					}else {
						if (MapCell_R[i][j].equals("1")||MapCell_R[i][j].equals("10"))
							out.print("1");
						else 
							out.print("X");
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
