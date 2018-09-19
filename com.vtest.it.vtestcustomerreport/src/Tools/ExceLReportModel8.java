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

public class ExceLReportModel8 extends Report_Model {

	private static final File Model=new File("/Config/SMC_List.xlsx");
	public ExceLReportModel8() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("List");
			XSSFRow Row_Summary=sheet.getRow(3);
			Row_Summary.getCell(0).setCellValue(Local_lot);
			Row_Summary.getCell(2).setCellValue(Device);
			Row_Summary.getCell(4).setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
			Row_Summary.getCell(6).setCellValue(file.listFiles().length);
			Row_Summary.getCell(8).setCellFormula("K4/(E4*"+file.listFiles().length+")");
			Row_Summary.getCell(10).setCellFormula("SUM(B7:C30)");
			sheet.getRow(0).getCell(2).setCellValue(Local_lot);
			String Time=null;
			String Version="NA";

			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					String TestStartTime_R=properties.get("Test Start Time");

					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					Integer Row_number=RightID_R+5;
					if (Time==null) {
						Time=TestStartTime_R;
					}
					sheet.getRow(Row_number).getCell(1).setCellValue(PassDie_R);
					sheet.getRow(Row_number).getCell(0).setCellValue(RightID_R);
				} catch (Exception e) {
					// TODO: handle exception
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
