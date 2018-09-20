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

public class TextReportModel51 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel51 model15=new TextReportModel51();
		File[] Filelist=DataSorce.listFiles();
		String priDevice=Device;
		Device=Device.substring(0, 10);
		Device=(CP.equals("CP1")?Device=Device+"WTH":(CP.equals("CP2")?Device+"WTH2":Device+"WTH3"));
		
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getHardBinTestDiesDimensionalArray();
			String PassDie_R=(properties.get("Pass Die"));	
			String grossDie=(properties.get("Gross Die"));	
			String yeild=properties.get("Wafer Yield");
			yeild=yeild.substring(0, yeild.length()-1);
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
			String[] passDieArray=properties.get("Pass Bins").split(",");
			ArrayList<String> passList=new ArrayList<>();
			for (int i = 0; i < passDieArray.length; i++) {
				passList.add(passDieArray[i]);
			}
			
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
			File Result_Text=new File(reportBath+CustomerCode+"/"+priDevice+"/"+Lot+"/"+CP+"/"+FinalName);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			MapCell_R =TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);
			int tempNumber=Row_R;
			Row_R=Col_R;
			Col_R=tempNumber;
			
			out.print("Customer:FM1\r\n");
			out.print("Device:"+Device+"\r\n");
			out.print("Cust Lot No:"+Lot+"\r\n");
			out.print("Flat Orientation:DOWN\r\n");
			out.print("Wafer Num:"+RightID_R+"\r\n");
			out.print("Good bin code:1\r\n");
			out.print("Bad bin code:X\r\n");
			out.print("TOTAL DIE\r\n");
			out.print("TESTED    GOOD DIE    YIELD\r\n");
			out.print(String.format("%-10s", grossDie)+String.format("%-12s", PassDie_R)+yeild+"\r\n");
			out.print("MAPSTART\r\n");
			for (int j =0 ; j < Col_R+4; j++)
			{
				out.print(".");
			}
			out.print("\r\n");
			for (int j =0 ; j < Col_R+4; j++)
			{
				out.print(".");
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				out.print("..");
				for (int j =0 ; j < Col_R; j++) {
					out.print(MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("..");
				out.print("\r\n");
			}
			for (int i = 0; i < 4; i++) {
				for (int j =0 ; j < Col_R+4; j++)
				{
					out.print(".");
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
