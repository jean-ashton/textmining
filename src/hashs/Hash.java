package hashs;

import datas.Word;

public abstract class Hash {

	public abstract int hash(String w);

	public String getName() {
		return getClass().getSimpleName();
	}

	public int hash(Word w) {
		return hash(w.getValue());
	}

	public int getTrailingZerosOf(Word w) {
		return Integer.numberOfTrailingZeros(hash(w));
	}

	public int[] hash(String[] wrds) {
		int[] wh = new int[wrds.length];
		for (int i = 0; i < wh.length; i++) {
			wh[i] = hash(wrds[i]);
		}
		return wh;
	}

	public static void main(String[] args) {
		Hash h = new SimpleHash();
		for (int i = 0; i < args.length; i++) {
			System.out.println(h.hash(args[i]));
		}
		System.out.println(Integer.toBinaryString(0));
	}

	public abstract String name();

}
