package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel9 extends Report_Model {

	private static final File Model=new File("/Config/SMC.xlsx");
	public ExceLReportModel9() throws IOException {
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

			XSSFSheet sheet=workbook.getSheet("report");
			XSSFRow Row_Summary=sheet.getRow(1);
			XSSFCell Device_Cell=Row_Summary.getCell(2);
			XSSFCell Lot_Cell=Row_Summary.getCell(10);
			XSSFCell Sum_Cell=Row_Summary.getCell(19);
			XSSFCell Date_Cell=Row_Summary.getCell(23);
			sheet.getRow(5).getCell(22).setCellValue(propertiesFirst.get("Tester Program"));
			
			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd HH:mm");
			Date_Cell.setCellValue(format.format(new Date()));
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
				
			String Time=null;
			String Version="NA";
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					
					String TestStartTime_R=properties.get("Test Start Time");
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();
					Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));

					String OPerater_R=properties.get("Operator");
					if (Time==null) {
						Time=TestStartTime_R;
					}

					Integer Cell_value=RightID_R+1;
					for (int j = 1; j < 10; j++) {
						if (Bin_Summary_Map_R.containsKey(j)) {
							sheet.getRow(10+j).getCell(Cell_value).setCellValue(Bin_Summary_Map_R.get(j));							
						}else
						{
							sheet.getRow(10+j).getCell(Cell_value).setCellValue(0);
						}
					}
					sheet.getRow(10).getCell(Cell_value).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R*100/(PassDie_R+Total_Fail_Die_R)))));
					sheet.getRow(26).getCell(Cell_value).setCellValue(PassDie_R);
					sheet.getRow(27).getCell(Cell_value).setCellValue(Total_Fail_Die_R);
					sheet.getRow(28).getCell(Cell_value).setCellValue(PassDie_R+Total_Fail_Die_R);
					sheet.getRow(29).getCell(Cell_value).setCellValue(OPerater_R);					
					sheet.getRow(10).getCell(26).setCellFormula("AVERAGE(C11:Z11)");
					sheet.getRow(4).getCell(4).setCellFormula("SUM(C27:Z27)");
					sheet.getRow(4).getCell(13).setCellFormula("AVERAGE(C11:Z11)/100");
					sheet.getRow(4).getCell(22).setCellFormula("SUM(C29:Z29)");
					for(int j=0;j<18;j++)
					{
						if (j<10) {
							sheet.getRow(11+j).getCell(26).setCellFormula("SUM(C"+(12+j)+":Z"+(12+j)+")");
						}else if (j>14) {
							sheet.getRow(11+j).getCell(26).setCellFormula("SUM(C"+(12+j)+":Z"+(12+j)+")");
						}
					}
					
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
