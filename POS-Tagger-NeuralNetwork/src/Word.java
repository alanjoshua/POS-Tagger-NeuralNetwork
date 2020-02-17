

import java.util.ArrayList;
import java.util.List;

public class Word {
	
	private List<WordIns> wordIns;
	private String text;
	
	public Word(String text) {
		this.text = text;
		wordIns = new ArrayList<WordIns>();
	}
	
	public String getText() {
		return text;
	}
	
	public void addWordIns(WordIns w) {
		wordIns.add(w);
	}
	
	public WordIns getWordIns(int x) {
		return wordIns.get(x);
	}
	
	public List<WordIns> getWordIns() {
		return wordIns;
	}
	
}
