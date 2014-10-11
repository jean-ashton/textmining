# Text Mining

This toolbox allows to perform several operations on large texts, such as:
- Counting the number of distinct words
- Computing similarity between texts
- Finding most frequent words

It relies on probabilistic algorithms that give precise results (though not exact) with linear time complexity and bounded memory.

## How to use ##

The application can be launched by double clicking on the file **TextMining.jar**, or by typing `java -jar TextMining.jar` in the command line. Texts should be encoded in UTF-8.

## Functionalities ##

### Counting distinct elements###

With the `hyperloglog` command, you can count the number of different words in a text or, more generally, of different _elements_. An element is defined as a k-tuple of consecutive words, with arbitrary k.

The `hyperloglog` command needs another argument, m, which should be a power of 2. Bigger m is, better the precision. However, when m becomes too large, the algorithm may fail. In general, the maximal value of m depends on the size of the text.

```
hyperloglog path m k
	Displays the approximate number of distinct elements of a given text.
	path: The path to the text.
	m (optional): Default value: 256. A power of 2. Bigger m is, better is the precision. 
	k (optional): Default value: 1. An element of the text is defined as a k-tuple of consecutive words. If the text is [a, b, c] the text will be treated as [(a,b), (b,c)] for k=2 and [(a,b,c)] for k=3.
```

Example:

```
hyperloglog Texts/bible_english.txt 2048 1
26605
```

This is performed with the HyperLogLog algorithm, due to Philippe Flajolet.

### Computing similarity between texts###

The `similarity` command aims to compute similarity between texts. It takes a `folder`, which is a directory path, a `threshold`, and optionally `m` and `k`. All the similarities between the texts of the folder are computed, and those that are larger than the threshold are returned.

The similarity between two texts is defined as the number of different words that they share, divided by the number of different words in their concatenation. The computation tries to estimate this similarity, again with the HyperLogLog method. The parameters m and k are defined in the same way as before.

```
similarity folder threshold m k
	Displays the similarity of couples of texts contained in the folder when it is greater or equal to the given threshold.
	folder: The path to the folder containing the texts.
	threshold: The minimum similarity to display a couple of texts. Between 0 and 1.
	The more similar two texts are, closer to 1 is their similarity.
	m (optional): A power of 2. Bigger m is, better is the precision of the similarity computation.
	k (optional): An element of the text is defined as a k-tuple of consecutive words. If the text is [a, b, c] the text will be treated as [(a,b), (b,c)] for k=2 and [(a,b,c)] for k=3.
```

Example:

This command can be used to cluster texts according to their language. For instance, when used with the folder Texts_2, containing 2 French texts and 2 English texts, `similarity` correctly recognizes same-language texts:

```
similarity Texts_2 0.2
avare.txt and lecid.txt are 27% similar
macbeth.txt and othello.txt are 32% similar
```

### Sampling###

The command `sample` returns a sample of words chosen randomly among the set of the distinct words of the text, of size between `k` and `2*k`. It does not just take `k` random words in the text: if it did that, it would give more weight to frequent words. The algorithm used is inspired by the ideas behind the HyperLogLog algorithm.

```
sample path k
	Displays a sample of the text, containing between k and 2*k words.
	path: The path to the text.
	k (optional): The desired number of words
```

Example:

```
sample Texts/allswell.txt 4
gentleman slew undertook offended melted 
```

### Detecting frequent words###

_Icebergs_ are defined as the words in a text whose frequency are larger than a certain threshold. It is possible, with a very simple algorithm, to get all icebergs of a text in linear time. This is what the command `icebergs` does.

```
icebergs path threshold
	Displays the words in the text whose frequency is greater or equal to the given threshold.	
	path: The path to the text.
	threshold: The minimum frequency for a word to be returned. Between 0 and 1.
```

Example:

```
icebergs Texts/asyoulikeit.txt 0.05
to as you the i exeunt farewell 
```