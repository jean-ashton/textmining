package datas;
import java.util.Stack;

/**
* A bag is a recursive structure containing words. It is:
	- Empty
	- Or defined by:
		- A size b
		- A LIFO of words whose hashs shifted by b bits to the right begin by a 1.
		- A Bag of size b+1
*/
public class Bag {
			
	private Bag subBag;
	private Stack<Word> stack;
	private int size;
	
	public Bag() {		
		stack = new Stack<Word>();
	}
	
	public boolean isEmpty() {
		return subBag == null && stack.size() == 0;
	}
	
	/**
	 * 
	 * @param w: word to add to the Bag
	 * @param i: the hash of w shited by b bits to the right
	 */
	public void add(Word w, int i) {
		if(i != 0) {
			size++;
			if((i & 1) == 1) {
				stack.push(w);
			}
			else {
				if(subBag == null) 
					subBag = new Bag();
				subBag.add(w, i >> 1);
			}
		}
	}
	
	/**
	 *	@return A string containing the words in the Bag, each separated by a space.
	 */
	public String getWords() {
		String s = "";
		for(Word w : stack) {
			s += w.getValue() + " ";
		}
		if(subBag != null)
			s += subBag.getWords();
		return s;
	}
	
	public int size() {
		return size;
	}
	
	public Bag getSubBag() {
		return subBag;
	}
	
	public Stack<Word> getStack() {
		return stack;
	}
}
