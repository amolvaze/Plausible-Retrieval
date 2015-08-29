import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AlphabetIndexing implements Runnable {

	private String folderloc = "/Users/Teja/Documents/IR_Main/IR_Proj/Index/";
	HashMap<String, List<Token>> index;
	String idx;

	public AlphabetIndexing(HashMap<String, List<Token>> index2, String idx) {
		this.index = index2;
		this.idx = idx;
	}

	@Override
	public void run() {
		
		 File theDir = new File(this.folderloc);
			
		 if (!theDir.exists()) {
			 theDir.mkdir();
		 }
		
		
		try {
			File f = new File(folderloc + idx);
			f.createNewFile();
			FileWriter fstream = new FileWriter(f, true);
			BufferedWriter out = new BufferedWriter(fstream);
			Iterator<Entry<String, List<Token>>> it = index.entrySet()
					.iterator();

			while (it.hasNext()) {
				Map.Entry<String, List<Token>> token = (Map.Entry<String, List<Token>>) it.next();
				out.newLine();
				 out.write(token.getKey()+"");
				List<Token> tList = token.getValue();
				
				for (int i = 0 ;i<tList.size();i++) {
				//	out.write(token.getKey() + "|");
					Token t = tList.get(i);
					try{
						if(t!=null){
							if(t.doclen == 0||t.file == 0||t.freq ==0 ||t.max_tf == 0){
								
							}
							else{
					out.write("|" + t.getFreq() + "," + t.getFile() + ","
							+  t.getMax_tf()+ "," + t.getDoclen() + "");
						}}
					}catch(NullPointerException e){
			
					}
				}
				
				out.write("|");
				
			}
			
			
			out.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	

	}
}
