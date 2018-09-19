package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel13 extends Report_Model {

	private static final File Model=new File("/Config/AK1504E.xlsx");
	public ExceLReportModel13() throws IOException {
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

			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.getRow(1);
			XSSFCell Device_Cell=Row_Summary.getCell(1);
			XSSFCell Lot_Cell=Row_Summary.getCell(7);
			XSSFCell Sum_Cell=Row_Summary.getCell(10);
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
			Row_Summary.getCell(13).setCellValue(propertiesFirst.get("Tester Program"));
			
			
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
					Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					String TestStartTime_R=properties.get("Test Start Time");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					if (Time==null) {
						Time=TestStartTime_R;
					}
					XSSFSheet ID_Sheet=workbook.createSheet(Wafer_ID_R);
					ID_Sheet.setZoom(25);
					ID_Sheet.setDefaultColumnWidth(1);
					for (int j = 0; j < Row_R; j++) {
						XSSFRow Map_Row=ID_Sheet.createRow(j);
						Map_Row.setHeightInPoints(12);
						for (int k = 0; k < Col_R; k++) {
							XSSFCell Bin_Cell=Map_Row.createCell(k);
							if (MapCell_R[j][k]!=null) {
								if (MapCell_R[j][k].equals("M")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals("S")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals(".")) {
									Bin_Cell.setCellValue("");
								}else {
									if (MapCell_R[j][k].equals("0")) {
										Bin_Cell.setCellStyle(Colors_Array.get(0));
										Bin_Cell.setCellValue(Integer.valueOf(MapCell_R[j][k]));
									}else {
										Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(MapCell_R[j][k])-1));
										Bin_Cell.setCellValue(Integer.valueOf(MapCell_R[j][k]));
									}
								}
							}
						}
					}
					
					XSSFRow Wafer_ID_Row=sheet.getRow(28-Row_Count);
					XSSFCell WaferID_Cell=Wafer_ID_Row.getCell(1);
					WaferID_Cell.setCellValue(RightID_R);
							
					XSSFCell Order_Cell=Wafer_ID_Row.getCell(0);
					Order_Cell.setCellValue(i+1);
					XSSFCell PassDie_Cell=Wafer_ID_Row.getCell(4);
					PassDie_Cell.setCellValue(PassDie_R);
					
					XSSFCell GrossDie_Cell=Wafer_ID_Row.getCell(3);
					GrossDie_Cell.setCellValue(Total_Fail_Die_R+PassDie_R);
						
					XSSFCell Yield_Cell=Wafer_ID_Row.getCell(2);
					Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R*100/(PassDie_R+Total_Fail_Die_R)))));
					
					for (int j = 1; j < 21; j++) {
						XSSFCell Bin_Cell=Wafer_ID_Row.getCell(4+j);
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue("");
						}
					}
					Row_Count--;
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}	
			XSSFRow Total_Row=sheet.getRow(29);
			Total_Row.getCell(1).setCellFormula("COUNTA(B5:B29)");
			Total_Row.getCell(2).setCellFormula("E30/D30*100");
			for (int i = 0; i < 22; i++) {
				Total_Row.getCell(3+i).setCellFormula("SUM("+(char)(68+i)+"5:"+(char)(68+i)+"29)");
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
