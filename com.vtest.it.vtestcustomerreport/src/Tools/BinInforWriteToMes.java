package Tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BinInforWriteToMes {
	
	private static final String SERVICE_HOST="http://vtmesap01";
	private static final String SERVICE_URL="http://vtmesap01/vt_mes/ajaxprocess.aspx";
	private static String serverURL;
	public void Write(String lotNumber,String waferID,String BinSummary,String CP) throws IOException
	{
		InputStream inputStream;
		String Acode="65195845153489435181";
		URL url=null;
		HttpURLConnection urlConnection=null;
		int RandomNumber=(int) ((Math.random()*100000000)/1);
		serverURL=SERVICE_URL+"?Action=UploadBinSummaryPerWafer&ACode="+Acode+"&ItemName=WaferLot:"+lotNumber+"|WaferID:"+waferID+"|CP:"+CP.trim()+BinSummary+"&rand="+RandomNumber;
		url=new URL(serverURL);
		urlConnection=(HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("HOST", SERVICE_HOST);
		urlConnection.setReadTimeout(10000);
		urlConnection.setConnectTimeout(10000);
		urlConnection.connect();
		inputStream=urlConnection.getInputStream();
		byte[] bs=new byte[1024];
		int length=0;
		while((length=inputStream.read(bs))!=-1)
		{
			System.out.println(new String(bs, 0, length));
		}
		urlConnection.disconnect();
	}
}
