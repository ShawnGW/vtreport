package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel11 extends Report_Model {

	private static final File Model=new File("/Config/THC.xlsx");
	public ExceLReportModel11() throws IOException {
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
			
			sheet.getRow(0).getCell(1).setCellValue(Device);
			sheet.getRow(0).getCell(4).setCellValue(Local_lot);
			sheet.getRow(0).getCell(7).setCellValue(file.listFiles().length);
			sheet.getRow(0).getCell(10).setCellValue(propertiesFirst.get("WF_Size")+"¡å");
			sheet.getRow(0).getCell(16).setCellValue(propertiesFirst.get("MES Test Gross Die"));
			sheet.getRow(2).getCell(17).setCellValue(propertiesFirst.get("Tester Program"));
			
			String Time=null;
			String Version="NA";

			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					Integer[] BinValueSummary=new Integer[65];
					for (int j = 0; j < BinValueSummary.length; j++) {
						BinValueSummary[j]=0;
					}
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();
					Set<Integer> keyset=Bin_Summary_Map_R.keySet();
					for (Integer key : keyset) {
						if (key==0)
							BinValueSummary[64]=Bin_Summary_Map_R.get(key);
						else
							BinValueSummary[key-1]=Bin_Summary_Map_R.get(key);
					}
					String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
					Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
					Integer Row_R=Integer.parseInt(properties.get("Map Rows"));
					Integer FailDie_R=Integer.parseInt(properties.get("Fail Die"));
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					String TestStartTime_R=properties.get("Test Start Time");
					String Tester_R=properties.get("Tester ID");
					String Prober_R=properties.get("Prober ID");
					String Notch_R=properties.get("Notch").split("-")[0];
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+4);
					Rows.getCell(0).setCellValue(RightID_R);
					Rows.getCell(1).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/GrossDie_R))));
					Rows.getCell(2).setCellValue(PassDie_R);
					Rows.getCell(3).setCellValue(FailDie_R);
					StringBuffer TestTime=new StringBuffer();
					TestTime.append(TestStartTime_R.substring(0, 4));
					TestTime.append("."+TestStartTime_R.substring(4,6));
					TestTime.append("."+TestStartTime_R.substring(6,8));
					Rows.getCell(4).setCellValue(TestTime.toString());
					Rows.getCell(5).setCellValue(Tester_R);
					Rows.getCell(6).setCellValue(Prober_R);
					
					for (int j =2; j <12; j++) {
						XSSFCell Bin_Cell=Rows.getCell(j+5);
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue(0);
						}
					}
					
					XSSFSheet ID_Sheet=workbook.createSheet(Wafer_ID_R);
					ID_Sheet.setZoom(25);
					ID_Sheet.setDefaultColumnWidth(1);
					for (int j = 0; j < Row_R; j++) {
						XSSFRow Map_Row=ID_Sheet.createRow(j);
						Map_Row.setHeightInPoints(12);
						for (int k = 0; k < Col_R; k++) {						
							if (MapCell_R[j][k]!=null) {
								XSSFCell Bin_Cell=Map_Row.createCell(k);
								if (MapCell_R[j][k].equals("M")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals("S")) {
									Bin_Cell.setCellValue("#");
								}else if (MapCell_R[j][k].equals(".")) {
									Bin_Cell.setCellValue(" ");
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
					ID_Sheet.getRow(0).createCell(0).setCellValue("Device Name:");
					ID_Sheet.getRow(1).createCell(0).setCellValue("LotNum:");
					ID_Sheet.getRow(2).createCell(0).setCellValue("SlotNum:");
					ID_Sheet.getRow(3).createCell(0).setCellValue("Flat/Notch:");
					ID_Sheet.getRow(0).createCell(1).setCellValue(Device);
					ID_Sheet.getRow(1).createCell(1).setCellValue(Lot);
					ID_Sheet.getRow(2).createCell(1).setCellValue(RightID_R);
					ID_Sheet.getRow(3).createCell(1).setCellValue(Notch_R);
					
					ID_Sheet.createRow(Row_R+1).createCell(0).setCellValue("Total");
					ID_Sheet.getRow(Row_R+1).createCell(1).setCellValue(GrossDie_R);
					ID_Sheet.createRow(Row_R+2).createCell(0).setCellValue("Pass");
					ID_Sheet.getRow(Row_R+2).createCell(1).setCellValue(PassDie_R);
					XSSFCellStyle style=workbook.createCellStyle();
					style.setFillForegroundColor(IndexedColors.RED.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					ID_Sheet.getRow(Row_R+2).createCell(2).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R*100/GrossDie_R))));
					ID_Sheet.getRow(Row_R+2).getCell(2).setCellStyle(style);
					for(int k=0;k<33;k++)
					{
						Integer Bin_value=0;
						if (Bin_Summary_Map_R.containsKey(k)) {
							Bin_value=Bin_Summary_Map_R.get(k);
						}else
						{
							Bin_value=0;
						}
						XSSFRow row=ID_Sheet.createRow(Row_R+3+k);
						row.createCell(0).setCellValue("Bin"+k);
						row.createCell(1).setCellValue(Bin_value);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}	
			sheet.getRow(30).getCell(1).setCellFormula("AVERAGE(B6:B30)");
			sheet.getRow(30).getCell(2).setCellFormula("SUM(C6:C30)");
			sheet.getRow(30).getCell(3).setCellFormula("SUM(D6:D30)");
			for (int i = 0; i < 8; i++) {
				sheet.getRow(30).getCell(7+i).setCellFormula("SUM("+(char)(i+72)+"6:"+(char)(i+72)+"30)");
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
