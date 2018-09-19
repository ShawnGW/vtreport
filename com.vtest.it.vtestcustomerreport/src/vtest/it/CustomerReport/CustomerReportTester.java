package vtest.it.CustomerReport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import Tools.ParseCustomerReport;

public class CustomerReportTester {

	ParseCustomerReport parseCustomerReport=new ParseCustomerReport();
	private final File DataSource=new File("/server212/RawData/TesterRawdata");
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		CustomerReportTester customerReport=new CustomerReportTester();
		customerReport.GenerateReport();
	}
	public void GenerateReport() throws ParserConfigurationException, IOException, SAXException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
				File[] CustomerCodes=DataSource.listFiles();
				for (int i = 0; i < CustomerCodes.length; i++) {
					if (!CustomerCodes[i].getName().equals("null")&&CustomerCodes[i].isDirectory()&&CustomerCodes[i].listFiles().length>0) {
						File[] Devices=CustomerCodes[i].listFiles();
						for (int j = 0; j < Devices.length; j++) {
							if (Devices[j].isDirectory()&&Devices[j].listFiles().length>0) {
								File[] Lots=Devices[j].listFiles();
								for (int k = 0; k < Lots.length; k++) {
									if (Lots[k].isDirectory()&&Lots[k].listFiles().length>0) {
										File[] CPProcess=Lots[k].listFiles();
										for (int l = 0; l < CPProcess.length; l++) {
											try {
												if (CPProcess[l].isDirectory()&&CPProcess[l].listFiles().length>0) {
													String CustomerCode=CustomerCodes[i].getName();
													String Device=Devices[j].getName();
													String Lot=Lots[k].getName();
													String CP=CPProcess[l].getName();
													File Backup=new File("/server212/RawData/TesterRawdatabackup/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP);
													if (!Backup.exists()) {
														Backup.mkdirs();
													}
													File[]  RawDatas=CPProcess[l].listFiles();
													for (File Rawdata : RawDatas) {
														File Backfile=new File("/server212/RawData/TesterRawdatabackup/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+Rawdata.getName());
														if (Backfile.exists()) {
															Backfile.delete();
														}
														Rawdata.renameTo(Backfile);
													}
													
													File Report_Directory=new File("/server212/TestReport/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP);
													if (!Report_Directory.exists()) {
														Report_Directory.mkdirs();
													}
													System.out.println(CustomerCode+":"+Device+":"+Lot+":"+CP);
													HashMap<String, String> ModelMap=parseCustomerReport.GetModel(CustomerCodes[i].getName(),Device);
													try {
														String[] Excel_Models=ModelMap.get("Excel_pattern").split("&");
														for (int m = 0; m < Excel_Models.length; m++) {
															String ExcelName=null;
															if (ModelMap.containsKey(Excel_Models[m])) 
																ExcelName=ModelMap.get(Excel_Models[m]);
															else
																ExcelName="LOT.xlsx";
															File Result_Excel=new File("/server212/TestReport/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/"+ExcelName);
															Class<?> class1=Class.forName("Tools."+Excel_Models[m]);
															Method Excel_method=class1.getMethod("Write_Excel", String.class,String.class,String.class,String.class,File.class,File.class);
															Excel_method.invoke(class1.newInstance(), CustomerCode, Device, Lot, CP, Backup, Result_Excel);									
														}
													} catch (Exception e) {
														// TODO: handle exception
														e.printStackTrace();
													}
													/*******************/
														String[] Text_models=ModelMap.get("Text_pattern").split("&");
														for (int m = 0; m < Text_models.length; m++) {
															try {
															String TxtName=null;
															if (ModelMap.containsKey(Text_models[m])) 
																TxtName=ModelMap.get(Text_models[m]);
															else
																TxtName="WAFER.txt";
															Class<?> class2=Class.forName("Tools."+Text_models[m]);
															Method Text_method=class2.getMethod("Write_text", String.class,String.class,String.class,String.class,File.class,String.class);
															Text_method.invoke(class2.newInstance(), CustomerCode, Device, Lot, CP, Backup,TxtName);
															} catch (Exception e) {
															// TODO: handle exception
															e.printStackTrace();
															}
														}							
												}else {
													try {
														CPProcess[l].delete();
													} catch (Exception e) {
														// TODO: handle exception
													}
												}
											} catch (Exception e) {
												// TODO: handle exception
												e.printStackTrace();
												continue;
											}
										}
									}else {
										try {
											Lots[k].delete();
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
								}
							}
						}
					}
				}
	}
}
