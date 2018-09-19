package Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import parseRawdata.parseRawdata;

public class TextReportModel53 extends FTPRealseModel implements Text_Report {


	@Override
	public void Write_text(String CustomerCode, String Device, String Lot, String CP, File DataSorce, String FileName)
			throws IOException {
		// TODO Auto-generated method stub
		TextReportModel53 model15=new TextReportModel53();
		File[] Filelist=DataSorce.listFiles();
		FTP_Release_delete(CustomerCode, Device, Lot, CP);
		for (int k = 0; k < Filelist.length; k++) {
			parseRawdata parseRawdata=new parseRawdata(Filelist[k]);
			LinkedHashMap<String, String> properties=parseRawdata.getProperties();
			TreeMap<Integer, Integer> binSummaryMap=parseRawdata.getBinSummary();
			String Wafer_ID_R=properties.get("Wafer ID");
			String[][] MapCell_R=parseRawdata.getSoftBinTestDiesDimensionalArray();
			Integer RightID_R=Integer.valueOf(properties.get("RightID"));
			Integer Col_R=(Integer.parseInt(properties.get("TestDieCol"))) ;
			Integer Row_R=(Integer.parseInt(properties.get("TestDieRow")));
			Integer testDieMinX=Integer.valueOf(properties.get("TestDieup"));
			Integer testDieMinY=Integer.valueOf(properties.get("TestDieleft"));
			
			String GrossDie_R=properties.get("Gross Die");
			String FinalID=RightID_R.toString();
			String TestStartTime_R=properties.get("Test Start Time");
			String TestEndTime_R=properties.get("Test End Time");
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
			SimpleDateFormat format_fin=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String program=properties.get("Tester Program");
			program=program.endsWith("()")?program.substring(0, program.length()-2):program;
			String notch=properties.get("Notch");
			if (notch.equals("180-Degree")) {
				notch="D";
			}else if (notch.equals("90-Degree")) {
				notch="R";
			}else if (notch.equals("270-Degree")) {
				notch="L";
			}else {
				notch="U";
			}
			if (FinalID.length()<2) {
				FinalID="0"+FinalID;
			}
			String VERSION="NA";
			HashMap<String, String> NameMap=model15.InitMap(Lot, FinalID, CP, TestEndTime_R, Device, Wafer_ID_R, VERSION);
			Set<String> keyset=NameMap.keySet();
			String FinalName=FileName;
			for (String key : keyset) {
				if (FinalName.contains(key)) {
					FinalName=FinalName.replace(key, NameMap.get(key));
				}
			}
			File[] files=(new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP)).listFiles();
			for (File file : files) {
				String fileName=file.getName();
				if (fileName.contains(FinalName.substring(0, 17))) {
					file.delete();
				}
			}
			File Result_Text=new File(reportBath+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+FinalName);
			PrintWriter out=null;
			try {
				out=new PrintWriter(new FileWriter(Result_Text));
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			out.print("<?xml version=\"1.0\" ?>\r\n");
			out.print("<TANGO_CP_FORMAT>\r\n");
			out.print("<HEADER>\r\n");
			out.print("<VERSION>2.0</VERSION>\r\n");
			out.print("<LOT_ID>"+Lot+"</LOT_ID>\r\n");
			out.print("<OP_NAME>"+CP+"</OP_NAME>\r\n");
			out.print("<WAF_NO>"+properties.get("Slot")+"</WAF_NO>\r\n");
			out.print("<WAFER_ID>"+Wafer_ID_R+"</WAFER_ID>\r\n");
			out.print("<PRODUCT_ID>"+Device+"</PRODUCT_ID>\r\n");
			out.print("<GROSS_DIE>"+GrossDie_R+"</GROSS_DIE>\r\n");
			out.print("<TEST_DIE>"+GrossDie_R+"</TEST_DIE>\r\n");
			out.print("<PASS_CNT>"+properties.get("Pass Die")+"</PASS_CNT>\r\n");
			out.print("<EQP_ID>"+properties.get("Tester ID")+"</EQP_ID>\r\n");
			out.print("<EQP_NAME>Chroma3360</EQP_NAME>\r\n");
			out.print("<SUBSYS_ID></SUBSYS_ID>\r\n");
			out.print("<OPERATOR_ID></OPERATOR_ID>\r\n");
			out.print("<TEST_PG>"+program+"</TEST_PG>\r\n");
			try {
				out.print("<ST_TIME>"+format_fin.format((format.parse(TestStartTime_R)))+"</ST_TIME>\r\n");
				out.print("<END_TIME>"+format_fin.format((format.parse(TestEndTime_R)))+"</END_TIME>\r\n");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.print("<PROB_CARD_ID>"+properties.get("Prober Card ID")+"</PROB_CARD_ID>\r\n");
			out.print("<LOAD_BOARD_ID></LOAD_BOARD_ID>\r\n");
			out.print("<TEMPERATURE>25</TEMPERATURE>\r\n");
			out.print("<BIN_DEF_NAME>CP1_FT5003A_J16S_A1</BIN_DEF_NAME>\r\n");
			out.print("<VENDOR_ID>Focal</VENDOR_ID>\r\n");
			out.print("<VENDORLOT_ID>"+Lot+"</VENDORLOT_ID>\r\n");
			out.print("<FAB_LOT_ID>"+Lot.substring(1)+"</FAB_LOT_ID>\r\n");
			out.print("<PART_ID>"+Device+"</PART_ID>\r\n");
			out.print("<NOTCH>"+notch+"</NOTCH>\r\n");
			out.print("<XYDIR>4</XYDIR>\r\n");
			out.print("<TEST_VENDOR_ID>VTEST</TEST_VENDOR_ID>\r\n");
			out.print("<LOT_TYPE>P</LOT_TYPE>\r\n");
			out.print("<EXTEND_INFO> </EXTEND_INFO>\r\n");
			out.print("<RAWFILE></RAWFILE>\r\n");
			out.print("<OUT_NAME></OUT_NAME>\r\n");
			out.print("</HEADER>\r\n");
			out.print("<LIMITS>\r\n");
			/***/
			if (CP.equals("CP1")) {
				out.print("<BIN>1|1|F|Bin1|0</BIN>\r\n");
				out.print("<BIN>4|4|F|INPUT_LEAKAGE_fail|0</BIN>\r\n");
				out.print("<BIN>5|5|F|POWER_SHORT_TEST_fail|0</BIN>\r\n");
				out.print("<BIN>6|6|F|SW_POWER_SHORT_TEST|0</BIN>\r\n");
				out.print("<BIN>7|7|F|OPEN_SHORT_fail|0</BIN>\r\n");
				out.print("<BIN>8|8|F|STANDBY_fail|0</BIN>\r\n");
				out.print("<BIN>9|9|F|NORMAL MODE_fail|0</BIN>\r\n");
				out.print("<BIN>10|10|F|RW_Register_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>11|11|F|RW_Flash_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>12|12|F|EVEN_ODD_DIFF_NUM_BYTE_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>13|13|F|LENGTH1_CFG_WR_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>14|14|F|LENGTH0_CFG_WR_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>15|15|F|RW_PROTECT_24MHz_fail|0</BIN>\r\n");
				out.print("<BIN>16|16|F|WR_SINGLE_INFO_10MHz_fail|0</BIN>\r\n");
				out.print("<BIN>17|17|F|RD_SINGLE_INFO_10MHz_fail|0</BIN>\r\n");
				out.print("<BIN>24|24|F|SW_CYCLING10X|0</BIN>\r\n");
				out.print("<BIN>31|31|F|POR_PAT_fail|0</BIN>\r\n");
				out.print("<BIN>32|32|F|LVR_PAT_fail|0</BIN>\r\n");
				out.print("<BIN>33|33|F|EARSE_REFERENCE_CELL_fail|0</BIN>\r\n");
				out.print("<BIN>34|34|F|MASS_PROGRAM_VERIFY_0000_fail|0</BIN>\r\n");
				out.print("<BIN>35|35|F|MASS_ERASE_WITH_IFR_fail|0</BIN>\r\n");
				out.print("<BIN>36|36|F|PROGRAM_FIRST_6ROWS_fail|0</BIN>\r\n");
				out.print("<BIN>37|37|F|MASS_ERASE_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>38|38|F|WRITE_DISTRUB_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>39|39|F|WEAK_ERASE_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>40|40|F|TOX_STRESS_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>41|41|F|MASS_ERASE_VERIFY_DIAGONAL_fail|0</BIN>\r\n");
				out.print("<BIN>42|42|F|MASS_PROGRAM_PAGE_ERASE_1ST_fail|0</BIN>\r\n");
				out.print("<BIN>43|43|F|EVEN_PAGE_ERASE_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>44|44|F|VERIFY_0X0000_ODD_PAGE_fail|0</BIN>\r\n");
				out.print("<BIN>45|45|F|ODD_PAGE_ERASE_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>46|46|F|PUNCH_THROUGH_VERIFY_MRG1_fail|0</BIN>\r\n");
				out.print("<BIN>47|47|F|PROGRAM_0XFFFF_DISTURB_fail|0</BIN>\r\n");
				out.print("<BIN>48|48|F|PROGRAM_CKBD_VERIFY_MRG01_fail|0</BIN>\r\n");
				out.print("<BIN>50|50|F|PROGRAM_ICKBD_VERIFY_MRG01_fail|0</BIN>\r\n");
				out.print("<BIN>52|52|F|PROGRAM_CKBD_VERIFY_MRG00_fail|0</BIN>\r\n");
				out.print("<BIN>53|53|F|THINOXIDE_LEAK_VERIFY_MRG0_fail|0</BIN>\r\n");
				out.print("<BIN>54|54|F|READ_DISTURB_VERIFY_MRG0_fail|0</BIN>\r\n");
				out.print("<BIN>55|55|F|MASS_ERASE_WCR_TEST_fail|0</BIN>\r\n");
				out.print("<BIN>58|58|F|CP1_PROGRAM_fail|0</BIN>\r\n");
				out.print("<BIN>60|60|F|Program_verify_55aa_fail|0</BIN>\r\n");
				out.print("<BIN>61|61|F|Program_verify_aa55_fail|0</BIN>\r\n");
				out.print("<BIN>62|62|F|SW_WRITE_INFO|0</BIN>\r\n");
				out.print("<BIN>63|63|F|SW_READ_PARA|0</BIN>\r\n");
				out.print("<BIN>66|66|F|DC_STRESS_fail|0</BIN>\r\n");
			}else {
				out.print("<BIN>1|1|F|Bin1|0</BIN>\r\n");
				out.print("<BIN>6|6|F|SW_POWER_SHORT_TEST|0</BIN>\r\n");
				out.print("<BIN>8|8|F|OPEN_SHORT|0</BIN>\r\n");
				out.print("<BIN>60|61|F|READ_PARA_fail|0</BIN>\r\n");
				out.print("<BIN>60|61|F|Program_verify_55aa_fail|0</BIN>\r\n");
			}
			/**/
			out.print("</LIMITS>\r\n");
			out.print("<DIEDATA>\r\n");
			out.print("<BINSUM>\r\n");
			Set<Integer> binSet=binSummaryMap.keySet();
			for (Integer bin : binSet) {
				out.print("<BIN>"+bin+"|"+binSummaryMap.get(bin)+"|"+binSummaryMap.get(bin)+"</BIN>\r\n");
			}
			out.print("</BINSUM>\r\n");
			out.print("<BINMAP>\r\n");
			for (int i = 0; i < Row_R; i++) {
				for (int j = 0; j < Col_R; j++) {
					if (MapCell_R[i][j]!=null) {
						out.print((j+testDieMinY)+" "+(i+testDieMinX)+" "+MapCell_R[i][j]+" 1\r\n");	
					}	
				}
			}
			out.print("</BINMAP>\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("\r\n");
			out.print("</DIEDATA>\r\n");
			out.print("</TANGO_CP_FORMAT>\r\n");
			out.flush();
			out.close();
			FTP_Release_FAB_Ndelete(CustomerCode, Device, Lot, CP, Result_Text);
		}
	}
}
