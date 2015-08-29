import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryAnalyzer {

	public static void main(String[] args) {
	//	String query = "southern Hellas in the Thermopylae passage";
		String query =	"I was visiting that day there was not a radio or tv so we weren";
		List<String> terms = parse(query);
		HashMap<Integer,Float>  w1 = new HashMap<Integer,Float>();
		HashMap<Integer,Float>  w2 = new HashMap<Integer,Float>();
		
		int collectionsize = 1175920;
		int avgdoclen = 100;
		for (String term : terms) {
			String idx = term.substring(0, 1);
			String file = "/Users/Teja/Documents/IR_Main/IR_Proj/Index/" + idx;
			
			
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));

				String line;
				
				while ((line = br.readLine()) != null) {
					String key = line.split("\\|")[0];
					HashMap<String ,List<Token>> termList = new HashMap<String ,List<Token>>();
					if(term.equals(key)){
						String[] token = line.split("\\|");
						if(!termList.containsKey(key)){
						List<Token> to = new ArrayList<Token>();
						termList.put(key, to);
						}
						
						for(int i = 1; i<token.length;i++){
							String set = token[i];
							String[] split = set.split(",");
							System.out.println(term+":"+set);
							Token t = new Token(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]),Integer.parseInt(split[3]));
							termList.get(key).add(t);
						}
						int df =termList.get(key).size();
						for(Token t : termList.get(key)){
							int tf = t.getFreq();
							int maxtf = t.getMax_tf();
							int doclen = t.doclen;
							Float v1 = (float) ((float) (0.4*0.6*Math.log10(tf+0.5)/Math.log10(maxtf+1.0))*(Math.log10((collectionsize/df)/Math.log10(collectionsize))));
							Float v2 = (float) (0.4+0.6*(tf/(tf+0.5+1.5*(doclen/avgdoclen)))*Math.log10(collectionsize/df)/Math.log10(collectionsize));
							if(w1.get(t.getFile())==null){
								w1.put(t.getFile(), v1);
								w2.put(t.getFile(), v2);}
								else{
									w1.put(t.getFile(),w1.get(t.getFile())+ v1);
									w2.put(t.getFile(), w1.get(t.getFile())+v2);
								}
						}
						
					}
					
				}
				
				
				

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		LinkedHashMap<Integer,Float> sw1=	sortHashMapByValues(w1);
		LinkedHashMap<Integer,Float> sw2= sortHashMapByValues(w2);
		
		
		System.out.println("The Top Ten Documents For Weighting Scheme W1:");
		Iterator it = sw1.entrySet().iterator();
		int k=0;
		 while (it.hasNext()&&k<10) {
			 Map.Entry pairs = (Map.Entry)it.next();
			 System.out.println("File: DOC#"+pairs.getKey() + "    W1 value: " + pairs.getValue());
			 it.remove();
			 k++;
		 }
		 k=0;
		 System.out.println("The Top Ten Documents For Weighting Scheme W2:");
			Iterator it2 = sw2.entrySet().iterator();
			 while (it2.hasNext()&&k<10) {
				 Map.Entry pairs = (Map.Entry)it2.next();
				 System.out.println("File: DOC#"+pairs.getKey() + "    W2 value: " + pairs.getValue());
				 it2.remove();
				 k++;
			 }
	}

	private static List<String> parse(String query) {
		List<String> tokenList = new ArrayList<String>();
		String[] tokens = query.split("[^a-zA-Z0-9\\-\'<>/]");
		for (String t : tokens) {

			String token = t.toLowerCase().replaceAll("\'s", "");

			if (token.startsWith("<")) {

			} else if (isStopWord(token)) {
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

				// Integer x = Integer.parseInt(token.replaceAll("[^0-9]", ""));
				//
				// tokenList.add(x.toString());

			} else {
				tokenList.add(token);
			}

		}

		List<String> stemTokenList = new ArrayList<String>();
		for (String token : tokenList) {
			Stemmer s = new Stemmer();
			s.add(token.toCharArray(), token.length());
			s.stem();
			if (s.toString().compareTo("") != 0) {
				stemTokenList.add(s.toString());
			}
		}
		java.util.Collections.sort(stemTokenList);
		return stemTokenList;
	}

	private static boolean isStopWord(String word) {
		String stopword = "a,all,an,and,any,are,as,be,been,but,by,few,for,have,he,her,here,him,his,how,i,in,is,it,its,any,me,my,none,of,on ,or,our,she,some,the,their,them,there,they,that, this,us,was,what,when,where,which,who,why,will,with,you,your";
		String[] stopWordList = stopword.split(",");
		for (String s : stopWordList) {
			if (word.compareToIgnoreCase(s) == 0) {
				return true;
			}
		}
		return false;
	}

	public static LinkedHashMap sortHashMapByValues(HashMap passedMap) {
		   List mapKeys = new ArrayList(passedMap.keySet());
		   List mapValues = new ArrayList(passedMap.values());
		   Collections.sort(mapValues);
		   Collections.reverse(mapValues);
		   Collections.sort(mapKeys);

		   LinkedHashMap sortedMap = new LinkedHashMap();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           Float comp1 = (Float) passedMap.get(key);
		           Float comp2 = (Float) val;

		           if (comp1.equals(comp2)){
		               passedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((Integer)key,(Float) val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
		}
	
	
	
}