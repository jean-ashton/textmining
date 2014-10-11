package hashs;

public class SimpleHash extends Hash {
	@Override
	public int hash(String w) {
		return w.hashCode();
	}

	@Override
	public String name() {
		return "SimpleHash";
	}

}