package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel41 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel41 model15=new TextReportModel41();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
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

			String GrossDie_R=properties.get("Gross Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
			Integer TestDie_MaxX_R=Integer.valueOf(properties.get("TestDieright"));
			Integer TestDie_MaxY_R=Integer.valueOf(properties.get("TestDiedown"));
			String OPerater_R=properties.get("Operator");
			String Yeild_R=properties.get("Wafer Yield");
			String TestEndTime_R=properties.get("Test End Time");
			String Tester_R=properties.get("Tester ID");
			String ProberCard_R=properties.get("Prober Card ID");
			String cpprocess=properties.get("CP Process");
			
			//Integer SlotID_R=Integer.valueOf(properties.get("Slot"));
			String Program_R=properties.get("Tester Program");
			String MapCols_R=properties.get("Map Cols");
			String MapRows_R=properties.get("Map Rows");
	
			Integer MinX_R=Integer.valueOf(properties.get("left"));
			Integer MinY_R=Integer.valueOf(properties.get("up"));
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
			out.print("Prod ID:"+Device+"\r\n");
			out.print("Test Program:"+Program_R+"\r\n");
			out.print("Tester ID:"+Tester_R+"\r\n");
			out.print("Operator ID:"+OPerater_R+"\r\n");
			out.print("Start Time:"+TestStartTime_R+"\r\n");
			out.print("End Time:"+TestEndTime_R+"\r\n");
			out.print("Notch Side:"+Notch_R.substring(0, 1).toUpperCase()+"\r\n");
			out.print("Sort ID:"+cpprocess.substring(0, 1)+"\r\n");
			out.print("Bin Definition File:8725B-1-1\r\n");
			out.print("Grid Xmax:"+(MinX_R+Integer.valueOf(MapCols_R))+"\r\n");
			out.print("Grid Ymax:"+(MinY_R+Integer.valueOf(MapRows_R))+"\r\n");
			out.print("Fab Site:\r\n");
			out.print("Test Site:V-TEST\r\n");
			out.print("Probe Card ID:"+ProberCard_R+"\r\n");
			out.print("Load Board ID:\r\n");
			out.print("ROM Code:  25.0 C\r\n");
			out.print("X Die Size:\r\n");
			out.print("Y Die Size:\r\n");
			out.print("X Street:\r\n");
			out.print("Y street:\r\n");
			out.print("Customer Code:NA\r\n");
			out.print("Customer PartID:\r\n");
			out.print("RawData"+"\r\n");
			for (int i = TestDie_MinY_R; i < TestDie_MaxY_R+1; i++) {
				for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++) {
					if (MapCell_R[i][j]!=null) {
						out.print(String.format("%5s", j)+String.format("%6s", i)+String.format("%6s", MapCell_R[i][j])+"\r\n");
					}
				}
			}
			out.print("DataEnd\r\n");
			out.print("TEST_DIE: "+GrossDie_R+"\r\n");
			out.print("PASS_CNT: "+PassDie_R+"\r\n");
			out.print("YIELD: "+Yeild_R+"\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
