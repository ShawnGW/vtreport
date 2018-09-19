package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCPProcessYield {
	private static final String SERVICE_HOST="http://211.149.241.228";
	private static final String SERVICE_URL="http://211.149.241.228/vt_mes/ajaxprocess.aspx?ACode=65195845153489435181&Action=GetWaferBinSummary&ItemName=";
	private static String serverURL;
	public static void main(String[] args) throws IOException {
		GetCPProcessYield getCPProcessYield=new GetCPProcessYield();
		getCPProcessYield.GetCPYield("TCUP27A126");
	}
	public HashMap<String, Float> GetCPYield(String Lot) throws IOException
	{
		ArrayList<String> LotInformation=new ArrayList<>();
		HashMap<String, Float> CPYield=new HashMap<>();
		InputStream inputStream=null;
		URL url=null;
		HttpURLConnection urlConnection=null;
		serverURL=SERVICE_URL+Lot;
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
			LotInformation.add(content);
		}			
		inputStream.close();
		urlConnection.disconnect();	
		for (String IDContent : LotInformation) {
			if (IDContent.contains("PassBin"))
				{				
					IDContent=IDContent.replaceAll("[\\s]{1,}", ",");
					String Yeild=IDContent.split(",")[10];
					String WaferID=IDContent.split(",")[1];
					String CPProcess=IDContent.split(",")[13];
					CPYield.put(WaferID+":"+CPProcess, Float.valueOf(Yeild));
				}
		}
		return CPYield;
	}
}
