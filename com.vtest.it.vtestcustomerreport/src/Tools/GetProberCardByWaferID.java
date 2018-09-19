package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class GetProberCardByWaferID {
	
	private static final String SERVICE_HOST="http://211.149.241.228";
	private static final String SERVICE_URL="http://211.149.241.228/vt_mes/ajaxprocess.aspx";
	private static String serverURL;	
	public static String CallserviceForDoc(String waferid) throws ParserConfigurationException, IOException, SAXException
	{
			InputStream inputStream=null;
			URL url=null;
			HttpURLConnection urlConnection=null;
			String information = null;
			serverURL=SERVICE_URL+"?ACode=65195845153489435181&Action=GetWaferAttributes&ItemName="+waferid;
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
			bufferedReader.readLine();
			bufferedReader.readLine();
			String numbers2=bufferedReader.readLine();
			String sublot=numbers2.split(" ")[0];
			inputStream.close();
			urlConnection.disconnect();	
			information=sublot;
			return information;				
	}
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		String customerName=GetProberCardByWaferID.CallserviceForDoc("AP1B324-02");
		System.out.println(customerName);
	}
}
