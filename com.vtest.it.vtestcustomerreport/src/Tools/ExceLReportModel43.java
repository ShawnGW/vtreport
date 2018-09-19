package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class ExceLReportModel43 extends Report_Model {

	private static final File Model=new File("/Config/ANE.xlsx");
	private final Integer[] Bin_Array={1,2001,2002,2003,2004,2005,3001,3002,3003,3004,3005,3006,3007,3008,3009,3010,3011,3012,3013,3014,3015,3016,4101,4102,4103,4104,4105,4106,4107,4108,4109,4110,4401,4402,4403,4404,4405,4406,4407,4408,4409,4410,4701,4702,4703,4704,4705,4706,4707,4708,4709,4710,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110,5112,5113,5114,5115,5116,5117,5118,5119,5120,5121,5123,5124,5125,5126,5127,5128,5129,5130,5131,5132,5133,5134,5135,5136,5137,5139,5140,5141,5142,5143,5144,5146,5147,5148,5149,5150,5152,5153,5154,5155,5158,5159,5160,5161,5162,5163,5164,5165,5167,5168,5169,5170,5171,5172,5173,5174,5176,5177,5178,5179,5180,5181,5182,5183,5184,5185,5186,5187,5188,5189,5190,5191,5192,5193,5194,5195,5196,5197,5198,5199,5200,5201,5202,5203,5204,5205,5206,5207,5208,5209,5210,5211,5212,5213,5214,5215,5217,5218,5219,5220,5221,5222,5223,5224,5225,5226,5227,5228,5229,5230,5231,5232,5233,5234,5235,5236,5237,5238,5239,5240,5241,5242,5243,5244,5245,5246,5247,5248,5249,5250,5251,5252,5253,5254,5255,5256,5258,5259,5260,5261,5262,5263,5264,5265,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,5280,5281,5282,5283,5284,5285,5286,5287,5288,5289,5290,5291,5292,5293,5295,5296,5297,5298,5299,5300,5301,5302,5304,5305,5306,5307,5308,5309,5310,5311,5401,5402,5403,5404,5405,5406,5407,5408,5409,5410,5412,5413,5414,5415,5416,5417,5418,5419,5420,5421,5423,5424,5425,5426,5427,5428,5430,5431,5433,5434,5436,5437,5439,5440,5442,5443,5444,5445,5446,5447,5449,5450,5451,5452,5453,5454,5456,5457,5458,5459,5460,5462,5463,5464,5465,5468,5469,5470,5471,5472,5473,5474,5475,5477,5478,5479,5480,5481,5482,5483,5484,5486,5487,5488,5489,5490,5491,5492,5493,5494,5495,5496,5497,5498,5499,5500,5501,5502,5503,5504,5505,5506,5507,5508,5509,5510,5511,5512,5513,5514,5515,5516,5517,5518,5519,5520,5521,5522,5523,5524,5525,5531,5532,5533,5534,5535,5536,5537,5538,5539,5540,5541,5542,5543,5544,5545,5546,5547,5548,5549,5550,5551,5552,5553,5554,5555,5556,5557,5558,5559,5560,5561,5562,5563,5564,5565,5566,5567,5568,5569,5570,5577,5578,5579,5580,5581,5582,5583,5584,5586,5587,5588,5589,5590,5591,5592,5593,5594,5595,5596,5597,5598,5599,5600,5601,5602,5603,5604,5605,5606,5607,5608,5609,5610,5611,5612,5614,5615,5616,5617,5618,5619,5620,5621,5623,5624,5625,5626,5627,5628,5629,5630,5701,5702,5703,5704,5705,5706,5707,5708,5709,5710,5712,5713,5714,5715,5716,5717,5718,5719,5720,5721,5723,5724,5725,5726,5727,5728,5729,5730,5731,5732,5733,5734,5735,5736,5737,5739,5740,5741,5742,5743,5744,5746,5750,5751,5752,5753,5755,5756,5757,5758,5761,5762,5763,5764,5765,5766,5767,5768,5770,5771,5772,5773,5774,5775,5776,5777,5779,5780,5781,5782,5783,5784,5785,5786,5787,5788,5789,5790,5791,5792,5793,5794,5795,5796,5797,5798,5799,5800,5801,5802,5803,5804,5805,5806,5807,5808,5809,5810,5811,5812,5813,5814,5815,5816,5817,5818,5820,5821,5822,5823,5824,5825,5826,5827,5828,5829,5830,5831,5832,5833,5834,5835,5836,5837,5838,5839,5840,5841,5842,5843,5844,5845,5846,5847,5848,5849,5850,5851,5852,5853,5854,5855,5856,5857,5858,5859,5861,5862,5863,5864,5865,5866,5867,5868,5870,5871,5872,5873,5874,5875,5876,5877,5878,5879,5880,5881,5882,5883,5884,5885,5886,5887,5888,5889,5890,5891,5892,5893,5894,5895,5896,5898,5899,5900,5901,5902,5903,5904,5905,5907,5908,5909,5910,5911,5912,5913,5914,6001,6101,6102,6103,6104,6105,6106,6107,6108,6109,6110,6201,6202,6203,6204,6205,6206,6207,6208,6209,6210
};
	public ExceLReportModel43() throws IOException {
		super(Model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Write_Excel(String CustomerCode, String Device, String Lot, String CP, File file, File Result_Excel) {
		// TODO Auto-generated method stub
		setExcelStyle setExcelStyle=new setExcelStyle();
		setExcelStyle.initMap();
		try {			
			String Local_lot=Lot;
			File[] Prober_Mappings=FileListOrder.GetList(file.listFiles());
			LinkedHashMap<String, String> propertiesFirst=new parseRawdata(Prober_Mappings[0]).getProperties();

			Integer MES_GrossDie=Integer.valueOf(propertiesFirst.get("MES Test Gross Die"));
			ArrayList<String> ErrorCollection=new ArrayList<>();
			XSSFSheet sheet=workbook.getSheet("Summary");
			XSSFRow Row_Summary=sheet.getRow(3);
			Row_Summary.getCell(6).setCellValue(Local_lot);
			Row_Summary.getCell(0).setCellValue(Device);
			Row_Summary.getCell(14).setCellValue(MES_GrossDie);
			Row_Summary.getCell(12).setCellValue(file.listFiles().length);
			Row_Summary.getCell(33).setCellValue(propertiesFirst.get("Tester Program"));
			
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
			
			Integer ID_ROW=0;
			for (int i =0; i< Prober_Mappings.length; i++) {
				try {
					parseRawdata parseRawdata=new parseRawdata(Prober_Mappings[i]);
					LinkedHashMap<String, String> properties=parseRawdata.getProperties();	

					String Wafer_ID_R=properties.get("Wafer ID");
					Integer PassDie_R=Integer.parseInt(properties.get("Pass Die"));
					Integer GrossDie_R=Integer.parseInt(properties.get("Gross Die"));
					Integer RightID_R=Integer.valueOf(properties.get("RightID"));

					String MapCols_R=properties.get("Map Cols");
					String MapRows_R=properties.get("Map Rows");
					TreeMap<Integer, Integer> Bin_Summary_R=parseRawdata.getBinSummary();
					String[][] Map_R=parseRawdata.getAllDiesDimensionalArray();
					
					String TestStartTime_R=properties.get("Test Start Time");
					if (!GrossDie_R.equals(MES_GrossDie)) {
						ErrorCollection.add(Wafer_ID_R+" : "+GrossDie_R);
					}
					if (Time==null) {
						Time=TestStartTime_R;
					}
					
					XSSFRow Rows=sheet.getRow(RightID_R+6);
					Rows.getCell(1).setCellValue(PassDie_R);
					Rows.getCell(2).setCellValue(Double.valueOf((String.format("%.4f", (double)PassDie_R/(GrossDie_R)))));
					for (int j = 0; j < Bin_Array.length; j++) {
						Integer Value=0;
						if (Bin_Summary_R.containsKey(Bin_Array[j])) {
							Value=Bin_Summary_R.get(Bin_Array[j]);
						}
						if (Value==0) 
							Rows.getCell(j+3).setCellValue("");
						else 
							Rows.getCell(j+3).setCellValue(Value);
					}
					Rows.getCell(Bin_Array.length+3).setCellFormula("SUM(E"+(RightID_R+7)+":YV"+(RightID_R+7)+")");
					//Rows.getCell(41).setCellFormula("SUM(D"+(RightID_R+7)+":AN"+(RightID_R+7)+")");
					XSSFSheet ID_Sheet=workbook.getSheet("Map");
					ID_Sheet.setDefaultColumnWidth(1);	
					int Max_Row=Integer.valueOf(MapRows_R);
					for (int j = 0; j < Max_Row; j++) {
						Integer Col_R=Integer.valueOf(MapCols_R);
						XSSFRow Map_Row=ID_Sheet.createRow(j+ID_ROW);
						if (j==0) {
							Map_Row.createCell(0).setCellValue(Wafer_ID_R);
							Map_Row.getCell(0).setCellStyle(Right_Style);
						}
						Map_Row.setHeightInPoints(6);												
						for (int k = 0; k < Col_R; k++) {						
							if (Map_R[j][k]!=null) {
								XSSFCell Bin_Cell=Map_Row.createCell(k);
								if (Map_R[j][k].equals("M")) {
									Bin_Cell.setCellValue("#");
								}else if (Map_R[j][k].equals("S")) {
									Bin_Cell.setCellValue("#");
								}else if (Map_R[j][k].equals(".")) {
									Bin_Cell.setCellValue(" ");
								}else {
									if (Map_R[j][k].equals("0")) {
										Bin_Cell.setCellStyle(Colors_Array.get(0));
										Bin_Cell.setCellValue(Integer.valueOf(Map_R[j][k]));
									}else {
										if (Integer.valueOf(Map_R[j][k])<33) {
											Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(Map_R[j][k])-1));
										}else {
											Bin_Cell.setCellStyle(Colors_Array.get(Integer.valueOf(Map_R[j][k].substring(0, 1))-1));
										}
										Bin_Cell.setCellValue(Integer.valueOf(Map_R[j][k]));
									}
								}
							}
						}
					}
					ID_ROW+= Max_Row+2;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;	
					
				}
			}
			XSSFRow Sum_Row=sheet.getRow(32);
			Sum_Row.getCell(1).setCellFormula("SUM(B8:B32)");
			XSSFCell Total_yield=Sum_Row.getCell(2);
			Total_yield.setCellFormula("AVERAGE(C8:C32)");
			for(int j=0;j<Bin_Array.length;j++)
			{	
				if (j>22) {
					XSSFCell Total_Cell=Sum_Row.getCell(j+3);
					Total_Cell.setCellFormula(setExcelStyle.set(j));
				}else {
					XSSFCell Total_Cell=Sum_Row.getCell(j+3);
					Total_Cell.setCellFormula("SUM("+(char)(68+j)+"8:"+(char)(68+j)+"32)");
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
			if (ErrorCollection.size()>0) {
				FileWriter writer=new FileWriter(new File(Path+"/error.log"));
				PrintWriter printWriter=new PrintWriter(writer);
				for (String error : ErrorCollection) {
					printWriter.print(error+"\r\n");
				}
				printWriter.close();
			}else {
				File log=new File(Path+"/error.log");
				if (log.exists()) {
					log.delete();
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
