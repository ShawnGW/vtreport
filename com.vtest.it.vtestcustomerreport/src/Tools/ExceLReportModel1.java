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

public class ExceLReportModel1 extends Report_Model{
	
	public ExceLReportModel1()throws IOException {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode,String Device,String Lot,String CP,File file,File Result_Excel) {
		// TODO Auto-generated method stub
		try {		
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.createRow(4);
			XSSFCell Device_Cell=Row_Summary.createCell(0);
			XSSFCell Lot_Cell=Row_Summary.createCell(1);
			XSSFCell Sum_Cell=Row_Summary.createCell(2);
			XSSFCell WaferSize_Cell=Row_Summary.createCell(4);
			XSSFCell System_Cell=Row_Summary.createCell(19);
			Device_Cell.setCellStyle(Center_Style);
			Lot_Cell.setCellStyle(Center_Style);
			Sum_Cell.setCellStyle(Center_Style);
			WaferSize_Cell.setCellStyle(Center_Style);;

			System_Cell.setCellStyle(Center_Style);	
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
			WaferSize_Cell.setCellValue(propertiesFirst.get("WF_Size")+" inch");
			System_Cell.setCellValue("STS");
			
			XSSFRow Averange_Row=sheet.createRow(31);
			XSSFCell Averange_name=Averange_Row.createCell(0);
			Averange_name.setCellStyle(Left_Style);
			Averange_name.setCellValue("Average");
			for (int i = 1; i < 37; i++) {
				if (i==3) {
					continue;
				}
				if (i>25) {
					XSSFCell aver_cell=Averange_Row.createCell(i);
					aver_cell.setCellFormula("AVERAGE(A"+(char)(i+39)+"7:A"+(char)(i+39)+"31)");
				}else
				{
					XSSFCell aver_cell=Averange_Row.createCell(i);
					if (i==5) {
						aver_cell.setCellStyle(Data_Style);
						aver_cell.setCellFormula("AVERAGE("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
					}else
						aver_cell.setCellFormula("AVERAGE("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
				}
			}
			
			XSSFRow Total_Row=sheet.createRow(32);
			XSSFCell Total_name=Total_Row.createCell(0);
			Total_name.setCellStyle(Left_Style);
			Total_name.setCellValue("Total");
			
			for (int i = 1; i < 37; i++) {
				if (i==3||i==5) {
					continue;
				}
				if (i>25) {
					XSSFCell aver_cell=Total_Row.createCell(i);
					aver_cell.setCellFormula("SUM(A"+(char)(i+39)+"7:A"+(char)(i+39)+"31)");
				}else
				{
					XSSFCell aver_cell=Total_Row.createCell(i);
					if (i==5) {
						aver_cell.setCellStyle(Data_Style);
						aver_cell.setCellFormula("SUM("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
					}else
						aver_cell.setCellFormula("SUM("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
				}
			}		
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
					XSSFRow Wafer_ID_Row=sheet.createRow(30-Row_Count);
					XSSFCell WaferID_Cell=Wafer_ID_Row.createCell(0);
					WaferID_Cell.setCellStyle(Left_Style);
					WaferID_Cell.setCellValue(Wafer_ID_R);
					
					XSSFCell Total_Cell=Wafer_ID_Row.createCell(1);
					Total_Cell.setCellStyle(Right_Style);
					Total_Cell.setCellValue(PassDie_R+Total_Fail_Die_R);
					
					XSSFCell PassDie_Cell=Wafer_ID_Row.createCell(2);
					PassDie_Cell.setCellStyle(Right_Style);
					PassDie_Cell.setCellValue(PassDie_R);
					
					XSSFCell GrossDie_Cell=Wafer_ID_Row.createCell(3);
					GrossDie_Cell.setCellStyle(Right_Style);
					GrossDie_Cell.setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
					
					XSSFCell FailDie_Cell=Wafer_ID_Row.createCell(4);
					FailDie_Cell.setCellStyle(Right_Style);
					FailDie_Cell.setCellValue(Total_Fail_Die_R);
					
					XSSFCell Yield_Cell=Wafer_ID_Row.createCell(5);
					Yield_Cell.setCellStyle(Data_Style);
					Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(PassDie_R+Total_Fail_Die_R)))));
					
					for (int j = 1; j < 32; j++) {
						XSSFCell Bin_Cell=Wafer_ID_Row.createCell(5+j);
						Bin_Cell.setCellStyle(Right_Style);
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue(0);
						}
					}
					Row_Count--;	
					
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
