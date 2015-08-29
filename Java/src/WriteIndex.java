import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class WriteIndex implements Runnable{
	protected ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> index;
	protected boolean isDone = false;
	public WriteIndex(
			ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> index) {
		this.index = index;
		
	}

	@Override
	public void run() {
		
		
	}

}
