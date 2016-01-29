import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

public class markov {
	// Hashmap
	public static Hashtable<String, Vector<String>> markovChain = new Hashtable<>();
	static Random rnd = new Random();

	public static void main(String[] args) throws IOException {
		//ScriptScraper.downloadEpisodes();
		ScriptReader scriptReader = new ScriptReader();
		// Create the first two entries (k:_start, k:_end)
		markovChain.put("_start", new Vector<>());
		markovChain.put("_end", new Vector<>());
		while (scriptReader.hasNext())
			addWords(scriptReader.nextLog());
		generateSentence();
		generateSentence();
		generateSentence();
		generateSentence();
		generateSentence();
		generateSentence();
	}

	public static void addWords(String phrase) {
		// put each word into an array
		String[] words = phrase.split(" ");
		for(String s : words){
			if(s.length() == 1)
				return;
		}
		// Loop through each word, check if it's already added
		// if its added, then get the suffix vector and add the word
		// if it hasn't been added then add the word to the list
		// if its the first or last word then select the _start / _end key
		for (int i = 0; i < words.length; i++) {
			// Add the start and end words to their own
			if (i == 0) {
				Vector<String> startWords = markovChain.get("_start");
				startWords.add(words[i]);
				Vector<String> suffix = markovChain.get(words[i]);
				if (suffix == null) {
					suffix = new Vector<>();
					suffix.add(words[i + 1]);
					markovChain.put(words[i], suffix);
				}
			} else if (i == words.length - 1) {
				Vector<String> endWords = markovChain.get("_end");
				endWords.add(words[i]);
			} else {
				Vector<String> suffix = markovChain.get(words[i]);
				if (suffix == null) {
					suffix = new Vector<>();
					suffix.add(words[i + 1]);
					markovChain.put(words[i], suffix);
				} else {
					suffix.add(words[i + 1]);
					markovChain.put(words[i], suffix);
				}
			}
		}
	}

	public static void generateSentence() {
		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<>();
		// String for the next word
		String nextWord;
		// Select the first word
		Vector<String> startWords = markovChain.get("_start");
		int startWordsLen = startWords.size();
		nextWord = startWords.get(rnd.nextInt(startWordsLen));
		newPhrase.add(nextWord);
		// Keep looping through the words until we've reached the end
		while (nextWord.charAt(nextWord.length() - 1) != '$') {
			Vector<String> wordSelection = markovChain.get(nextWord);
			int wordSelectionLen = wordSelection.size();
			nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
			newPhrase.add(nextWord);
		}
		showPhrase(newPhrase);
	}
	public static void showPhrase(Vector<String> phrase){
		String out = "";
		for(String s : phrase){
			out += s + " ";
		}
		System.out.println(out.replace("$", ""));
	}
}