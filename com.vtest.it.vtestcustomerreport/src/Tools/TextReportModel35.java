package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel35 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel35 model15=new TextReportModel35();
		File[] Filelist=FileListOrder.GetList(DataSorce.listFiles());
		PrintWriter printWriter=new PrintWriter(new File("/TestReport/CustReport/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"_summary.sum"));
		printWriter.print("CPSite:     VTEST\r\n");
		printWriter.print("Product:    "+Device+"\r\n");
		printWriter.print("LotID:      "+Lot+"\r\n");
		printWriter.print("WaferQty:   "+Filelist.length+"\r\n");
		printWriter.print("\r\n");
		printWriter.print("MAPPING             WAFER_ID    BIN 1     BIN X\r\n");
		Integer passDieSum=0;
		Integer failDieSum=0;
		ArrayList<String> sumArray=new ArrayList<>();
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
			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
			Integer TestDie_MaxX_R=Integer.valueOf(properties.get("TestDieright"));
			Integer TestDie_MaxY_R=Integer.valueOf(properties.get("TestDiedown"));

			passDieSum+=PassDie_R;
			failDieSum+=Integer.valueOf(FailDie_R);
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			sumArray.add(String.format("%-20s", Wafer_ID_R+".txt")+String.format("%-12s", (Lot+"-"+FinalID))+String.format("%-10s", PassDie_R)+FailDie_R);
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
			out.print("CPSite  :       VTEST\r\n");
			out.print("Product:    "+Device+"\r\n");
			out.print("LotID   :       "+Lot+"\r\n");
			out.print("WaferID:        "+Wafer_ID_R+"\r\n");
			if (Flat_R.toUpperCase().equals("LEFT")) {
				out.print("FlatNotch:      270 Left\r\n");
			}else if (Flat_R.toUpperCase().equals("RIGHT")) {
				out.print("FlatNotch:      90 Right\r\n");
			}else if (Flat_R.toUpperCase().equals("UP")) {
				out.print("FlatNotch:      0 Up\r\n");
			}else if (Flat_R.toUpperCase().equals("DOWN")) {
				out.print("FlatNotch:      180 Bottom\r\n");
			}
			out.print("TotalDies:      "+GrossDie_R+"\r\n");
			out.print("PassDies:       "+PassDie_R+"\r\n");
			for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++)
			{
				out.print(".");
			}
			out.print("\r\n");
			for (int i = TestDie_MinY_R; i < TestDie_MaxY_R+1; i++) {
				for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++) {
					out.print(MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++)
			{
				out.print(".");
			}
			out.print("\r\n");
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
		for (String binSum : sumArray) {
			printWriter.print(binSum+"\r\n");
		}
		printWriter.print("\r\n");
		printWriter.print(String.format("%29s", "TOTAL")+"  "+passDieSum+"     "+failDieSum+"\r\n");
		printWriter.flush();
		printWriter.close();
	}
}
