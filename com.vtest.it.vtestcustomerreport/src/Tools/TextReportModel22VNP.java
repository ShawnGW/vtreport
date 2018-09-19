package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel22VNP extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel22VNP model15=new TextReportModel22VNP();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");


			Integer rows=Integer.valueOf(properties.get("TestDieRow"));
			Integer cols=Integer.valueOf(properties.get("TestDieCol"));
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
			out.print("Lot ID:"+Lot+"\r\n");
			out.print("Wafer ID:"+Wafer_ID_R+"\r\n");
			out.print("Product ID:"+Device+"\r\n");
			out.print("Customer Code:"+"\r\n");
			out.print("Mapping file:"+Lot+"-"+Wafer_ID_R+"\r\n");
			out.print("Notch side:Down\r\n");
			out.print("\r\n");
			out.print("\r\n");
			String[][] A2=TurnNighteenDegree.Turn(MapCell_R, rows, cols);
			for (int i =0; i <cols  ; i++) {
				for (int j =0 ; j <rows ; j++) {
					out.print( MapCell_Modify2.Modify(A2[i][j]));
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("Good  die(bin 1)    "+PassDie_R+"EA\r\n");
			out.print("X - Bad die\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
