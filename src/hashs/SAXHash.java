package hashs;

public class SAXHash extends Hash {
	@Override
	public int hash(String w) {
		 int h = 0;
		 char[] ch = w.toCharArray();
		 for (int i = 0; i < ch.length; i++ ) {
			 h ^= ( h << 5 ) + ( h >> 2 ) + ch[i];
		 }
		 return h;
	}

	@Override
	public String name() {
		return "SAXHash";
	}
}
