package datas;

import iterables.FileItr;

import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Cette classe repr�sente l'ensemble des mots.
 */
public abstract class Word {
	protected String value;

	/**
	 * Renvoie la valeur textuelle du contenu.
	 */
	public String getValue() {
		return value;
	}

	public int getIntValue() {
		return new Integer(value);
	}

	/**
	 * Cr�e un mot al�atoire de longeur maximale maxLength
	 */
	public static String randomWord(int maxLength) {
		Random rand = new Random();
		int wrds = 1 + rand.nextInt(maxLength);
		char[] wc = new char[wrds];
		for (int i = 0; i < wc.length; i++) {
			int value = 0;
			while (value < 48 || value >= 58 && value <= 64 || value >= 91
					&& value <= 96)
				value = 1 + rand.nextInt(122);
			wc[i] = (char) value;
		}
		String s = new String(wc);
		return s;
	}

	/**
	 * Cr�e un ensemble de mots de longueur maximale maxLength et de taille size
	 */
	public static String[] randomWords(int size, int maxLength) {
		String ws[] = new String[size];

		for (int i = 0; i < size; i++) {
			ws[i] = randomWord(maxLength);
		}
		return ws;
	}

	/**
	 * Cr�e un ensemble de mots de taille length et de longueur maximale wordMaxLength
	 */
	public static Word[] randWords(int length, int wordMaxLength) {
		Word[] words = new Word[length];
		for (int i = 0; i < length; i++) {
			words[i] = new SWord(randomWord(wordMaxLength));
		}
		return words;
	}

	@Override
	public String toString() {
		return value;
	}

	public static void main(String[] args) throws FileNotFoundException {
		FileItr fitr = new FileItr("test.txt", 5);
		for (Word w : fitr) {
			System.out.println(w);
		}
	}
}
