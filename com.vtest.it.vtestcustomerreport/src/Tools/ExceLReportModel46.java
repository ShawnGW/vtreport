package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class ExceLReportModel46 extends Report_Model {

	private static final File Model=new File("/Config/GXL_D_Format_Update.xlsx");
	private final Integer[] Bin_Array={1,10,20,21,22,30,31,32,33,34,35,36,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,60,70,71,72,73,74,75,76,77,80,81,82,83,84,85,86,90,91,92,410,411,412,413,414,415,900,902,904,905,906,907
};
	public ExceLReportModel46() throws IOException {
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

			Integer MES_GrossDie=Integer.valueOf(propertiesFirst.get("MES Test Gross Die"));
			ArrayList<String> ErrorCollection=new ArrayList<>();
			XSSFSheet sheet=workbook.getSheet("Summary");
			//workbook.createSheet("Map");
			XSSFRow Row_Summary=sheet.getRow(3);		
			Row_Summary.getCell(6).setCellValue(Local_lot);
			Row_Summary.getCell(0).setCellValue(Device);
			Row_Summary.getCell(14).setCellValue(MES_GrossDie);
			Row_Summary.getCell(12).setCellValue(file.listFiles().length);
			Row_Summary.getCell(33).setCellValue(propertiesFirst.get("Tester Program"));
			
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
	
			Integer ID_ROW=0;
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
					String[][] Map_R=parseRawdata.getAllDiesDimensionalArraySoftBin();
					
					String TestStartTime_R=properties.get("Test Start Time");
					if (!GrossDie_R.equals(MES_GrossDie)) {
						ErrorCollection.add(Wafer_ID_R+" : "+GrossDie_R);
					}
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+6);
					Rows.getCell(1).setCellValue(PassDie_R);
					Rows.getCell(2).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));
					for (int j = 0; j < Bin_Array.length; j++) {
						Integer Value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							Value=Bin_Summary_R.get(Bin_Array[j]);
						}
						if (Value==0) 
							Rows.getCell(j+3).setCellValue("");
						else 
							Rows.getCell(j+3).setCellValue(Value);
					}
					XSSFSheet ID_Sheet=workbook.getSheet("Map");
					ID_Sheet.setDefaultColumnWidth(1);	
					int Max_Row=0;
					if (Integer.valueOf(MapRows_R)>Bin_Array.length+3) {
						Max_Row=Integer.valueOf(MapRows_R);
					}else {
						Max_Row=Bin_Array.length+3;
					}
					for (int j = 0; j < Max_Row; j++) {
						Integer Col_R=Integer.valueOf(MapCols_R);
						XSSFRow Map_Row=ID_Sheet.createRow(j+ID_ROW);
						if (j==0) {
							Map_Row.createCell(0).setCellValue(Wafer_ID_R);
							Map_Row.getCell(0).setCellStyle(Right_Style);
						}
						Map_Row.setHeightInPoints(6);
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
					ID_ROW+=Max_Row+2;
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
			for(int j=0;j<Bin_Array.length;j++)
			{	
				if (j>48) {
					XSSFCell Total_Cell=Sum_Row.getCell(j+3);					
					Total_Cell.setCellFormula("SUM(B"+(char)(16+j)+"8:B"+(char)(16+j)+"32)");	
				}
				else if (j>22) {
					XSSFCell Total_Cell=Sum_Row.getCell(j+3);					
					Total_Cell.setCellFormula("SUM(A"+(char)(42+j)+"8:A"+(char)(42+j)+"32)");
				}else
				{
					XSSFCell Total_Cell=Sum_Row.getCell(j+3);
					Total_Cell.setCellFormula("SUM("+(char)(68+j)+"8:"+(char)(68+j)+"32)");
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
			if (ErrorCollection.size()>0) {
				FileWriter writer=new FileWriter(new File(Path+"/error.log"));
				PrintWriter printWriter=new PrintWriter(writer);
				for (String error : ErrorCollection) {
					printWriter.print(error+"\r\n");
				}
				printWriter.close();
			}else {
				File log=new File(Path+"/error.log");
				if (log.exists()) {
					log.delete();
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
