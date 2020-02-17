

import java.util.List;

public class Pair {
	
//	this list stores wordIns List and  
	private List<WordIns> wordIns;
	private List<double[]> answers;
	
	public Pair(List<double[]> answers, List<WordIns> wordIns) {
		this.answers = answers;
		this.wordIns = wordIns;
	}
	
	public List<double[]> getAnswers() {
		return answers;
	}
	
	public List<WordIns> getWordIns() {
		return wordIns;
	}
	
}
