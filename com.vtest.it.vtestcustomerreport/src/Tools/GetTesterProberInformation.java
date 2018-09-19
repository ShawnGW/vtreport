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

	public class GetTesterProberInformation {
		
		private static final String SERVICE_HOST="http://211.149.241.228";
		private static final String SERVICE_URL="http://211.149.241.228/vt_mes/ajaxprocess.aspx";
		private static String serverURL;	
		public static String CallserviceForDoc(String waferid) throws ParserConfigurationException, IOException, SAXException
		{
				InputStream inputStream=null;
				URL url=null;
				HttpURLConnection urlConnection=null;
				String information="NA";
				serverURL=SERVICE_URL+"?ACode=65195845153489435181&Action=GetLotHistory&ItemName="+waferid;
				url=new URL(serverURL);
				urlConnection=(HttpURLConnection)url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setRequestProperty("HOST", SERVICE_HOST);
				urlConnection.setReadTimeout(10000);
				urlConnection.setConnectTimeout(10000);
				urlConnection.connect();
				inputStream=urlConnection.getInputStream();
				InputStreamReader IsReader=new InputStreamReader(inputStream);
				BufferedReader bufferedReader=new BufferedReader(IsReader);
				String content;
				String webs_Information="";
				while((content=bufferedReader.readLine())!=null)
				{
					webs_Information+=content+"\n";
				}
				//System.out.println(webs_Information);
				String Tester_regex="T[A-Z]{2}-[0-9]{2}";
				Pattern Tester_pattern=Pattern.compile(Tester_regex);
				Matcher Tester_matcher=Tester_pattern.matcher(webs_Information);
				if (Tester_matcher.find()) {
					information+=":"+Tester_matcher.group();
				}else
				{
					information+=":NA";
				}
				String Prober_regex="P[A-Z]{2}-[0-9]{2}";
				Pattern Prober_pattern=Pattern.compile(Prober_regex);
				Matcher Prober_matcher=Prober_pattern.matcher(webs_Information);
				if (Prober_matcher.find()) {
					information+=":"+Prober_matcher.group();
				}else
				{
					information+=":NA";
				}
				String Prober_Card_ID_regex="[0-9A-Z]{1,}-PC-[0-9]{2}";
				Pattern Prober_Card_ID_pattern=Pattern.compile(Prober_Card_ID_regex);
				Matcher Prober_Card_ID_matcher=Prober_Card_ID_pattern.matcher(webs_Information);
				if (Prober_Card_ID_matcher.find()) {
					information+=":"+Prober_Card_ID_matcher.group();
				}else
				{
					information+=":NA";
				}	
				inputStream.close();
				urlConnection.disconnect();	
				
				return information;				
		}
		public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
			String customerName=GetTesterProberInformation.CallserviceForDoc("PRAM01002.02");
			System.out.println(customerName);
		}
	}



