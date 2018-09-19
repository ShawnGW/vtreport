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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import parseRawdata.parseRawdata;

public class ExceLReportModel33 extends Report_Model {

	private static final File Model=new File("/Config/HGK.xlsx");
	private final Integer[] Bin_Array={1, 21, 22, 23, 24, 30, 31, 40, 41, 42, 43, 44, 45, 46, 50, 60, 61, 62};
	public ExceLReportModel33() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			XSSFSheet sheet=workbook.getSheet("Bin_Summary");
			sheet.getRow(0).getCell(2).setCellValue(CustomerCode);
			sheet.getRow(0).getCell(5).setCellValue(Device);
			sheet.getRow(0).getCell(9).setCellValue("");
			
			sheet.getRow(1).getCell(2).setCellValue(Device);
			sheet.getRow(1).getCell(5).setCellValue(Lot);
			sheet.getRow(1).getCell(8).setCellValue(file.listFiles().length);
			sheet.getRow(1).getCell(10).setCellValue(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
			
			

			XSSFFont font=workbook.createFont();
			font.setFontHeight(6);
			font.setFontName("Calibri");
			Right_Style.setBorderLeft((short)1);
			Right_Style.setBorderRight((short)1);
			Right_Style.setBorderTop((short)1);
			Right_Style.setBorderBottom((short)1);
			Right_Style.setFont(font);
			
			XSSFCellStyle DataStyle2=workbook.createCellStyle();
			XSSFDataFormat dataFormat=workbook.createDataFormat();
			DataStyle2.setDataFormat(dataFormat.getFormat("0.00%"));
			DataStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			DataStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			DataStyle2.setFont(font);
			DataStyle2.setBorderLeft((short)1);
			DataStyle2.setBorderRight((short)1);
			DataStyle2.setBorderTop((short)1);
			DataStyle2.setBorderBottom((short)1);
			String Time=null;
			String Version="NA";
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			Integer TotalPassdie=0;
			Integer TotalTestDie=0;
			Integer TotalFailDie=0;
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					String Tester_R=properties.get("Tester ID");
					String Prober_R=properties.get("Prober ID");				
					String MapCols_R=properties.get("Map Cols");
					String MapRows_R=properties.get("Map Rows");
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String[][] Map_R=parseRawdata.getAllDiesDimensionalArray();
					
					String TestStartTime_R=properties.get("Test Start Time");
					String Yeild_R=properties.get("Wafer Yield");
					String TestEndTime_R=properties.get("Test End Time");
					String FailDie_R=properties.get("Fail Die");
					String waferSize_R=properties.get("WF_Size");
					String Notch_R=properties.get("Notch");
					Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
					Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
					
					Integer Row_number=RightID_R+6;
					XSSFRow Rows=sheet.getRow(Row_number);

					TotalFailDie+=Integer.valueOf(FailDie_R);
					TotalPassdie+=Integer.valueOf(PassDie_R);
					TotalTestDie+=Integer.valueOf(GrossDie_R);
					if (Time==null) {
						Time=TestStartTime_R;
					}
					for (int j = 0; j < Bin_Array.length; j++) {
						XSSFCell Bin_Cell=Rows.getCell(5+j);
						Integer Bin_value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							Bin_value=Bin_Summary_R.get(Bin_Array[j]);
						}
						Bin_Cell.setCellValue(Bin_value);
					}
					if (RightID_R.toString().length()==1) {
						Rows.getCell(0).setCellValue(Lot.split("\\.")[0]+"-0"+RightID_R);
					}else {
						Rows.getCell(0).setCellValue(Lot.split("\\.")[0]+"-"+RightID_R);
					}
					Rows.getCell(1).setCellValue(Float.parseFloat(Yeild_R.replace("%", "")));	
					Rows.getCell(2).setCellValue(Tester_R);
					Rows.getCell(3).setCellValue(Prober_R);
					Rows.getCell(4).setCellValue(Device);
						
