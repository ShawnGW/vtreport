package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel47 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel47 model15=new TextReportModel47();
		File[] Filelist=DataSorce.listFiles();
		String adLot=Lot;
		try {
		adLot=GetWSInformation.CallserviceForDoc(Lot,CP).split(":")[9];
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getHardBinTestDiesDimensionalArray();

			//Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Date date=null;
			try {
				 date=(new SimpleDateFormat("yyyyMMddHHmmss")).parse(TestStartTime_R);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String VERSION="NA";
			HashMap<String, String> NameMap=model15.InitMap(adLot, FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION);
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
			out.print("WAFER MAP CATEGORY DISPLAY\r\n");
			out.print("FILE   TYPE: WMAP\r\n");
			out.print("TEST   TYPE: "+CP+"(NORMAL)\r\n");
			out.print("PRODUCT  NO: "+Device+"\r\n");
			out.print("LOT      NO: "+adLot+"\r\n");
			out.print("TESTER   NO: "+properties.get("Tester ID")+"\r\n");
			out.print("TEST   DATE: "+(new SimpleDateFormat("yyyy/MM/dd")).format(date)+"\r\n");
			out.print("WAFER    NO: "+RightID_R+"\r\n");
			out.print("XMAX       : "+(properties.get("TestDieCol"))+"\r\n");
			out.print("YMAX       : "+(properties.get("TestDieRow"))+"\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					out.print( MapCell_Modify2.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
