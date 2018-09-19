package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel27 extends Report_Model {

	private static final File Model=new File("/Config/MCM.xlsx");
	public ExceLReportModel27() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("Bin_summary");
			
			String Time=null;
			String Version="NA";
			String formartOld="yyyyMMddhhmmss",formartNew="yy-MM-dd hh:mm";
			SimpleDateFormat format=new SimpleDateFormat(formartOld);
			SimpleDateFormat format2=new SimpleDateFormat(formartNew);
			
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					
					String TestStartTime_R=properties.get("Test Start Time");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					Integer Row_number=RightID_R+5;
					XSSFRow Rows=sheet.getRow(Row_number);
					String Yeild_R=properties.get("Wafer Yield");
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();

					String Device_R=properties.get("Device Name");
					String TestEndTime_R=properties.get("Test End Time");
					String GrossDie_R=properties.get("Gross Die");
					String PassDie_R=properties.get("Pass Die");
					String FailDie_R=properties.get("Fail Die");
					String waferSize_R=properties.get("WF_Size");
//					String MapCols_R=properties.get("Map Cols");
//					String MapRows_R=properties.get("Map Rows");
					String Notch_R=properties.get("Notch");
					String[][] Map_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
					Integer TestDie_MinX_R=Integer.valueOf(properties.get("TestDieleft"));
					Integer TestDie_MinY_R=Integer.valueOf(properties.get("TestDieup"));
					
					Integer MapCols_R=Integer.valueOf(properties.get("TestDieCol"));
					Integer MapRows_R=Integer.valueOf(properties.get("TestDieRow"));
					
					if (Time==null) {
						Time=TestStartTime_R;
					}
					for (int j = 0; j < 65; j++) {
						XSSFCell Bin_Cell=Rows.getCell(2+j);
						Integer Bin_value=0;
						if (Bin_Summary_R.containsKey(j)) {
							Bin_value=Bin_Summary_R.get(j);
						}
						Bin_Cell.setCellValue(Bin_value);
					}
					Rows.getCell(0).setCellValue(RightID_R);
					Rows.getCell(1).setCellValue(Float.parseFloat(Yeild_R.replace("%", ""))/100);					
					XSSFSheet ID_Sheet=workbook.cloneSheet(1);
					workbook.setSheetName(workbook.getSheetIndex(ID_Sheet.getSheetName()), Wafer_ID_R);	
					
					for (int j = 1; j < 64; j++) {
						Integer bin_value=0;
						if (Bin_Summary_R.containsKey(j)) {
							 bin_value=Bin_Summary_R.get(j);
						}
						if (j>54) {
							ID_Sheet.getRow(26+j).getCell(1).setCellValue(bin_value);
							ID_Sheet.getRow(26+j).getCell(2).setCellFormula("+B"+(19+j)+"/$B$13");
						}else {
							ID_Sheet.getRow(18+j).getCell(1).setCellValue(bin_value);
							ID_Sheet.getRow(18+j).getCell(2).setCellFormula("+B"+(19+j)+"/$B$13");
						}
					}
					ID_Sheet.getRow(18).getCell(2).setCellFormula("+B"+(19)+"/$B$13");
					ID_Sheet.getRow(18).getCell(2).setCellFormula("+B"+(84)+"/$B$13");
					
					ID_Sheet.getRow(15).getCell(1).setCellValue(Float.parseFloat(Yeild_R.replace("%", ""))/100);
					ID_Sheet.getRow(14).getCell(1).setCellValue(FailDie_R);
					ID_Sheet.getRow(13).getCell(1).setCellValue(PassDie_R);
					ID_Sheet.getRow(12).getCell(1).setCellValue(GrossDie_R);
					ID_Sheet.getRow(11).getCell(1).setCellValue(format2.format(format.parse(TestEndTime_R)));
					ID_Sheet.getRow(10).getCell(1).setCellValue(format2.format(format.parse(Time)));
//					ID_Sheet.getRow(11).getCell(1).setCellValue(TestEndTime_R.substring(0, 2)+"-"+TestEndTime_R.substring(2,4)+"-"+TestEndTime_R.substring(4,6)+" "+TestEndTime_R.substring(6,8)+":"+TestEndTime_R.substring(8,10));
//					ID_Sheet.getRow(10).getCell(1).setCellValue(Time.substring(0, 2)+"-"+Time.substring(2,4)+"-"+Time.substring(4,6)+" "+Time.substring(6,8)+":"+Time.substring(8,10));
					ID_Sheet.getRow(9).getCell(1).setCellValue(format2.format(format.parse(TestEndTime_R)));
					ID_Sheet.getRow(8).getCell(1).setCellValue(format2.format(format.parse(Time)));
					ID_Sheet.getRow(7).getCell(1).setCellValue(Notch_R.split("-")[0]);
					ID_Sheet.getRow(6).getCell(1).setCellValue(Integer.parseInt(waferSize_R)*10);
					ID_Sheet.getRow(5).getCell(1).setCellValue(Wafer_ID_R);
					ID_Sheet.getRow(4).getCell(1).setCellValue(Lot+"-"+CP);
					ID_Sheet.getRow(3).getCell(1).setCellValue(Device_R);
					for (int j = 0; j <MapRows_R ; j++) {
						for (int j2 = 0; j2 < MapCols_R; j2++) {
							if (Map_R[j][j2]!=null) {
								XSSFCell Map_Cell=ID_Sheet.getRow(j-TestDie_MinY_R+3).createCell(j2-TestDie_MinX_R+6);
								Integer value=Integer.valueOf(Map_R[j][j2]);
								if (value==1) {
									Map_Cell.setCellStyle(Bin_8);
								}else {
									Map_Cell.setCellStyle(Bin_2);
								}
								Map_Cell.setCellValue(value);
							}
						}
					}					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
			}
			sheet.getRow(0).getCell(2).setCellValue(Device);
			sheet.getRow(2).getCell(5).setCellValue(Lot);
			sheet.getRow(1).getCell(2).setCellValue("VTEST");
			sheet.getRow(1).getCell(5).setCellValue(propertiesFirst.get("Tester Program"));
			//sheet.getRow(1).getCell(8).setCellValue(Lot);
			sheet.getRow(2).getCell(8).setCellValue(file.listFiles().length);

			sheet.getRow(2).getCell(10).setCellValue(Time.substring(0, 2)+"-"+Time.substring(2,4)+"-"+Time.substring(4,6)+" "+Time.substring(6,8)+":"+Time.substring(8,10));
			sheet.getRow(3).getCell(2).setCellFormula("F4+K4");;
			sheet.getRow(3).getCell(5).setCellFormula("D32");;
			sheet.getRow(3).getCell(8).setCellFormula("F4/C4");;
			sheet.getRow(3).getCell(10).setCellFormula("SUM(E32:BO32)");
			sheet.getRow(32).getCell(1).setCellFormula("AVERAGE(B7:B31)");
			
			for (int i = 0; i < 65; i++) {
				if (i<24) {
					sheet.getRow(31).getCell(2+i).setCellFormula("SUM("+(char)(i+67)+"7:"+(char)(i+67)+"31)");
					sheet.getRow(32).getCell(2+i).setCellFormula((char)(i+67)+"32/$C4");
				}else if (23<i&&i<50){
					sheet.getRow(31).getCell(2+i).setCellFormula("SUM(A"+(char)(i+41)+"7:A"+(char)(i+41)+"31)");
					sheet.getRow(32).getCell(2+i).setCellFormula("A"+(char)(i+41)+"32/$C4");
				}else {
					sheet.getRow(31).getCell(2+i).setCellFormula("SUM(B"+(char)(i+15)+"7:B"+(char)(i+15)+"31)");
					sheet.getRow(32).getCell(2+i).setCellFormula("B"+(char)(i+15)+"32/$C4");
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
			workbook.removeSheetAt(1);
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
