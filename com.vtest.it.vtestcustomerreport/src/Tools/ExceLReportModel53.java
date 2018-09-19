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

public class ExceLReportModel53 extends Report_Model {

	private static final File Model=new File("/Config/NTO_Summary.xlsx");
	public ExceLReportModel53() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());

			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.getRow(26);

			String Time=null;
			String Version="NA";
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					String TestStartTime_R=properties.get("Test Start Time");

					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					Integer Row_number=RightID_R;
					if (Time==null) {
						Time=TestStartTime_R;
					}
					sheet.getRow(Row_number).createCell(0).setCellValue(properties.get("Wafer ID"));
					sheet.getRow(Row_number).createCell(1).setCellValue(RightID_R);
					sheet.getRow(Row_number).createCell(2).setCellValue(PassDie_R);
					sheet.getRow(Row_number).getCell(3).setCellValue(Integer.valueOf(properties.get("Gross Die")));
					sheet.getRow(Row_number).getCell(4).setCellFormula("C"+(RightID_R+1)+"/D"+(RightID_R+1));
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}
			Row_Summary.createCell(2).setCellFormula("SUM(C2:C26)");
			Row_Summary.createCell(3).setCellFormula("SUM(D2:D26)");
			Row_Summary.getCell(4).setCellFormula("AVERAGE(E2:E26)");
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
