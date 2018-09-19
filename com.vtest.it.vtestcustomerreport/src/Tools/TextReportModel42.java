package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel42 extends FTPRealseModel implements Text_Report {
	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel42 model13=new TextReportModel42();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			String mapReferenceDie=properties.get("Map Reference");
			Integer CoordinateX=0;
			Integer CoordinateY=0;
			try {
				if (!mapReferenceDie.equals("NA")) {
					 CoordinateX=Integer.parseInt(mapReferenceDie.split(",")[0].substring(1).trim())-Integer.valueOf(properties.get("TestDieleft"));
					 CoordinateY=Integer.parseInt(mapReferenceDie.split(",")[1].substring(0, mapReferenceDie.split(",")[1].length()-1))-Integer.valueOf(properties.get("TestDieup"));
					MapCell_R[CoordinateY][CoordinateX]="M";
				}
			} catch (Exception e) {
				// TODO: handle exception
			}		
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
			String Wafer_Load_Time_R=properties.get("Test End Time");
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			String waferSize_R=properties.get("WF_Size");
			
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			Integer minX=Integer.valueOf(properties.get("TestDieleft"));
			Integer minY=Integer.valueOf(properties.get("TestDieup"));
			
			String GrossDie_R=properties.get("Gross Die");
			String FailDie_R=properties.get("Fail Die");
			String Yeild_R=properties.get("Wafer Yield");
			String FinalID=RightID_R.toString();

			String VERSION="NA";
			HashMap<String, String> NameMap=model13.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
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
			out.print("Wafer Size        : "+waferSize_R+"\r\n");
			out.print("Flat/Notch        : "+Flat_R.substring(0, 1)+Flat_R.substring(1, Flat_R.length()).toLowerCase()+"\r\n");
			out.print("Map Column        : "+Col_R+"\r\n");
			out.print("Map Row           : "+Row_R+"\r\n");
			out.print("Total Tested      : "+GrossDie_R+"\r\n");
			out.print("Total Pass        : "+PassDie_R+"\r\n");
			out.print("Total Fail        : "+FailDie_R+"\r\n");
			out.print("Yield             : "+Yeild_R+"\r\n");
			out.print(""+"\r\n");
			out.print("[SOFT BIN MAP]"+"\r\n");
			out.print("     ");
			if (Row_R<CoordinateY) {
				Row_R=CoordinateY+1;
			}
			if (Col_R<CoordinateX) {
				Col_R=CoordinateX+1;
			}
			for(int i=0;i<=Col_R;i++)
			{
				Integer i2=i+minX;
				out.print(i2/100);
			}
			out.print("\r\n");
			out.print("     ");
			for(int i=0;i<=Col_R;i++)
			{
				Integer i2=i+minX;
				out.print((i2/10)%10);
			}
			out.print("\r\n");
			out.print("     ");
			for(int i=0;i<=Col_R;i++)
			{
				Integer i2=i+minX;
				out.print(i2%10);
			}
			out.print("\r\n");
			out.print("\r\n");
			
			for (int i = 0; i < Row_R; i++) {
				Integer i2=i+minY;
				if (i2<10) {
					out.print("00"+i2);
				}else if (i2>9&&i2<100) {
					out.print("0"+i2);
				}else {
					out.print(i2);
				}
				out.print("  ");
				for (int j = 0; j < Col_R; j++) {
					if (i==(CoordinateY)&&j==(CoordinateX)) {
						out.print("M");
						continue;
					}
					try {
						out.print(MapCell_Modify12.Modify(MapCell_R[i][j]));
					} catch (Exception e) {
						// TODO: handle exception
						out.print(" ");
					}						
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}

}
