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

public class TextReportModel55 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel55 model15=new TextReportModel55();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			String[] passDieArray=properties.get("Pass Bins").split(",");
			ArrayList<String> passList=new ArrayList<>();
			for (int i = 0; i < passDieArray.length; i++) {
				passList.add(passDieArray[i]);
			}
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

//			MapCell_R=TurnNighteenDegree.Turn(MapCell_R, Row_R, Col_R);
//			MapCell_R=TurnNighteenDegree.Turn(MapCell_R, Col_R,Row_R);
//			
			MapCell_R=TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);
			
			Integer temp=Row_R;
			Row_R=Col_R;
			Col_R=temp;
			
			
			
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
			out.print(CustomerCode+"\r\n");
			out.print(Device+"\r\n");
			out.print(Lot+"-"+FinalID+"\r\n");
			out.print(Lot+FinalID+".CSM\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j =0 ; j < Col_R; j++) {
					out.print(MapCell_Modify5.Modify3(MapCell_R[i][j],passList));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
			//Fab_Mapping
		}
	}
}
