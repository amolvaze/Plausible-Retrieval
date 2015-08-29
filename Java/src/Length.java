import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Length {

	public static void main(String[] args) throws Exception {
		parseXml();
	}

	private static void parseXml() {
		ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> index = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE,
				Boolean.FALSE);
		int id = 0;
		try {
			XMLEventReader xmlEventReader = xmlInputFactory
					.createXMLEventReader(new FileInputStream(
							"/Users/Teja/Documents/IR_FILE/outputFinal.txt"));

			while (xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()) {

					StartElement startElement = xmlEvent.asStartElement();
					if (startElement.getName().getLocalPart().equals("item")) {
						id++;
						System.out.println(id);
					}
				}
				 if(xmlEvent.isEndElement()){
						
	                   EndElement endElement = xmlEvent.asEndElement();
	                   if(endElement.getName().getLocalPart().equals("item")){
	                	   XMLEvent end =  xmlEventReader.nextEvent();
	                	   if(end.isEndDocument()){
	                		   EndElement endElement2 = xmlEvent.asEndElement();
	                		   if(endElement2.getName().getLocalPart().equals("items")){
	                			   break;
	                		   }
	                	   }
	                	  
	                   }

				 }
			}

		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
