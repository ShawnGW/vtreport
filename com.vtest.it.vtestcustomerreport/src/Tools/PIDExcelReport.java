package Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.xml.sax.SAXException;

public class PIDExcelReport extends Report_Model{

	
	private static final File Model=new File("/Config/Production_Release_Form.xlsx");
	public PIDExcelReport() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
	}
	public void Write_Excel(String PID) throws IOException, ParserConfigurationException, SAXException {
		XSSFSheet ReleaseFormSheet=workbook.getSheet("release_form");
		HashMap<String, String> ValueMap=GetPIDInformations.CallserviceForDoc(PID);
		Integer baseRow=11;
		String UV=ValueMap.get("UV");
		String BAK=ValueMap.get("BAK");
		String CPT=ValueMap.get("CPT");
		String DescLenght=ValueMap.get("Lengths");
		String[] Lengths=DescLenght.substring(0, DescLenght.length()-1).split(":");
		int LotYield=0;
		int WaferYield=0;
		int OSYield=0;
		int StartIndex=0;
		
		int SiteStartindex=0;
		int SiteEndindex=0;
		int TimeStartindex=0;
		int TimeEndindex=0;
		int TestBaseStartindex=0;
		int TestBaseEndIndex=0;
		String Temperature=ValueMap.get("Temperature");
		HashMap<String, String> Temperaturemap=new HashMap<>();
		if (!Temperature.equals("--")) {
			String[] temperatures=Temperature.split(";");
			for (int i = 0; i < temperatures.length; i++) {
				Temperaturemap.put(temperatures[i].split(":")[0], temperatures[i].split(":")[1]);
			}
		}
		for (int i = 0; i < Lengths.length; i++) {
			if (i<4) {
				SiteStartindex+=Integer.valueOf(Lengths[i]);
			}
			if (i<5) {
				SiteEndindex+=Integer.valueOf(Lengths[i]);
			}
			if (i<6) {
				TimeStartindex+=Integer.valueOf(Lengths[i]);
			}
			if (i<7) {
				TimeEndindex+=Integer.valueOf(Lengths[i]);
			}
			if (i<8) {
				TestBaseStartindex+=Integer.valueOf(Lengths[i]);
			}
			if (i<9) {
				TestBaseEndIndex+=Integer.valueOf(Lengths[i]);
			}
			if (i<9) {
				StartIndex+=Integer.valueOf(Lengths[i]);
			}
			if (i==9) {
				LotYield=Integer.valueOf(Lengths[i]);
			}
			if (i==10) {
				WaferYield=Integer.valueOf(Lengths[i]);
			}
			if (i==11) {
				OSYield=Integer.valueOf(Lengths[i]);
			}
		}
		
		int waferStartindex=StartIndex+LotYield-1;
		int osStartindex=StartIndex+LotYield+WaferYield-1;
		int osendIndex=osStartindex+OSYield-1;
		
		if (UV.equals("Start")) {
			ReleaseFormSheet.getRow(12).getCell(19).setCellValue("--");
		}else {
			StringBuilder UVSB=new StringBuilder();
			String[] UVS=UV.split(":");
			for (int i = 1; i < UVS.length; i++) {
				String UVNumber=UVS[i].substring(0,5);
				String UVTime=UVS[i].substring(22,49).trim();
				UVSB.append(UVNumber+":"+UVTime+";");
			}
			ReleaseFormSheet.getRow(12).getCell(19).setCellValue(UVSB.toString().trim());
		}
		
		if (BAK.equals("Start")) {
			ReleaseFormSheet.getRow(12).getCell(7).setCellValue("--");
		}else {
			StringBuilder BKSB=new StringBuilder();
			String[] BAKS=BAK.split(":");
			for (int i = 1; i < BAKS.length; i++) {
				String UVNumber=BAKS[i].substring(0,5);
				String UVTime=BAKS[i].substring(22,49).trim();
				BKSB.append(UVNumber+":"+UVTime+";");
			}
			ReleaseFormSheet.getRow(12).getCell(7).setCellValue(BKSB.toString().trim());
		}
		if (!CPT.equals("Start")) {
			String[] CPTS=CPT.split(":");
			StringBuilder waferYield=new StringBuilder();
			StringBuilder lotYield=new StringBuilder();
			StringBuilder osYield=new StringBuilder();
			for (int i = 1; i < CPTS.length; i++) {
				Integer Row=baseRow+Integer.valueOf(CPTS[i].substring(2, 3))*2;
				if (!CPTS[i].substring(waferStartindex,osStartindex).trim().equals("")&&CPTS[i].substring(3,4).equals("T")) {
					waferYield.append(CPTS[i].substring(0,3).trim()+":"+CPTS[i].substring(waferStartindex,osStartindex).trim()+";");
				}
				if (!CPTS[i].substring(StartIndex,waferStartindex).trim().equals("")&&CPTS[i].substring(3,4).equals("T")) {
					lotYield.append(CPTS[i].substring(0,3).trim()+":"+CPTS[i].substring(StartIndex,waferStartindex).trim()+";");
				}
				if (!CPTS[i].substring(osStartindex, osendIndex).trim().equals("")&&CPTS[i].substring(3,4).equals("T")) {
					osYield.append(CPTS[i].substring(0,3).trim()+":"+CPTS[i].substring(osStartindex, osendIndex).trim()+";");
				}
				if (CPTS[i].substring(3,4).equals("R"))
					Row+=1;
				ReleaseFormSheet.getRow(Row).getCell(7).setCellValue(CPTS[i].substring(22, 49).trim());
				ReleaseFormSheet.getRow(Row).getCell(15).setCellValue(Temperaturemap.get(CPTS[i].substring(0, 3)));
				ReleaseFormSheet.getRow(Row).getCell(18).setCellValue(CPTS[i].substring(SiteStartindex, SiteEndindex).trim());
				ReleaseFormSheet.getRow(Row).getCell(22).setCellValue(CPTS[i].substring(TimeStartindex, TimeEndindex).trim());
				ReleaseFormSheet.getRow(Row).getCell(26).setCellValue(CPTS[i].substring(TestBaseStartindex, TestBaseEndIndex).trim());
			}
			ReleaseFormSheet.getRow(55).getCell(7).setCellValue(waferYield.toString().trim());
			ReleaseFormSheet.getRow(55).getCell(19).setCellValue(osYield.toString().trim());
			ReleaseFormSheet.getRow(56).getCell(7).setCellValue(lotYield.toString().trim());
			ReleaseFormSheet.getRow(56).getCell(19).setCellValue(osYield.toString().trim());
		}
		ReleaseFormSheet.getRow(2).getCell(7).setCellValue(ValueMap.get("PartNumber"));
		ReleaseFormSheet.getRow(2).getCell(19).setCellValue(ValueMap.get("ProcessSPEC")+"/"+ValueMap.get("Ver"));
		ReleaseFormSheet.getRow(3).getCell(7).setCellValue(ValueMap.get("CustomerCode")+"|"+ValueMap.get("ChineseName"));
		ReleaseFormSheet.getRow(3).getCell(19).setCellValue(new SimpleDateFormat(("yyyy/MM/dd HH:mm:ss")).format(new Date()));
		ReleaseFormSheet.getRow(4).getCell(7).setCellValue(ValueMap.get("DeviceName"));
		ReleaseFormSheet.getRow(4).getCell(19).setCellValue(ValueMap.get("CustomerTestSPEC"));
		ReleaseFormSheet.getRow(5).getCell(7).setCellValue(ValueMap.get("CustomerExtraCode"));
		ReleaseFormSheet.getRow(5).getCell(19).setCellValue(ValueMap.get(""));
		ReleaseFormSheet.getRow(6).getCell(7).setCellValue(ValueMap.get("GrossDie"));
		ReleaseFormSheet.getRow(6).getCell(19).setCellValue(ValueMap.get("TestDie"));
		ReleaseFormSheet.getRow(7).getCell(7).setCellValue(ValueMap.get("DieSizeX")+"/"+ValueMap.get("DieSizeY"));
		ReleaseFormSheet.getRow(7).getCell(19).setCellValue(ValueMap.get("MapOrInk"));
		ReleaseFormSheet.getRow(8).getCell(7).setCellValue(ValueMap.get("WaferNotch"));
		ReleaseFormSheet.getRow(8).getCell(19).setCellValue(ValueMap.get("WaferSize"));
		if (ValueMap.get("OTP").toLowerCase().equals("true")) {
			ReleaseFormSheet.getRow(9).getCell(7).setCellValue("YES");
		}else {
			ReleaseFormSheet.getRow(9).getCell(7).setCellValue("NO");
		}
		if (ValueMap.get("CUPPAD").toLowerCase().equals("true")) {
			ReleaseFormSheet.getRow(9).getCell(19).setCellValue("YES");
		}else {
			ReleaseFormSheet.getRow(9).getCell(19).setCellValue("NO");
		}
		ReleaseFormSheet.getRow(10).getCell(7).setCellValue(ValueMap.get("CPFlow"));
		ReleaseFormSheet.getRow(11).getCell(7).setCellValue(ValueMap.get("ProberDeviceName"));
		ReleaseFormSheet.getRow(30).getCell(7).setCellValue(ValueMap.get("ProberCard"));
		ReleaseFormSheet.getRow(31).getCell(7).setCellValue(ValueMap.get("SiteRelative"));
		ReleaseFormSheet.getRow(32).getCell(7).setCellValue(ValueMap.get("Tester_Model"));
		ReleaseFormSheet.getRow(32).getCell(19).setCellValue(ValueMap.get("HandlerProberModel"));
		ReleaseFormSheet.getRow(33).getCell(7).setCellValue(ValueMap.get("TesterConfig"));
		ReleaseFormSheet.getRow(33).getCell(19).setCellValue(ValueMap.get("TesterSoftwareRev"));
		ReleaseFormSheet.getRow(34).getCell(7).setCellValue(ValueMap.get("ReferenceX")+"/"+ValueMap.get("ReferenceY"));
		ReleaseFormSheet.getRow(34).getCell(19).setCellValue(ValueMap.get("CableRequest"));
		ReleaseFormSheet.getRow(35).getCell(7).setCellValue(ValueMap.get("ProberRequest"));
		ReleaseFormSheet.getRow(35).getCell(19).setCellValue(ValueMap.get("GPIBBin"));
		ReleaseFormSheet.getRow(36).getCell(7).setCellValue(ValueMap.get("LoadBoard"));
		ReleaseFormSheet.getRow(36).getCell(19).setCellValue(ValueMap.get("DUTboard"));
		ReleaseFormSheet.getRow(37).getCell(7).setCellValue(ValueMap.get("PIB"));
		ReleaseFormSheet.getRow(37).getCell(19).setCellValue(ValueMap.get("TrimCard"));
		ReleaseFormSheet.getRow(38).getCell(7).setCellValue(ValueMap.get("Tower"));
		ReleaseFormSheet.getRow(38).getCell(19).setCellValue(ValueMap.get("DPSPower"));
		ReleaseFormSheet.getRow(39).getCell(7).setCellValue(ValueMap.get("N2"));
		ReleaseFormSheet.getRow(39).getCell(19).setCellValue(ValueMap.get("DockingKit"));
		ReleaseFormSheet.getRow(40).getCell(7).setCellValue(ValueMap.get("WaferIDread"));
		ReleaseFormSheet.getRow(40).getCell(19).setCellValue(ValueMap.get("WaferSequence"));
		ReleaseFormSheet.getRow(45).getCell(0).setCellValue(ValueMap.get("ConfirmStand"));
		ReleaseFormSheet.getRow(41).getCell(7).setCellValue(ValueMap.get("ODmux"));
		ReleaseFormSheet.getRow(51).getCell(7).setCellValue(ValueMap.get("Pass"));
		ReleaseFormSheet.getRow(51).getCell(19).setCellValue(ValueMap.get("OS"));
		ReleaseFormSheet.getRow(52).getCell(7).setCellValue(ValueMap.get("SpecialBin"));
		ReleaseFormSheet.getRow(52).getCell(19).setCellValue(ValueMap.get("MajorFail"));
		ReleaseFormSheet.getRow(53).getCell(7).setCellValue(ValueMap.get("AutoRetest"));
		ReleaseFormSheet.getRow(53).getCell(19).setCellValue(ValueMap.get("LotRetest"));
		ReleaseFormSheet.getRow(58).getCell(7).setCellValue(ValueMap.get("StopYield"));
		ReleaseFormSheet.getRow(58).getCell(19).setCellValue(ValueMap.get("ContinuousFailStop"));
		ReleaseFormSheet.getRow(59).getCell(7).setCellValue(ValueMap.get("SiteSiteStop"));
		ReleaseFormSheet.getRow(102).getCell(7).setCellValue(ValueMap.get("VTESTPTE"));
		ReleaseFormSheet.getRow(102).getCell(19).setCellValue(ValueMap.get("Email"));
		ReleaseFormSheet.getRow(122).getCell(0).setCellValue(ValueMap.get("Comment"));
		File Directory=new File("/TestReport/PIDReleaseForm");
		if (!Directory.exists()) {
			Directory.mkdirs();
		}
		FileOutputStream outputStream=new FileOutputStream(new File("/TestReport/PIDReleaseForm/"+PID+".xlsx"));
		workbook.write(outputStream);
	}
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		PIDExcelReport pidExcelReport=new PIDExcelReport();
		pidExcelReport.Write_Excel("PRA_CP25Q40HD00");
	}
}
