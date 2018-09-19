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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel54 extends Report_Model {

	private static final File Model=new File("/Config/SFD.xlsx");
	private final Integer[] Bin_Array={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63
};
	public ExceLReportModel54() throws IOException {
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

			XSSFSheet sheet=workbook.getSheet("summary");
			XSSFRow Row_Summary=sheet.getRow(3);
			Row_Summary.getCell(4).setCellValue(Local_lot);
			Row_Summary.getCell(1).setCellValue("FM347U04_128CP1");
			Row_Summary.getCell(8).setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
			String program=propertiesFirst.get("Tester Program");
			program=(program.endsWith("()")?program.substring(0, program.length()-2):program);
			Row_Summary.getCell(7).setCellValue(file.listFiles().length);

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
			
			SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddhhmmss");
			SimpleDateFormat format2=new SimpleDateFormat("MM-dd");
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String TestStartTime_R=properties.get("Test Start Time");
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+6);
					Rows.getCell(2).setCellValue(PassDie_R);
					Rows.getCell(3).setCellValue(Double.valueOf((String.format("%.2f", (double)PassDie_R*100/(GrossDie_R)))));
					for (int j = 0; j < Bin_Array.length; j++) {
						Integer Value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							Value=Bin_Summary_R.get(Bin_Array[j]);
						}
						if (Value==0) 
							Rows.getCell(j+4).setCellValue(0);
						else 
							Rows.getCell(j+4).setCellValue(Value);
					}
					Rows.getCell(68).setCellFormula("SUM(E"+(RightID_R+7)+":BP"+(RightID_R+7)+")-C"+(RightID_R+7));
					Rows.getCell(71).setCellValue(format2.format(format1.parse(properties.get("Test Start Time"))));
					Rows.getCell(72).setCellValue(properties.get("Operator"));
					Rows.getCell(73).setCellValue(properties.get("Tester ID"));
					Rows.getCell(74).setCellValue(properties.get("Prober ID"));
					Rows.getCell(75).setCellValue(properties.get("Prober Card ID"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;	
					
				}
			}
			XSSFRow Sum_Row=sheet.getRow(32);
			Sum_Row.getCell(2).setCellFormula("SUM(C8:C32)");
			XSSFCell Total_yield=Sum_Row.getCell(3);
			Total_yield.setCellFormula("AVERAGE(D8:D32)");
			for(int j=0;j<Bin_Array.length+1;j++)
			{
				XSSFCell Total_Cell=Sum_Row.getCell(j+4);
				if(j>47)
				{
					Total_Cell.setCellFormula("SUM(B"+(char)(17+j)+"8:B"+(char)(17+j)+"32)");
				}
				else if (j<22){
					Total_Cell.setCellFormula("SUM("+(char)(69+j)+"8:"+(char)(69+j)+"32)");
				 }
				else if (j>21){
					 Total_Cell.setCellFormula("SUM(A"+(char)(43+j)+"8:A"+(char)(43+j)+"32)");
				 }
			}
			sheet.getRow(34).getCell(1).setCellValue("测试机："+propertiesFirst.get("Tester ID")+" 探针台："+propertiesFirst.get("Prober ID")+"  测试程序名："+program+"   填表人：V011   审核人：V005");
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
