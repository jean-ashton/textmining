package main;

import hashs.Hash;
import iterables.MyIterable;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import datas.Bag;
import datas.SamplingPair;
import datas.Word;

/*
 * Sampling consists into extracting k representative elements of a text. A random selection
 * will favor frequent elements. Sampling does a random selection on the set of distinct elements.
 * To do so, we keep in memory a size b initialized to 0. We fill a bag of size 2k with elements.
 * Each time the bag is full, we increase b and only keep in the bag the elements who first b bits are 0.
 */
public class Sampling {
	
	public static SamplingPair samplingWithBag(Hash hash, MyIterable iterable,
			int k, int maxOccurrencesCount) throws FileNotFoundException {
		iterable.init();
		Bag bag = new Bag();
		Stack<Word> stack;
		HashMap<String, Integer> wordsCountMap = new HashMap<String, Integer>();
		int b = 0;
		int bagMaxSize = 2 * k;

		for (Word word : iterable) {
			int wordHash = hash.hash(word);
			if (Integer.numberOfTrailingZeros(wordHash) >= b) {
				if (!wordsCountMap.containsKey(word.getValue())) {
					wordsCountMap.put(word.getValue(), 1);
					bag.add(word, wordHash >> b);
				} else {
					wordsCountMap.put(word.getValue(),
							wordsCountMap.get(word.getValue()) + 1);
				}
			}
			if (bag.size() == bagMaxSize) {

				// Emptying the bag

				stack = bag.getStack();
				bag = bag.getSubBag();
				for (Word w : stack) {
					wordsCountMap.remove(w.getValue());
				}

				b++;
			}
		}

		String s = "";
		int[] occurrences = new int[maxOccurrencesCount];
		int wordsCount = 0;
		while (wordsCount < k && bag != null) {
			for (Word w : bag.getStack()) {
				int repeatCount = wordsCountMap.get(w.getValue());
				s += w + " ";
				if (repeatCount < maxOccurrencesCount)
					occurrences[repeatCount - 1]++;
			}
			bag = bag.getSubBag();
		}
		
		int factor = 1 << b;

		for (int i = 0; i < maxOccurrencesCount; i++) {
			occurrences[i] *= factor;
		}
		return new SamplingPair(s, occurrences);

	}

	public static SamplingPair samplingWithLinkedList(Hash hash,
			MyIterable iterable, int k, int maxOccurrencesCount)
			throws FileNotFoundException {

		iterable.init();
		LinkedList<Word> bag = new LinkedList<Word>();
		HashMap<String, Integer> wordsCountMap = new HashMap<String, Integer>();
		int b = 0;
		int bagSize = 0;
		int bagMaxSize = 2 * k;

		for (Word word : iterable) {
			if (hash.getTrailingZerosOf(word) >= b) {
				if (!wordsCountMap.containsKey(word.getValue())) {
					wordsCountMap.put(word.getValue(), 1);
					bag.addFirst(word);
					bagSize++;
				} else {
					wordsCountMap.put(word.getValue(),
							wordsCountMap.get(word.getValue()) + 1);
				}
			}
			if (bagSize == bagMaxSize) {

				// Emptying the bag

				Iterator<Word> bagIterator = bag.iterator();
				while (bagIterator.hasNext()) {
					Word w = bagIterator.next();
					if (hash.getTrailingZerosOf(w) <= b) {
						bagIterator.remove();
						bagSize--;
						wordsCountMap.remove(w.getValue());
					}
				}

				b++;
			}
		}

		String s = "";
		int[] occurrences = new int[maxOccurrencesCount];
		int wordsCount = 0;
		for (Word word : bag) {
			int repeatCount = wordsCountMap.get(word.getValue());
			if (wordsCount <= k) {
				s += word.getValue() + " ";
				wordsCount++;
			}
			if (repeatCount < maxOccurrencesCount)
				occurrences[repeatCount - 1]++;
		}

		int factor = 1 << b;

		for (int i = 0; i < maxOccurrencesCount; i++) {
			occurrences[i] *= factor;
		}

		return new SamplingPair(s, occurrences);

	}

	/**
	 * @return The first free row of the array tab.
	 */
	public static int getPlace(int[] tab) {
		for (int i = 0; i < tab.length; i++)
			if (tab[i] == Integer.MIN_VALUE)
				return i;
		return -1;
	}

	/**
	 * Only keeps in tab the elements whose first b bits are 0. Returns the first free row.
	 */
	public static int makePlace(int[] tab, int b) {
		int first = -1;
		for (int i = 0; i < tab.length; i++) {
			if (b < Integer.numberOfLeadingZeros(tab[i])) {
				tab[i] = Integer.MIN_VALUE;
				if (first == -1)
					first = i;
			}
		}
		return first;
	}

	/**
	 * @return A array of words of size <= 2k selected randomly from the set of distinct words of the iterable 
	 */
	public static String[] echantillonnage(Hash hash, MyIterable iterable, int k) {
		long start = System.currentTimeMillis();
		int b = 0;
		String[] frequent = new String[2 * k];
		int hashs[] = new int[2 * k];
		for (int i = 0; i < hashs.length; i++) {
			hashs[i] = Integer.MIN_VALUE;
		}
		for (Word wrd : iterable) {
			int x = hash.hash(wrd);
			int p = getPlace(hashs);
			if (p == -1) {
				b++;
				p = makePlace(hashs, b);
			}
			if (p == -1) {
				b++;
			} else {
				hashs[p] = x;
				frequent[p] = wrd.toString();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Calcul terminé en " + (end - start) + "ms");
		return frequent;
	}
}
