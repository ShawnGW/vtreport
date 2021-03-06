package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import parseRawdata.parseRawdata;

public class TextReportModel12NSY extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel12NSY model12=new TextReportModel12NSY();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();			
			String Wafer_ID_R=properties.get("Wafer ID");
			String Wafer_Load_Time_R=properties.get("Test End Time");
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));	
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer SlotID_R=Integer.valueOf(properties.get("Slot"));
			String waferSize_R=properties.get("WF_Size");		
			Integer  MapRows_R=Integer.valueOf(properties.get("TestDieRow"));
			Integer  MapCols_R=Integer.valueOf(properties.get("TestDieCol"));
			String[][] MapCell_R=TurnNighteenDegree.Turn(parseRawdata.getSoftBinTestDiesDimensionalArray(), MapRows_R,MapCols_R);
			String FinalID=RightID_R.toString();
			if(FinalID.length()==1)
				FinalID="0"+FinalID;
			String VERSION="NA";
			HashMap<String, String> NameMap=model12.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
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
			out.print("Test House:VTEST"+"\r\n");
			out.print("Customer Name:NSY"+"\r\n");
			out.print("Device Name:"+Device+"\r\n");
			out.print("Wafer Size:"+waferSize_R+".0Inch\r\n");
			out.print("LOT:"+Lot+"\r\n");
			out.print("SLOT:"+SlotID_R+"\r\n");
			out.print("Good die:"+PassDie_R+"\r\n");
			out.print("Orientation:Down\r\n");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < MapRows_R+5; j++) {
					out.print(".");
				}
				out.print("\r\n");
			}
			for ( int i = 0; i <MapCols_R; i++) {
				out.print("...");
				for (int j = 0; j <  MapRows_R; j++) {
						try {							
							out.print(MapCell_Modify2.Modify(MapCell_R[i][j]));
						} catch (Exception e) {
							// TODO: handle exception
							out.print(MapCell_Modify2.Modify(null));
						}
				}
				out.print("..");
				out.print("\r\n");
			}
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < MapRows_R+5; j++) {
					out.print(".");
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
