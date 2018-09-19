package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class GetPIDInformations {
	
	GetProberCardByWaferID getProberCardByWaferID=new GetProberCardByWaferID();
	private static final String SERVICE_HOST="http://211.149.241.228";
	private static final String SERVICE_URL="http://211.149.241.228/vt_mes/ajaxprocess.aspx";
	private static String serverURL;	
	public static HashMap<String, String> CallserviceForDoc(String PID) throws ParserConfigurationException, IOException, SAXException
	{
			InputStream inputStream=null;
			URL url=null;
			HttpURLConnection urlConnection=null;
			serverURL=SERVICE_URL+"?Action=GetProductAndProcessSpecAttributes&ACode=65195845153489435181&ItemName="+PID;
			url=new URL(serverURL);
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("HOST", SERVICE_HOST);
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(10000);
			urlConnection.connect();
			inputStream=urlConnection.getInputStream();
			InputStreamReader IsReader=new InputStreamReader(inputStream,"UTF-8");
			BufferedReader bufferedReader=new BufferedReader(IsReader);
			String content=bufferedReader.readLine();
			HashMap<String, String> ValueMap=new HashMap<>();
			StringBuilder DescLengths=new StringBuilder();
			String PartNumber=null;
			String ProcessSPEC=null;
			String Ver=null;
			String CustomerCode=null;
			String DeviceName=null;
			String CustomerTestSPEC=null;
			String CustomerExtraCode=null;
			String TestDie=null;
			String GrossDie=null;
			String DieSizeX=null;
			String DieSizeY=null;
			String MapOrInk=null;
			String WaferSize=null;
			String WaferNotch=null;
			String CUPPAD=null;
			String OTP=null;
			String CPFlow=null;
			String ProberDeviceName=null;
			String ProberCard=null;
			String SiteRelative=null;
			String Tester_Model=null;
			String TesterConfig=null;
			String ReferenceX=null;
			String ReferenceY=null;
			String ProberRequest=null;
			String LoadBoard=null;
			String Tower=null;
			String PIB=null;
			String N2=null;
			String WaferIDread=null;
			String ODmux=null;
			String HandlerProberModel=null;
			String TesterSoftwareRev=null;
			String GPIBBin=null;
			String DUTboard=null;
			String TrimCard=null;
			String DPSPower=null;
			String DockingKit=null;
			String WaferSequence=null;
			String Pass=null;
			String CableRequest=null;
			String SpecialBin=null;
			String AutoRetest=null;
			String OS=null;
			String MajorFail=null;
			String LotRetest=null;
			String StopYield=null;
			String ContinuousFailStop=null;
			String SiteSiteStop=null;
			String VTESTPTE=null;
			String Email=null;
			String ChineseName=null;
			String Comment=null;
			String temperature=null;
			String ConfirmStand=null;
			try {
				String[] InforMations=content.split("\\|");
				for (String informationCell : InforMations) {
					if (!informationCell.endsWith("=")) {
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ProberCard]")) {					
							ProberCard=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:SiteRelative]")) {					
							SiteRelative=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ValidationStandard]")) {					
							ConfirmStand=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:TesterModel]")) {					
							Tester_Model=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:TesterConfig]")) {					
							TesterConfig=informationCell.split("=")[1];
							continue;
						}
						
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ReferenceX]")) {					
							ReferenceX=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:CustCName]")) {					
							ChineseName=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ProductionNotice]")) {					
							Comment=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Temperature]")) {	
							temperature=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ReferenceY]")) {					
							ReferenceY=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:TesterConfig]")) {					
							ProberRequest=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:LoadBoard]")) {					
							LoadBoard=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Tower]")) {					
							Tower=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:PIB]")) {					
							PIB=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:N2]")) {					
							N2=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:WaferID_read]")) {					
							WaferIDread=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:OD_mux]")) {					
							ODmux=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:ProberModel]")) {					
							HandlerProberModel=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Tester_Software_Revision]")) {					
							TesterSoftwareRev=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Tester_Software_Revision]")) {					
							CableRequest=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:GPIB_Bin]")) {					
							GPIBBin=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:DUT_boarn]")) {					
							DUTboard=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:TrimCard]")) {					
							TrimCard=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:DPS_Power]")) {					
							DPSPower=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Docking]")) {					
							DockingKit=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Wafer_Sequence]")) {					
							WaferSequence=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:PassBinCodes]")) {					
							Pass=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Special_Bin]")) {					
							SpecialBin=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:AutoRetest]")) {					
							AutoRetest=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:OpenShortBinCode]")) {					
							OS=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Major_Fail]")) {					
							MajorFail=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:LotRetest]")) {					
							LotRetest=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:StopYield]")) {					
							StopYield=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Continuous_Fail]")) {					
							ContinuousFailStop=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Site-Site]")) {					
							SiteSiteStop=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:PE_Owner]")) {					
							VTESTPTE=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:PE_Owner]")) {					
							Email=informationCell.split("=")[1]+"@v-test.com.cn";
							continue;
						}
						/***/
						if (informationCell.contains("[PN:PartNum]")) {					
							PartNumber=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[ProcessSpecName]")) {
							ProcessSPEC=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[ProcessSpecRevision]")) {
							Ver=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:CustCode]")) {
							CustomerCode=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:CustPart]")) {
							DeviceName=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[FlexibleItem_ProcessSpecAttributes:Cust_TestSpec]")) {
							CustomerTestSPEC=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:CustExtraCode]")) {
							CustomerExtraCode=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:TestedDies]")) {
							TestDie=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:DPW]")) {
							GrossDie=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:DieSizeX]")) {
							DieSizeX=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:DieSizeY]")) {
							DieSizeY=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:MapOrInk]")) {
							MapOrInk=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:WaferNotch]")) {
							WaferNotch=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:WaferSize]")) {
							WaferSize=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:isOTP]")) {
							OTP=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:isCOP]")) {
							CUPPAD=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[ProcessShortFlow]")) {
							CPFlow=informationCell.split("=")[1];
							continue;
						}
						if (informationCell.contains("[PN:ProberDevice]")) {
							ProberDeviceName=informationCell.split("=")[1];
							continue;
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			StringBuilder CPTSB=new StringBuilder("Start");
			StringBuilder BAKSB=new StringBuilder("Start");
			StringBuilder UVSB=new StringBuilder("Start");
			boolean flag=false;
			while((content=bufferedReader.readLine())!=null)
			{
				if (flag) {
					if (content.contains("--")) {
						break;
					}
					else {
						if (content.substring(0, 2).equals("CP")) {
							CPTSB.append(":"+content);
						}				
						if (content.substring(0, 2).equals("BK")) {
							BAKSB.append(":"+content);
						}
						if (content.substring(0, 2).equals("UV")) {
							UVSB.append(":"+content);
						}
					}
				}
				if (content.contains("--")) {
					String[] tempDesc=content.split(" ");
					for (String One : tempDesc) {
						DescLengths.append((One.length()+1)+":");
					}
					flag=true;
					continue;
				}
			}
			ValueMap.put("PartNumber", PartNumber);
			ValueMap.put("ProcessSPEC", ProcessSPEC);
			ValueMap.put("Ver", Ver);
			ValueMap.put("CustomerCode", CustomerCode);
			ValueMap.put("DeviceName", DeviceName);
			ValueMap.put("CustomerTestSPEC", CustomerTestSPEC);
			ValueMap.put("CustomerExtraCode", CustomerExtraCode);
			ValueMap.put("TestDie", TestDie);
			ValueMap.put("GrossDie", GrossDie);
			ValueMap.put("DieSizeX", DieSizeX);
			ValueMap.put("DieSizeY", DieSizeY);
			ValueMap.put("MapOrInk", MapOrInk);
			ValueMap.put("WaferSize", WaferSize);
			ValueMap.put("WaferNotch", WaferNotch);
			ValueMap.put("CUPPAD", CUPPAD);
			ValueMap.put("OTP", OTP);
			ValueMap.put("CPFlow", CPFlow);
			ValueMap.put("ProberDeviceName", ProberDeviceName);
			ValueMap.put("ProberCard", ProberCard);
			ValueMap.put("SiteRelative", SiteRelative);
			ValueMap.put("Tester_Model", Tester_Model);
			ValueMap.put("TesterConfig", TesterConfig);
			ValueMap.put("ReferenceX", ReferenceX);
			ValueMap.put("ReferenceY", ReferenceY);
			ValueMap.put("ProberRequest", ProberRequest);
			ValueMap.put("LoadBoard", LoadBoard);
			ValueMap.put("Tower", Tower);
			ValueMap.put("PIB", PIB);
			ValueMap.put("N2", N2);
			ValueMap.put("WaferIDread", WaferIDread);
			ValueMap.put("ODmux", ODmux);
			ValueMap.put("HandlerProberModel", HandlerProberModel);
			ValueMap.put("TesterSoftwareRev", TesterSoftwareRev);
			ValueMap.put("GPIBBin", GPIBBin);
			ValueMap.put("DUTboard", DUTboard);
			ValueMap.put("TrimCard", TrimCard);
			ValueMap.put("DPSPower", DPSPower);
			ValueMap.put("DockingKit", DockingKit);
			ValueMap.put("WaferSequence", WaferSequence);
			ValueMap.put("Pass", Pass);
			ValueMap.put("CableRequest", CableRequest);
			ValueMap.put("SpecialBin", SpecialBin);
			ValueMap.put("AutoRetest", AutoRetest);
			ValueMap.put("OS", OS);
			ValueMap.put("MajorFail", MajorFail);
			ValueMap.put("LotRetest", LotRetest);
			ValueMap.put("StopYield", StopYield);
			ValueMap.put("ContinuousFailStop", ContinuousFailStop);
			ValueMap.put("SiteSiteStop", SiteSiteStop);
			ValueMap.put("VTESTPTE", VTESTPTE);
			ValueMap.put("Email", Email);
			ValueMap.put("ChineseName", ChineseName);
			ValueMap.put("CPT", CPTSB.toString());
			ValueMap.put("BAK", BAKSB.toString());
			ValueMap.put("UV", UVSB.toString());
			ValueMap.put("Comment", Comment);
			ValueMap.put("Lengths", DescLengths.toString());
			ValueMap.put("Temperature", temperature);
			ValueMap.put("ConfirmStand", ConfirmStand);
			Set<String> NameSet=ValueMap.keySet();
			for (String name : NameSet) {
				if (ValueMap.get(name)==null) {
					ValueMap.put(name, "--");
				}
			}			
		return ValueMap;				
	}
	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		HashMap<String, String> ValueMap=CallserviceForDoc("PRA_CP25Q80HD00");
		String[] LimitYields=ValueMap.get("CPT").split(":");
		String Yield="NA";
		try {
			for (int i = 0; i < LimitYields.length; i++) {
				if (!LimitYields[i].equals("Start")&&LimitYields[i].substring(0, 5).equals("CP2T0")) {
					System.out.println(LimitYields[i]);
					Yield=LimitYields[i].substring(130, 137).trim();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.err.println(Yield);
//		Set<String> keyset=ValueMap.keySet();
//		for (String string : keyset) {
//			System.out.println(string+":"+ValueMap.get(string));
//		}
	}
}
