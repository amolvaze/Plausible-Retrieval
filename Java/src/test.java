import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;


public class test {
	public static void main(String[] args) throws Exception {
		ConcurrentSkipListMap<String, List<Token>> index = new ConcurrentSkipListMap<String, List<Token>>();
		
	
	
	try {
    	String file ="/Users/Teja/Documents/IR_Main/IR_Proj/Index/a";
  
        BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] list = line.split("|");
			String key = list[0];
			List<Token> lToken = new ArrayList<Token>();
			for(int i =1; i<list.length;i++){
				String[] values = list[i].split(",");
				Token token = new Token(Integer.parseInt(values[0]),Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]));
				lToken.add(token);
				}
			if(index.containsKey(key)){
				index.get(key).addAll(lToken);
			}
			else{
				index.put(key, lToken);
			}
		}
        
       
	}  catch (IOException e) {
		
		e.printStackTrace();
	}
	System.out.println(index);
	
}
}