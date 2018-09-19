package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel16 extends Report_Model {

	private static final File Model=new File("/Config/NTO.xlsx");
	public ExceLReportModel16() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
		//	FileOutputStream outputStream=new FileOutputStream(Result_Excel);
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("REPORT");
			XSSFRow Row_Summary=sheet.getRow(2);
			Row_Summary.createCell(1).setCellValue(Local_lot);
			Row_Summary.createCell(0).setCellValue(Device);
			Row_Summary.createCell(10).setCellValue(Device);
			Row_Summary.createCell(3).setCellValue(propertiesFirst.get("WF_Size"));
			Row_Summary.createCell(11).setCellValue(propertiesFirst.get("Tester Program"));
			Row_Summary.createCell(2).setCellValue(file.listFiles().length);
			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd HH:mm");
			Row_Summary.createCell(7).setCellValue(format.format(new Date()));		
			for (XSSFCellStyle style : Colors_Array) {
				style.setBorderBottom((short)1);
				style.setBorderLeft((short)1);
				style.setBorderRight((short)1);
				style.setBorderTop((short)1);
				style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
				style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
				style.setRightBorderColor(IndexedColors.WHITE.getIndex());
				style.setTopBorderColor(IndexedColors.WHITE.getIndex());
			}
			
			String Local_Time=null;
			String Version="NA";

			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					String TestStartTime_R=properties.get("Test Start Time");

					String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
					Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
					Integer Row_R=Integer.parseInt(properties.get("Map Rows"));
					
	
					String Tester_R=properties.get("Tester ID");
					String Time=TestStartTime_R.substring(0, 4)+"-"+Integer.valueOf(TestStartTime_R.substring(4,6))+"-"+Integer.valueOf(TestStartTime_R.substring(6,8));			
					XSSFRow Rows=sheet.getRow(RightID_R+3);
					Rows.getCell(0).setCellValue("VT");
					Rows.getCell(1).setCellValue(Time);
					Rows.getCell(2).setCellValue(Device);
					Rows.getCell(3).setCellValue(Tester_R);
					Rows.getCell(4).setCellValue(Local_lot);
					Rows.getCell(5).setCellValue(RightID_R);
					Rows.getCell(6).setCellValue(GrossDie_R);
					Rows.getCell(7).setCellValue(PassDie_R);
					Rows.getCell(8).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));
					if (Local_Time==null) {
						Local_Time=TestStartTime_R;
					}
					//
					
					
					for (int j = 1; j < 33; j++) {
						
						XSSFCell Bin_Cell;
						if (j==2) {
							continue;
						}else {
							if (j==1) {
								Bin_Cell=Rows.getCell(9);
							}else {
								Bin_Cell=Rows.getCell(7+j);
							}
							if (Bin_Summary_Map_R.containsKey(j)) {
								Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
							}else
							{
								Bin_Cell.setCellValue(0);
							}
						}
					}
					String sheetName=RightID_R.toString();
					if (RightID_R<10) {
						sheetName="0"+RightID_R.toString();
					}
					XSSFSheet ID_Sheet=workbook.createSheet(sheetName);
					ID_Sheet.setZoom(25);
					ID_Sheet.setDefaultColumnWidth(1);
					
					for (int j = 0; j < Row_R; j++) {
						XSSFRow Map_Row=ID_Sheet.createRow(j+7);
						Map_Row.setHeightInPoints(12);
						for (int k = 0; k < Col_R; k++) {
							XSSFCell Bin_Cell=Map_Row.createCell(k);
							if (MapCell_R[j][k]!=null) {
								if (MapCell_R[j][k].equals("M")) {
									Bin_Cell.setCellValue(" ");
								}else if (MapCell_R[j][k].equals("S")) {
									Bin_Cell.setCellValue(" ");
								}else if (MapCell_R[j][k].equals(".")) {
									Bin_Cell.setCellValue("");
								}else {
									if (MapCell_R[j][k].equals("0")) {
										Bin_Cell.setCellStyle(Colors_Array.get(0));
									}else {
										Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(MapCell_R[j][k])-1));
									}
								}
							}
						}
					}
					XSSFRow Map_Row1=ID_Sheet.createRow(0);
					XSSFRow Map_Row2=ID_Sheet.createRow(1);
					XSSFRow Map_Row3=ID_Sheet.createRow(2);
					XSSFRow Map_Row4=ID_Sheet.createRow(3);
					XSSFRow Map_Row5=ID_Sheet.createRow(4);
					XSSFRow Map_Row6=ID_Sheet.createRow(5);
					XSSFRow Map_Row7=ID_Sheet.createRow(6);
					Map_Row1.setHeightInPoints(15);
					Map_Row2.setHeightInPoints(15);
					Map_Row3.setHeightInPoints(15);
					Map_Row4.setHeightInPoints(15);
					Map_Row5.setHeightInPoints(15);
					Map_Row6.setHeightInPoints(15);
					Map_Row7.setHeightInPoints(15);
					
			        CellRangeAddress cra1=new CellRangeAddress(0, 0, 0, 3);   
			        CellRangeAddress cra11=new CellRangeAddress(0, 0, 4, 13); 
			        CellRangeAddress cra12=new CellRangeAddress(0, 0, 15, 25); 
			        CellRangeAddress cra13=new CellRangeAddress(0, 0, 27, 37); 
			        CellRangeAddress cra14=new CellRangeAddress(0, 0, 39, 49); 
			        CellRangeAddress cra15=new CellRangeAddress(0, 0, 51, 61); 
			        ID_Sheet.addMergedRegion(cra1);
			        ID_Sheet.addMergedRegion(cra11);
			        ID_Sheet.addMergedRegion(cra12);
			        ID_Sheet.addMergedRegion(cra13);
			        ID_Sheet.addMergedRegion(cra14);
			        ID_Sheet.addMergedRegion(cra15);
			        
			        CellRangeAddress cra2=new CellRangeAddress(1, 1, 0, 3);  
			        CellRangeAddress cra21=new CellRangeAddress(1, 1, 4, 13); 
			        CellRangeAddress cra22=new CellRangeAddress(1, 1, 15, 25); 
			        CellRangeAddress cra23=new CellRangeAddress(1, 1, 27, 37); 
			        CellRangeAddress cra24=new CellRangeAddress(1, 1, 39, 49); 
			        CellRangeAddress cra25=new CellRangeAddress(1, 1, 51, 61); 
			        ID_Sheet.addMergedRegion(cra2);
			        ID_Sheet.addMergedRegion(cra21);
			        ID_Sheet.addMergedRegion(cra22);
			        ID_Sheet.addMergedRegion(cra23);
			        ID_Sheet.addMergedRegion(cra24);
			        ID_Sheet.addMergedRegion(cra25);
			        
			        
			        CellRangeAddress cra3=new CellRangeAddress(2, 2, 0, 3); 
			        CellRangeAddress cra31=new CellRangeAddress(2, 2, 4, 13); 
			        CellRangeAddress cra32=new CellRangeAddress(2, 2, 15, 25); 
			        CellRangeAddress cra33=new CellRangeAddress(2, 2, 27, 37); 
			        CellRangeAddress cra34=new CellRangeAddress(2, 2, 39, 49); 
			        CellRangeAddress cra35=new CellRangeAddress(2, 2, 51, 61); 
			        ID_Sheet.addMergedRegion(cra3);
			        ID_Sheet.addMergedRegion(cra31);
			        ID_Sheet.addMergedRegion(cra32);
			        ID_Sheet.addMergedRegion(cra33);
			        ID_Sheet.addMergedRegion(cra34);
			        ID_Sheet.addMergedRegion(cra35);
			        
			        
			        CellRangeAddress cra4=new CellRangeAddress(3, 3, 0, 3);  
			        CellRangeAddress cra41=new CellRangeAddress(3, 3, 4, 13); 
			        CellRangeAddress cra42=new CellRangeAddress(3, 3, 15, 25); 
			        CellRangeAddress cra43=new CellRangeAddress(3, 3, 27, 37); 
			        CellRangeAddress cra44=new CellRangeAddress(3, 3, 39, 49); 
			        CellRangeAddress cra45=new CellRangeAddress(3, 3, 51, 61); 
			        ID_Sheet.addMergedRegion(cra4);
			        ID_Sheet.addMergedRegion(cra41);
			        ID_Sheet.addMergedRegion(cra42);
			        ID_Sheet.addMergedRegion(cra43);
			        ID_Sheet.addMergedRegion(cra44);
			        ID_Sheet.addMergedRegion(cra45);
			        
			        CellRangeAddress cra5=new CellRangeAddress(4, 4, 0, 3); 
			        CellRangeAddress cra51=new CellRangeAddress(4, 4, 4, 13); 
			        CellRangeAddress cra52=new CellRangeAddress(4, 4, 15, 25); 
			        CellRangeAddress cra53=new CellRangeAddress(4, 4, 27, 37); 
			        CellRangeAddress cra54=new CellRangeAddress(4, 4, 39, 49); 
			        CellRangeAddress cra55=new CellRangeAddress(4, 4, 51, 61); 
			        ID_Sheet.addMergedRegion(cra5);
			        ID_Sheet.addMergedRegion(cra51);
			        ID_Sheet.addMergedRegion(cra52);
			        ID_Sheet.addMergedRegion(cra53);
			        ID_Sheet.addMergedRegion(cra54);
			        ID_Sheet.addMergedRegion(cra55);
			        
			        CellRangeAddress cra6=new CellRangeAddress(5, 5, 0, 3);  
			        CellRangeAddress cra61=new CellRangeAddress(5, 5, 4, 13); 
			        CellRangeAddress cra62=new CellRangeAddress(5, 5, 15, 25); 
			        CellRangeAddress cra63=new CellRangeAddress(5, 5, 27, 37); 
			        CellRangeAddress cra64=new CellRangeAddress(5, 5, 39, 49); 
			        CellRangeAddress cra65=new CellRangeAddress(5, 5, 51, 61); 
			        ID_Sheet.addMergedRegion(cra6);
			        ID_Sheet.addMergedRegion(cra61);
			        ID_Sheet.addMergedRegion(cra62);
			        ID_Sheet.addMergedRegion(cra63);
			        ID_Sheet.addMergedRegion(cra64);
			        ID_Sheet.addMergedRegion(cra65);
			        CellRangeAddress cra7=new CellRangeAddress(6, 6, 0, 3);   
			        CellRangeAddress cra71=new CellRangeAddress(6, 6, 4, 13); 
			        CellRangeAddress cra72=new CellRangeAddress(6, 6, 15, 25); 
			        CellRangeAddress cra73=new CellRangeAddress(6, 6, 27, 37); 
			        CellRangeAddress cra74=new CellRangeAddress(6, 6, 39, 49); 
			        CellRangeAddress cra75=new CellRangeAddress(6, 6, 51, 61); 
			        ID_Sheet.addMergedRegion(cra7);
			        ID_Sheet.addMergedRegion(cra71);
			        ID_Sheet.addMergedRegion(cra72);
			        ID_Sheet.addMergedRegion(cra73);
			        ID_Sheet.addMergedRegion(cra74);
			        ID_Sheet.addMergedRegion(cra75);
			  
			        CellRangeAddress cra8=new CellRangeAddress(0, 0, 63, 73);
			        CellRangeAddress cra81=new CellRangeAddress(1, 1, 63, 73); 
			        CellRangeAddress cra82=new CellRangeAddress(2, 2, 63, 73);
			        CellRangeAddress cra83=new CellRangeAddress(3, 3, 63, 73);
			        ID_Sheet.addMergedRegion(cra8);
			        ID_Sheet.addMergedRegion(cra81);
			        ID_Sheet.addMergedRegion(cra82);
			        ID_Sheet.addMergedRegion(cra83);
			        Map_Row1.createCell(62).setCellStyle(Bin_21);
			        Map_Row2.createCell(62).setCellStyle(Bin_22);
			        Map_Row3.createCell(62).setCellStyle(Bin_23);
			        Map_Row4.createCell(62).setCellStyle(Bin_24);
			        Map_Row1.createCell(63).setCellValue("BIN29:FAIL");
			        Map_Row2.createCell(63).setCellValue("BIN30:FAIL");
			        Map_Row3.createCell(63).setCellValue("BIN31:FAIL");
			        Map_Row4.createCell(63).setCellValue("BIN32:FAIL");
			        
					Map_Row1.createCell(0).setCellValue("Device Name");
					Map_Row2.createCell(0).setCellValue("Wafer Id");
					Map_Row3.createCell(0).setCellValue("Wafer Size");
					Map_Row4.createCell(0).setCellValue("Total Pass");
					Map_Row5.createCell(0).setCellValue("Total Fail");
					Map_Row6.createCell(0).setCellValue("Total Tested");
					Map_Row7.createCell(0).setCellValue("Yield");
					
		      
					Map_Row1.createCell(4).setCellValue(Device);
					Map_Row2.createCell(4).setCellValue(RightID_R);
					Map_Row3.createCell(4).setCellValue(propertiesFirst.get("WF_Size")+" inch");

					XSSFCell pass_Die_Cell=Map_Row4.createCell(4);
					pass_Die_Cell.setCellStyle(Center_Style);
					pass_Die_Cell.setCellValue(PassDie_R);
					
					XSSFCell Total_Fail_Die_Cell=Map_Row5.createCell(4);
					Total_Fail_Die_Cell.setCellStyle(Center_Style);
					Total_Fail_Die_Cell.setCellValue(GrossDie_R);
					
					XSSFCell Total_Die_Cell=Map_Row6.createCell(4);
					Total_Die_Cell.setCellStyle(Center_Style);
					Total_Die_Cell.setCellValue(GrossDie_R);
							
					XSSFCell Yeild_Cell=Map_Row7.createCell(4);
					Yeild_Cell.setCellStyle(Data_Style);
					Yeild_Cell.setCellValue(Double.parseDouble((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));
					
					
					ArrayList<XSSFRow> Map_Rows=new ArrayList<>();
					Map_Rows.add(Map_Row1);
					Map_Rows.add(Map_Row2);
					Map_Rows.add(Map_Row3);
					Map_Rows.add(Map_Row4);
					Map_Rows.add(Map_Row5);
					Map_Rows.add(Map_Row6);
					Map_Rows.add(Map_Row7);
					
					for (int l=0;l<Colors_Array.size();l++) {
						if (l<7) {
							Map_Rows.get(l).createCell(14).setCellStyle(Colors_Array.get(l));
							if (l!=0) 
								Map_Rows.get(l).createCell(15).setCellValue("BIN"+(l+1)+":FAIL");
							else
								Map_Rows.get(l).createCell(15).setCellValue("BIN"+(l+1)+":PASS");
						}
						if (l<14&&l>6) {
							Map_Rows.get(l-7).createCell(26).setCellStyle(Colors_Array.get(l));
							Map_Rows.get(l-7).createCell(27).setCellValue("BIN"+(l+1)+":FAIL");
						}
						if (l<21&&l>13) {
							Map_Rows.get(l-14).createCell(38).setCellStyle(Colors_Array.get(l));
							Map_Rows.get(l-14).createCell(39).setCellValue("BIN"+(l+1)+":FAIL");
						}
					}
					for (int j =7; j <14 ; j++) {
							Map_Rows.get(j-7).createCell(50).setCellStyle(Colors_Array.get(j+14));
							Map_Rows.get(j-7).createCell(51).setCellValue("BIN"+(j+15)+":FAIL");
						
					}				
				} catch (Exception e) {
					// TODO: handle exception
					//continue;	
					e.printStackTrace();
				}
			}	
			HashMap<String, String> NameMap=InitMap(Lot, CP, Local_Time, Device, Version);
			Set<String> keyset=NameMap.keySet();
			String Path=Result_Excel.getParent();
			String FileName=Result_Excel.getName();
			System.out.println(CP);
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
