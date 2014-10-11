
package hashs;

import iterables.FileItr;
import iterables.MyIterable;

import java.io.FileNotFoundException;
import java.util.Arrays;

import datas.IO;
import datas.Word;

public class TestHash {

	static private final int ZEROS_NUM_MAX = 11;

	static double[] leadingZeros(Hash hash, Iterable<Word> iterable) {
		double[] leadingZeros = new double[ZEROS_NUM_MAX + 1];
		int wordsCount = 0;
		for (Word word : iterable) {
			wordsCount++;
			int nbLeadingZeros = Integer.numberOfLeadingZeros(hash.hash(word));
			if (nbLeadingZeros <= ZEROS_NUM_MAX)
				leadingZeros[nbLeadingZeros]++;
		}
		for (int i = 0; i <= ZEROS_NUM_MAX; i++) {
			leadingZeros[i] /= wordsCount;
		}

		return leadingZeros;
	}

	static double[] trailingZeros(Hash hash, Iterable<Word> iterable) {
		double[] trailingZeros = new double[ZEROS_NUM_MAX + 1];
		int wordsCount = 0;
		for (Word word : iterable) {
			wordsCount++;
			int nbTrailingZeros = Integer
					.numberOfTrailingZeros(hash.hash(word));
			if (nbTrailingZeros <= ZEROS_NUM_MAX)
				trailingZeros[nbTrailingZeros]++;
		}
		for (int i = 0; i <= ZEROS_NUM_MAX; i++) {
			trailingZeros[i] /= wordsCount;
		}

		return trailingZeros;
	}

	static void printExTime(Hash[] hashs, int stringsNb) {
		int stringMaxLength = 10;
		String[] strings = Word.randomWords(stringsNb, stringMaxLength);
		for (int i = 0; i < hashs.length; i++) {
			double t0 = System.currentTimeMillis();
			for (int j = 0; j < strings.length; j++) {
				hashs[i].hash(strings[j]);
			}
			double exTime = (System.currentTimeMillis() - t0);
			System.out.println("Le hash " + hashs[i].getClass() + " a mis "
					+ exTime + "ms pour hasher " + stringsNb + " chaînes.");
		}
	}

	public static void createHashHisto(Hash[] hashs, MyIterable iterable)
			throws FileNotFoundException {
		for (Hash hash : hashs) {
			iterable.init();
			IO.scilabify(hash, iterable, iterable.name());
		}
	}

	public static double[] frequency(Hash hash, MyIterable iterable) {
		double[] frequencies = new double[32];
		int count = 0;
		for (Word w : iterable) {
			count++;
			int h = hash.hash(w);
			String bits = Integer.toBinaryString(h);
			int lz = Integer.numberOfLeadingZeros(h);
			for (int i = lz; i < 32; i++) {
				if (bits.charAt(i - lz) == '1') {
					frequencies[i]++;
				}
			}
		}
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i] /= count;
			frequencies[i] *= 100;
			frequencies[i] = Math.floor(frequencies[i]);
		}

		return frequencies;
	}

	public static void createHashHistoFrequency(Hash[] hashs,
			MyIterable iterable) throws FileNotFoundException {
		for (Hash hash : hashs) {
			iterable.init();
			double fs[] = frequency(hash, iterable);
			IO.scilabify(fs, hash.name() + "Frequency");
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(Arrays.toString(frequency(new PretyHash(),
				new FileItr("Texts/shakesp.txt", 1))));
	}

}
