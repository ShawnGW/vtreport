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

public class ExceLReportModel1s extends ReportModel2 {

	public ExceLReportModel1s() throws IOException {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void Write_Excel(String CustomerCode,String Device,String Lot,String CP,File file,File Result_Excel) {
		// TODO Auto-generated method stub
		try {		
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			Sheet sheet=workbook.getSheet("Summary");
			
			CellStyle blue_style=workbook.createCellStyle();
			blue_style.setAlignment(CellStyle.ALIGN_CENTER);
			blue_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			blue_style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			blue_style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			ArrayList<CellRangeAddress> RangList=new ArrayList<>();
			sheet.createRow(0);
			sheet.createRow(1);
			sheet.createRow(2);
			sheet.createRow(3);
			sheet.createRow(4);
			sheet.createRow(5);
			sheet.createRow(6);

			sheet.getRow(3).createCell(0).setCellValue("Product");
			sheet.getRow(3).createCell(1).setCellValue("Lot No.");
			sheet.getRow(3).createCell(2).setCellValue("Quantity");
			sheet.getRow(3).createCell(4).setCellValue("Wafer Size");
			sheet.getRow(3).createCell(6).setCellValue("Chip Size(um)");
			sheet.getRow(3).createCell(8).setCellValue("Chip QTY");
			sheet.getRow(3).createCell(10).setCellValue("Received Date");
			sheet.getRow(3).createCell(13).setCellValue("Testing Date");
			sheet.getRow(3).createCell(16).setCellValue("Probe Station");
			sheet.getRow(3).createCell(19).setCellValue("Test System");
			sheet.getRow(3).createCell(22).setCellValue("Device");
			sheet.getRow(3).createCell(25).setCellValue("Test Program");
			sheet.getRow(3).createCell(28).setCellValue("Ink Date");
			sheet.getRow(5).createCell(0).setCellValue("Wafer No.");
			sheet.getRow(5).getCell(0).setCellStyle(blue_style);
			sheet.getRow(5).createCell(1).setCellValue("Total");
			sheet.getRow(5).getCell(1).setCellStyle(blue_style);
			sheet.getRow(5).createCell(2).setCellValue("Pass");
			sheet.getRow(5).getCell(2).setCellStyle(blue_style);
			sheet.getRow(5).createCell(3).setCellValue("理论管芯数");
			sheet.getRow(5).getCell(3).setCellStyle(blue_style);
			sheet.getRow(5).createCell(4).setCellValue("Fail");
			sheet.getRow(5).getCell(4).setCellStyle(blue_style);
			sheet.getRow(5).createCell(5).setCellValue("Yield");
			sheet.getRow(5).getCell(5).setCellStyle(blue_style);
			sheet.getRow(2).createCell(0).setCellValue("Wafer Test Report");
			sheet.getRow(2).getCell(0).setCellStyle(Center_Style);
			for (int i = 0; i < 31; i++) {
				sheet.getRow(5).createCell(i+6).setCellValue("Bin "+(i+1));
				sheet.getRow(5).getCell(i+6).setCellStyle(blue_style);
			}
			
			CellRangeAddress rangeAddress_Report=new CellRangeAddress(2, 2, 0, 36);
			RangList.add(rangeAddress_Report);
			CellRangeAddress rangeAddress_empty=new CellRangeAddress(0, 1, 0, 36);
			RangList.add(rangeAddress_empty);
			CellRangeAddress rangeAddress_wafe=new CellRangeAddress(3, 3, 4, 5);
			RangList.add(rangeAddress_wafe);
			CellRangeAddress rangeAddress_chip=new CellRangeAddress(3, 3, 6, 7);
			RangList.add(rangeAddress_chip);
			CellRangeAddress rangeAddress_qty=new CellRangeAddress(3, 3, 8, 9);
			RangList.add(rangeAddress_qty);
			CellRangeAddress rangeAddress_recv=new CellRangeAddress(3, 3, 10, 12);
			RangList.add(rangeAddress_recv);
			CellRangeAddress rangeAddress_test=new CellRangeAddress(3, 3, 13, 15);
			RangList.add(rangeAddress_test);
			CellRangeAddress rangeAddress_stas=new CellRangeAddress(3, 3, 16, 18);
			RangList.add(rangeAddress_stas);
			CellRangeAddress rangeAddress_syst=new CellRangeAddress(3, 3, 19, 21);
			RangList.add(rangeAddress_syst);
			CellRangeAddress rangeAddress_Devi=new CellRangeAddress(3, 3, 22, 24);
			RangList.add(rangeAddress_Devi);
			CellRangeAddress rangeAddress_prog=new CellRangeAddress(3, 3, 25, 27);
			RangList.add(rangeAddress_prog);
			CellRangeAddress rangeAddress_ink=new CellRangeAddress(3, 3, 28, 30);
			RangList.add(rangeAddress_ink);
			CellRangeAddress rangeAddress_wafe2=new CellRangeAddress(4, 4, 4, 5);
			RangList.add(rangeAddress_wafe2);
			CellRangeAddress rangeAddress_chip2=new CellRangeAddress(4, 4, 6, 7);
			RangList.add(rangeAddress_chip2);
			CellRangeAddress rangeAddress_qty2=new CellRangeAddress(4, 4, 8, 9);
			RangList.add(rangeAddress_qty2);
			CellRangeAddress rangeAddress_recv2=new CellRangeAddress(4, 4, 10, 12);
			RangList.add(rangeAddress_recv2);
			CellRangeAddress rangeAddress_test2=new CellRangeAddress(4, 4, 13, 15);
			RangList.add(rangeAddress_test2);
			CellRangeAddress rangeAddress_stas2=new CellRangeAddress(4, 4, 16, 18);
			RangList.add(rangeAddress_stas2);
			CellRangeAddress rangeAddress_syst2=new CellRangeAddress(4, 4, 19, 21);
			RangList.add(rangeAddress_syst2);
			CellRangeAddress rangeAddress_Devi2=new CellRangeAddress(4, 4, 22, 24);
			RangList.add(rangeAddress_Devi2);
			CellRangeAddress rangeAddress_prog2=new CellRangeAddress(4, 4, 25, 27);
			RangList.add(rangeAddress_prog2);
			CellRangeAddress rangeAddress_ink2=new CellRangeAddress(4, 4, 28, 30);
			RangList.add(rangeAddress_ink2);
			for (CellRangeAddress cellRangeAddress : RangList) {
				sheet.addMergedRegion(cellRangeAddress);
			}
			Row Row_Summary=sheet.createRow(4);
			Cell Device_Cell=Row_Summary.createCell(0);
			Cell Lot_Cell=Row_Summary.createCell(1);
			Cell Sum_Cell=Row_Summary.createCell(2);
			Cell WaferSize_Cell=Row_Summary.createCell(4);
			Cell System_Cell=Row_Summary.createCell(19);
			Device_Cell.setCellStyle(Center_Style);
			Lot_Cell.setCellStyle(Center_Style);
			Sum_Cell.setCellStyle(Center_Style);
			WaferSize_Cell.setCellStyle(Center_Style);

			System_Cell.setCellStyle(Center_Style);	
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
			WaferSize_Cell.setCellValue(propertiesFirst.get("WF_Size")+" inch");
			System_Cell.setCellValue("STS");
			
			Row Averange_Row=sheet.createRow(31);
			Cell Averange_name=Averange_Row.createCell(0);
			Averange_name.setCellStyle(Left_Style);
			Averange_name.setCellValue("Average");
			for (int i = 1; i < 37; i++) {
				if (i==3) {
					continue;
				}
				if (i>25) {
					Cell aver_cell=Averange_Row.createCell(i);
					aver_cell.setCellFormula("AVERAGE(A"+(char)(i+39)+"7:A"+(char)(i+39)+"31)");
					
				}else
				{
					Cell aver_cell=Averange_Row.createCell(i);
					if (i==5) {
						aver_cell.setCellStyle(Data_Style);
						aver_cell.setCellFormula("AVERAGE("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
					}else
						aver_cell.setCellFormula("AVERAGE("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
				}
			}
			
			Row Total_Row=sheet.createRow(32);
			Cell Total_name=Total_Row.createCell(0);
			Total_name.setCellStyle(Left_Style);
			Total_name.setCellValue("Total");
			
			for (int i = 1; i < 37; i++) {
				if (i==3||i==5) {
					continue;
				}
				if (i>25) {
					Cell aver_cell=Total_Row.createCell(i);
					aver_cell.setCellFormula("SUM(A"+(char)(i+39)+"7:A"+(char)(i+39)+"31)");
				}else
				{
					Cell aver_cell=Total_Row.createCell(i);
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

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));
					
					int sum=GrossDie_R;
					boolean HVFlag=false;
					if (sum < 10) {
						HVFlag=true;
					}	
					if (HVFlag) {
						Bin_Summary_Map_R.remove(0);
					}
					if (Bin_Summary_Map_R.containsKey(0)) {
						Integer Bin0=Bin_Summary_Map_R.get(0);
						Integer tempBin0=Bin0;
						Bin_Summary_Map_R.remove(0);
						PassDie_R=PassDie_R-Bin0;
						if (Bin_Summary_Map_R.containsKey(5)) {
							Bin0+=Bin_Summary_Map_R.get(5);
						}
						Bin_Summary_Map_R.put(5, Bin0);					
						Total_Fail_Die_R=Total_Fail_Die_R+tempBin0;
					}
					String TestStartTime_R=properties.get("Test Start Time");
					
					if (Time==null) {
						Time=TestStartTime_R;
					}
					Sheet ID_Sheet=workbook.createSheet(Wafer_ID_R);	
					ID_Sheet.setZoom(1, 4);
					ID_Sheet.setDefaultColumnWidth(1);
					for (int j = 0; j < Row_R; j++) {
						Row Map_Row=ID_Sheet.createRow(j);
						Map_Row.setHeightInPoints(12);
						for (int k = 0; k < Col_R; k++) {
							Cell Bin_Cell=Map_Row.createCell(k);
							if (MapCell_R[j][k]!=null) {
								if (MapCell_R[j][k].equals("M")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals("S")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals(".")) {
									Bin_Cell.setCellValue("");
								}else {
									if (MapCell_R[j][k].equals("0")) {
										Bin_Cell.setCellStyle(Colors_Array.get(4));
										if (HVFlag) {
											Bin_Cell.setCellValue("S");
										}else {
											Bin_Cell.setCellValue(5);
										}
									}else if (Integer.valueOf(MapCell_R[j][k])>32) {
										Bin_Cell.setCellStyle(Colors_Array.get(31));
										Bin_Cell.setCellValue(Integer.valueOf(MapCell_R[j][k]));
									}else {
										Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(MapCell_R[j][k])-1));
										Bin_Cell.setCellValue(Integer.valueOf(MapCell_R[j][k]));
									}
								}
							}
						}
					}
					Row Wafer_ID_Row=sheet.createRow(30-Row_Count);
					Cell WaferID_Cell=Wafer_ID_Row.createCell(0);
					WaferID_Cell.setCellStyle(Left_Style);
					WaferID_Cell.setCellValue(Wafer_ID_R);
					
					Cell Total_Cell=Wafer_ID_Row.createCell(1);
					Total_Cell.setCellStyle(Right_Style);
					Total_Cell.setCellValue(PassDie_R+Total_Fail_Die_R);
					
					Cell PassDie_Cell=Wafer_ID_Row.createCell(2);
					PassDie_Cell.setCellStyle(Right_Style);
					PassDie_Cell.setCellValue(PassDie_R);
					
					Cell GrossDie_Cell=Wafer_ID_Row.createCell(3);
					GrossDie_Cell.setCellStyle(Right_Style);
					if (HVFlag) {
						GrossDie_Cell.setCellValue(sum);
					}else {
						GrossDie_Cell.setCellValue(Integer.valueOf(propertiesFirst.get("MES Stand Gross Die")));
					}
					
					Cell FailDie_Cell=Wafer_ID_Row.createCell(4);
					FailDie_Cell.setCellStyle(Right_Style);
					FailDie_Cell.setCellValue(Total_Fail_Die_R);
					
					Cell Yield_Cell=Wafer_ID_Row.createCell(5);
					Yield_Cell.setCellStyle(Data_Style);
					if (HVFlag) {
						Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(sum)))));
					}else {
						Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(Integer.valueOf(propertiesFirst.get("MES Stand Gross Die")))))));
					}					
					for (int j = 1; j < 32; j++) {
						Cell Bin_Cell=Wafer_ID_Row.createCell(5+j);
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
					System.out.println(Prober_Mappings[i].getName());
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
