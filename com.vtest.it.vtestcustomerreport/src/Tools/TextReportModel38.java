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

public class TextReportModel38 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel38 model15=new TextReportModel38();
		File[] Filelist=DataSorce.listFiles();
		String coordinates="390,29";
		Integer coordinateX=Integer.valueOf(coordinates.split(",")[1]);
		Integer coordinateY=Integer.valueOf(coordinates.split(",")[0]);		
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

			String GrossDie_R=properties.get("Gross Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
			Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
			Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
			Integer TestDie_MaxX_R=Integer.valueOf(properties.get("TestDieright"));
			Integer TestDie_MaxY_R=Integer.valueOf(properties.get("TestDiedown"));
			String Yeild_R=properties.get("Wafer Yield");

			Integer SlotID_R=Integer.valueOf(properties.get("Slot"));
			Integer MinX_R=Integer.valueOf(properties.get("left"));
			Integer MinY_R=Integer.valueOf(properties.get("up"));
			coordinateX=coordinateX-MinY_R;
			coordinateY=coordinateY-MinX_R;
			Set<Integer> binkeyset=Bin_Summary_R.keySet();
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String VERSION="NA";
			MapCell_R[coordinateX][coordinateY]="9";
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
			out.print("Device Name: "+Device+"\r\n");
			out.print("LotNum: "+Lot+"\r\n");
			out.print("SlotNum: "+SlotID_R+"\r\n");			
			if (Flat_R.equals("DOWN")) {
				out.print("Flat/Notch: 180\r\n");
			}else if (Flat_R.equals("UP")) {
				out.print("Flat/Notch: 0\r\n");
			}else if (Flat_R.equals("LEFT")) {
				out.print("Flat/Notch: 270\r\n");
			}else {
				out.print("Flat/Notch: 90\r\n");
			}	
			boolean addNineFlag=false;
			for (int i = TestDie_MinY_R; i < TestDie_MaxY_R+1; i++) {
				if (i==coordinateX) {
					addNineFlag=true;	
				}else {
					addNineFlag=false;
				}
				for (int j =TestDie_MinX_R ; j < TestDie_MaxX_R+1; j++){					
					if (addNineFlag)
					{
						if (Check(MapCell_R[i][j])&&CheckValue(MapCell_R[i][j+1])) {
							out.print("9");
						}else {
							out.print(MapCell_Modify11.Modify(MapCell_R[i][j]));
						}	
					}else {
						out.print(MapCell_Modify11.Modify(MapCell_R[i][j]));	
					}									
				}
				out.print("\r\n");
			}
			out.print("Total:"+GrossDie_R+"\r\n");
			out.print("Pass :"+PassDie_R+" "+Yeild_R+"\r\n");
			for (int i = 1; i < 23; i++) {
				Integer value=0;
				boolean flag=false;
				if (binkeyset.contains(i)) {
					value=Bin_Summary_R.get(i);
					flag=true;
				}
				if (flag) {
					if (i==1) {
						out.print(String.format("%-5s", "Bin1")+":"+value+"\r\n");
					}else {
						out.print(String.format("%-5s", "Bin2")+":"+value+"\r\n");
					}
				}else {
					out.print(String.format("%-5s", "Bin"+i)+":"+value+"\r\n");
				}
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
	private boolean Check(String value)
	{
		if (value==null||value.equals("M")||value.equals("S")) {
			return true;
		}else {
			return false;
		}
	}
	private boolean CheckValue(String value)
	{
		if (value!=null&&!value.equals("M")&&!value.equals("S")) {
			return true;
		}else {
			return false;
		}
	}
}
