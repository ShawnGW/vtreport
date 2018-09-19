package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel28 extends Report_Model {

	private static final File Model=new File("/Config/ArrivalOrder.xlsx");
	public ExceLReportModel28() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		XSSFCellStyle style=workbook.createCellStyle();
		style.setBorderTop((short)1);
		style.setBorderBottom((short)1);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		try {			
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			XSSFSheet sheet=workbook.getSheet("SHIP");
			String PassDies=propertiesFirst.get("Pass Bins");
			ArrayList<Integer> Pass_array=new ArrayList<>();			
			String[] PassBins=PassDies.split(",");
			try {
				for (int n = 0; n < PassBins.length; n++) {
					Pass_array.add(Integer.valueOf(PassBins[n]));
				}
			} catch (Exception e) {
				// TODO: handle exception
				Pass_array.add(1);
			}
			String Time=null;
			String Version="NA";
			
			HashMap<Integer, String> PassBin_Sum=new HashMap<>();	
			HashMap<String, HashMap<String, Integer>> GrossDiePassDieFailDie_Sum=new HashMap<>();
			HashMap<String, Integer> grossDieMap=new HashMap<>();
			HashMap<String, Integer> passDieMap=new HashMap<>();
			HashMap<String, Integer> failDieMap=new HashMap<>();
			HashMap<String, String> pidMap=new HashMap<>();
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					 Integer FailDie_R=Integer.parseInt(properties.get("Fail Die"));	 
					 grossDieMap.put(Wafer_ID_R, GrossDie_R);
					 passDieMap.put(Wafer_ID_R, PassDie_R);
					 failDieMap.put(Wafer_ID_R, FailDie_R);
					 pidMap.put(Wafer_ID_R, properties.get("Asy Device"));
					 String TestStartTime_R=properties.get("Test Start Time");
					 TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					for (Integer passbin : Pass_array) {
						if (PassBin_Sum.containsKey(passbin)) {
							if (Bin_Summary_R.containsKey(passbin)) {
								PassBin_Sum.put(passbin,PassBin_Sum.get(passbin)+":"+Wafer_ID_R+"&"+Bin_Summary_R.get(passbin).toString());
							}else
							{
								PassBin_Sum.put(passbin,PassBin_Sum.get(passbin)+":"+Wafer_ID_R+"&0");
							}
						}else {
							if (Bin_Summary_R.containsKey(passbin))
							{
								PassBin_Sum.put(passbin,Wafer_ID_R+"&"+Bin_Summary_R.get(passbin).toString());
							}else
							{
								PassBin_Sum.put(passbin,Wafer_ID_R+"&0");
							}
						}
					}
					if (Time==null) {
						Time=TestStartTime_R;
					}					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;				
				}
			}
			 GrossDiePassDieFailDie_Sum.put("GrossDie", grossDieMap);
			 GrossDiePassDieFailDie_Sum.put("PassDie", passDieMap);
			 GrossDiePassDieFailDie_Sum.put("FailDie", failDieMap);
				
			Set<Integer> PassKey=PassBin_Sum.keySet();
			Integer StartRow=1;
			boolean flag=false;
			if (PassKey.size()>1) {
				flag=true;
			}
			for (Integer pass : PassKey) {		
				if (pass<10000) {
					String[] Sigle_PassbinSum=PassBin_Sum.get(pass).split(":");	
					for (int i =0; i< Sigle_PassbinSum.length; i++) {
						XSSFRow Rows=sheet.getRow(StartRow);
						String waferId=Sigle_PassbinSum[i].split("&")[0];
						Rows.getCell(0).setCellValue(Lot);
						Rows.getCell(1).setCellValue(waferId.substring(0, waferId.length()-2));
						Rows.getCell(2).setCellValue(GrossDiePassDieFailDie_Sum.get("GrossDie").get(Sigle_PassbinSum[i].split("&")[0]));
						Rows.getCell(3).setCellValue(GrossDiePassDieFailDie_Sum.get("PassDie").get(Sigle_PassbinSum[i].split("&")[0]));
						Rows.getCell(4).setCellValue(GrossDiePassDieFailDie_Sum.get("FailDie").get(Sigle_PassbinSum[i].split("&")[0]));
						Rows.getCell(5).setCellValue("城东/芯片库");
						Rows.getCell(6).setCellValue("NORMAL");
						Rows.getCell(7).setCellValue("Mapping");
						Rows.getCell(8).setCellValue(Sigle_PassbinSum[i].split("&")[0].substring(0, Sigle_PassbinSum[i].split("&")[0].length()-2));
						Rows.getCell(9).setCellValue("\\\\172.17.220.254\\Mapping\\AEO\\"+Device);
						Rows.getCell(10).setCellValue("客供");
						Rows.getCell(11).setCellValue("指定");
						Rows.getCell(12).setCellValue("FALSE");
						if (flag) {
							Rows.getCell(16).setCellValue("Bin "+pass);
							Rows.getCell(17).setCellValue(Sigle_PassbinSum[i].split("&")[1]);
						}
						String pid=pidMap.get(waferId);
						Rows.getCell(21).setCellValue(pid);
						Rows.getCell(22).setCellValue(pid.length()>3?pid.substring(pid.length()-3, pid.length()):"NA");
						StartRow++;
					}
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
