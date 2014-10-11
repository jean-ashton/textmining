package main;

import hashs.Hash;
import hashs.SimpleHash;
import iterables.FileItr;
import iterables.MyIterable;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import datas.Word;

public class Icebergs {

	/**
	 * Displays the icebergs of the iterable, which are the elements whose frequency is larger then theta.
	 */
	static public String icebergs(Hash hash, MyIterable iterable, double theta) {

		HashMap<String, Integer> icebergs = new HashMap<String, Integer>();
		int mapMaxSize = (int) Math.ceil(1 / theta);
		int mapSize = 0;

		for (Word word : iterable) {
			if (icebergs.containsKey(word.getValue())) {
				icebergs.put(word.getValue(), icebergs.get(word.getValue()) + 1);
			} else {
				icebergs.put(word.getValue(), 1);
				mapSize++;
			}
			if (mapSize == mapMaxSize) {
				Iterator<Entry<String, Integer>> iterator = icebergs.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, Integer> next = iterator.next();
					if (next.getValue() == 1) {
						iterator.remove();
						mapSize--;
					} else
						icebergs.put(next.getKey(), next.getValue() - 1);
				}
			}
		}

		String words = "";
		for (String s : icebergs.keySet()) {
			words += s + " ";
		}

		return words;
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(icebergs(new SimpleHash(), new FileItr("Texts/asyoulikeit.txt", 1), 0.05));
	}
}
