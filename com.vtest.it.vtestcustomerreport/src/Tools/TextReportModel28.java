package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel28 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel28 model15=new TextReportModel28();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
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
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));

			Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
			String FailDie_R=properties.get("Fail Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));

			String waferSize_R=properties.get("WF_Size");

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
			out.print("Device Name       : "+Device+"\r\n");
			out.print("Lot No.           : "+Lot+"\r\n");
			out.print("Wafer ID          : "+Wafer_ID_R+"\r\n");
			out.print("Wafer Size        : "+waferSize_R+" Inch\r\n");
			out.print("Flat/Notch        : "+Notch_R.substring(0,1)+Notch_R.substring(1, Notch_R.length()).toLowerCase()+"\r\n");
			out.print("Map Column        : "+Col_R+"\r\n");
			out.print("Map Row           : "+Row_R+"\r\n");
			out.print("Total Tested      : "+GrossDie_R+"\r\n");
			out.print("Total Pass        : "+PassDie_R+"\r\n");
			out.print("Total Fail        : "+FailDie_R+"\r\n");
			out.print("  Yield:"+String.format("%2.2f",(double)PassDie_R*100/GrossDie_R)+"%\r\n");
			for (int i = TestDie_MinY_R-1; i < Row_R+TestDie_MinY_R; i++) {
				for (int j =TestDie_MinX_R-1; j < Col_R+TestDie_MinX_R; j++) {
					out.print( MapCell_Modify4.Modify(MapCell_R[i][j]));		
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release_FAB(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
	public static String Time(String Time)
	{
		return Time.substring(0,2)+"/"+Time.substring(2,4)+"/"+Time.substring(4,6)+" "+Time.substring(6,8)+":"+Time.substring(8,10)+":00";
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
////		File MailReleaseDirectory=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
////		if (!MailReleaseDirectory.exists()) {
////			MailReleaseDirectory.mkdirs();
////		}
////		File MailReleaseFile=new File("/home/UploadReport/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
////		if (MailReleaseFile.exists()) {
////			MailReleaseFile.delete();
////		}
////		try {
////			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}


}
