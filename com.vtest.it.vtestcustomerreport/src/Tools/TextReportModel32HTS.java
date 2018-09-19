package Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parseRawdata.parseRawdata;

public class TextReportModel32HTS extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel32HTS model15=new TextReportModel32HTS();
		File[] Filelist=DataSorce.listFiles();
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getHardBinTestDiesDimensionalArray();
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
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));

			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");

			String Index_X_R=properties.get("Index X(mm)");
			String Index_Y_R=properties.get("Index Y(mm)");
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
			out.print("Device: "+Device+"\r\n");
			out.print("Supplier: HunterSun\r\n");
			out.print("Lot ID:  "+Lot+"\r\n");
			out.print("Wafer ID:  "+Wafer_ID_R+"\r\n");
			out.print("Die Size X,Y: "+Index_X_R+","+Index_Y_R+"\r\n");
			out.print("NOTCH: "+Flat_R+"\r\n");
			out.print("Column: "+Col_R+"\r\n");
			out.print("ROW: "+Row_R+"\r\n");
			out.print("Passed: "+PassDie_R+"\r\n");
			out.print("Good bin code: 1\r\n");
			HashMap<String, String> MergeMap=new HashMap<>();
			try {
				MergeMap=parse(new File("D:/TestReport/HTS/"+Wafer_ID_R+".tma"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			for (int i = 0; i < Row_R; i++) {
				for (int j =0 ; j < Col_R; j++) {
//					out.print(Modify(MapCell_R[i][j]));		
					String key=j+":"+i;
					if (MapCell_R[i][j]!=null) {
						if (MapCell_R[i][j].equals("1")) {
							if (MergeMap.get(key).equals("F")) {
								out.print("3");
							}else {
								out.print("1");
							}						
						}else {
							out.print("3");
						}
					}else{
						if (dieCheck(i, j, MapCell_R, Row_R)) {
							if (MergeMap.get(key).equals("P")) {
								out.print("2");
							}else {
								out.print("3");
							}
						}else {
							out.print(".");
						}
					}
				}
				out.print("\r\n");
			}
			out.flush();
			out.close();
			FTP_Release(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
	public static String Modify(String Value) {
		if (Value==null) {	
			return ".";
		}
		if (Value.equals("S")||Value.equals("M")) {
			return ".";
		}
		if (Value.equals("1"))
			return "1";
		else
			return Value;
	}
	public static Boolean dieCheck(Integer coordinateX,Integer coordinateY,String[][] map,Integer row)
	{
		boolean upCheck=false;
		boolean downCheck=false;
		for (int i = coordinateX-row; i < coordinateX+row; i++) {
			if (i<coordinateX) {
				try {
					if (map[i][coordinateY]!=null) {
						downCheck=true;
						i=coordinateX;
					};
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else  {
				try {
					if (map[i][coordinateY]!=null) {
						upCheck=true;
						break;
					};
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		if (downCheck&&upCheck) {
			return true;
		}else {
			return false;
		}
	}
	public static HashMap<String, String> parse(File file)throws IOException, ParserConfigurationException, SAXException
	{
		PrintWriter printWriter=new PrintWriter(new FileWriter(new File("D:/TestReport/HTS/sss.txt")));
		HashMap<String, String> MergeMap=new HashMap<>();
		//ArrayList<String> failDieArray=new ArrayList<>();		
		FileReader in=new FileReader(file);
		BufferedReader bReader=new BufferedReader(in);
		String content;
		boolean flag=false;
		boolean start=true;
		int startX=1;
		while((content=bReader.readLine())!=null)
		{
			if (start) 
			{
				startX=Integer.valueOf(content.substring(3, 6).trim());
				start=false;
			}
			if(content.contains("++-"))
			{
				flag=true;
				continue;
			}
			if(flag)
			{
				if(!content.contains("|"))
				{
					flag=false;
					break;
				}
			}			
			if (flag) {
				int coordianteY=Integer.valueOf(content.substring(0, 3))-1;
				int length=content.length();
				for (int i = 6; i < length; i=i+3) {
					if (i+3>length) {
//						if (content.substring(i, i+1).trim().equals("F")) {
//							failDieArray.add(((i)/3+startX-3)+","+(coordianteY-1)+",99");
//						}
						MergeMap.put(((i)/3+startX-3)+":"+coordianteY, content.substring(i, i+1).trim());
						printWriter.println(((i)/3+startX-3)+":"+coordianteY+":"+content.substring(i, i+1).trim());
					}else {
//						if (content.substring(i, i+3).trim().equals("F")) {
//							failDieArray.add(((i)/3+startX-3)+","+(coordianteY-1)+",99");
//						}
						MergeMap.put(((i)/3+startX-3)+":"+coordianteY, content.substring(i, i+3).trim());
						printWriter.println(((i)/3+startX-3)+":"+coordianteY+":"+content.substring(i, i+3).trim());
					}
					
				}
			}
		}
		bReader.close();
		printWriter.flush();
		printWriter.close();
		return MergeMap;
	}
}
