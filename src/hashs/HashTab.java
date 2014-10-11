package hashs;

import datas.Word;

public class HashTab extends Hash {

	@Override
	public int hash(String w) {
		String result = "";
		for (int i = 0; i < w.length(); i++)
			result += Integer.toBinaryString(w.charAt(i));
		return result.hashCode();
	}

	@Override
	public int hash(Word w) {
		return w.getValue().hashCode();
	}

	static public int simpleHash(String s) {
		char ch[];
		ch = s.toCharArray();
		int slength = s.length();
		int i, sum;
		for (sum = 0, i = 0; i < slength; i++)
			sum += ch[i];
		return sum % 17;
	}

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			System.out.println(HashTab.simpleHash(args[i]));
		}
	}

	@Override
	public String name() {
		return "HashTab";
	}
}
