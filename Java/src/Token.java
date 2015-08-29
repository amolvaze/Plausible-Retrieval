import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Token implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int freq ;
    protected int file ;
	protected int max_tf ;
	protected int doclen;
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public int getFile() {
		return file;
	}
	public void setFile(int file) {
		this.file = file;
	}
	public int getMax_tf() {
		return max_tf;
	}
	public void setMax_tf(int max_tf) {
		this.max_tf = max_tf;
	}
	public int getDoclen() {
		return doclen;
	}
	public void setDoclen(int doclen) {
		this.doclen = doclen;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Token(int freq, int file, int max_tf, int doclen) {
		super();
		this.freq = freq;
		this.file = file;
		this.max_tf = max_tf;
		this.doclen = doclen;
	}
	
	public String toString(){
		return ""+this.file+":"+this.freq+":"+this.doclen+":"+this.max_tf;
	}
	

	

}
