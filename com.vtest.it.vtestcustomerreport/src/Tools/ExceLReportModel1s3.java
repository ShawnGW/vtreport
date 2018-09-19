package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExceLReportModel1s3 extends ReportModel2 {

	public ExceLReportModel1s3() throws IOException {
		// TODO Auto-generated constructor stub
		super(new File("/Config/NSY.xlsx"));
	}
	@Override
	public void Write_Excel(String CustomerCode,String Device,String Lot,String CP,File file,File Result_Excel) {
		// TODO Auto-generated method stub
		try {		
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			
			String Time=null;
			String Version="NA";
			int Row_Count=Prober_Mappings.length-1;
			for (int i = 0; i<Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();

					
					String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
					Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
					Integer Row_R=Integer.parseInt(properties.get("Map Rows"));

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));
											
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
				
			}	
			HashMap<String, String> NameMap=InitMap(Lot, CP, Time, Device, Version);
			Set<String> keyset=NameMap.keySet();
			String Path=Result_Excel.getParent();
			String FileName=Result_Excel.getName();
			for (String key : keyset) {
				if (FileName.contains(key)) {
					FileName=FileName.replace(key, NameMap.get(key));
				}
			}
			File Final_Result_Excel=new File(Path+"/"+FileName);
			FileOutputStream outputStream=new FileOutputStream(Final_Result_Excel);
			workbook.write(outputStream);
			outputStream.close();	
			FTP_Release(CustomerCode, Device, Lot, CP, Final_Result_Excel);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	

}
