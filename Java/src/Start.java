import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.ir.actions.Record;

public class Start {
	 
	public static void main(String[] args) throws Exception {
		parseXml();
		
	}

	private static void parseXml() {
		ConcurrentSkipListMap<String, List<Token>> index = new ConcurrentSkipListMap<String, List<Token>>();
		List<Record> recordList = new ArrayList<Record>();
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
		
		
		ExecutorService pool;
		int id = 1;
		
		pool = Executors.newFixedThreadPool(10);
		try {
			 XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream("/Users/Teja/Documents/IR_FILE/outputFinal.txt"));
			Record rec = null;
			int count =1;
			
			
			while (xmlEventReader.hasNext()) {
				
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				 if (xmlEvent.isStartElement()){
					 
					 StartElement startElement = xmlEvent.asStartElement();
					 if(startElement.getName().getLocalPart().equals("item")){
						 rec = new Record();
						 rec.setId(id);
						 rec.setCategory(new ArrayList<String>());
						// System.out.println("ID:"+ id);
					 }
					 else if(startElement.getName().getLocalPart().equals("title")){
						 xmlEvent = xmlEventReader.nextEvent();
						 if(xmlEvent.isCharacters()){
						 rec.setTitle(xmlEvent.asCharacters().getData());}
						 
						
					 }
					 else if(startElement.getName().getLocalPart().equals("link")){
						 xmlEvent = xmlEventReader.nextEvent();
						 if(xmlEvent.isCharacters()){
						 rec.setLink(xmlEvent.asCharacters().getData());}
						
					 }
					 else if(startElement.getName().getLocalPart().equals("category")){
						 xmlEvent = xmlEventReader.nextEvent();
						 if(xmlEvent.isCharacters()){
						 rec.getCategory().add(xmlEvent.asCharacters().getData());}
						
					 }
					 else if(startElement.getName().getLocalPart().equals("description")){
						 StringWriter sw = new StringWriter();
						 XMLEventWriter xw = XMLOutputFactory.newInstance().createXMLEventWriter(sw);
						 while(xmlEventReader.hasNext()){
						 XMLEvent subxmlEvent = xmlEventReader.nextEvent();
						 
						 if(subxmlEvent.isEndElement() && ((EndElement)subxmlEvent).getName().getLocalPart().equals("description")){
							 break;
						 }
						 else{
							 xw.add(subxmlEvent);
						 }
						 }
						
						 pool.execute(new Indexer(index,id,sw.toString()));
						}
				 }
				 if(xmlEvent.isEndElement()){
					  
	                   EndElement endElement = xmlEvent.asEndElement();
	                   if(endElement.getName().getLocalPart().equals("item")){
	                	   recordList.add(rec);
	                	   count++;
	                	  
							if (recordList.size() == 100000) {
							
								pool.shutdown();
								try {
									while (!pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
									
									}
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}	
								writeIndex(index);
								writeRecords("records_"+id,recordList);
								
								
						
								recordList = new ArrayList<Record>();
								index =new ConcurrentSkipListMap<String, List<Token>>();
								pool = Executors.newFixedThreadPool(10);
							}
							id++;
							 
	                   }
				 }
				 if(xmlEvent.isEndElement()){
					 
				 }
			}
			
			
		} catch (XMLStreamException e) {
			pool.shutdown();
			try {
				while (!pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
				
				}
			} catch (InterruptedException ex) {
				
				ex.printStackTrace();
			}	
			writeRecords("records_"+id,recordList);
			writeIndex(index);
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} 
		

			}

	private static void writeIndex(
			ConcurrentSkipListMap<String, List<Token>> index) {
		//AlphabetIndexing(index,);
		ExecutorService pool = Executors.newFixedThreadPool(10);
		Iterator<Entry<String, List<Token>>> it =  index.entrySet().iterator();
		Map.Entry<String, List<Token>> temp =( Map.Entry<String, List<Token>>) it.next();
		String idx=temp.getKey().substring(0, 1);
		HashMap<String,List<Token>> alphaHash = new HashMap<String,List<Token>>();
		alphaHash.put(temp.getKey(), temp.getValue());
		while(it.hasNext()){
			Map.Entry<String, List<Token>> token =( Map.Entry<String, List<Token>>) it.next();
			String t = token.getKey();
			List<Token> lt = token.getValue();
			String firstChr = t.substring(0, 1);
			if(!idx.equals(firstChr)){
				 
				 pool.execute(new AlphabetIndexing(alphaHash,idx));
				 idx = firstChr;
				 alphaHash = new HashMap<String,List<Token>>();
				 alphaHash.put(t, lt);
			}else{
				alphaHash.put(t, lt);
			}
		}
		pool.shutdown();
		try {
			while (!pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
			
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}	
	}
		

	private static void writeRecords(String name, List<Record> recordList) {
		try {
			 File theDir = new File("./Record");
				
			 if (!theDir.exists()) {
				 theDir.mkdir();
			 }
			File f = new File("./Record/" +name);
			f.createNewFile();

			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("./Record/" +name));
			o.writeObject(recordList);
			o.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
