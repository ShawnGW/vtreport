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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel34 extends Report_Model {

	private static final File Model=new File("/Config/HGK_Summary.xlsx");
	private static final Integer[] Bin_Array={1, 21, 22, 23, 24, 30, 31, 40, 41, 42, 43, 44, 45, 46, 50, 60, 61, 62};
	public ExceLReportModel34() throws IOException {
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
			sheet.getRow(0).getCell(0).setCellValue("A.Device       :"+Device);
			sheet.getRow(1).getCell(0).setCellValue("B.Lot No#     :"+Lot);
			sheet.getRow(8).getCell(0).setCellValue("I.Test Site       :VT");
			
			String Time=null;
			String Version="NA";
			String  Tester="NA";
			String Prober="NA";
			String ProberCard="NA";
			String Operator="NA";
			String StartTime="NA";
			
			HashMap<Integer, Integer> Bin_summary=new HashMap<>();
		
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String TestStartTime_R=properties.get("Test Start Time");
					String Tester_R=properties.get("Tester ID");
					String Prober_R=properties.get("Prober ID");
					String ProberCard_R=properties.get("Prober Card ID");

					String OPerater_R=properties.get("Operator");

					if (Tester.equals("NA")) {
						Tester=Tester_R;
					}if (Prober.equals("NA")) {
						Prober=Prober_R;
					}
					if (Operator.equals("NA")) {
						Operator=OPerater_R;
					}
					if (ProberCard.equals("NA")) {
						ProberCard=ProberCard_R;
					}
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					Integer Row_number=RightID_R+11;
					sheet.createRow(Row_number);
					
					
					for (int j = 0; j < Bin_Array.length; j++) {
						
						Integer value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							value=Bin_Summary_R.get(Bin_Array[j]);
						}
						if (Bin_summary.containsKey(Bin_Array[j])) {
							Bin_summary.put(Bin_Array[j], Bin_summary.get(Bin_Array[j])+value);
						}else {
							Bin_summary.put(Bin_Array[j], value);
						}
						sheet.getRow(Row_number).createCell(j+1).setCellValue(value);
					}
					sheet.getRow(Row_number).createCell(0).setCellValue(RightID_R);
					sheet.getRow(Row_number).createCell(19).setCellValue(GrossDie_R);
					sheet.getRow(Row_number).createCell(20).setCellValue(Double.parseDouble(String.format("%.4f", (double)PassDie_R/GrossDie_R)));
					sheet.getRow(Row_number).getCell(20).setCellStyle(Data_Style);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
			}
			if (Time!=null) {
				StartTime="20"+Time.substring(0, 2)+"/"+Time.substring(2,4)+"/"+Time.substring(4, 6);
			}
			sheet.getRow(2).getCell(0).setCellValue("C.Tester#       :"+Tester);
			sheet.getRow(3).getCell(0).setCellValue("D.Prober#      :"+Prober);
			sheet.getRow(4).getCell(0).setCellValue("E.Operator     :"+Operator);
			sheet.getRow(5).getCell(0).setCellValue("F.Probe Card  :"+ProberCard);
			sheet.getRow(6).getCell(0).setCellValue("G.Program      :"+propertiesFirst.get("Tester Program"));
			sheet.getRow(7).getCell(0).setCellValue("H.Start Date    :"+StartTime);
			sheet.createRow(37);
			sheet.getRow(37).createCell(0).setCellValue("Total");
			Integer TotalDie=0;
			for (int i = 0; i < Bin_Array.length; i++) {
				XSSFCell Total_Cell=sheet.getRow(37).createCell(i+1);
				Integer value=0;
				if (Bin_summary.containsKey(Bin_Array[i])) {
					value=Bin_summary.get(Bin_Array[i]);
				}
				Total_Cell.setCellValue(value);
				TotalDie+=value;
				//sheet.getRow(37).getCell(i+1).setCellFormula("");
			}
			sheet.getRow(37).createCell(20).setCellFormula("AVERAGE(U13:U37)");
			sheet.getRow(37).getCell(20).setCellStyle(Data_Style);
			sheet.getRow(37).createCell(19).setCellFormula("SUM(T13:T37)");
			
			sheet.createRow(41);
			sheet.createRow(42);
			for (int i = 0; i < Bin_Array.length; i++) {
				XSSFCell Total_Cell=sheet.getRow(41).createCell(i);
				XSSFCell Avg_Cell=sheet.getRow(42).createCell(i);
				Integer value=0;
				if (Bin_summary.containsKey(Bin_Array[i])) {
					value=Bin_summary.get(Bin_Array[i]);
				}
				Total_Cell.setCellValue(value);
				Avg_Cell.setCellValue(Double.parseDouble(String.format("%.4f", (double)value/TotalDie)));
				Avg_Cell.setCellStyle(Data_Style);
				sheet.getRow(46+i).getCell(2).setCellValue(Double.parseDouble(String.format("%.4f", (double)value/TotalDie)));
				
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
