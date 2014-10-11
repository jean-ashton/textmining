package main;

import hashs.Hash;
import hashs.SimpleHash;
import iterables.FileItr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Similarity {

	File folder;
	int k;
	File content[];
	public static final double PRECISION = 0.95;

	public Similarity(File folder) {
		if (!folder.isDirectory())
			throw new Error("The path must link to a folder.");
		this.folder = folder;
		this.content = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
	}

	public Similarity() {
	}

	/**
	 * Create a table of similarities between each couple of texts from the folder.
	 */
	public static String[][] similarities(Hash hash, File folder, int k, int m) throws FileNotFoundException {
		String[][] table = new String[][] {};
		File[] content = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		
		HyperLogLog[] hlls = new HyperLogLog[content.length];
		for (int i = 0; i < content.length; i++) {
			hlls[i] = new HyperLogLog(hash, new FileItr(content[i].getAbsolutePath(), k));
		}
		
		table = new String[content.length + 1][content.length + 1];
		table[0][0] = "";
		for (int l = 0; l < content.length; l++) {
			if (!content[l].isDirectory()) {
				table[0][l + 1] = content[l].getName();
			}
		}

		for (int i = 0; i < content.length; i++) {
			if (!content[i].isDirectory()) {
				table[i + 1][0] = content[i].getName();
				for (int j = 0; j < content.length; j++) {
					if (!content[j].isDirectory()) {
						if (table[i + 1][j + 1] == null) {
							String value = ""
									+ similarity(hlls[i], hlls[j], m);
							table[i + 1][j + 1] = value;
							table[j + 1][i + 1] = value;
						}
					}
				}
			}
		}

		return table;
	}

	/**
	 * @return The couples of texts from the folder and their similarities when it is larger than the threshold seuil
	 */
	public static String getSimilarityBySeuil(Hash hash, String folder, int k,
			int m, double seuil) throws FileNotFoundException {
		String results = "";
		File fold = new File(folder);
		File[] content = fold.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		if(content == null) throw new FileNotFoundException();
		HyperLogLog[] hlls = new HyperLogLog[content.length];
		for (int i = 0; i < content.length; i++) {
			hlls[i] = new HyperLogLog(hash, new FileItr(content[i].getAbsolutePath(), k));
		}
		
		for (int i = 0; i < content.length; i++) {
			if (!content[i].isDirectory()) {
				for (int j = i + 1; j < content.length; j++) {
					if (!content[j].isDirectory()) {
						double value = similarity(hlls[i], hlls[j], m);
						if (value >= seuil) {
							String line = content[i].getName() + " and "
									+ content[j].getName()
									+ " are " + Math.round((1000 * value)/10)
									+ "% similar";
							results += line + "\n";
						}
					}
				}
			}
		}
		
		return results;
	}

	/**
	 * @return The simimilarity between two texts
	 */
	public static double similarity(HyperLogLog hll1, HyperLogLog hll2, int m) {
		double a = hll1.hyperLogLog(m);
		double b = hll2.hyperLogLog(m);
		double union = hll1.hllUnion(hll2, m);
		double inter = a + b - union;
		double simila = inter / union;
		return simila;
	}

	/**
	 * Write the table of similarities in a xls file.
	 */
	public static void writeOnFile(String[] table[], String file)
			throws FileNotFoundException {
		File out = new File(file);
		PrintWriter pw = new PrintWriter(out);
		pw.println("Result");
		for (int i = 0; i < table.length; i++) {
			pw.print(table[i][0]);
			for (int j = 1; j < table.length; j++) {
				pw.print("\t" + table[i][j]);
			}
			pw.println();
		}
		pw.close();
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Hash hash = new SimpleHash();
		if (args.length == 1) {
			File folder = new File(args[0]);
			try {
				writeOnFile(similarities(hash, folder, 4, HyperLogLog.ME),
						"./Results.xls");
				long end = System.currentTimeMillis();
				System.out.println("Calcul terminé en " + (end - start));
				System.out
						.println("Le resultat de la comparaison se trouve dans ./Results.xls");
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			}
		} else if (args.length == 2) {
			File folder = new File(args[0]);
			try {
				System.out.println("En cours...");
				writeOnFile(
						similarities(hash, folder, Integer.parseInt(args[1]),
								HyperLogLog.ME), "./Results" + args[1] + ".xls");
				long end = System.currentTimeMillis();
				System.out.println("Calcul terminé en " + (end - start));
				System.out
						.println("Le resultat de la comparaison se trouve dans ./Results"
								+ args[0] + ".xls");
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.out.println("Similarity k path");
			}
		} else if (args.length == 3) {
			try {
				System.out.println("En cours...");
				System.out.println(Arrays.toString(args));
				getSimilarityBySeuil(hash, args[0], Integer.parseInt(args[1]),
						HyperLogLog.ME, Double.parseDouble(args[2]));
				long end = System.currentTimeMillis();
				System.out.println("Calcul terminé en " + (end - start));
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.out.println("Similarity path k");
			}
		} else {
			System.out.println("Similarity path [k seuil]");
		}
	}
}
