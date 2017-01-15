package tweetCollector.nlp;

import java.util.regex.Pattern;

public class Punctuation {
	
	// TODO: Add these punctuations as well:(){}<> 
	private static final Pattern PUNCTUATION = Pattern.compile("[/\\*\\$,.:;!?+'&\\-\"\\%\\[\\]\\(\\)\\{\\}\\<\\>]");
	
	public static String removePunctuation(String text) {
		return PUNCTUATION.matcher(text).replaceAll("");
	}

	public static void main(String[] args) {
		// Test cases
		System.out.println(removePunctuation("."));
		System.out.println(removePunctuation(","));
		System.out.println(removePunctuation(";"));
		System.out.println(removePunctuation("..."));
		System.out.println(removePunctuation("?;"));
		System.out.println(removePunctuation("twitter_event"));
		System.out.println(removePunctuation("hello world"));
		System.out.println(removePunctuation("Dr.Pepper"));
		System.out.println(removePunctuation("Dr%Percent %"));	
		System.out.println(removePunctuation("Dr\"Pepper \""));	
		System.out.println(removePunctuation("[Dr.Bracket] [ ] []"));
		System.out.println(removePunctuation("(Dr.Paranthesis) ( ) ()"));	
		System.out.println(removePunctuation("{Dr.Curly} { } {}"));	
		System.out.println(removePunctuation("<Dr.thething> < > <>"));	
		System.out.println(removePunctuation("*Dr*Asteriks* * **"));	
		System.out.println(removePunctuation("$Dr$Dolla$ $ $$"));
		System.out.println(removePunctuation("-Dr-Hyphen- - --"));
		System.out.println(removePunctuation("/Dr/Slash/ / //"));
	}
}