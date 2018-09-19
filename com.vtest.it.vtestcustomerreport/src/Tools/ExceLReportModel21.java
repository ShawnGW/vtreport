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

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel21 extends Report_Model {

	private static final File Model=new File("/Config/NSY.xlsx");
	public ExceLReportModel21() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			

			String TestTime=null;
			boolean flag=false;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			workbook.setSheetName(0, Lot+"_YieldReport");
			XSSFSheet sheet=workbook.getSheet(Lot+"_YieldReport");
			sheet.getRow(3).getCell(0).setCellValue(Device);
			sheet.getRow(3).getCell(2).setCellValue(Lot);
			sheet.getRow(3).getCell(15).setCellValue(file.listFiles().length);
			sheet.getRow(3).getCell(19).setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
			
			XSSFFont font_Mark=workbook.createFont();
			font_Mark.setFontName("Tahoma");
			font_Mark.setFontHeight(6);
			XSSFCellStyle Mark_style=workbook.createCellStyle();
			Mark_style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			Mark_style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			Mark_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Mark_style.setFont(font_Mark);

			XSSFCellStyle Skip_style=workbook.createCellStyle();
			Skip_style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			Skip_style.setFillForegroundColor(IndexedColors.RED.getIndex());
			Skip_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Skip_style.setFont(font_Mark);

			
			SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			sheet.getRow(3).getCell(11).setCellValue(format.format(new Date()));
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String GrossDie_R=properties.get("Gross Die");
					String PassDie_R=properties.get("Pass Die");
					String FailDie_R=properties.get("Fail Die");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					String WaferID_R=properties.get("Wafer ID");
					String TestStartTime_R=properties.get("Test Start Time").substring(2);

					if (!flag) {					
						String year="20"+TestStartTime_R.substring(0,2);
						String mounth=TestStartTime_R.substring(2,4);
						String day=TestStartTime_R.substring(4,6);
						String Hour=TestStartTime_R.substring(6,8);
						String Minute=TestStartTime_R.substring(8,10);
						TestTime=year+"年"+mounth+"月"+day+"日"+" "+Hour+":"+Minute+":00";
						flag=true;
					}
					System.out.println(WaferID_R);
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String[][] Map_R=parseRawdata.getAllDiesDimensionalArray();
					XSSFSheet workID=workbook.createSheet(WaferID_R);
					
					String MapCols_R=properties.get("Map Cols");
					String MapRows_R=properties.get("Map Rows");
					workID.setDefaultColumnWidth(1);
					workID.setDefaultRowHeightInPoints(6);
					for (int j = 0; j < Integer.valueOf(MapRows_R); j++) {
						XSSFRow waferID_Row=workID.createRow(j);
						waferID_Row.setHeightInPoints(6);
						for (int j2 = 0; j2 < Integer.valueOf(MapCols_R); j2++) {
							XSSFCell Bin_cell=waferID_Row.createCell(j2);
							if (Map_R[j][j2]==null) {
								Bin_cell.setCellValue(".");
							}else if (Map_R[j][j2].equals("M")) {
								Bin_cell.setCellStyle(Mark_style);
								Bin_cell.setCellValue("M");
							}else if (Map_R[j][j2].equals("S")) {
								Bin_cell.setCellStyle(Skip_style);
								Bin_cell.setCellValue("S");
							}else {
								Bin_cell.setCellStyle(Colors_Array.get(Integer.valueOf(Map_R[j][j2])-1));
								Bin_cell.setCellValue(Map_R[j][j2]);
							}
						}
					}
					XSSFRow Rows=sheet.getRow(6+i);
					Rows.getCell(0).setCellValue(RightID_R);
					Rows.getCell(1).setCellValue(Integer.valueOf(GrossDie_R));
					Rows.getCell(2).setCellValue(Integer.valueOf(PassDie_R));
					Rows.getCell(3).setCellValue(Integer.valueOf(FailDie_R));
					Rows.getCell(4).setCellValue(Double.valueOf((String.format("%.4f", (double)Integer.valueOf(PassDie_R)/Integer.valueOf(GrossDie_R)))));
					for (int j = 0; j <32; j++) {
						XSSFCell Bin_Cell=Rows.getCell(5+j);
						if (Bin_Summary_R.containsKey(j+1)) {
							Bin_Cell.setCellValue(Bin_Summary_R.get(j+1));
						}else {
							Bin_Cell.setCellValue("");
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}
			sheet.getRow(3).getCell(8).setCellValue(TestTime);
			XSSFRow TotalRow=sheet.getRow(31);
			TotalRow.getCell(1).setCellFormula("SUM(B7:B31)");
			TotalRow.getCell(2).setCellFormula("SUM(C7:C31)");
			TotalRow.getCell(3).setCellFormula("SUM(D7:D31)");
			TotalRow.getCell(4).setCellFormula("AVERAGE(E7:E31)");
			for (int i = 0; i < 33; i++) {
				if (i<21)
					TotalRow.getCell(i+5).setCellFormula("SUM("+(char)(i+70)+"7:"+(char)(i+70)+"31)");
				else
					TotalRow.getCell(i+5).setCellFormula("SUM(A"+(char)(i+44)+"7:A"+(char)(i+44)+"31)");
			}
			String Time=null;
			String Version="NA";
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
