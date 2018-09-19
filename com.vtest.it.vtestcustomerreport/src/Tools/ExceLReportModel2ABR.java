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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel2ABR extends Report_Model {
	private final static File Model=new File("/Config/ABR.xlsx");
	public ExceLReportModel2ABR() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file,File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.getRow(3);
			XSSFRow Row_Summary2=sheet.getRow(2);
			Row_Summary2.getCell(5).setCellValue(propertiesFirst.get("Customer Code"));
			XSSFCell Device_Cell=Row_Summary2.getCell(11);
			XSSFCell Lot_Cell=Row_Summary.getCell(5);
			XSSFCell Sum_Cell=Row_Summary.getCell(11);
			XSSFCell Date_Cell=Row_Summary2.getCell(18);
			sheet.getRow(38).getCell(5).setCellValue(propertiesFirst.get("Tester Program"));
			
			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd");
			Date_Cell.setCellValue(format.format(new Date()));
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
			
			XSSFRow Total_Row=sheet.getRow(31);		
			for (int i =3; i < 20; i++) {
				{
					XSSFCell aver_cell=Total_Row.getCell(i);					
					aver_cell.setCellFormula("SUM("+(char)(i+65)+"7:"+(char)(i+65)+"31)");
				}
			}		
			XSSFCell Averange_Cell=Total_Row.getCell(21);
			Averange_Cell.setCellFormula("E32/D32");
			
			sheet.getRow(33).getCell(8).setCellFormula("E32");
			sheet.getRow(33).getCell(18).setCellFormula("D32");
			sheet.getRow(34).getCell(4).setCellFormula("E32");
			sheet.getRow(35).getCell(4).setCellFormula("F32");
			sheet.getRow(36).getCell(4).setCellFormula("G32");
			sheet.getRow(37).getCell(4).setCellFormula("H32");
			
			sheet.getRow(34).getCell(8).setCellFormula("I32");
			sheet.getRow(35).getCell(8).setCellFormula("J32");
			sheet.getRow(36).getCell(8).setCellFormula("K32");
			sheet.getRow(37).getCell(8).setCellFormula("L32");
			
			sheet.getRow(34).getCell(13).setCellFormula("M32");
			sheet.getRow(35).getCell(13).setCellFormula("N32");
			sheet.getRow(36).getCell(13).setCellFormula("O32");
			sheet.getRow(37).getCell(13).setCellFormula("P32");
			
			sheet.getRow(34).getCell(18).setCellFormula("Q32");
			sheet.getRow(35).getCell(18).setCellFormula("R32");
			sheet.getRow(36).getCell(18).setCellFormula("S32");
			sheet.getRow(37).getCell(18).setCellFormula("T32");

			
			String Time=null;
			String Version="NA";
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();

					String[][] MapCell_R=parseRawdata.getAllDiesDimensionalArray();
					Integer Col_R=Integer.parseInt(properties.get("Map Cols"));
					Integer Row_R=Integer.parseInt(properties.get("Map Rows"));

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					String Tester_R=properties.get("Tester ID");
					String Prober_R=properties.get("Prober ID");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					String TestStartTime_R=properties.get("Test Start Time");
					
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFSheet ID_Sheet=workbook.createSheet(Wafer_ID_R);
					ID_Sheet.setDefaultColumnWidth(1);
					for (int j = 0; j < Row_R; j++) {
						XSSFRow Map_Row=ID_Sheet.createRow(j);
						Map_Row.setHeightInPoints(6);
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
					XSSFRow Wafer_ID_Row=sheet.getRow(RightID_R+5);
					XSSFCell WaferID_Cell=Wafer_ID_Row.getCell(0);
					WaferID_Cell.setCellValue(RightID_R+"#");

					XSSFCell Tester_Cell=Wafer_ID_Row.getCell(1);
					Tester_Cell.setCellValue(Tester_R);

					XSSFCell Prober_Cell=Wafer_ID_Row.getCell(2);
					Prober_Cell.setCellValue(Prober_R);

					XSSFCell GrossDie_Cell=Wafer_ID_Row.getCell(3);
					GrossDie_Cell.setCellValue(Integer.valueOf(propertiesFirst.get("MES Stand Gross Die")));
	

					try {
						XSSFCell Yield_Cell=Wafer_ID_Row.getCell(21);
						Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(Integer.valueOf(propertiesFirst.get("MES Stand Gross Die")))))));

					} catch (Exception e) {
						// TODO: handle exception
						XSSFCell Yield_Cell=Wafer_ID_Row.getCell(21);
						Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)0/(Integer.valueOf(propertiesFirst.get("MES Stand Gross Die")))))));
					}
					
					Integer others=0;
					Set<Integer> keyset=Bin_Summary_Map_R.keySet();
					for (Integer key : keyset) {
						if (key>15) {
							others+=Bin_Summary_Map_R.get(key);
						}
					}
					for (int j = 1; j < 17; j++) {
						XSSFCell Bin_Cell=Wafer_ID_Row.getCell(3+j);
						if (j==16) {
							Bin_Cell.setCellValue(others);
							continue;
						}
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue("");
						}
					}
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
