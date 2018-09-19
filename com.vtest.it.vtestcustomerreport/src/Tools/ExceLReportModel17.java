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

public class ExceLReportModel17 extends Report_Model {

	private static final File Model=new File("/Config/NTO_QC.xlsx");
	public ExceLReportModel17() throws IOException {
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

			XSSFSheet sheet=workbook.getSheet("Sheet1");
			sheet.getRow(2).getCell(0).setCellValue("型号: "+Device+"   出货型号："+Device+"   批号："+Local_lot+"   片量："+file.listFiles().length+"   测试程序名："+propertiesFirst.get("Tester Program")+"   测试程序版本：V03   测试工步：OTP"+CP);
			XSSFRow Total_Row=sheet.getRow(31);
			Total_Row.getCell(1).setCellValue(file.listFiles().length+"Pcs");			
			Total_Row.getCell(2).setCellFormula("ROUND(E32*100/D32,2)");
			for (int j = 0; j < 33; j++) {
				if(j<23)
					Total_Row.getCell(3+j).setCellFormula("SUM("+(char)(j+68)+"7:"+(char)(j+68)+"31)");
				else
					Total_Row.getCell(3+j).setCellFormula("SUM(A"+(char)(j+42)+"7:A"+(char)(j+42)+"31)");
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

					String Tester_R=properties.get("Tester ID");

					String Prober_R=properties.get("Prober ID");
					String Notch_R=properties.get("Notch");

					String OPerater_R=properties.get("Operator");
					String ProberCard_R=properties.get("Prober Card ID");

					String Time=TestStartTime_R.substring(0, 4)+"-"+Integer.valueOf(TestStartTime_R.substring(4,6))+"-"+Integer.valueOf(TestStartTime_R.substring(6,8));			
					XSSFRow rows=sheet.getRow(RightID_R+5);
					if (Local_Time==null) {
						Local_Time=TestStartTime_R;
					}
					rows.getCell(1).setCellValue(RightID_R);
					rows.getCell(2).setCellValue(Double.valueOf((String.format("%.2f", (double)PassDie_R*100/(GrossDie_R)))));
					rows.getCell(3).setCellValue(GrossDie_R);
					rows.getCell(4).setCellValue(PassDie_R);
					for (int j = 1; j < 33; j++) {
						XSSFCell Bin_Cell;
						if (j==2) {
							continue;
						}else {
							if (j==1) {
								Bin_Cell=rows.getCell(5);
							}else {
								Bin_Cell=rows.getCell(3+j);
							}
							if (Bin_Summary_Map_R.containsKey(j)) {
								Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
							}else
							{
								Bin_Cell.setCellValue(0);
							}
						}
					}
					rows.getCell(37).setCellValue(Time);
					rows.getCell(38).setCellValue(OPerater_R);
					rows.getCell(39).setCellValue(Tester_R);
					rows.getCell(40).setCellValue(Prober_R);
					rows.getCell(41).setCellValue(ProberCard_R);
					if (Notch_R.equals("0-Degree")) {
						rows.getCell(43).setCellValue("上");
					}else if (Notch_R.equals("90-Degree")) {
						rows.getCell(43).setCellValue("右");//下左
					}else if (Notch_R.equals("180-Degree")) {
						rows.getCell(43).setCellValue("下");//下左
					}else if (Notch_R.equals("270-Degree")) {
						rows.getCell(43).setCellValue("左");//下
					}
					
					rows.getCell(44).setCellValue("V019");
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}
			HashMap<String, String> NameMap=InitMap(Lot, CP, Local_Time, Device, Version);
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
