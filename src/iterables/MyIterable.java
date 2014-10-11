package iterables;

import java.io.FileNotFoundException;

import datas.Word;

public interface MyIterable extends Iterable<Word> {
	void init() throws FileNotFoundException;

	String name();
}
