import hashs.Djb2Hash;
import hashs.Hash;
import iterables.FileItr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.HyperLogLog;
import main.Icebergs;
import main.Sampling;
import main.Similarity;
import datas.SamplingPair;

/**
 * The entry point of the program
 */
public class Main {

	static private final Hash hash = new Djb2Hash(); // Hash that gives the best results

	static private final int DEFAULT_M_VALUE = 256;
	static private final int DEFAULT_K_VALUE = 1;
	static private final int DEFAULT_SAMPLING_VALUE = 15;
	static private final int MAX_OCCURRENCES_COUNT = 10;

	static private final String METHOD_HYPERLOGLOG = "hyperloglog";
	static private final String METHOD_SIMILARITY = "similarity";
	static private final String METHOD_SAMPLE = "sample";
	static private final String METHOD_ICEBERGS = "icebergs";
	static private final String METHOD_HELP = "help";
	
	static private final String HYPERLOGLOG_SUMMARY = 
		METHOD_HYPERLOGLOG + " path m k"
		+ "\n\tDisplays the approximate number of distinct elements of a given text.";
	
	static private final String HYPERLOGLOG_DETAILS = 
		"\tfile: The path to the text."
		+ "\n\tm: A power of 2. Bigger m is, better is the precision. Default value: 256."
		+ "\n\tk: An element of the text is defined as a k-tuple of consecutive words."
		+ "\n\t If the text is [a, b, c] the text will be treated as [(a,b), (b,c)] for k=2 and [(a,b,c)] for k=3.";
	
	static private final String SIMILARITY_SUMMARY = 
		METHOD_SIMILARITY + " folder threshold m k"
		+ "\n\tDisplays the similarity of couples of texts contained in the folder when it is greater or equal to the given threshold.";
	
	static private final String SIMILARITY_DETAILS = 
			"\tfolder: The path to the folder containing the texts."
			+ "\n\tthreshold: The minimum similarity to display a couple of texts. Between 0 and 1."
			+ "\n\tThe more similar two texts are, closer to 1 is their similarity."
			+ "\n\tm: A power of 2. Bigger m is, better is the precision of the similarity computation."
			+ "\n\tk: An element of the text is defined as a k-tuple of consecutive words."
			+ "\n\t If the text is [a, b, c] the text will be treated as [(a,b), (b,c)] for k=2 and [(a,b,c)] for k=3.";
	
	static private final String SAMPLE_SUMMARY = 
			METHOD_SAMPLE+ " path k"
			+ "\n\tDisplays a sample of the text, containing between k and 2*k words.";
	
	static private final String SAMPLE_DETAILS = 
			"\tpath: The path to the text."
			+ "\n\tk: The number of wanted samples";
	
	static private final String ICEBERGS_SUMMARY = 
			METHOD_ICEBERGS + " path threshold"
			+ "\n\tDisplays the words in the text whose frequency is greater or equal to the given threshold.";
	
	static private final String ICEBERGS_DETAILS =
			"\tpath: The path to the text"
			+ "\n\tthreshold: The minimum frequency for a word to be returned. Between 0 and 1.";
	
	static private final String HELP_SUMMARY =
			METHOD_HELP + " command"
			+ "\n\t Gives details on the command.";

