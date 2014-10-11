package hashs;

public class OneAtTimeHash extends Hash {
	@Override
	public int hash(String w) {
		int h = 0;
		char[] ch = w.toCharArray();
		int i;
		for (i = 0; i < ch.length; i++) {
			h += ch[i];
			h += h << 10 ;
			h ^= h >> 6;
		}
		h += h << 3 ;
		h ^= h >> 11;
		h += h << 15;
		return h;
	}

	@Override
	public String name() {
		return "OneAtTimeHash";
	}
}
