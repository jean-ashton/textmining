package hashs;

public class PretyHash extends Hash {

	public static final double A = (Math.sqrt(5) - 1) / 2;
	public static final int MAX_VALUE = Integer.MAX_VALUE;

	@Override
	public int hash(String w) {
		int k = w.hashCode();
		int s;
		if(k!= 0)
			s = k / Math.abs(k);
		else 
			s = 1;
		int h = (int) Math.floor(MAX_VALUE * (k * A - Math.floor(k * A)));
		return s * h;
	}

	public static void main(String[] args) {
		PretyHash ph = new PretyHash();
		String text = "I go to school every day.";
		String[] wds = text.split(" ");

		System.out.println((int) 'a');
		for (String s : wds)
			System.out.println(Integer.numberOfLeadingZeros(ph.hash(s)));
	}

	@Override
	public String name() {
		return "PretyHash";
	}
}
