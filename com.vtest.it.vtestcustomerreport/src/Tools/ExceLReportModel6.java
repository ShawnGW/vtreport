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

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.xml.sax.SAXException;

import parseRawdata.parseRawdata;

public class ExceLReportModel6 extends Report_Model {

	private static final File Model=new File("/Config/SNU.xlsx");
	public ExceLReportModel6() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		try {			
		//	FileOutputStream outputStream=new FileOutputStream(Result_Excel);
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("Bin_Summary");
			XSSFRow Row_Summary=sheet.getRow(2);
			XSSFRow Row_Summary2=sheet.getRow(3);
			XSSFCell Device_Cell=Row_Summary.getCell(8);
			XSSFCell Lot_Cell=Row_Summary2.getCell(2);
			XSSFCell Sum_Cell=Row_Summary2.getCell(8);
			XSSFCell Date_Cell=Row_Summary.getCell(13);
			
			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd HH:mm");
			Date_Cell.setCellValue(format.format(new Date()));
			Device_Cell.setCellValue(Device);
			Lot_Cell.setCellValue(Local_lot);
			Sum_Cell.setCellValue(file.listFiles().length);
			
			ArrayList<String> Bin_Defination_Array = null;
			try {
				Bin_Defination_Array = GetSoftBinDefination.CallserviceForDoc(Device);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] Bin_Defination=new String[32];
			for (int i = 0; i < Bin_Defination.length; i++) {
				Bin_Defination[i]="Fail";
			}
			String Version="NA";
			String Report_Dev="NA";
			for (String BinIdInformation : Bin_Defination_Array) {
				String Value=BinIdInformation.split("&")[3];
				if (!Value.equals("VERSION")&&!Value.equals("report_dev")) 
				{
					Integer id=Integer.valueOf(BinIdInformation.split("&")[2]);
					Bin_Defination[id-1]=Value;
				}else if(Value.equals("VERSION"))
				{
					Version=BinIdInformation.split("&")[2].toUpperCase();
				}else if (Value.equals("report_dev")) {
					Report_Dev=BinIdInformation.split("&")[2];
				}
			}
			
			XSSFRow Bin_OneToSeven=sheet.getRow(32);
			XSSFCell Bin_OneToSeven_information=Bin_OneToSeven.getCell(1);
			StringBuffer sbBufferOneToSeven=new StringBuffer();
			for(int i=0;i<7;i++)
			{
				sbBufferOneToSeven.append("BIN"+(i+1)+"="+Bin_Defination[i]+"      ");
			}
			Bin_OneToSeven_information.setCellValue(sbBufferOneToSeven.toString());
			
			XSSFRow Bin_EightTofifth=sheet.getRow(33);
			XSSFCell Bin_EightTofifth_information=Bin_EightTofifth.getCell(1);
			StringBuffer sbBufferEightTofifth=new StringBuffer();
			for(int i=7;i<15;i++)
			{
				sbBufferEightTofifth.append("BIN"+(i+1)+"="+Bin_Defination[i]+"      ");
			}
			Bin_EightTofifth_information.setCellValue(sbBufferEightTofifth.toString());
			
			XSSFRow Bin_SixtyTotwentyThree=sheet.getRow(34);
			XSSFCell Bin_SixtyTotwentyThree_information=Bin_SixtyTotwentyThree.getCell(1);
			StringBuffer sbBufferSixtyTotwentyThree=new StringBuffer();
			for(int i=15;i<23;i++)
			{
				sbBufferSixtyTotwentyThree.append("BIN"+(i+1)+"="+Bin_Defination[i]+"      ");
			}
			Bin_SixtyTotwentyThree_information.setCellValue(sbBufferSixtyTotwentyThree.toString());
			
			XSSFRow Bin_TwentyFourToThreetyTwo=sheet.getRow(35);
			XSSFCell Bin_TwentyFourToThreetyTwo_information=Bin_TwentyFourToThreetyTwo.getCell(1);
			StringBuffer sbBufferTwentyFourToThreetyTwo=new StringBuffer();
			for(int i=23;i<32;i++)
			{
				sbBufferTwentyFourToThreetyTwo.append("BIN"+(i+1)+"="+Bin_Defination[i]+"      ");
			}
			Bin_TwentyFourToThreetyTwo_information.setCellValue(sbBufferTwentyFourToThreetyTwo.toString());
		
			
			
			XSSFRow Total_Row=sheet.getRow(30);		
			for (int i =0; i < 35; i++) {
				if (i<25) {
					XSSFCell aver_cell=Total_Row.getCell(i+1);					
					aver_cell.setCellFormula("SUM("+(char)(i+66)+"6:"+(char)(i+66)+"30)");
				}else
				{
					XSSFCell aver_cell=Total_Row.getCell(i+1);					
					aver_cell.setCellFormula("SUM(A"+(char)(i+40)+"6:A"+(char)(i+40)+"30)");
				}
			}		
			Total_Row.getCell(36).setCellFormula("C31/B31");
			
			String Time=null;
			

		
			Integer Total_PassDie=0;
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));
					
					String TestStartTime_R=properties.get("Test Start Time");
					TreeMap<Integer, Integer> Bin_Summary_Map_R=parseRawdata.getBinSummary();
					Integer Total_Fail_Die_R=Integer.parseInt(properties.get("Fail Die"));

					if (Time==null) {
						Time=TestStartTime_R;
					}
					XSSFRow Wafer_ID_Row=sheet.getRow(RightID_R+4);
					
					XSSFCell WaferID_Cell=Wafer_ID_Row.getCell(0);
					WaferID_Cell.setCellValue(RightID_R+"#");

					XSSFCell GrossDie_Cell=Wafer_ID_Row.getCell(1);
					GrossDie_Cell.setCellValue(Integer.valueOf(propertiesFirst.get("MES Test Gross Die")));
	
					XSSFCell PassDie_Cell=Wafer_ID_Row.getCell(2);
					PassDie_Cell.setCellValue(PassDie_R);
					
					XSSFCell Yield_Cell=Wafer_ID_Row.getCell(36);
					Yield_Cell.setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(PassDie_R+Total_Fail_Die_R)))));
					
					for (int j = 1; j <33 ; j++) {
						if (j==1) {
							if (Bin_Summary_Map_R.containsKey(j)) {
								Total_PassDie+=Bin_Summary_Map_R.get(j);
							}
						}
						XSSFCell Bin_Cell=Wafer_ID_Row.getCell(2+j);
						if (Bin_Summary_Map_R.containsKey(j)) {
							Bin_Cell.setCellValue(Bin_Summary_Map_R.get(j));
						}else
						{
							Bin_Cell.setCellValue("");
						}
					}
					sheet.getRow(31).getCell(1).setCellValue("TOTAL PASS(³ýÍâÓ^²»Á¼£©="+Total_PassDie);
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}	
			
			HashMap<String, String> NameMap=InitMap(Lot, CP, Time, Report_Dev, Version);
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
