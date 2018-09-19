package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel50 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel50 model15=new TextReportModel50();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			TreeMap<Integer, Integer> binSummaryMap=parseRawdata.getBinSummary();
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			MapCell_R =TurnNighteenDegree.turnNegativeNighteen(MapCell_R, Row_R, Col_R);
			int tempNumber=Row_R;
			Row_R=Col_R;
			Col_R=tempNumber;
			
			String GrossDie_R=properties.get("Gross Die");
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
			out.print("Wafer ID:"+FinalID+"\r\n");
			for (int i = 0; i < Row_R; i++) {
				if ((i+1)<10) {
					out.print("00"+(i+1)+"        ");
				}else if ((i+1)>9&&(i+1)<100) {
					out.print("0"+(i+1)+"        ");
				}else {
					out.print((i+1)+"        ");
				}
				for (int j = 0; j < Col_R; j++) {
					out.print( MapCell_Modify13.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print(" TOTAL		:  "+properties.get("Gross Die")+"\r\n");
			out.print(" GOOD(-)	:  0		0.0%\r\n");
			Integer preDieSum=0;
			Integer NumGrossDie=Integer.valueOf(GrossDie_R);
			for (int i = 1; i < 36; i++) {
				Integer binSum=binSummaryMap.containsKey(i)?binSummaryMap.get(i):0;
				preDieSum+=binSum;
				String markInfor=MapCell_Modify13.Modify(String.valueOf(i));
				out.print(" BIN"+i+"("+markInfor+")	:  "+String.format("%-9s", binSum)+String.format("%.1f", ((double)binSum*100/NumGrossDie))+"%\r\n");
			}
			out.print(" BIN#(#)	:  "+String.format("%-9s", NumGrossDie-preDieSum)+String.format("%.1f", ((double)(NumGrossDie-preDieSum)*100/NumGrossDie))+"%\r\n");
			out.flush();
			out.close();
			FTP_Release_FAB_Mapping(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
