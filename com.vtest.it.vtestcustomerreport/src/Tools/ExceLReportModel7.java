package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import parseRawdata.parseRawdata;

public class ExceLReportModel7 extends Report_Model {

	private static final File Model=new File("/Config/SIN.xlsx");
	public ExceLReportModel7() throws IOException {
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

			SimpleDateFormat format=new SimpleDateFormat("YYYY/MM/dd HH:mm");
			
			CellStyle cellStyle=workbook.createCellStyle();
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setBorderLeft(CellStyle.BORDER_DASHED);
			cellStyle.setBorderRight(CellStyle.BORDER_DASHED);
			cellStyle.setBorderBottom(CellStyle.BORDER_DASHED);
			cellStyle.setBorderTop(CellStyle.BORDER_DASHED);
			XSSFFont xssfFont=workbook.createFont();
			xssfFont.setFontHeight(10);
			xssfFont.setFontName("黑体");
			xssfFont.setBold(true);
			cellStyle.setFont(xssfFont);
			
			CellStyle cellStyleb=workbook.createCellStyle();
			cellStyleb.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyleb.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			cellStyleb.setBorderLeft(CellStyle.BORDER_DASHED);
			cellStyleb.setBorderRight(CellStyle.BORDER_DASHED);
			cellStyleb.setBorderBottom((short)2);
			cellStyleb.setBorderTop(CellStyle.BORDER_DASHED);
			cellStyleb.setFont(xssfFont);
			
			
			CellStyle cellStyle2=workbook.createCellStyle();
			cellStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			cellStyle2.setBorderLeft(CellStyle.BORDER_DASHED);
			cellStyle2.setBorderRight((short)2);
			cellStyle2.setBorderBottom(CellStyle.BORDER_DASHED);
			cellStyle2.setBorderTop(CellStyle.BORDER_DASHED);
			cellStyle2.setFont(xssfFont);
			
			
			
			CellStyle cellStyle2b=workbook.createCellStyle();
			cellStyle2b.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle2b.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			cellStyle2b.setBorderLeft(CellStyle.BORDER_DASHED);
			cellStyle2b.setBorderRight((short)2);
			cellStyle2b.setBorderBottom((short)2);
			cellStyle2b.setBorderTop(CellStyle.BORDER_DASHED);
			cellStyle2b.setFont(xssfFont);
		
			CellStyle cellStyle4=workbook.createCellStyle();
			cellStyle4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			cellStyle4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			cellStyle4.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle4.setBorderLeft((short)2);
			cellStyle4.setBorderRight((short)2);
			cellStyle4.setBorderBottom((short)2);
			cellStyle4.setBorderTop((short)2);
			XSSFFont xssfFont4=workbook.createFont();
			xssfFont4.setFontHeight(9);
			xssfFont4.setFontName("宋体");
			cellStyle4.setFont(xssfFont4);

			
			CellStyle cellStyle3=workbook.createCellStyle();
			cellStyle3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			XSSFFont xssfFont2b=workbook.createFont();
			xssfFont2b.setFontHeight(9);
			xssfFont2b.setFontName("宋体");
			xssfFont2b.setColor(IndexedColors.VIOLET.getIndex());
			
			cellStyle3.setBorderLeft((short)2);
			cellStyle3.setBorderRight((short)2);
			cellStyle3.setBorderBottom((short)2);
			cellStyle3.setBorderTop((short)2);
			XSSFFont xssfFont2=workbook.createFont();
			xssfFont2.setFontHeight(9);
			xssfFont2.setFontName("宋体");
			cellStyle3.setFont(xssfFont2);

			Data_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Data_Style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			Data_Style.setFont(xssfFont);
			Data_Style.setBorderLeft(CellStyle.BORDER_DASHED);
			Data_Style.setBorderRight(CellStyle.BORDER_DASHED);
			Data_Style.setBorderBottom(CellStyle.BORDER_DASHED);
			Data_Style.setBorderTop(CellStyle.BORDER_DASHED);

			XSSFCellStyle DataStyle2=workbook.createCellStyle();
			XSSFDataFormat dataFormat2=workbook.createDataFormat();
			DataStyle2.setDataFormat(dataFormat2.getFormat("0.00%"));
			DataStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			DataStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			DataStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			DataStyle2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			DataStyle2.setFont(xssfFont2);
			DataStyle2.setBorderLeft((short)2);
			DataStyle2.setBorderRight((short)2);
			DataStyle2.setBorderBottom((short)2);
			DataStyle2.setBorderTop((short)2);

			
			XSSFSheet sheet=workbook.getSheet("出货清单");	
			XSSFSheet sheet2=workbook.getSheet("镜检表");
			
			XSSFRow Row1=sheet.getRow(2);
			XSSFCell Device_Cell=Row1.getCell(5);
			XSSFCell Total_Sum_Cell=Row1.getCell(7);
			XSSFCell Date_Cell=Row1.getCell(3);
			Device_Cell.setCellValue(Device);
			//Total_Sum_Cell.setCellValue(file.listFiles().length);
			Total_Sum_Cell.setCellFormula("COUNT(G6:G31)-1");
			Date_Cell.setCellValue(format.format(new Date()));

			XSSFRow Device_Infor=sheet2.getRow(1);
			XSSFCell Sheet2_Device_Cell=Device_Infor.getCell(1);
			XSSFCell Sheet2_Lot_Cell=Device_Infor.getCell(3);
			XSSFCell Sheet2_Date_Cell=Device_Infor.getCell(6);
			XSSFCell Sheet2_Available_Cell=Device_Infor.getCell(7);
			Sheet2_Device_Cell.setCellValue(Device);
			Sheet2_Lot_Cell.setCellValue(Local_lot);
			Sheet2_Date_Cell.setCellValue(format.format(new Date()));
			Sheet2_Available_Cell.setCellValue(Device+"有效图形数："+propertiesFirst.get("MES Test Gross Die"));

			String Time=null;
			String Version="NA";

			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					String OPerater_R=properties.get("Operator");
					String TestStartTime_R=properties.get("Test Start Time");
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					if (Time==null) {
						Time=TestStartTime_R;
					}
					int Row_number=i+1;
					XSSFRow RowS=sheet.createRow(Row_number+4);
					XSSFRow Sheet2_Rows=sheet2.createRow(i+3);
					
					XSSFCell waferID_Cell=RowS.createCell(2);
					waferID_Cell.setCellStyle(cellStyle);
					waferID_Cell.setCellValue(RightID_R);
					
					XSSFCell sheet2_waferID_Cell=Sheet2_Rows.createCell(0);
					sheet2_waferID_Cell.setCellStyle(cellStyle3);
					sheet2_waferID_Cell.setCellValue(RightID_R);
				
					
					XSSFCell Lot_Cell=RowS.createCell(3);
					Lot_Cell.setCellStyle(cellStyle);
					Lot_Cell.setCellValue(Lot);
					
					XSSFCell sheet2_Lot_Cell=Sheet2_Rows.createCell(1);
					sheet2_Lot_Cell.setCellStyle(cellStyle4);
					if (RightID_R>9) {
						sheet2_Lot_Cell.setCellValue(Lot+"#"+RightID_R);
					}else {
						sheet2_Lot_Cell.setCellValue(Lot+"#0"+RightID_R);
					}
					
					
					XSSFCell waferID_Cell_2=RowS.createCell(4);
					waferID_Cell_2.setCellStyle(cellStyle);
					waferID_Cell_2.setCellValue(RightID_R);
				
					XSSFCell Pass_cell=RowS.createCell(5);
					Pass_cell.setCellStyle(cellStyle);
					Pass_cell.setCellFormula("镜检表!C"+(Row_number+3)+"-镜检表!H"+(Row_number+3));

					XSSFCell sheet2_Pass_cell=Sheet2_Rows.createCell(2);
					sheet2_Pass_cell.setCellStyle(cellStyle4);
					sheet2_Pass_cell.setCellValue(PassDie_R);
					
					XSSFCell Leave_cell=RowS.createCell(7);
					Leave_cell.setCellStyle(cellStyle2);
					Leave_cell.setCellValue("");

					XSSFCell Yield_cell=RowS.createCell(6);	
					Yield_cell.setCellStyle(Data_Style);
					Yield_cell.setCellFormula("F"+(Row_number+5)+"/"+GrossDie_R);
									
					XSSFCell sheet2_Yield_cell=Sheet2_Rows.createCell(3);	
					sheet2_Yield_cell.setCellStyle(DataStyle2);
					sheet2_Yield_cell.setCellValue(Double.parseDouble((String.format("%.4f", (double)PassDie_R/GrossDie_R))));
				
					XSSFCell sheet2_OP_cell=Sheet2_Rows.createCell(4);
					sheet2_OP_cell.setCellStyle(cellStyle4);
					sheet2_OP_cell.setCellValue(OPerater_R);
					for(int k=0;k<10;k++)
					{
						if (k==2) {
							XSSFCell One_cell=Sheet2_Rows.createCell(5+k);
							One_cell.setCellStyle(cellStyle3);
							One_cell.setCellValue(0);
						}else
						{
							XSSFCell One_cell=Sheet2_Rows.createCell(5+k);
							One_cell.setCellStyle(cellStyle3);
							One_cell.setCellValue("");
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					continue;				
				}
			}	
			XSSFCellStyle DataStyle=workbook.createCellStyle();
			XSSFDataFormat dataFormat=workbook.createDataFormat();
			DataStyle.setDataFormat(dataFormat.getFormat("0.00%"));
			DataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			DataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			DataStyle.setFont(xssfFont);
			DataStyle.setBorderLeft(CellStyle.BORDER_DASHED);
			DataStyle.setBorderRight(CellStyle.BORDER_DASHED);
			DataStyle.setBorderBottom((short)2);
			DataStyle.setBorderTop(CellStyle.BORDER_DASHED);
			
			int length=file.listFiles().length;
			XSSFRow Total_Row=sheet.createRow(length+5);
			for(int i=2;i<8;i++)
			{
			if (i==7) {
					XSSFCell temp_call=Total_Row.createCell(i);
					temp_call.setCellStyle(cellStyle2b);
				} else if (i==6) {
					XSSFCell temp_call=Total_Row.createCell(i);
					temp_call.setCellStyle(DataStyle);
				}else if (i==2) {
					XSSFCell temp_call=Total_Row.createCell(i);
					temp_call.setCellStyle(cellStyleb);
					temp_call.setCellValue("总计");
				}else {
					XSSFCell temp_call=Total_Row.createCell(i);
					temp_call.setCellStyle(cellStyleb);
				}
			}
			
			XSSFCell Total_Total=Total_Row.getCell(5);
			Total_Total.setCellFormula("SUM(F6:F"+(length+5)+")");
			
			XSSFRow sheet2_Total_Row=sheet2.createRow(length+3);
			for(int i=0;i<15;i++)
			{
				XSSFCell temp_call=sheet2_Total_Row.createCell(i);
				temp_call.setCellStyle(cellStyle3);
			}
			
			sheet2_Total_Row.getCell(0).setCellValue("合计");
			
			XSSFCell sheet2_Sum=sheet2_Total_Row.getCell(2);
			sheet2_Sum.setCellFormula("SUM(C4:C"+(length+3)+")");
			for(int k=0;k<7;k++)
			{
				XSSFCell SUM_Func=sheet2_Total_Row.getCell(7+k);
				SUM_Func.setCellFormula("SUM("+(char)(72+k)+"4:"+(char)(72+k)+(length+3)+")");
			}
			XSSFCellStyle DataStyle3=workbook.createCellStyle();
			XSSFDataFormat dataFormat3=workbook.createDataFormat();
			DataStyle3.setDataFormat(dataFormat3.getFormat("0.00%"));
			DataStyle3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			DataStyle3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			DataStyle3.setFont(xssfFont2b);
			DataStyle3.setBorderLeft((short)2);
			DataStyle3.setBorderRight((short)2);
			DataStyle3.setBorderBottom((short)2);
			DataStyle3.setBorderTop((short)2);
			
			XSSFCell sheet2_Total_yield=sheet2_Total_Row.getCell(3);
			sheet2_Total_yield.setCellStyle(DataStyle3);
			sheet2_Total_yield.setCellFormula("AVERAGE(D4:D"+(length+3)+")");
			
			XSSFCell Total_yield=Total_Row.getCell(6);
			Total_yield.setCellFormula("AVERAGE(G6:G"+(length+5)+")");
			
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