	public static void execute(String[] args) {
		if (args.length < 1) {
			System.out.println("Not recognized as a command.");
		} else {
			if (args[0].equals(METHOD_HYPERLOGLOG)) {
				if (args.length == 2)
					hyperLogLog(args[1], DEFAULT_M_VALUE, DEFAULT_K_VALUE);
				else if (args.length == 3)
					hyperLogLog(args[1], Integer.parseInt(args[2]),
							DEFAULT_K_VALUE);
				else if (args.length == 4)
					hyperLogLog(args[1], Integer.parseInt(args[2]),
							Integer.parseInt(args[3]));
				else
					displayWrongParameters();
			} else if (args[0].equals(METHOD_SIMILARITY)) {
				if (args.length == 3)
					similarity(args[1], Double.parseDouble(args[2]),
							DEFAULT_M_VALUE, DEFAULT_K_VALUE);
				else if (args.length == 4)
					similarity(args[1], Double.parseDouble(args[2]),
							Integer.parseInt(args[3]), DEFAULT_K_VALUE);
				else if (args.length == 5)
					similarity(args[1], Double.parseDouble(args[2]),
							Integer.parseInt(args[3]),
							Integer.parseInt(args[4]));
				else
					displayWrongParameters();
			} else if (args[0].equals(METHOD_SAMPLE)) {
				switch (args.length) {
				case 2:
					sampling(args[1], DEFAULT_SAMPLING_VALUE);
					break;

				case 3:
					sampling(args[1], Integer.parseInt(args[2]));
					break;
				default:
					displayWrongParameters();
					break;
				}
			} else if (args[0].equals(METHOD_ICEBERGS)) {
				switch (args.length) {
				case 3:
					icerbergs(args[1], Double.parseDouble(args[2]));
					break;

				default:
					displayWrongParameters();
					break;
				}
			}
			else if(args[0].equals(METHOD_HELP)) {
				if(args.length == 2) {
					help(args[1]);
				}
				else
					displayWrongParameters();
			}
			else {
				System.err.println("Not recognized as a command.");
			}
		}
	}
	
	public static void help(String method) {
		if(method.equals(METHOD_HYPERLOGLOG)) {
			System.out.println(
				HYPERLOGLOG_SUMMARY +
				"\n" + HYPERLOGLOG_DETAILS
			);
		}
		else if(method.equals(METHOD_SIMILARITY)) {
			System.out.println(
					SIMILARITY_SUMMARY +
				"\n" + SIMILARITY_DETAILS
			);
		}
		else if(method.equals(METHOD_SAMPLE)) {
			System.out.println(
					SAMPLE_SUMMARY +
				"\n" + SAMPLE_DETAILS
			);
		}
		else if(method.equals(METHOD_ICEBERGS)) {
			System.out.println(
					ICEBERGS_SUMMARY +
				"\n" + ICEBERGS_DETAILS
			);
		}
	}

	public static void displayWrongParameters() {
		System.out.println("Wrong parameters.");
	}

	public static void displayFileNotFound(String fileName) {
		System.out.println("The file " + fileName + " was not found.");
	}

	public static void printSummary() {
		System.out.println("Commands : \n\n"
						+ "- " + HYPERLOGLOG_SUMMARY + "\n\n"
						+ "- " + SIMILARITY_SUMMARY + "\n\n"
						+ "- " + SAMPLE_SUMMARY + "\n\n"
						+ "- " + ICEBERGS_SUMMARY + "\n\n"
						+ "- " + HELP_SUMMARY+"\n");
		System.out.println("Texts must be encoded in UTF8. Waiting for a command...");
	}

	public static void hyperLogLog(String filePath, int m, int k) {
		HyperLogLog hll;
		try {
			hll = new HyperLogLog(hash, new FileItr(filePath, k));
			System.out.println((int) hll.hyperLogLog(m));
		} catch (FileNotFoundException e) {
			displayFileNotFound(filePath);
		}

	}

	public static void similarity(String folder, double threshold, int m, int k) {
		try {
			System.out.println(Similarity.getSimilarityBySeuil(hash, folder, k,
					m, threshold));
		} catch (FileNotFoundException e) {
			System.out.println("Problem while reading the folder " + folder
					+ ".");
		}
	}

	public static void sampling(String filePath, int k) {
		SamplingPair sp;
		try {
			sp = Sampling.samplingWithBag(hash,
					new FileItr(new File(filePath).getAbsolutePath(), 1), k,
					MAX_OCCURRENCES_COUNT);
			System.out.println("Representative words : " + sp.words);
		} catch (FileNotFoundException e) {
			displayFileNotFound(filePath);
		}
	}

	public static void icerbergs(String filePath, double theta) {
		try {
			System.out.println(Icebergs.icebergs(hash,
					new FileItr(filePath, 1), theta));
		} catch (FileNotFoundException e) {
			displayFileNotFound(filePath);
		}
	}

	public static void main(String[] args) {		
		Scanner sc = new Scanner(System.in);
		printSummary();
		String command = sc.nextLine();
		while (command != null && !command.equals("END")) {
			args = command.split("[ ]+");
			try {
				execute(args);
			} catch (Error e) {
				e.printStackTrace();
			}
			command = sc.nextLine();
		}
		sc.close();
	}
}
