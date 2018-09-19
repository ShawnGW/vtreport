package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel18 extends Report_Model {

	private static final File Model=new File("/Config/AMC_List.xlsx");
	public ExceLReportModel18() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			//FileOutputStream outputStream=new FileOutputStream(Result_Excel);
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("List");
			XSSFRow Row_Summary=sheet.getRow(3);
			Row_Summary.getCell(0).setCellValue(Local_lot);
			Row_Summary.getCell(2).setCellValue(Device);
			Integer Loaclgrossdie=0;
			try {
			Loaclgrossdie=Integer.valueOf(propertiesFirst.get("MES Test Gross Die"));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Row_Summary.getCell(4).setCellValue(Loaclgrossdie);
			Row_Summary.getCell(6).setCellValue(file.listFiles().length);
			Row_Summary.getCell(8).setCellFormula("K4/E4");
			Row_Summary.getCell(10).setCellFormula("SUM(B7:B31)");
			sheet.getRow(0).getCell(2).setCellValue(Local_lot);	
			String Time=null;
			String Version="NA";
			Integer TotalD=0;
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					TotalD+=GrossDie_R;
					Integer Row_number=RightID_R+5;
					String TestStartTime_R=properties.get("Test Start Time");
					if (Time==null) {
						Time=TestStartTime_R;
					}
					sheet.getRow(Row_number).getCell(1).setCellValue(PassDie_R);
					sheet.getRow(Row_number).getCell(0).setCellValue(RightID_R);
					sheet.getRow(Row_number).getCell(2).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));					
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}
			sheet.getRow(3).getCell(4).setCellValue(TotalD);
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
