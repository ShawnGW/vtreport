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

public class ExceLReportModel51 extends Report_Model {

	private static final File Model=new File("/Config/WQT.xlsx");
	private final Integer[] Bin_Array={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32
};
	public ExceLReportModel51() throws IOException {
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

			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.getRow(3);
			Row_Summary.getCell(5).setCellValue(Local_lot);
			Row_Summary.getCell(0).setCellValue(Device);
			Row_Summary.getCell(13).setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
			String program=propertiesFirst.get("Tester Program");
			Row_Summary.getCell(32).setCellValue((program.endsWith("()")?program.substring(0, program.length()-2):program));
			Row_Summary.getCell(11).setCellValue(file.listFiles().length);

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
			Integer TotalD=0;
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					String MapCols_R=properties.get("Map Cols");
					String MapRows_R=properties.get("Map Rows");
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String[][] Map_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
					
					String TestStartTime_R=properties.get("Test Start Time");
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+6);
					Rows.getCell(0).setCellValue(RightID_R);
					Rows.getCell(1).setCellValue(PassDie_R);
					Rows.getCell(2).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));
					for (int j = 0; j < Bin_Array.length; j++) {
						Integer Value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							Value=Bin_Summary_R.get(Bin_Array[j]);
						}
						if (Value==0) 
							Rows.getCell(j+3).setCellValue(0);
						else 
							Rows.getCell(j+3).setCellValue(Value);
					}
					XSSFSheet ID_Sheet=workbook.createSheet(Wafer_ID_R);
					ID_Sheet.setZoom(25);
					ID_Sheet.setDefaultColumnWidth(1);	
					Integer MapRows=Integer.valueOf(MapRows_R);
					if (MapRows<Bin_Array.length) {
						MapRows=Bin_Array.length;
					}
					Integer Col_R=Integer.valueOf(MapCols_R);
					for (int j = 0; j < MapRows; j++) {
						XSSFRow Map_Row=ID_Sheet.createRow(j);
						if (j==0) {
							Map_Row.createCell(0).setCellValue("T");
							Map_Row.getCell(0).setCellStyle(Right_Style);
							Map_Row.createCell(1).setCellValue(Wafer_ID_R/*Integer.valueOf(Wafer_ID_R.split("-")[1].substring(0, 2))*/);
							Map_Row.getCell(1).setCellStyle(Right_Style);
						}
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
						if (j>2&&j<Bin_Array.length+3) {
							Integer value=0;
							if (Bin_Summary_R.containsKey(Bin_Array[j-3])) {
								value=Bin_Summary_R.get(Bin_Array[j-3]);
							}
							XSSFCell empty_cell=Map_Row.createCell(Col_R+3);
							if (Bin_Array[j-3]<33)								
								empty_cell.setCellStyle(Colors_Array.get(Bin_Array[j-3]-1));
							else
								empty_cell.setCellStyle(Colors_Array.get(31));
							empty_cell.setCellValue("");
							XSSFCell Bin_cell=Map_Row.createCell(Col_R+4);
							Bin_cell.setCellStyle(Right_Style);
							Bin_cell.setCellValue("Bin"+Bin_Array[j-3]);
							XSSFCell quanty_cell=Map_Row.createCell(Col_R+5);
							quanty_cell.setCellStyle(Right_Style);
							quanty_cell.setCellValue(value);
							XSSFCell Yid_cell=Map_Row.createCell(Col_R+6);
							Yid_cell.setCellStyle(DataStyle2);
							Yid_cell.setCellValue(Double.valueOf(String.format("%4.2f", (double)value*100/GrossDie_R))/100);

						}
						for (int k = 0; k < Col_R; k++) {						
							try {
								if (Map_R[j][k]!=null) {
									XSSFCell Bin_Cell=Map_Row.createCell(k);
									if (Map_R[j][k].equals("M")) {
										Bin_Cell.setCellValue("#");
									}else if (Map_R[j][k].equals("S")) {
										Bin_Cell.setCellValue("#");
									}else if (Map_R[j][k].equals(".")) {
										Bin_Cell.setCellValue(" ");
									}else {
										if (Map_R[j][k].equals("0")) {
											Bin_Cell.setCellStyle(Colors_Array.get(0));
											Bin_Cell.setCellValue(Integer.valueOf(Map_R[j][k]));
										}else {
											if (Integer.valueOf(Map_R[j][k])<33) {
												Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(Map_R[j][k])-1));
											}else {
												Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(Map_R[j][k].substring(0, 1))-1));
											}
											Bin_Cell.setCellValue(Integer.valueOf(Map_R[j][k]));
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					//ID_ROW+= Integer.valueOf(MapRows_R)+3;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;	
					
				}
			}
			XSSFRow Sum_Row=sheet.getRow(32);
			Sum_Row.getCell(1).setCellFormula("SUM(B8:B32)");
			XSSFCell Total_yield=Sum_Row.getCell(2);
			Total_yield.setCellFormula("AVERAGE(C8:C32)");
			for(int j=0;j<Bin_Array.length+1;j++)
			{
				XSSFCell Total_Cell=Sum_Row.getCell(j+3);
				if (j<23)			
					Total_Cell.setCellFormula("SUM("+(char)(68+j)+"8:"+(char)(68+j)+"32)");
				 if (j>22) 
					Total_Cell.setCellFormula("SUM(A"+(char)(42+j)+"8:A"+(char)(42+j)+"32)");
			}
			sheet.getRow(3).getCell(4).setCellValue(TotalD);
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
