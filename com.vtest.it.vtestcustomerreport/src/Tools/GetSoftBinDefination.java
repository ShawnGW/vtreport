package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GetSoftBinDefination {


	//GetProberCardByWaferID getProberCardByWaferID=new GetProberCardByWaferID();
	private static final String SERVICE_HOST="http://vtmesap01";
	private static final String SERVICE_URL="http://vtmesap01/vt_mes/ajaxprocess.aspx";
	private static String serverURL;	
	public static ArrayList<String> CallserviceForDoc(String Device) throws ParserConfigurationException, IOException, SAXException
	{
			ArrayList<String> SoftBinDefination=new ArrayList<>();
			InputStream inputStream=null;
			URL url=null;
			HttpURLConnection urlConnection=null;
			serverURL=SERVICE_URL+"?ACode=65195845153489435181&Action=GetSystemParametersByParmType&ItemName=SoftBinDefinition";
			//System.out.println(serverURL);
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
			String content=null;			
			while((content=bufferedReader.readLine())!=null)
			{
				if (content.contains(Device)) {
					String[] tokens=content.split(" ");
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < tokens.length; i++) {						
						if (!tokens[i].equals("")) {
							sb.append(tokens[i]+"&");							
						}	
					}
					SoftBinDefination.add(sb.toString());
				}	
			}			
			inputStream.close();
			urlConnection.disconnect();				
		return SoftBinDefination;				
	}
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		ArrayList<String> customerName=GetSoftBinDefination.CallserviceForDoc("P25Q40HB");
		for (String string : customerName) {
			System.out.println(string);
		}
	}
}
