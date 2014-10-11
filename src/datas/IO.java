package datas;

import hashs.Hash;
import iterables.MyIterable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class IO {

	public static void scilabify(int[] hashed) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("Scilab/shakesSum.sce"));
		DecimalFormat df = new DecimalFormat();
		double v = (hashed[0]) % hashed.length;
		pw.print("y = [" + df.format(v));
		for (int i = 1; i < hashed.length; i++) {
			v = (hashed[i]) % hashed.length;
			pw.print(" " + df.format(v));
		}
		pw.println("]");
		pw.println("histplot(" + hashed.length + ", y)");
		pw.close();
	}

	public static void scilabify(Hash hash, MyIterable iterable, String file) {
		PrintWriter pw;
		long start = System.currentTimeMillis();
		try {
			pw = new PrintWriter(new File("Scilab/" + hash.name() + file
					+ "mod.sce"));

			pw.print("y = [");
			int count = 0;
			int nbMot = 0;
			for (Word w : iterable) {
				if (count++ == 500) {
					pw.println("]");
					pw.println("histplot(" + count + ", y)");
					pw.print("y = [");
					count = 0;
				}
				int v = hash.hash(w);
				double d = ((double) v) / Integer.MAX_VALUE;
				d = d * 50000;
				pw.print(" " + (int) (d / 100));
				nbMot++;
			}
			if (count < 500)
				pw.println("]");
			pw.println("xtitle(\"Histogramme des valeurs hashées par le hash "
					+ hash.name() + " pour " + file + " contenant " + nbMot
					+ " mots.\")");
			pw.println("histplot(" + count + ", y)");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Calcul terminé pour " + hash.name() + " en "
				+ (end - start) + "ms");
	}

	public static void scilabify(double[] table, String file) {
		PrintWriter pw;
		long start = System.currentTimeMillis();
		try {
			pw = new PrintWriter(new File("Scilab/" + file + ".sce"));

			pw.print("y = [");
			for (double d : table) {
				pw.print(" " + d);
			}
			pw.println("]");
			pw.println("histplot(" + table.length + ", y)");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Calcul terminé pour " + file + " en "
				+ (end - start) + "ms");
	}
}
