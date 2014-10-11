package main;

import hashs.Djb2Hash;
import hashs.Hash;
import hashs.SimpleHash;
import iterables.Itr;
import iterables.MyIterable;

import java.io.FileNotFoundException;

import datas.Word;

public class HyperLogLog {

	public int M[];
	public int N[];
	public static int ME = 256; // the size of the M array

	private final Hash hash;
	private final MyIterable iterable;
	
	public HyperLogLog(MyIterable iterable) {
		this.hash = new Djb2Hash();
		this.iterable = iterable;
	}
	
	public HyperLogLog(Hash hash, MyIterable iterable) {
		this.hash = hash;
		this.iterable = iterable;
	}

	/**
	 * @returns the position of the first 1 in the binary representation of i
	 */
	public static int rho(int i) {
		return Integer.numberOfTrailingZeros(i) + 1;
	}

	public static double alpha(int m) {
		switch (m) {
			case 1 << 4:
				return 0.673;
			case 1 << 5:
				return 0.697;
			case 1 << 6:
				return 0.709;
			default:
				return 0.7213 / (1 + 1.079 / m);
		}
	}

	/**
	 * @return the approximate number of distinct elements of iterable
	 */
	public double hyperLogLog(int m) {

		/**
		 * If the M array has already been computed, we compute directly the number of distinct elements
		 */

		if (M != null && m == M.length) {
			double sum = 0d;
			for (int j = 0; j < m; j++) {
				sum += Math.pow(2, -M[j]);
				if (M[j] == Integer.MIN_VALUE)
					throw new Error("m is too large for this text. Please choose a smaller value.");
			}
			return Math.round((alpha(m) * m * m) / sum);
		}

		if (iterable == null)
			throw new Error("iterable must not be null");

		double bd = (Math.log(m) / Math.log(2));
		if (Math.abs(((int) bd) - bd) > 1E-10)
			throw new Error(
					"m must be a power of 2 between entre 2^4 et 2^16");
		int b = (int) bd;
		if (b < 4 || b > 16)
			throw new Error(
					"m must be a power of 2 between entre 2^4 et 2^16");

		M = new int[m];
		for (int i = 0; i < m; i++) {
			M[i] = Integer.MIN_VALUE;
		}
		for (Word v : iterable) {
			String wds = v.getValue();
			if (!wds.isEmpty()) {
				int x = hash.hash(v);
				int j = (x << (32 - b)) >>> (32 - b);
				int w = x >> b;
				int r = rho(w);
				M[j] = Math.max(M[j], r);
			}
		}
		double sum = 0d;
		for (int j = 0; j < m; j++) {
			sum += Math.pow(2, -M[j]);
			if (M[j] == Integer.MIN_VALUE)
				throw new Error("m is too large for this text. Please choose a smaller value.");
		}
		
		return Math.round((alpha(m) * m * m) / sum);
	}

	/**
	 * @return the approximate number of distinct elements of the union of two iterables
	 */
	public double hllUnion(HyperLogLog other, int m) {
		if (M == null)
			hyperLogLog(m);
		if (other.M == null)
			other.hyperLogLog(m);
		int[] M1 = other.M;
		double sum = 0d;
		for (int j = 0; j < m; j++) {
			int mj = Math.max(M[j], M1[j]);
			sum += Math.pow(2, -mj);
		}
		return Math.round((alpha(m) * m * m) / sum);
	}

	/**
	 * @return the approximate number of distinct elements of the intersection of two iterables
	 */
	public double hllInter(HyperLogLog other, int m) {
		return hyperLogLog(m) + other.hyperLogLog(m) - hllUnion(other, m);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		HyperLogLog hll;
		hll = new HyperLogLog(new SimpleHash(), new Itr("Texts/bible_english.txt"));
		System.out.println(hll.hyperLogLog(2048));
		hll.iterable.init();
	}
}
