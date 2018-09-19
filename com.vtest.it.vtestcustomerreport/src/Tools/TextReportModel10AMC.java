package Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import parseRawdata.parseRawdata;

public class TextReportModel10AMC extends FTPRealseModel implements Text_Report {

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		File[] Filelist=DataSorce.listFiles();
		String PassBins="-1";
		try {
			String[]  tempPassBins=new parseRawdata(Filelist[0]).getProperties().get("Pass Bins").split(",");
			StringBuilder sb=new StringBuilder();
			for (int i = 0; i < tempPassBins.length; i++) {
				sb.append("-"+tempPassBins[i]);
			}
			PassBins=sb.toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
			Integer Row_R=Integer.parseInt(properties.get("Map Rows"));

			String Wafer_Load_Time_R=properties.get("Test Start Time");
			Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			
			Integer gross_die=Integer.parseInt(properties.get("Gross Die"));
			String waferid=properties.get("Wafer ID");
			String FailDie_R=(properties.get("Fail Die"));
			String TestStartTime_R=properties.get("Test Start Time");
			String TestEndTime_R=properties.get("Test End Time");
			String Yeild_R=properties.get("Wafer Yield");
			
			TextReportModel3 model1=new TextReportModel3();
			Wafer_ID_R=Wafer_ID_R.substring(0, Wafer_ID_R.length()-2);
			String VERSION="NA";
			String FinalID=RightID_R.toString();
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
			Set<String> keyset=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			FileOutputStream ResultTextOutputStream=new FileOutputStream(Result_Text);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new OutputStreamWriter(ResultTextOutputStream, "UTF-8"));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			out.print("Operator           :   VTEST-"+Flat_R.substring(0, 1)+Flat_R.substring(1, Flat_R.length()).toLowerCase()+"\r\n");
			out.print("Device             :   "+Device+"-"+Flat_R.substring(0, 1).toUpperCase()+"-P"+PassBins+"\r\n");
			out.print("Lot ID             :   "+Lot+"\r\n");
			out.print("Wafer ID           :   "+waferid+"\r\n");
			out.print("Test Start Time    :   "+ModifyTime.Modify(TestStartTime_R)+"\r\n");
			out.print("Test End Time      :   "+ModifyTime.Modify(TestEndTime_R)+"\r\n");
			out.print("Wafer Load Time    :   "+ModifyTime.Modify(TestStartTime_R)+"\r\n");
			out.print("Wafer Unload Time  :   "+ModifyTime.Modify(TestEndTime_R)+"\r\n");
			out.print("Gross Die          :   "+gross_die+"\r\n");
			out.print("Pass Die           :   "+PassDie_R+"\r\n");
			out.print("Fail Die           :   "+FailDie_R+"\r\n");
			out.print("Yield              :   "+Yeild_R+"\r\n");
			out.print("\r\n");
			out.print("©¤©¤¡úX"+"\r\n");
			out.print("©¦"+"\r\n");
			out.print("©¦"+"\r\n");
			out.print("¡ýY"+"\r\n");
			out.print("Wafer Map in Soft Bin"+"\r\n");
			out.print("\r\n");
			out.print("Y¡ýX¡ú");
			for(int i=-1;i<Col_R+1;i++)
			{					
				if (i==Col_R)
					out.print(String.format("%3s", i)+"\r\n");
				else
					out.print(String.format("%3s", i));
			}
			for (int j = 0; j < Row_R; j++) {
				out.print(String.format("%6s",j+"|"));
				out.print(String.format("%3s"," "));
				for(int i=0;i<Col_R;i++)
				{
					if (MapCell_R[j][i]!=null&&!MapCell_R[j][i].equals("S")&&!MapCell_R[j][i].equals("M")) {
						out.print(String.format("%3s", MapCell_R[j][i]));
					}else {
						out.print(String.format("%3s", " "));
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
