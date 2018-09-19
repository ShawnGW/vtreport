package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel46 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel46 model15=new TextReportModel46();
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
			String[] passDieArray=properties.get("Pass Bins").split(",");
			ArrayList<String> passList=new ArrayList<>();
			for (int i = 0; i < passDieArray.length; i++) {
				passList.add(passDieArray[i]);
			}
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			TreeMap<Integer, Integer> binSummary=parseRawdata.getBinSummary();
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
			out.print("SUMMARY REPORT\r\n");
			out.print("FILE   TYPE: BINDATA\r\n");
			out.print("TEST   TYPE: "+CP+"(NORMAL)\r\n");
			out.print("PRODUCT  NO: "+Device+"\r\n");
			out.print("LOT      NO: "+adLot+"\r\n");
			out.print("TESTER   NO: "+properties.get("Tester ID")+"\r\n");
			out.print("TEST   PRGM:\r\n");
			out.print("TEST   DATE: "+(new SimpleDateFormat("yyyy/MM/dd")).format(date)+"\r\n");
			out.print("TEST   TIME: "+(new SimpleDateFormat("hh/MM/ss")).format(date)+"\r\n");
			out.print("EA       NO: "+properties.get("Operator")+"\r\n");
			out.print("PROCARD  NO:\r\n");
			out.print("LOAD  BOARD:\r\n");
			out.print("GROSSDIES  : "+properties.get("Gross Die")+"\r\n");
			out.print("WAFER    NO: "+RightID_R+"\r\n");
			out.print("PASS       : "+properties.get("Pass Die")+"\r\n");
			out.print("YIELD      : "+properties.get("Wafer Yield").substring(0, 5)+"\r\n");
			out.print("BIN SUMMARY:\r\n");
			for (int i = 1; i < 51; i++) {
				String charValue=String.valueOf(i);
				if (charValue.length()<2) {
					charValue="0"+i;
				}
				out.print("CAT"+charValue+":"+String.format("%6s", binSummary.containsKey(i)?binSummary.get(i):0)+"\r\n");
			}
			out.flush();
			out.close();
			FTP_Release_CP(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
