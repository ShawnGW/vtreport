package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel20 extends FTPRealseModel  implements Text_Report{
/**
 * @author shawn.sun
 * @category IT
 * @since 2017.9.21
 * @serial 1.0
 */
	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel20 model17=new TextReportModel20();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			String Notch_R=properties.get("Notch");
			if (Notch_R.equals("0-Degree")) {
				Notch_R="UP";
			}else if (Notch_R.equals("90-Degree")) {
				Notch_R="RIGHT";
			}else if (Notch_R.equals("180-Degree")) {
				Notch_R="DOWN";
			}else {
				Notch_R="LEFT";
			}
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			

			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}

			String VERSION="NA";
			HashMap<String, String> NameMap=model17.InitMap(Lot.split("\\.")[0], FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION);
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
			out.print("LotID:"+Lot.split("\\.")[0]+"\r\n");
			out.print("WaferID:"+FinalID+"\r\n");
			if (Device.equals("MT304")) {
				out.print("ProductID:G5310"+"\r\n");
			}else if (Device.equals("MZ306")){
				out.print("ProductID:G5474"+"\r\n");
			}else {
				out.print("ProductID:"+Device+"\r\n");
			}
			out.print("Mappingfile:"+Lot.split("\\.")[0]+"-"+FinalID+"_"+CP+".txt"+"\r\n");
			out.print("NotchSide:"+Notch_R.substring(0, 1).toUpperCase()+Notch_R.substring(1, Notch_R.length()).toLowerCase()+"\r\n");
			for (int i = 1; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print(MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("1 - Good die "+PassDie_R+" EA\r\n");
			out.print("X - Bad die "+FailDie_R+" EA\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
