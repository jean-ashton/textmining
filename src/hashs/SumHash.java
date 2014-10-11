package hashs;

public class SumHash extends Hash {
	@Override
	public int hash(String w) {
		int h = 0;
		for (int i = 0; i < w.length(); i++)
			h += w.charAt(i);
		return h;
	}

	@Override
	public String name() {
		return "SumHash";
	}
}
