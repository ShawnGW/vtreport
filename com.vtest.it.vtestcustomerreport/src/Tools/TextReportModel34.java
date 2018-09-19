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

public class TextReportModel34 extends FTPRealseModel implements Text_Report{

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel34 model1=new TextReportModel34();
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
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("Map Cols"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("Map Rows")));
			String FinalID=RightID_R.toString();

			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			Integer gross_die=Integer.parseInt(properties.get("Gross Die"));
			String Version="NA";
			String Report_Dev="NA";
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			if (Report_Dev.equals("NA")) {
				Report_Dev=Device;
			}
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Report_Dev, Wafer_ID_R, Version);
			Set<String> keyset1=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset1) {
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
			out.print("DEVICE NAME:"+Device+"\r\n");
			out.print("Wafer ID:"+Wafer_ID_R+"\r\n");
			if (Flat_R.equals("DOWN")) {
				out.print("FLAT/NOTCH:180\r\n");
			}else if (Flat_R.equals("UP")) {
				out.print("FLAT/NOTCH:0\r\n");
			}else if (Flat_R.equals("LEFT")) {
				out.print("FLAT/NOTCH:270\r\n");
			}else {
				out.print("FLAT/NOTCH:90\r\n");
			}
			out.print("\r\n");
			out.print(" ");
			out.printf("%-11S", "TOTAL");
			out.print(": "+gross_die+"\r\n");
			out.print(" ");
			out.printf("%-11S", "GOOD(-)");
			out.print(": 0        0.0%\r\n");
			for (int i = 1; i < 35; i++) {
				Integer Value=0;
				if (Bin_Summary_R.containsKey(i)) {
					Value=Bin_Summary_R.get(i);
				}
				double yeild=(double)Value*100/gross_die;
				out.print(" ");
				if (i>=10) {
					out.printf("%-11S", "BIN"+i+"("+(char)(i+55)+")");
				}else {
					out.printf("%-11S", "BIN"+i+"("+i+")");
				}
				out.print(": ");
				out.printf("%-9S", Value);
				out.print(String.format("%.1f",yeild)+"%");
				out.print("\r\n");
			}
			out.print(" BIN#(#)    : 0        0.0%"+"\r\n");
			out.print("\r\n");
			for (int i = 1; i < Row_R; i++) {
				if (i<10) {
					out.print("00"+i);
				}else if (i>9&&i<100) {
					out.print("0"+i);
				}else {
					out.print(i);
				}
				out.print("  ");
				for (int j = 1; j < Col_R; j++) {	
					if (MapCell_R[i][j]==null) {
						out.print(" ");
					}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
						out.print(" ");
					}else {
						out.print(MapCell_Modify.Modify(MapCell_R[i][j]));	
					}						
				}
				out.print("\r\n");
			}
			
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
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
