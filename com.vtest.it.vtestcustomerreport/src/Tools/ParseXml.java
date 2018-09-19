package Tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXml {
	
	private String Customercode=null;
	private  HashMap<String, String> Result_map;
	public HashMap<String, String> Get(String CustomerCode, File file) throws IOException {
		Result_map=new HashMap<>();
		try {
		SAXReader reader=new SAXReader();		
		Document document=reader.read(file.getPath());
		Element root=document.getRootElement();
		Customercode=CustomerCode;
		readNode(root);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return Result_map;
	}
	public void readNode(Element node)
	{				
			if (node==null) {
				return ;
			}		
			if (node.getName().equals("CustomerCode")&&node.attribute("id").getValue().equals(Customercode)) {
				@SuppressWarnings("unchecked")
				Iterator<Element> iterator=node.elementIterator();
				while(iterator.hasNext())
				{
					Element element=iterator.next();
					if (element.getName().equals("pattern")) {
						if (!element.getTextTrim().equals("")) {
							if (Result_map.containsKey("pattern")) {
								Result_map.put("pattern", Result_map.get("pattern")+"&"+element.getTextTrim());
							}else
							{
								Result_map.put("pattern", element.getTextTrim());
							}
						}
					}
					if (element.getName().equals("Order")) {
						if (!element.getTextTrim().equals("")) {
							Result_map.put("Order", element.getTextTrim());						
						}
					}
				}
				return ;
			}
			@SuppressWarnings("unchecked")
			Iterator<Element> iterator=node.elementIterator();
			while(iterator.hasNext())
			{
				Element element=iterator.next();
				readNode(element);
			}
		}
	
}
