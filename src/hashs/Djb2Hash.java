package hashs;

/*
 * This hash gives the best result with the HyperLogLog algorithm
 */
public class Djb2Hash extends Hash {

	@Override
	public int hash(String w) {
		int hash = 5381;
		char[] ch = w.toCharArray();
		for (int i = 0; i < w.length(); i++) {
			hash = ((hash << 5) + hash) + ch[i]; 
		}
		return hash;
	}

	@Override
	public String name() {
		return "Djb2Hash";
	}

}