package hashs;

public class SdbmHash extends Hash {
	@Override
	public int hash(String w) {
		int hash = 0;
		char ch[] = w.toCharArray();
		for (int i = 0; i < w.length(); i++) {
			hash = ch[i] + (hash << 6) + (hash << 16) - hash;
		}
		return Math.abs(hash);
	}

	@Override
	public String name() {
		return "SdbmHash";
	}

}
