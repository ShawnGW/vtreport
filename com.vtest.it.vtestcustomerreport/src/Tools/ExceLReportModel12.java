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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;
public class ExceLReportModel12 extends Report_Model {

	private static final File Model=new File("/Config/PRA.xlsx");
	public ExceLReportModel12() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			String tester="NA",prober="NA",processYield="NA",proberCardId="NA";
					
			ArrayList<String> Bin_Defination_Array =new ArrayList<>();
			try {
				Bin_Defination_Array=GetSoftBinDefination.CallserviceForDoc(Device);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String[] Bin_Defination=new String[32];
			for (int i = 0; i < Bin_Defination.length; i++) {
				Bin_Defination[i]="Fail";
			}
			try {
				for (String BinIdInformation : Bin_Defination_Array) {
					String Value=BinIdInformation.split("&")[3];
					if (Value.contains("Version")) {
						
					}else if (BinIdInformation.split("&")[2].matches("^[0-9]{1,}$")) 
					{
						Integer id=Integer.valueOf(BinIdInformation.split("&")[2]);
						Bin_Defination[id-1]=Value;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			XSSFSheet sheet=workbook.getSheet("Bin_Summary");
			sheet.getRow(2).getCell(3).setCellValue(CustomerCode);
			sheet.getRow(2).getCell(11).setCellValue(Device);
			sheet.getRow(3).getCell(3).setCellValue(Lot);

			sheet.getRow(3).getCell(11).setCellValue(file.list().length);
			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd HH:mm");	
			sheet.getRow(2).getCell(27).setCellValue(format.format(new Date()));
			sheet.getRow(36).getCell(4).setCellValue(format.format(new Date()));
			sheet.getRow(0).getCell(29).setCellValue(propertiesFirst.get("Tester Program"));
			
			String Time=null;
			String Version="NA";

			File[] files=file.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
			}
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					if (tester.equals("NA")) {
						tester=properties.get("Tester ID");
					}
					if (prober.equals("NA")) {
						prober=properties.get("Prober ID");
					}
					if (processYield.equals("NA")) {
						processYield=properties.get("Process Yield");
					}
					if (proberCardId.equals("NA")) {
						proberCardId=properties.get("Prober Card ID");
					}
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();
					String TestStartTime_R=properties.get("Test Start Time");
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+4);
					Rows.getCell(0).setCellValue(RightID_R+"#");
					Rows.getCell(1).setCellValue(GrossDie_R);
					Rows.getCell(33).setCellValue(0);
					Rows.getCell(34).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/GrossDie_R))));
					
					for (int j = 1; j <32 ; j++) {
						XSSFCell Bin_Cell=Rows.getCell(1+j);
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue(0);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
			}
			sheet.getRow(2).getCell(17).setCellValue(tester);
			sheet.getRow(3).getCell(17).setCellValue(prober);
			sheet.getRow(3).getCell(27).setCellValue(processYield);
			sheet.getRow(1).getCell(29).setCellValue(proberCardId);
			XSSFRow Total_Row=sheet.getRow(30);			
			for(int j=0;j<34;j++)
			{
				if (j==33) {
					XSSFCell Total_Cell=Total_Row.getCell(34);
					Total_Cell.setCellFormula("AVERAGE(AI6:AI30)");
				}
				else if (j>24) {
					XSSFCell Total_Cell=Total_Row.getCell(j+1);
					Total_Cell.setCellFormula("SUM(A"+(char)(40+j)+"6:A"+(char)(40+j)+"30)");
				}else
				{
					XSSFCell Total_Cell=Total_Row.getCell(j+1);
					Total_Cell.setCellFormula("SUM("+(char)(66+j)+"6:"+(char)(66+j)+"30)");
				}
				
			}
			XSSFRow Fail_Total_Row=sheet.getRow(31);		
			Fail_Total_Row.getCell(8).setCellFormula("SUM(D31:AH31)");
			Fail_Total_Row.getCell(24).setCellFormula("SUM(D31:AH31)");

			int row=31;
			int col=2;
			for (int i = 0; i < Bin_Defination.length; i++) {							
				if (i!=0&&(i%4)==0) {
					row=32;		
					col+=4;
				}else
				{
					row++;
				}
				sheet.getRow(row).getCell(col).setCellValue(Bin_Defination[i]);
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
