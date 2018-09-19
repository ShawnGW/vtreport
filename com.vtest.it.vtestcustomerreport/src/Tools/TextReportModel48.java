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

public class TextReportModel48 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		ParseXinTang parseXinTang=new ParseXinTang();
		TextReportModel48 model15=new TextReportModel48();
		String adLot=Lot;
		try {
		adLot=GetWSInformation.CallserviceForDoc(Lot,CP).split(":")[9];
		} catch (Exception e) {
			// TODO: handle exception
		}
		File summaryFile=new File(reportBath+"/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+adLot+"-L1.WIN");
		PrintWriter printWriter=new PrintWriter(summaryFile);
		File[] Filelist=DataSorce.listFiles();
		printWriter.print("Nuvoton\r\n");
		printWriter.print(Device+"\r\n");
		printWriter.print(Device+"\r\n");
		printWriter.print(adLot+"\r\n");
		printWriter.print(Filelist.length+"\r\n");
		printWriter.print("       MAPPING          WAFER_ID  BIN A\r\n");
		TreeMap<String, String> infoSummary=new TreeMap<>();
		HashMap<String, String> binSummary=new HashMap<>();
		Integer sumA=0;
		
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
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
//			String PassDie_R=(properties.get("Pass Die"));	
//			String grossDie=(properties.get("Gross Die"));	
			String yeild=properties.get("Wafer Yield");
			yeild=yeild.substring(0, yeild.length()-1);
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Integer grossDie=Integer.valueOf(properties.get("Gross Die"));
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
			String[][] resultMapping=parseXinTang.parse(adLot+"_"+FinalID, Row_R, Col_R);		
			String[][] finMapping=new String[Row_R][Col_R];
			Integer numberA=MergeXNT.merge(resultMapping, MapCell_R, finMapping, Row_R, Col_R);
			Integer numberX=grossDie-numberA;
			binSummary.put(Wafer_ID_R, "|Bin1:"+numberA+"|Bin99:"+numberX);
			sumA+=numberA;
			infoSummary.put(Wafer_ID_R,  adLot+"."+FinalID+".WIN"+String.format("%17s", adLot+"."+FinalID)+String.format("%8s", numberA));
			out.print("Nuvoton\r\n");
			out.print("Flat :"+Flat_R+"\r\n");
			out.print(adLot+"\r\n");
			out.print(adLot+"."+FinalID+"\r\n");
			out.print(adLot+"."+FinalID+".WIN\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j =0 ; j < Col_R; j++) {
					if (finMapping[i][j]!=null) {
						out.print(finMapping[i][j]);
					}else {
						out.print(".");
					}		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release_Merge(CustomerCode, Device, Lot, CP, Result_Text);
		}
		Set<String> inforSet=infoSummary.keySet();
		try {
			BinInforWriteToMes binInforWriteToMes=new BinInforWriteToMes();
			Set<String> waferidSet=binSummary.keySet();
			for (String waferid : waferidSet) {
				binInforWriteToMes.Write(Lot, waferid, binSummary.get(waferid), "CP9");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for (String waferId : inforSet) {
			printWriter.print(infoSummary.get(waferId)+"\r\n");
		}
		printWriter.print("                           TOTAL"+String.format("%8s", sumA));
		printWriter.flush();
		printWriter.close();
		FTP_Release(CustomerCode, Device, Lot, CP, summaryFile);
	}
}
