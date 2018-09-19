package Tools;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseCustomerReport {
	private final File Config=new File("/Config/CustomerReport.xml");
	HashMap<String, String> ModelMap;
	public HashMap<String, String> GetModel(String CustomerCode,String Device) throws DocumentException
	{
		ModelMap=new HashMap<>();
		SAXReader reader=new SAXReader();		
		Document document=reader.read(Config.getPath());
		Element root=document.getRootElement();
		readNode(root, CustomerCode,Device);
		if (!ModelMap.containsKey("Excel_pattern"))
		ModelMap.put("Excel_pattern", "ExceLReportModel1");
		if (!ModelMap.containsKey("Text_pattern"))
		ModelMap.put("Text_pattern","TextReportModel1");
		return ModelMap;
	}
	public void readNode(Element node,String CustomerCode,String Device)
	{
		if (node==null)
			return;
		@SuppressWarnings("unchecked")
		Iterator<Element> iterator=node.elementIterator();
		if (node.getName().equals("CustomerCode")&&node.attributeValue("id").equals(CustomerCode)) {		
			while(iterator.hasNext())
			{
				Element element=iterator.next();
				if (element.getName().equals("NamePattern"))
				{
					@SuppressWarnings("unchecked")
					Iterator<Element> Iterater_Names=element.elementIterator();
					while(Iterater_Names.hasNext())
					{
						try {
							Element name=Iterater_Names.next();
							String[] Content=name.getTextTrim().split(":");
							ModelMap.put(Content[0], Content[1]);
						} catch (Exception e) {
							// TODO: handle exception
							continue;
						}
					}
				}
				if (element.getName().equals("Device")&&element.attributeValue("name").equals(Device)) {				
					@SuppressWarnings("unchecked")
					Iterator<Element> Pattern_List=element.elementIterator();
					while(Pattern_List.hasNext())
					{
						Element pattern=Pattern_List.next();
						if (pattern.getName().equals("Excel_pattern")) {				
							if (ModelMap.containsKey("Excel_pattern")) 
								ModelMap.put("Excel_pattern", ModelMap.get("Excel_pattern")+"&"+pattern.getTextTrim());
							else 
								ModelMap.put("Excel_pattern",pattern.getTextTrim());
						}
						if (pattern.getName().equals("Text_pattern")) {
							if (ModelMap.containsKey("Text_pattern")) 
								ModelMap.put("Text_pattern", ModelMap.get("Text_pattern")+"&"+pattern.getTextTrim());
							else 
								ModelMap.put("Text_pattern",pattern.getTextTrim());
						}
					}
				}
				if (!ModelMap.containsKey("Excel_pattern")&&!ModelMap.containsKey("Text_pattern")) {
					if (element.getName().equals("default")) {
						@SuppressWarnings("unchecked")
						Iterator<Element> pattern_list=element.elementIterator();
						while(pattern_list.hasNext())
						{
							Element pattern=pattern_list.next();
							if (pattern.getName().equals("Excel_pattern")) {				
								if (ModelMap.containsKey("Excel_pattern")) 
									ModelMap.put("Excel_pattern", ModelMap.get("Excel_pattern")+"&"+pattern.getTextTrim());
								else 
									ModelMap.put("Excel_pattern",pattern.getTextTrim());
							}
							if (pattern.getName().equals("Text_pattern")) {
								if (ModelMap.containsKey("Text_pattern")) 
									ModelMap.put("Text_pattern", ModelMap.get("Text_pattern")+"&"+pattern.getTextTrim());
								else 
									ModelMap.put("Text_pattern",pattern.getTextTrim());
							}
						}
					}
				}
			}
		}
		while(iterator.hasNext())
		{
			Element element=iterator.next();
			readNode(element, CustomerCode,Device);
		}
	}
	public static void main(String[] args) throws DocumentException {
		ParseCustomerReport parseCustomerReport=new ParseCustomerReport();
		HashMap<String, String> ModelMap=parseCustomerReport.GetModel("ZSW","MAXB2858D");
		Set<String> set=ModelMap.keySet();
		for (String key : set) {
			System.out.println(key+":"+ModelMap.get(key));
		}
	}
}
