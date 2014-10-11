package datas;

/**
* Represents a set of words and their occurrences in a text.
*/
public class SamplingPair {
	public String words;
	public int[] occurrences;
	
	public SamplingPair(String words, int[] occurrences) {
		this.words = words;
		this.occurrences = occurrences;
	}
}
