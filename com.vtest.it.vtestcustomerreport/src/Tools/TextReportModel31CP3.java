package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel31CP3 extends FTPRealseModel implements Text_Report{

	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce,String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		if (!CP.equals("CP3")) {
			return;
		}
		PRA_Merge pra_Merge=new PRA_Merge();
		File[] Filelist=DataSorce.listFiles();
		File directory=new File("/TestReport/CustReport/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-EQC");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File  MergeSummary=new File("/TestReport/CustReport/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-EQC/"+Lot+"_EQC_SUMMARY.txt");
		FileWriter in=new FileWriter(MergeSummary);
		PrintWriter printWriter=new PrintWriter(in);
		printWriter.print("PRODUCT       ="+Device+"\r\n");
		printWriter.print("LOT           ="+Lot+"\r\n");
		printWriter.print("\r\n");
		printWriter.print("WAFER ID    Qty   Merge Result(任一结果为X则显示fail)\r\n");
//		ArrayList<String> ResultSummary=new ArrayList<>();
		TreeMap<Integer, String> treeMap=new TreeMap<>();
		for (int k = 0; k < Filelist.length; k++) {	
			File CP1RawData=new File(Filelist[k].getPath().replace("CP3", "CP2"));
			if (!CP1RawData.exists()) {
				continue;
			}
			HashMap<String, String> FailDieMap=pra_Merge.GetCPoneInfor(CP1RawData);
			Set<String> Failkeyset=FailDieMap.keySet();
			Integer FaildieSum=FailDieMap.size();	
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
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
			String FinalID=RightID_R.toString();
			String Wafer_Load_Time_R=properties.get("Test Start Time");
			TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();	
			Integer left=Integer.valueOf(properties.get("TestDieleft"));
			Integer up=Integer.valueOf(properties.get("TestDieup"));
			Integer[] Bininfor=new Integer[32];
			for (int i = 0; i < Bininfor.length; i++) {
				Bininfor[i]=0;
			}
			Set<Integer> keyset=Bin_Summary_R.keySet();
			for (Integer key : keyset) {
				Bininfor[key-1]=Bin_Summary_R.get(key);
			}
			TextReportModel3 model1=new TextReportModel3();
			String VERSION="NA";
			if (RightID_R<10) {
				FinalID="0"+RightID_R.toString();
			}
			HashMap<String, String> NameMap=model1.InitMap(Lot, FinalID, CP, Wafer_Load_Time_R, Device, Wafer_ID_R, VERSION);
			Set<String> keyset1=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset1) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
		
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Lot+"-EQC/"+FinalName);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
			
			out.print("PRODUCT       ="+Device+"\r\n");
			out.print("LOT           ="+Lot+"\r\n");
			out.print("WAFER ID      ="+FinalID+"\r\n");
			out.print("X QUANTUM     ="+Col_R+"\r\n");
			out.print("Y QUANTUM     ="+Row_R+"\r\n");
			out.print("FLAT/NOTCH    ="+Flat_R+"\r\n");
			out.print("\r\n");
			out.print("[ EQC MAP]"+"\r\n");
			int mergefailsum=0;
			ArrayList<String> FailPointsArray=new ArrayList<>();
			for (int i = 0-3; i < Row_R+3; i++) {
				for (int j = 0-3; j < Col_R+4; j++) {					
					try {
						if (MapCell_R[i][j]==null) {
							out.print(".");
						}else if (MapCell_R[i][j].equals("S")||MapCell_R[i][j].equals("M")) {
							out.print(".");
						}else {
							if (Failkeyset.contains((i+up)+":"+(j+left))) {
								if(MapCell_R[i][j].equals("1"))
								{
									FailPointsArray.add(i+","+j+"  "+FailDieMap.get(i+":"+j)+"/1");
									out.print("X");
									mergefailsum++;
								}
								else
								{
									out.print("V");
								}
							}else {
								out.print(" ");
							}						
						}
					} catch (Exception e) {
						// TODO: handle exception
						out.print(".");
					}						
				}
				out.print("\r\n");
			}
			out.print("[ BIN SUMMARY]"+"\r\n");
			out.print("BIN No.    Quan.    Yield%"+"\r\n");
			out.print("========================================================"+"\r\n");
			String passyield=String.format("%.2f", (double)(FaildieSum-mergefailsum)*100/(FaildieSum));
			String failyield=String.format("%.2f", (double)(mergefailsum)*100/(FaildieSum));
			out.print("BIN   V  =  "+String.format("%-8s", FaildieSum-mergefailsum)+String.format("%5s", passyield)+"%\r\n");
			out.print("BIN   X  =  "+String.format("%-8s", mergefailsum)+String.format("%5s",failyield)+"%\r\n");
			out.print("\r\n");
			out.print("EQC Fail location\r\n");
			out.print("\r\n");
			out.print("X  ,Y    CP1 bin no/CP2 bin no\r\n"); 
			for (String point : FailPointsArray) {
				out.print(point+"\r\n");
			}
			out.flush();
			out.close();
			String resultFlag="pass";
			if (mergefailsum>0) {
				resultFlag="fail";
			}
			treeMap.put(Integer.valueOf(FinalID), String.format("%-8s", FinalID)+String.format("%7s",FaildieSum)+String.format("%8s", resultFlag));
			//ResultSummary.add(String.format("%-8s", FinalID)+String.format("%7s",FaildieSum)+String.format("%8s", resultFlag));
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
		Set<Integer> idSet=treeMap.keySet();
		for (Integer id : idSet) {
			printWriter.print(treeMap.get(id)+"\r\n");
		}
//		for (String result : ResultSummary) {
//			printWriter.print(result+"\r\n");
//		}
		printWriter.flush();
		printWriter.close();
	}
}
