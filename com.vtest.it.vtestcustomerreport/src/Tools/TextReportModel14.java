package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel14 extends FTPRealseModel  implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel14 model14=new TextReportModel14();
		File[] Filelist=DataSorce.listFiles();
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
			String waferSize_R=properties.get("WF_Size");
			
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String Yeild_R=properties.get("Wafer Yield");
			String FinalID=RightID_R.toString();

			String OPerater_R=properties.get("Operator");
			Integer SlotID_R=Integer.valueOf(properties.get("Slot"));
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String TestStartTime_R=properties.get("Test Start Time");
			String TestEndTime_R=properties.get("Test End Time");

			String VERSION="NA";
			HashMap<String, String> NameMap=model14.InitMap(Lot, FinalID, CP, TestStartTime_R, Device, Wafer_ID_R, VERSION,String.valueOf(SlotID_R));
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
			out.print(String.format("%3s", " "));
			for (int i = 0; i < Col_R; i++)
			{
				if (i+1<10) {
					out.print(String.format("%3s", "0"+(i+1)));
				}else {
					if (i==Col_R-1) {
						out.print(String.format("%3s", (i+1))+"\r\n");
					}else {
						out.print(String.format("%3s", (i+1)));
					}
				}
			}
			out.print(String.format("%3s", "+"));
			for (int i = 0; i < Col_R; i++)
			{		
					out.print(String.format("%3s", "+-+"));				
			}
			out.print("\r\n");
			for (int i = 0; i < Row_R; i++) {
				if (i+1<10) {
					out.print("00"+(i+1)+"|");
				}else if (i+1<100) {
					out.print("0"+(i+1)+"|");
				}else {
					out.print((i+1)+"|");
				}
				for (int j = 0; j < Col_R; j++) {
					out.print(String.format("%3s", MapCell_Modify3.Modify(MapCell_R[i][j])));		
				}
				out.print("\r\n");
			}
			out.print("\r\n");
			out.print("\r\n");
			
			out.print("============ Wafer Information () ==========="+"\r\n");
			out.print("  Device: "+Device+"\r\n");
			out.print("  Lot NO: "+Lot+"\r\n");
			out.print("  Slot No: "+SlotID_R+"\r\n");
			out.print("  Wafer ID: "+Wafer_ID_R+"\r\n");
			out.print("  Operater: "+OPerater_R+"\r\n");
			out.print("  Wafer Size: "+waferSize_R+" Inch"+"\r\n");
			if (Flat_R.toUpperCase().equals("UP")) {
				out.print("  Flat Dir: 0 Degree\r\n");
			}else if(Flat_R.toUpperCase().equals("RIGHT")) {
				out.print("  Flat Dir: 90 Degree\r\n");
			}else if(Flat_R.toUpperCase().equals("DOWN")){
				out.print("  Flat Dir: 180 Degree\r\n");
			}else if(Flat_R.toUpperCase().equals("LEFT")){
				out.print("  Flat Dir: 270 Degree\r\n");
			}
			out.print("  Wafer Test Start Time: "+TestStartTime_R+"\r\n");
			out.print("  Wafer Test Finish Time: "+TestEndTime_R+"\r\n");
			out.print("  Wafer Load Time: "+TestStartTime_R+"\r\n");
			out.print("  Wafer Unload Time: "+TestEndTime_R+"\r\n");
			out.print("  Total test die: "+GrossDie_R+"\r\n");
			out.print("  Pass Die: "+PassDie_R+"\r\n");
			out.print("  Fail Die: "+FailDie_R+"\r\n");
			out.print("  Yield: "+Yeild_R+"\r\n");
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
//	@Override
//	public void FTP_Release(String CustomerCode, String Device, String Lot, String CP, File file) {
//		// TODO Auto-generated method stub
//		File ReleaseDirectory=new File("/home/UploadReport/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping");
//		if (!ReleaseDirectory.exists()) {
//			ReleaseDirectory.mkdirs();
//		}
//		File ReleaseFile=new File("/home/UploadReport/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/"+file.getName());
//		if (ReleaseFile.exists()) {
//			ReleaseFile.delete();
//		}
//		try {
//			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
