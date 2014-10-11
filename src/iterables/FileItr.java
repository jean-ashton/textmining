package iterables;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import datas.SWord;
import datas.Word;

/**
 * This class is used to iterate over the elements of a text. 
 * An element of the text is defined as a k-tuple of consecutive words.
 * If the text is [a, b, c] the text will be treated as [(a,b), (b,c)] for k=2 and [(a,b,c)] for k=3.
 */
public class FileItr implements MyIterable {

	Scanner scan;
	String file;
	int k;

	public FileItr(String file, int k) throws FileNotFoundException {
		if (k < 1)
			throw new Error("k must be >= 1");
		this.file = file;
		this.k = k;
		scan = new Scanner(new File(file));
		scan.useDelimiter("(( |\n)+|\\.(( |\n)+)?|,(( |\n)+)?)");
	}

	@Override
	public Iterator<Word> iterator() {
		Iterator<Word> itr = new Iterator<Word>() {

			@Override
			public void remove() {
			}

			@Override
			public Word next() {
				String res = scan.next();
				for (int i = 0; i < k - 1 && hasNext(); i++) {
					String s = scan.next();
					res += " " + s;
				}
				return new SWord(res.toLowerCase());
			}

			@Override
			public boolean hasNext() {
				boolean has = scan.hasNext();
				return has;
			}
		};
		return itr;
	}

	@Override
	public void init() throws FileNotFoundException {
		scan.close();
		scan = new Scanner(new File(file));
		scan.useDelimiter("(( |\n)+|\\.(( |\n)+)?|,(( |\n)+)?)");

	}

	@Override
	public String name() {
		return file;
	}

}