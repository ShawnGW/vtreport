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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel3ETS extends Report_Model {

	private static final File Model=new File("/Config/ETS_Summary.xlsx");
	public ExceLReportModel3ETS() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("Summary");
			sheet.getRow(0).getCell(1).setCellValue(Device);
			sheet.getRow(1).getCell(1).setCellValue(Lot);
			sheet.getRow(2).getCell(1).setCellValue(propertiesFirst.get("WF_Size")+".0 inch");
			sheet.getRow(3).getCell(1).setCellFormula("COUNTA(D11:D35)");
			sheet.getRow(4).getCell(1).setCellValue(propertiesFirst.get("Tester Program")+".exe");
			for (XSSFCellStyle xssfCellStyle : Colors_Array) {
				xssfCellStyle.setBorderLeft((short)1);
				xssfCellStyle.setBorderRight((short)1);
				xssfCellStyle.setBorderTop((short)1);
				xssfCellStyle.setBorderBottom((short)1);
			}
			
			XSSFFont font=workbook.createFont();
			font.setFontHeight(6);
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
			
			Integer Total_TotalDie=0;
			Integer Total_PassDie=0;
			Integer Total_FailDie=0;
						
			String Time=null;
			String Version="NA";
	
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					Integer[] BinValueSummary=new Integer[65];
					for (int j = 0; j < BinValueSummary.length; j++) {
						BinValueSummary[j]=0;
					}
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();

					Set<Integer> keyset=Bin_Summary_Map_R.keySet();
					for (Integer key : keyset) {
						if (key==0)
							BinValueSummary[64]=Bin_Summary_Map_R.get(key);
						else if(key<65)
							BinValueSummary[key-1]=Bin_Summary_Map_R.get(key);
					}
					String Tester_R=properties.get("Tester ID");
					String Prober_R=properties.get("Prober ID");
					String ProberCard_R=properties.get("Prober Card ID");
					
					String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
					Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
					Integer Row_R=Integer.parseInt(properties.get("Map Rows"));

					Integer FailDie_R=Integer.parseInt(properties.get("Fail Die"));
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));

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
						if (j==2) {
							XSSFCell empty_cell=Map_Row.createCell(Col_R+3);
							empty_cell.setCellStyle(Right_Style);
							empty_cell.setCellValue("");
							XSSFCell Bin_cell=Map_Row.createCell(Col_R+4);
							Bin_cell.setCellStyle(Right_Style);
							Bin_cell.setCellValue("Bin#");
							XSSFCell quanty_cell=Map_Row.createCell(Col_R+5);
							quanty_cell.setCellStyle(Right_Style);
							quanty_cell.setCellValue("Qty");
							XSSFCell Yid_cell=Map_Row.createCell(Col_R+6);
							Yid_cell.setCellStyle(Right_Style);
							Yid_cell.setCellValue("YID");
						}
						if (j<35&&j>2) {
							Integer value=0;
							if (Bin_Summary_Map_R.containsKey(j-2)) {
								value=Bin_Summary_Map_R.get(j-2);
							}
							XSSFCell empty_cell=Map_Row.createCell(Col_R+3);
							empty_cell.setCellStyle(Colors_Array.get(j-3));
							empty_cell.setCellValue("");
							XSSFCell Bin_cell=Map_Row.createCell(Col_R+4);
							Bin_cell.setCellStyle(Right_Style);
							Bin_cell.setCellValue("Bin"+(j-2));
							XSSFCell quanty_cell=Map_Row.createCell(Col_R+5);
							quanty_cell.setCellStyle(Right_Style);
							quanty_cell.setCellValue(value);
							XSSFCell Yid_cell=Map_Row.createCell(Col_R+6);
							Yid_cell.setCellStyle(DataStyle2);
							Yid_cell.setCellValue(Double.valueOf(String.format("%4.2f", (double)value*100/GrossDie_R))/100);
						}
						if (j<65&&j>34) {
							Integer value=0;
							if (Bin_Summary_Map_R.containsKey(j-2)) {
								value=Bin_Summary_Map_R.get(j-2);
							}
							XSSFCell empty_cell=Map_Row.createCell(Col_R+3);
							empty_cell.setCellStyle(Colors_Array.get(31));
							empty_cell.setCellValue("");
							XSSFCell Bin_cell=Map_Row.createCell(Col_R+4);
							Bin_cell.setCellStyle(Right_Style);
							Bin_cell.setCellValue("Bin"+(j-2));
							XSSFCell quanty_cell=Map_Row.createCell(Col_R+5);
							quanty_cell.setCellStyle(Right_Style);
							quanty_cell.setCellValue(value);
							XSSFCell Yid_cell=Map_Row.createCell(Col_R+6);
							Yid_cell.setCellStyle(DataStyle2);
							Yid_cell.setCellValue(Double.valueOf(String.format("%4.2f", (double)value*100/GrossDie_R))/100);
						}
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
									}else if (Integer.valueOf(MapCell_R[j][k])>=33) {
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
					XSSFRow Wafer_ID_Row=sheet.getRow(i+10);
					XSSFCell Tester_Cell=Wafer_ID_Row.getCell(0);
					Tester_Cell.setCellStyle(Center_Style);
					Tester_Cell.setCellValue(Tester_R);

					XSSFCell Prober_Cell=Wafer_ID_Row.getCell(1);
					Prober_Cell.setCellStyle(Center_Style);
					Prober_Cell.setCellValue(Prober_R);

					XSSFCell Prober_Card_Cell=Wafer_ID_Row.getCell(2);
					Prober_Card_Cell.setCellStyle(Center_Style);
					Prober_Card_Cell.setCellValue(ProberCard_R);

					XSSFCell TotalDie_Cell=Wafer_ID_Row.createCell(4);
					TotalDie_Cell.setCellStyle(Center_Style);
					TotalDie_Cell.setCellValue(GrossDie_R);

					XSSFCell PassDie_Cell=Wafer_ID_Row.createCell(5);
					PassDie_Cell.setCellStyle(Center_Style);
					PassDie_Cell.setCellValue(PassDie_R);

					XSSFCell FailDie_Cell=Wafer_ID_Row.createCell(6);
					FailDie_Cell.setCellStyle(Center_Style);
					FailDie_Cell.setCellValue(FailDie_R);

					
					XSSFCell WaferID_Cell=Wafer_ID_Row.createCell(3);
					WaferID_Cell.setCellStyle(Center_Style);
					WaferID_Cell.setCellValue(Wafer_ID_R);
	
					XSSFCell Yield_Cell=Wafer_ID_Row.createCell(7);
					Yield_Cell.setCellStyle(Data_Style);
					Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/GrossDie_R))));
					
					for (int j = 0; j < BinValueSummary.length; j++) {
						Total_TotalDie+=BinValueSummary[j];
						if (j==0) {
							Total_PassDie+=BinValueSummary[j];
						}else
						{
							Total_FailDie+=BinValueSummary[j];
						}						
						if (j==64) {
							XSSFCell Bin_Cell=Wafer_ID_Row.createCell(8);
							Bin_Cell.setCellStyle(Center_Style);
							Bin_Cell.setCellValue(BinValueSummary[64]);
						}else {
							XSSFCell Bin_Cell=Wafer_ID_Row.createCell(9+j);
							Bin_Cell.setCellStyle(Center_Style);
							Bin_Cell.setCellValue(BinValueSummary[j]);
						}
					}



					sheet.getRow(36).createCell(4).setCellFormula("AVERAGE(E11:E35)");
					sheet.getRow(36).createCell(5).setCellFormula("AVERAGE(F11:F35)");
					sheet.getRow(36).createCell(6).setCellFormula("AVERAGE(G11:G35)");
					sheet.getRow(36).createCell(7).setCellFormula("AVERAGE(H11:H35)");
					sheet.getRow(36).getCell(7).setCellStyle(Data_Style);
					sheet.getRow(36).getCell(4).setCellStyle(Center_Style);
					sheet.getRow(36).getCell(5).setCellStyle(Center_Style);
					sheet.getRow(36).getCell(6).setCellStyle(Center_Style);					
					
					sheet.getRow(37).createCell(4).setCellValue(Total_TotalDie);
					sheet.getRow(37).createCell(5).setCellValue(Total_PassDie);
					sheet.getRow(37).createCell(6).setCellValue(Total_FailDie);
					
					sheet.getRow(37).getCell(4).setCellFormula("SUM(E11:E35)");
					sheet.getRow(37).getCell(5).setCellFormula("SUM(F11:F35)");
					sheet.getRow(37).getCell(6).setCellFormula("SUM(G11:G35)");
					sheet.getRow(37).createCell(7).setCellFormula("F38/E38");
					sheet.getRow(37).getCell(7).setCellStyle(Data_Style);
					
					sheet.getRow(5).createCell(1).setCellFormula("SUM(F11:F35)");
					sheet.getRow(6).createCell(1).setCellFormula("SUM(E11:E35)");;
					sheet.getRow(5).getCell(1).setCellStyle(Center_Style);
					sheet.getRow(6).getCell(1).setCellStyle(Center_Style);
					sheet.getRow(7).createCell(1).setCellFormula("B6/B7");;
					sheet.getRow(7).getCell(1).setCellStyle(Data_Style);
				} catch (Exception e) {
					// TODO: handle exception
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
