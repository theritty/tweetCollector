package tweetCollector.nlp;

import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class CustomStopWords {
    File resourcesDirectory = new File("src/main/resources");
	private static String stopWordsFileName = "stopWords.txt";
	private static Set<String> stopWords = new HashSet<>();
	
	public CustomStopWords() {
		try {
			File stopWordsFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + stopWordsFileName);
			CustomStopWords.setStopWords(Sets.newHashSet(Files.readLines(stopWordsFile, Charset.forName("UTF-8"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<String> getStopWords() {
		return stopWords;
	}

	public static void setStopWords(Set<String> stopWords) {
		CustomStopWords.stopWords = stopWords;
	}

}
