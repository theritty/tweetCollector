package tweetCollector.nlp;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class SpecialPrefixes {
	File resourcesDirectory = new File("src/main/resources");
	private static String sPrefixesFileName = "specialPrefixes.txt";
	private static Set<String> specialPrefixes = new HashSet<>();
	private static String[] specialPrefixList;
	
	SpecialPrefixes(){
		try {
			File sPrefixesFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + sPrefixesFileName);
			SpecialPrefixes.setSpecialPrefixes(Sets.newHashSet(Files.readLines(sPrefixesFile, Charset.forName("UTF-8"))));
			specialPrefixList = specialPrefixes.toArray(new String[specialPrefixes.size()]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Set<String> getSpecialPrefixes() {
		return specialPrefixes;
	}

	public static void setSpecialPrefixes(Set<String> specialPrefixes) {
		SpecialPrefixes.specialPrefixes = specialPrefixes;
	}
	
	// Given a sentence and a prefix list, checks whether sentence starts with any of prefixes
	public boolean hasSpecialPrefix(String sentence) {
		return StringUtils.startsWithAny(sentence, specialPrefixList);
	}
}