					XSSFSheet ID_Sheet=workbook.cloneSheet(1);
					workbook.setSheetName(workbook.getSheetIndex(ID_Sheet.getSheetName()), Wafer_ID_R);	
					
					for (int j = 0; j < Bin_Array.length; j++) {
						Integer bin_value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							 bin_value=Bin_Summary_R.get(Bin_Array[j]);
						}				
						ID_Sheet.getRow(17+j).getCell(1).setCellValue(bin_value);
						ID_Sheet.getRow(17+j).getCell(2).setCellValue((double)bin_value/Integer.valueOf(GrossDie_R));
						
					}
					ID_Sheet.getRow(3).getCell(1).setCellValue(Device);
					ID_Sheet.getRow(4).getCell(1).setCellValue(Device);
					ID_Sheet.getRow(5).getCell(1).setCellValue(Lot);
					ID_Sheet.getRow(6).getCell(1).setCellValue(RightID_R);
					ID_Sheet.getRow(7).getCell(1).setCellValue(waferSize_R);
					if (Notch_R!=null) {
						if (Notch_R.equals("UP")) 
							ID_Sheet.getRow(8).getCell(1).setCellValue(0);
						else if (Notch_R.equals("RIGHT")) 
							ID_Sheet.getRow(8).getCell(1).setCellValue(90);
						else if (Notch_R.equals("DOWN")) 
							ID_Sheet.getRow(8).getCell(1).setCellValue(180);
						else if (Notch_R.equals("LEFT")) 
							ID_Sheet.getRow(8).getCell(1).setCellValue(270);
					}else {
						ID_Sheet.getRow(8).getCell(1).setCellValue(0);
					}
					ID_Sheet.getRow(9).getCell(1).setCellValue("20"+TestStartTime_R+"00");
					ID_Sheet.getRow(10).getCell(1).setCellValue("20"+TestEndTime_R+"00");
					ID_Sheet.getRow(11).getCell(1).setCellValue(GrossDie_R);
					ID_Sheet.getRow(12).getCell(1).setCellValue(PassDie_R);
					ID_Sheet.getRow(13).getCell(1).setCellValue(FailDie_R);
					
					for (int j = TestDie_MinY_R; j < Integer.valueOf(MapRows_R)+TestDie_MinY_R; j++) {
						for (int j2 = TestDie_MinX_R; j2 < Integer.valueOf(MapCols_R)+TestDie_MinX_R; j2++) {
							if (Map_R[j][j2]!=null) {
								XSSFCell Map_Cell=ID_Sheet.getRow(j-TestDie_MinY_R+2).createCell(j2-TestDie_MinX_R+5);
								Integer value=Integer.valueOf(Map_R[j][j2]);
								XSSFCellStyle localstyle=null;
								if (value<32) {
									localstyle=Colors_Array.get(value-1);
								}else {
									localstyle=Bin_32;
								}
								Map_Cell.setCellStyle(localstyle);								
								Map_Cell.setCellValue(value);
							}
						}
					}					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
			}			
			sheet.getRow(2).getCell(2).setCellValue(TotalTestDie);
			sheet.getRow(2).getCell(5).setCellValue(TotalPassdie);
			sheet.getRow(2).getCell(8).setCellValue(Double.parseDouble(String.format("%.2f", (double)TotalPassdie/TotalTestDie)));
			sheet.getRow(2).getCell(10).setCellValue(TotalFailDie);

			XSSFRow Sum_Row=sheet.getRow(32);			
			XSSFCell Total_yield=Sum_Row.getCell(1);
			Total_yield.setCellFormula("AVERAGE(B7:B32)");
			for(int j=0;j<Bin_Array.length;j++)
			{
				Sum_Row.getCell(j+5).setCellFormula("AVERAGE("+(char)(j+70)+"7:"+(char)(j+70)+"32)");
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
			workbook.removeSheetAt(1);
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
