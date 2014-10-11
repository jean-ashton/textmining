package iterables;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import datas.SWord;
import datas.Word;

/**
 * This class is used to iterate over the words of a text.
 */
public class Itr implements MyIterable {

	Scanner scan;
	String file;
	
	public Itr(String file) throws FileNotFoundException {
		this.file = file;
		scan = new Scanner(new File(file), "UTF-8");
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
				return new SWord(scan.next());
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
	public void init() {
		scan.close();
		try {
			scan = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		scan.useDelimiter("(( |\n)+|\\.(( |\n)+)?|,(( |\n)+)?)");
	}

	@Override
	public String name() {
		return file;
	}

}