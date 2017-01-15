package tweetCollector.nlp;

import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class CustomAbbreviations {
	File resourcesDirectory = new File("src/main/resources");
	private static String abbreviationsFileName = "abbreviations.txt";
	private static Set<String> abbreviations = new HashSet<>();
	
	public CustomAbbreviations() {
		try {
			File abbreviationsFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + abbreviationsFileName);
			CustomAbbreviations.setAbbreviations(Sets.newHashSet(Files.readLines(abbreviationsFile, Charset.forName("UTF-8"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<String> getAbbreviations() {
		return abbreviations;
	}

	public static void setAbbreviations(Set<String> abbreviations) {
		CustomAbbreviations.abbreviations = abbreviations;
	}

}
