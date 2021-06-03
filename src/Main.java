import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws IOException {
		// Read files into strings.
		String sequence1 = readFile("seq1.txt", StandardCharsets.US_ASCII);
		String sequence2 = readFile("seq2.txt", StandardCharsets.US_ASCII);
		// Remove any newline commands.
		sequence1 = sequence1.replace("\n", "");
		sequence2 = sequence2.replace("\n", "");
		// Run sequence alignment algorithm.
		SequenceAlignment test = new SequenceAlignment(sequence1, sequence2);
		String[] alignment = test.getAlignment();
		String display = displayMutations(alignment);
		// Begin output.
		System.out.println("Needleman-Wunsch Algorithm: Dynamic Programming for Sequence Alignment");
		System.out.println("--------------------------------------------");
		System.out.println("Alignment Symbols: '*' = Match, '|' = Mismatch (Substitution), ' ' = Gap (Indel)");
		System.out.println("Scoring: Match = 1, Mismatch = -1, Gap = -2");
		System.out.println("--------------------------------------------");
		// Due to the size limitations, divide the sequences accordingly and print portions of it.
		// Added further proofing for various sizes.
		if (alignment[0].length() > 80) {
			int length = alignment[0].length() / 80;
			if ((length * 80) + 80 > alignment[0].length()) { length++; }
			for (int i = 0; i < length; i++) {
				if (i == length - 1) {
					System.out.println("Sequence 1:       " + alignment[0].substring(i * 80));
					System.out.printf("            %4d  " + display.substring(i * 80) + "  %d\n", i * 80 + 1, alignment[0].length());
					System.out.println("Sequence 2:       " + alignment[1].substring(i * 80));
				} else {
					System.out.println("Sequence 1:       " + alignment[0].substring(i * 80, (i * 80) + 80));
					System.out.printf("            %4d  " + display.substring(i * 80, (i * 80) + 80) + "  %d\n", i * 80 + 1, (i * 80) + 80);
					System.out.println("Sequence 2:       " + alignment[1].substring(i * 80, (i * 80) + 80));
				}
			}
		} else {
			System.out.println("Sequence 1:       " + alignment[0]);
			System.out.printf("            %4d  " + display + "  %d\n", 1, alignment[0].length());
			System.out.println("Sequence 2:       " + alignment[1]);
		}
		System.out.println("--------------------------------------------");
		System.out.println("Alignment Score: " + test.getAlignmentScore());
		parseInfo(alignment, alignment[0].length());
	}
	
	/**
	 * Simple read file utility method that returns a string.
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	/**
	 * Read alignment info and display match/mismatch/gap through symbols.
	 */
	public static String displayMutations(String[] alignment) {
		String result = "";
		for (int i = 0; i < alignment[0].length(); i++) {
			if (alignment[0].charAt(i) == '-' || alignment[1].charAt(i) == '-') {
				result = result.concat(" ");
			} else if (alignment[0].charAt(i) == alignment[1].charAt(i)) {
				result = result.concat("*");
			} else {
				result = result.concat("|");
			}
		}
		return result;
	}
	
	/**
	 * Simple parsing method to determine what pair is indel, match, or substitution
	 * and print it neatly.
	 */
	public static void parseInfo(String[] alignment, int length) {
		String gap_locations = "";
		String substitutions = "";
		String format = "";
		int gaps = 0;
		int subs = 0;
		int mats = 0;
		// Check for formatting length.
		if (length < 1000) { format = "%3d"; }
		else { format = "%4d"; }
		
		for (int i = 0; i < length; i++) {
			char align1 = alignment[0].charAt(i);
			char align2 = alignment[1].charAt(i);
			
			if (align1 == '-' || align2 == '-') {
				gaps++;
				if (gaps % 25 == 0) {
					gap_locations = gap_locations.concat(String.format(format, i + 1) + ",\n");
				} else {
					gap_locations = gap_locations.concat(String.format(format, i + 1) + ", ");
				}
			} else if (align1 == align2) {
				mats++;
			} else {
				subs++;
				if (subs % 10 == 0) {
					substitutions = substitutions.concat(String.format(format, i + 1) + ": " + align1 + " <> " + align2 + ",\n");
				} else {
					substitutions = substitutions.concat(String.format(format, i + 1) + ": " + align1 + " <> " + align2 + ", ");
				}
			}
		}
		
		if (gap_locations.length() > 0) {
			gap_locations = gap_locations.substring(0, gap_locations.length() - 2);
		} else {
			gap_locations = "No gaps detected.";
		}
		
		if (substitutions.length() > 0) {
			substitutions = substitutions.substring(0, substitutions.length() - 2);
		} else {
			substitutions = "No substitutions detected.";
		}
		
		System.out.println("Number of Matches: " + mats);
		System.out.println("Number of Insertions/Deletions: " + gaps);
		System.out.println("Locations of Insertion/Deletions:\n" + gap_locations);
		System.out.println("--------------------------------------------");
		System.out.println("Number of Substitutions: " + subs);
		System.out.println("Locations of Substitutions:\n" + substitutions);
	}
}