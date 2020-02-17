

import java.util.List;

/*
 * 0 = text;
 * 1 = nth Word in sentence
 * 2 = 1WordAfterIt
 * 3 = 2WordsAfterIt
 * 4 = 3WordsAfterIt
 * 5 = 4WordsAfterIt
 * 6 = 1WordBeforeIt
 * 7 = 2WordBeforeIt
 * 8 = 31WordBeforeIt
 * 9 = 4WordBeforeIt
 * 10 = textSize
 * 11 = wordType
 */

public class WordIns {

	public static final int numberOfComponents = 903;
	private String text;
	private ComponentArray compArray;
	private int nthWordInSentence;
	private String[] parentArray;
	public static final int sizeOfString = 100;
	private int tempCount = 0;

	public WordIns(String text, int nthWordInSentence, String[] parentArray) {
		this.text = text;
		compArray = new ComponentArray();
		this.nthWordInSentence = nthWordInSentence;
		this.parentArray = parentArray;
		addComponents();
	}

	public String getText() {
		return text;
	}

	public ComponentArray getComponentArray() {
		return compArray;
	}

	public void addComponents() {
		
		List<Integer> temp = Utils.convertStringToBit(text);
		
		int diff;
			if (temp.size() <= sizeOfString) {
				diff = sizeOfString - temp.size();
					for (int j = 0; j < diff; j++) {
						temp.add(0);
					}
			} else {
				diff = temp.size() - sizeOfString;
				for (int j = 0; j < diff; j++) {
					temp.remove(temp.size() - 1);
				}
			}

		for (int x : temp) {
			compArray.addComponent((double) x, false);
		}
		
		compArray.addComponent(nthWordInSentence);

		for (int i = 0; i < 4; i++) {
			if (i + nthWordInSentence < parentArray.length) {

				temp = Utils.convertStringToBit(parentArray[i + nthWordInSentence]);

				if (temp.size() <= sizeOfString) {
					diff = sizeOfString - temp.size();
					for (int j = 0; j < diff; j++) {
						temp.add(0);
					}
				} else {
					diff = temp.size() - sizeOfString;
					for (int j = 0; j < diff; j++) {
						temp.remove(temp.size() - 1);
					}
				}

				for (int x : temp) {
					compArray.addComponent((double) x, false);
				}
			} else {
				for (int j = 0; j < sizeOfString; j++) {
					compArray.addComponent(-1.0);
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			if ((nthWordInSentence - 2) - i >= 0) {
				
				temp = Utils.convertStringToBit(parentArray[nthWordInSentence - 2 - i]);

					if (temp.size() <= sizeOfString) {
						diff = sizeOfString - temp.size();

						for (int j = 0; j < diff; j++) {
							temp.add(0);
					}

					} else {
						diff = temp.size() - sizeOfString;
						for (int j = 0; j < diff; j++) {
							temp.remove(temp.size() - 1);
						}
					}

					for (int x : temp) {
						compArray.addComponent((double) x, false);
					}

			} else {
				for (int j = 0; j < sizeOfString; j++) {
					compArray.addComponent(-1.0);
				}
			}
		}

		compArray.addComponent(text.length());
		
		compArray.addComponent(-1);
		
	}
}
