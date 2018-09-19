package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class GetWSInformation {
	
	GetProberCardByWaferID getProberCardByWaferID=new GetProberCardByWaferID();
	private static final String SERVICE_HOST="http://vtmesap01";
	private static final String SERVICE_URL="http://vtmesap01/vt_mes/ajaxprocess.aspx";
	private static String serverURL;	
	public static String CallserviceForDoc(String Lot_number,String CP) throws ParserConfigurationException, IOException, SAXException
	{
			InputStream inputStream=null;
			URL url=null;
			HttpURLConnection urlConnection=null;
			String information = null;
			serverURL=SERVICE_URL+"?ACode=65195845153489435181&Action=GetLotAttributes&ItemName="+Lot_number;
//			System.out.println(serverURL);
			url=new URL(serverURL);
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("HOST", SERVICE_HOST);
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(10000);
			urlConnection.connect();
			inputStream=urlConnection.getInputStream();
			InputStreamReader IsReader=new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferedReader=new BufferedReader(IsReader);
			String numbers=bufferedReader.readLine();
			String regex="\\[LOT:CustCode\\]=[A-Z0-9]{1,}";
			String device_regex="\\[LOT:CustPart\\]=[0-9A-Z-]{1,}";
			String wafersize_regex="\\[PN:WaferSize\\]=[0-9A-Z]{1,}";
			String gross_die_regex="\\[PN:DPW\\]=[0-9]{1,}";
			String program_regex="\\[RecipeID\\]="+CP+"T0:+[0-9A-Za-z_-]{1,}";
			String LocationPoint_regex="Map_Reference\\]=\\(([0-9,]{1,})\\)";
			String PRONumber_regex="WO:PONumber]=([0-9A-Z]{1,})";
			String PID_regex="LOT:PartNum\\]=([0-9A-Z_]{1,})";
			String FabDevice_regex="PN:FabDevice]=([0-9A-Z]{1,})";
			//[WO:WODesc]
			String adlot_regex="WO:WODesc\\]=([0-9A-Z,\\- \\._]{1,})";
			Pattern pattern=Pattern.compile(regex);
			Pattern device_pattern=Pattern.compile(device_regex);
			Pattern wafersize_pattern=Pattern.compile(wafersize_regex);
			Pattern grossdie_pattern=Pattern.compile(gross_die_regex);
			Pattern program_pattern=Pattern.compile(program_regex);
			Pattern Localtion_pattern=Pattern.compile(LocationPoint_regex);
			Pattern PRONumber_pattern=Pattern.compile(PRONumber_regex);
			Pattern PID_Pattern=Pattern.compile(PID_regex);
			Pattern FabDevice_Pattern=Pattern.compile(FabDevice_regex);
			Pattern adlot_Pattern=Pattern.compile(adlot_regex);
			Matcher matcher=pattern.matcher(numbers);
			Matcher device_matcher=device_pattern.matcher(numbers);
			Matcher wafersize_matcher=wafersize_pattern.matcher(numbers);
			Matcher gross_matcher=grossdie_pattern.matcher(numbers);
			Matcher program_matcher=program_pattern.matcher(numbers);
			Matcher Location_matcher=Localtion_pattern.matcher(numbers);
			Matcher PRODNumber_matcher=PRONumber_pattern.matcher(numbers);
			Matcher PID_matcher=PID_Pattern.matcher(numbers);
			Matcher FabDevice_matcher=FabDevice_Pattern.matcher(numbers);
			Matcher adLot_matcher=adlot_Pattern.matcher(numbers);
			if (matcher.find()) {
				information=(matcher.group().split("="))[1];
			}else
			{
				information+=":NA";
			}
			if (device_matcher.find()) {
				information+=":"+(device_matcher.group().split("="))[1];
			}else
			{
				information+=":NA";
			}
			if (wafersize_matcher.find()) {
				information+=":"+(wafersize_matcher.group().split("="))[1];
			}else
			{
				information+=":NA";
			}
			if (gross_matcher.find()) {
				information+=":"+(gross_matcher.group().split("="))[1];
			}else
			{
				information+=":NA";
			}
			if (program_matcher.find()) {
				information+=":"+((program_matcher.group().split(":"))[1]);
			}else
			{
				information+=":NA";
			}
			if (Location_matcher.find()) {
				information+=":"+((Location_matcher.group(1)));
			}else
			{
				information+=":NA";
			}
			if (PRODNumber_matcher.find()) {
				information+=":"+((PRODNumber_matcher.group(1)));
			}else
			{
				information+=":NA";
			}
			if(PID_matcher.find())
			{
				information+=":"+((PID_matcher.group(1)));
			}else {
				information+=":NA";
			}
			if(FabDevice_matcher.find())
			{
				information+=":"+((FabDevice_matcher.group(1)));
			}else {
				information+=":NA";
			}
			if(adLot_matcher.find())
			{
				String adlot="NA";
				String[] tokens=adLot_matcher.group(1).split(",");
				for (int i = 0; i < tokens.length; i++) {
					if (tokens[i].endsWith("AD")||tokens[i].contains("AD")) {
						adlot=tokens[i+1];
						break;
					}
				}
				information+=":"+adlot;
			}else {
				information+=":NA";
			}
			inputStream.close();
			urlConnection.disconnect();	
			if (information.split(":")[0].equals("null")) {
				String Lot_regex1="[0-9A-Z]{4}-[0-9A-Z]{4}";
				String Lot_regex2="[0-9A-Z]{4}-[0-9A-Z]{4}-[A-Z]{1,}";
				String sublot_regex="[0-9A-Z]{1,}";
				Pattern Pattern_lot1=Pattern.compile(Lot_regex1);
				Pattern Pattern_lot2=Pattern.compile(Lot_regex2);
				Pattern Pattern_Sublot=Pattern.compile(sublot_regex);
				Matcher lot1matcher=Pattern_lot1.matcher(Lot_number);
				Matcher lot2matcher=Pattern_lot2.matcher(Lot_number);
				if (Lot_number.contains(".")) {
					String[] nametokens=Lot_number.split("\\.");
					String wafer_Id=nametokens[0]+"-01";
					String sublot=GetProberCardByWaferID.CallserviceForDoc(wafer_Id);
					Matcher sublot_matcher=Pattern_Sublot.matcher(sublot);
					if (sublot_matcher.find()) {
						information=GetWSInformation.CallserviceForDoc(sublot,CP);
					}
				}else if (lot1matcher.find()) {
					String[] nametokens=Lot_number.split("-");
					String wafer_Id=nametokens[0]+nametokens[1]+"-01";
					String sublot=GetProberCardByWaferID.CallserviceForDoc(wafer_Id);
					Matcher sublot_matcher=Pattern_Sublot.matcher(sublot);
					if (sublot_matcher.find()) {
						information=GetWSInformation.CallserviceForDoc(sublot,CP);
					}
				}
			else if (lot2matcher.find()) {
				String[] nametokens=Lot_number.split("-");
				String wafer_Id=nametokens[0]+nametokens[1]+"-01";
				String sublot=GetProberCardByWaferID.CallserviceForDoc(wafer_Id);
				Matcher sublot_matcher=Pattern_Sublot.matcher(sublot);
				if (sublot_matcher.find()) {
					information=GetWSInformation.CallserviceForDoc(sublot,CP);
				}
			}
			 else {
				String wafer_Id=Lot_number+"-01";
				String sublot=GetProberCardByWaferID.CallserviceForDoc(wafer_Id);
				Matcher sublot_matcher=Pattern_Sublot.matcher(sublot);
				if (sublot_matcher.find()) {
					information=GetWSInformation.CallserviceForDoc(sublot,CP);
				}
			}	
			}
		return information;				
	}
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		String customerName=GetWSInformation.CallserviceForDoc("A742203.1","CP1");
		System.out.println(customerName.split(":")[9]);
	}
}
