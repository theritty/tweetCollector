package tweetCollector.nlp;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class TextAnalyzer {
	
	private final static String[] schemesForValidation = {"http", "https"};
	private static StanfordCoreNLP pipeline;
	
	public TextAnalyzer() {
    	// Create a StanfordCoreNLP object
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        TextAnalyzer.pipeline = new StanfordCoreNLP(props);
	}

	// Takes a text, returns a lemmatized clean word list
    public List<String> extractWordList(String text) {
    	// Initialize special prefixes
    	SpecialPrefixes sp = new SpecialPrefixes();
    	// Initialize URL validator
    	UrlValidator uval = new UrlValidator(schemesForValidation);
    	// Get the custom stop word set
        CustomStopWords csw = new CustomStopWords();
        Set<String> stopWords = csw.getStopWords();
    	// Get custom abbreviations
        CustomAbbreviations ca = new CustomAbbreviations();
        Set<String> abbreviations = ca.getAbbreviations();

        Annotation document = pipeline.process(text);
        List<String> extractedWords = new ArrayList<>();

        for(CoreMap sentence : document.get(SentencesAnnotation.class)){
        	// If a sentence starts with a special prefix, just skip it
        	if (sp.hasSpecialPrefix(sentence.toString())) {
        		continue;
        	}

        	// Else attempt to tokenize, lemmatize, etc.
            for(CoreLabel token: sentence.get(TokensAnnotation.class)){
                String word = token.word().toLowerCase().trim();

                // If the parsed word is a numeric, ignore it
                if(StringUtils.isNumeric(word)) {
                	continue;
                }
                
                // Eliminate Twitter mentions, e.g. @username
                else if(word.startsWith("@")) {
                	continue;
                }

                // If the word is nothing but a punctuation, ignore it
                else if(Punctuation.removePunctuation(word).equalsIgnoreCase("")){
                	continue;
                }
              
                // Eliminate, if word is in stop word set
                else if (stopWords.contains(word)) {
                	continue;
                }
                
                // Eliminate, if word is in abbreviations set
                else if(abbreviations.contains(word)) {
                	continue;
                }
                
                // If the parsed word is a valid URL, ignore it
                else if(uval.isValid(word)) {
                	continue;
                }
                
                // If word passes all the tests, attempt to lemmatize
                else {
                	String lemma = token.lemma().toLowerCase().trim();
                	Punctuation.removePunctuation(lemma);
                    extractedWords.add(lemma);
                }
            }
        }
        
        return extractedWords;
    }

    public static void main(String[] args) {
    	
        String text = "It was Ms. Fitzhugh. She was walking fast. "
       		+ "A strange expression crossed the faces of the students as they glanced toward "
       		+ "the door and saw the principal go straight into the boys’ restroom. The footsteps stopped."
       		+ " There was a deep, throaty sound difficult to describe. Then came an eruption of shrill "
       		+ "screaming and a rapid sound of heels. Moments later, Ms. Fitzhugh emerged, her eyes wild. "
       		+ "Screaming, she skidded in the hall and headed toward the office.";
        
        String tweet1 = "We're proud to join 160+ companies in @HRC's petition to #RepealHB2,"
        		+ " because we believe in equal rights for all:";
        
        String tweet2 = "TODAY WAS MY GRANDPARENTS 65TH WEDDING ANNIVERSARY AND "
        		+ "I THREW FIRST PITCH AT @GoSquirrels  AND I DEADLIFTED 295 SO IT WAS A GOOD DAY.";
        
        String tweet3 = "Adding space…in space. Watch as BEAM module attaches to @Space_Station "
        		+ "tomorrow at 5:30am ET http://go.nasa.gov/1SgkTSv ";
        
        String tweet4 = "Donate to HRC for a chance to win 2 tickets to @springsteen's NYC show. "
        		+ "Enter on @crowdrise at http://crowdrise.com/nohate  #NoHateInMyState";
        
        String tweet5 = "Made the ray marcher into a 3.5 KB html page: http://doodle.notch.net/unmandelboxing/ "
        		+ "IE seems to dislike it.";
        
        String tweet6 = "Woke up before 8 am for the first time in a long time, "
        		+ "and I kind of like it. So many hours of sunlight ahead of me! Coffee? Need coffee.";
        
        String tweet7 = "I only follow'ed you bc I was waiting for you to finish http://cliffhorse.com . "
        		+ "Now, I follow bc you're damn funny.";
        
        String tweet8 = "lol afaik this #test is the best thing related to league of legends omg ahahah";
        String tweet9 = "I liked a @YouTube video http://youtu.be/Vi1tkUNpohM?a  Cologne Shop Roleplay (ASMR) #test";
        String tweet10 = "I'm at @AstirBeach in Vouliagmeni, Athens https://www.swarmapp.com/c/7lwQrizYC06 #test";
       
        String randomTweet = "HDP leaders speaking after being stripped of their parliamentary immunity. Not one major Turkish news channel will broadcast it.";
        
      TextAnalyzer ta = new TextAnalyzer();
//      System.out.println(String.join(" ", ta.extractWordList(text)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet1)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet2)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet3)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet4)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet5)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet6)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet7)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet8)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet9)));
//      System.out.println(String.join(" ", ta.extractWordList(tweet10)));

      System.out.println(String.join(" ", ta.extractWordList(randomTweet)));
      
    }
}
