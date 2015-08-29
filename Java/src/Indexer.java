import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.commons.lang3.StringEscapeUtils;

public class Indexer implements Runnable {
	ConcurrentSkipListMap<String, List<Token>> index;
	
	int id;
	String data;
	int max_tf = 5;
	int doc_len =20;
	public Indexer(
			ConcurrentSkipListMap<String, List<Token>> index,
			int id, String data) {
		this.id = id;
		this.index = index;
		this.data = data;
		
	}

	@Override
	public void run() {
		//System.out.println(StringEscapeUtils.unescapeHtml3(this.data));
		//System.out.println(StringEscapeUtils.unescapeHtml3(this.data).replaceAll("\\<[^>]*>",""));
		String[] tokens = StringEscapeUtils.unescapeHtml3(this.data).replaceAll("\\<[^>]*>","").split(
				"[^a-zA-Z\\-\'<>/]");
		
		List<String> tokenList = new ArrayList<String>();
		for (String t : tokens) {
			
			String token = StringEscapeUtils.unescapeHtml3(t.toLowerCase()).replaceAll("\'s",
					"");
			
			if (token.startsWith("<")) {

			} else if (token.contains("-")) {

				String parts[] = token.split("-");
				String s = "";
				for (String split : parts) {
					s.concat(split);
				}
				tokenList.add(s);
			} else if (token.contains("/")) {
				String parts[] = token.split("/");
				for (String split : parts) {
					tokenList.add(split);
				}
			} else if (token.contains(",")) {
				String parts[] = token.split(",");
				for (String split : parts) {
					tokenList.add(split);
				}
			} else if (token.contains("'")) {
				String parts[] = token.split("'");
				for (String split : parts) {
					tokenList.add(split);
				}
			} else if (token.matches("-?\\d+(\\.\\d+)?")) {

//				Integer x = Integer.parseInt(token.replaceAll("[^0-9]", ""));
//
//				tokenList.add(x.toString());

			} else {
				tokenList.add(token);
			}

		}
		this.doc_len = tokenList.size();
		List<String> stemTokenList = new ArrayList<String>();
		for(String token : tokenList){
			if (isStopWord(token)) {
			}else{
			Stemmer s = new Stemmer();
			s.add(token.toCharArray(),token.length());
			s.stem();
			if (s.toString().compareTo("") != 0) {
				stemTokenList.add(s.toString());
			}
			}
		}
		java.util.Collections.sort(stemTokenList);
		
		HashMap<String,Integer>tokenMap = new HashMap<String,Integer>();
		
		for(String word : stemTokenList){
			if(tokenMap.containsKey(word)){
				int count = tokenMap.get(word)+1;
				tokenMap.put(word, count);
			}
			else{
				tokenMap.put(word, 1);
			}
		}
		int frq = 1;
		
		//System.out.println(tokenMap);
		Iterator<Entry<String,Integer>> ite =  tokenMap.entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry<String,Integer> token =( Map.Entry<String,Integer>) ite.next();
			String t = token.getKey();
			int weight = token.getValue();
			if(weight>frq){
				frq =weight;
			}
		}
		this.max_tf = frq;
		
		Iterator<Entry<String,Integer>> it =  tokenMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,Integer> token =( Map.Entry<String,Integer>) it.next();
			String t = token.getKey();
			int weight = token.getValue();
			if(this.index.get(t)!=null){
			
			//	value.put(Integer.toString(this.id), weight);
				Token token1 = new Token(weight,this.id,this.max_tf,this.doc_len);
				if(token1!=null){
				this.index.get(t).add(token1);
				}
				
			}
			else{
				List<Token> value = new ArrayList<Token>();
				Token token1 = new Token(weight,this.id,this.max_tf,this.doc_len);
				if(token1!=null){
				value.add(token1);
				this.index.put(t,value);
				}
			}
		//	it.remove();
		}
		
		System.out.println(this.id);
 
	}

	private boolean isStopWord(String word) {
		String stopword = "a,all,an,and,any,are,as,be,been,but,by,few,for,have,he,her,here,him,his,how,i,in,is,it,its,any,me,my,none,of,on ,or,our,she,some,the,their,them,there,they,that, this,us,was,what,when,where,which,who,why,will,with,you,your";
		String[] stopWordList = stopword.split(",");
		for (String s : stopWordList) {
			if (word.compareToIgnoreCase(s) == 0) {
				return true;
			}
		}
		return false;
	}
}
