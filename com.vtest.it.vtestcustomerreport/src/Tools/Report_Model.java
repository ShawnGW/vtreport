package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class Report_Model{
	
		private  final File BFS_Excel=new File("/Config/Summary.xlsx");
		
		protected XSSFWorkbook workbook=null;
		
		protected XSSFCellStyle Left_Style=null;
		protected XSSFCellStyle Right_Style=null;
		protected XSSFCellStyle Center_Style=null;
		protected XSSFCellStyle Data_Style=null;
		
		protected XSSFCellStyle Bin_1=null;
		protected XSSFCellStyle Bin_2=null;
		protected XSSFCellStyle Bin_3=null;
		protected XSSFCellStyle Bin_4=null;
		protected XSSFCellStyle Bin_5=null;
		protected XSSFCellStyle Bin_6=null;
		protected XSSFCellStyle Bin_7=null;
		protected XSSFCellStyle Bin_8=null;
		protected XSSFCellStyle Bin_9=null;
		protected XSSFCellStyle Bin_10=null;
		protected XSSFCellStyle Bin_11=null;
		protected XSSFCellStyle Bin_12=null;
		protected XSSFCellStyle Bin_13=null;
		protected XSSFCellStyle Bin_14=null;
		protected XSSFCellStyle Bin_15=null;
		protected XSSFCellStyle Bin_16=null;	
		protected XSSFCellStyle Bin_17=null;
		protected XSSFCellStyle Bin_18=null;
		protected XSSFCellStyle Bin_19=null;
		protected XSSFCellStyle Bin_20=null;
		protected XSSFCellStyle Bin_21=null;
		protected XSSFCellStyle Bin_22=null;
		protected XSSFCellStyle Bin_23=null;
		protected XSSFCellStyle Bin_24=null;
		protected XSSFCellStyle Bin_25=null;
		protected XSSFCellStyle Bin_26=null;
		protected XSSFCellStyle Bin_27=null;
		protected XSSFCellStyle Bin_28=null;
		protected XSSFCellStyle Bin_29=null;
		protected XSSFCellStyle Bin_30=null;
		protected XSSFCellStyle Bin_31=null;
		protected XSSFCellStyle Bin_32=null;	
		
		protected ArrayList<XSSFCellStyle> Colors_Array=new ArrayList<XSSFCellStyle>();
		

		public Report_Model() throws IOException {
			// TODO Auto-generated constructor stub
			FileInputStream fins=new FileInputStream(BFS_Excel);
			workbook=new XSSFWorkbook(fins);
			Init();
		}
		public Report_Model(File Model) throws IOException {
			// TODO Auto-generated constructor stub
			FileInputStream fins=new FileInputStream(Model);
			workbook=new XSSFWorkbook(fins);
			Init();
		}
		public abstract void Write_Excel(String CustomerCode,String Device,String Lot,String CP,File file,File Result_Excel);
		public void Remove(String LotNum)
		{
			File LotFile=new File("D:/LocalMapping/"+LotNum);
			File backup_File=new File("D:/Map_Backup/"+LotNum);
			if (!backup_File.exists()) {
				backup_File.mkdirs();
			}
			File[] filelist=LotFile.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				if ((new File("D:/Map_Backup/"+LotNum+"/"+filelist[i].getName())).exists()) {
					(new File("D:/Map_Backup/"+LotNum+"/"+filelist[i].getName())).delete();
				}
				filelist[i].renameTo(new File("D:/Map_Backup/"+LotNum+"/"+filelist[i].getName()));
			}
			LotFile.delete();
		}
		public void Init()
		{
			XSSFFont font=workbook.createFont();
			font.setFontHeight(6);
			workbook.setForceFormulaRecalculation(true);
			Center_Style=workbook.createCellStyle();
			Center_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Center_Style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			Left_Style=workbook.createCellStyle();
			Left_Style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			Left_Style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			Right_Style=workbook.createCellStyle();
			Right_Style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
			Right_Style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			Data_Style=workbook.createCellStyle();
			XSSFDataFormat dataFormat=workbook.createDataFormat();
			Data_Style.setDataFormat(dataFormat.getFormat("0.00%"));
			Data_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Data_Style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			Bin_1=workbook.createCellStyle();
			Bin_1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_1.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
			Bin_1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
			Bin_2=workbook.createCellStyle();
			Bin_2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_2.setFillForegroundColor(IndexedColors.RED.getIndex());
			Bin_2.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_3=workbook.createCellStyle();
			Bin_3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_3.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			Bin_3.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_4=workbook.createCellStyle();
			Bin_4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			Bin_4.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_5=workbook.createCellStyle();
			Bin_5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_5.setFillForegroundColor(IndexedColors.PINK.getIndex());
			Bin_5.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_6=workbook.createCellStyle();
			Bin_6.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_6.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_6.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
			Bin_6.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_7=workbook.createCellStyle();
			Bin_7.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_7.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_7.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			Bin_7.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_8=workbook.createCellStyle();
			Bin_8.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_8.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_8.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			Bin_8.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_9=workbook.createCellStyle();
			Bin_9.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_9.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_9.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
			Bin_9.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_10=workbook.createCellStyle();
			Bin_10.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_10.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_10.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
			Bin_10.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_11=workbook.createCellStyle();
			Bin_11.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_11.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_11.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
			Bin_11.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_12=workbook.createCellStyle();
			Bin_12.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_12.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_12.setFillForegroundColor(IndexedColors.TEAL.getIndex());
			Bin_12.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_13=workbook.createCellStyle();
			Bin_13.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_13.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_13.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
			Bin_13.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_14=workbook.createCellStyle();
			Bin_14.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_14.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_14.setFillForegroundColor(IndexedColors.MAROON.getIndex());
			Bin_14.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_15=workbook.createCellStyle();
			Bin_15.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_15.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_15.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			Bin_15.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_16=workbook.createCellStyle();
			Bin_16.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_16.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_16.setFillForegroundColor(IndexedColors.ORCHID.getIndex());
			Bin_16.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_17=workbook.createCellStyle();
			Bin_17.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_17.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_17.setFillForegroundColor(IndexedColors.CORAL.getIndex());
			Bin_17.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_18=workbook.createCellStyle();
			Bin_18.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_18.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_18.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
			Bin_18.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_19=workbook.createCellStyle();
			Bin_19.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_19.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_19.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			Bin_19.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_20=workbook.createCellStyle();
			Bin_20.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_20.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_20.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			Bin_20.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_21=workbook.createCellStyle();
			Bin_21.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_21.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_21.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
			Bin_21.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_22=workbook.createCellStyle();
			Bin_22.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_22.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_22.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			Bin_22.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_23=workbook.createCellStyle();
			Bin_23.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_23.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_23.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			Bin_23.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_24=workbook.createCellStyle();
			Bin_24.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_24.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_24.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			Bin_24.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_25=workbook.createCellStyle();
			Bin_25.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_25.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_25.setFillForegroundColor(IndexedColors.ROSE.getIndex());
			Bin_25.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_26=workbook.createCellStyle();
			Bin_26.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_26.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_26.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			Bin_26.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_27=workbook.createCellStyle();
			Bin_27.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_27.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_27.setFillForegroundColor(IndexedColors.TAN.getIndex());
			Bin_27.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_28=workbook.createCellStyle();
			Bin_28.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_28.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_28.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			Bin_28.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_29=workbook.createCellStyle();
			Bin_29.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_29.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_29.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			Bin_29.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_30=workbook.createCellStyle();
			Bin_30.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_30.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_30.setFillForegroundColor(IndexedColors.GOLD.getIndex());
			Bin_30.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_31=workbook.createCellStyle();
			Bin_31.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_31.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_31.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			Bin_31.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Bin_32=workbook.createCellStyle();
			Bin_32.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Bin_32.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Bin_32.setFillForegroundColor(IndexedColors.BROWN.getIndex());
			Bin_32.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
			Colors_Array.add(Bin_1);
			Colors_Array.add(Bin_2);
			Colors_Array.add(Bin_3);
			Colors_Array.add(Bin_4);
			Colors_Array.add(Bin_5);
			Colors_Array.add(Bin_6);
			Colors_Array.add(Bin_7);
			Colors_Array.add(Bin_8);
			Colors_Array.add(Bin_9);
			Colors_Array.add(Bin_10);
			Colors_Array.add(Bin_11);
			Colors_Array.add(Bin_12);
			Colors_Array.add(Bin_13);
			Colors_Array.add(Bin_14);
			Colors_Array.add(Bin_15);
			Colors_Array.add(Bin_16);
			Colors_Array.add(Bin_17);
			Colors_Array.add(Bin_18);
			Colors_Array.add(Bin_19);
			Colors_Array.add(Bin_20);
			Colors_Array.add(Bin_21);
			Colors_Array.add(Bin_22);
			Colors_Array.add(Bin_23);
			Colors_Array.add(Bin_24);
			Colors_Array.add(Bin_25);
			Colors_Array.add(Bin_26);
			Colors_Array.add(Bin_27);
			Colors_Array.add(Bin_28);
			Colors_Array.add(Bin_29);
			Colors_Array.add(Bin_30);
			Colors_Array.add(Bin_31);
			Colors_Array.add(Bin_32);
			
			for (XSSFCellStyle xssfCellStyle : Colors_Array) {
				xssfCellStyle.setFont(font);
			}
		}
		public static HashMap<String, String> InitMap(String LOT, String CPPROC, String TIME, String DEVICE, 
				String VERSION) {
					HashMap<String, String> NameMap=new HashMap<>();
					NameMap.put("LOT", LOT);
					NameMap.put("CPPROC", CPPROC);
					NameMap.put("TIME", TIME);
					NameMap.put("DEVICE", DEVICE);
					NameMap.put("VERSION", VERSION);
					return NameMap;
			// TODO Auto-generated method stub
		}
		public void FTP_Release(String CustomerCode, String Device, String Lot, String CP, File file) {
			// TODO Auto-generated method stub
			File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP);
			if (!ReleaseDirectory.exists()) {
				ReleaseDirectory.mkdirs();
			}
			File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+file.getName());
			if (ReleaseFile.exists()) {
				ReleaseFile.delete();
			}
			try {
				FilesCopy.copyfile(file, ReleaseDirectory.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP);
			if (!MailReleaseDirectory.exists()) {
				MailReleaseDirectory.mkdirs();
			}
			File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+file.getName());
			if (MailReleaseFile.exists()) {
				MailReleaseFile.delete();
			}
			try {
				FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
