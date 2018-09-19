package vtest.it.CustomerReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Tools.PIDExcelReport;

public class PIDReport {
	private static final File PIDDataSource=new File("/home/ManMap/ManCustomerPID/PID.txt");
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {	
		if (PIDDataSource.exists()) {
			
			FileReader reader=new FileReader(PIDDataSource);
			BufferedReader bufferedReader=new BufferedReader(reader);
			String Content;
			while((Content=bufferedReader.readLine())!=null)
			{
				try {
					PIDExcelReport pidExcelReport=new PIDExcelReport();
					pidExcelReport.Write_Excel(Content);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			bufferedReader.close();
			PIDDataSource.delete();
		}
	}
}
