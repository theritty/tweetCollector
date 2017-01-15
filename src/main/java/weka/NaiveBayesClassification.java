package weka;

import tweetCollector.nlp.TextAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NaiveBayesClassification {

  static File resourcesDirectory = new File("src/main/resources");
  private static String politicsTrainingFileName = "politicsTweets.txt";
  private static String musicTrainingFileName = "musicTweets.txt";
  private static String sportsTrainingFileName = "sportsTweets.txt";

  private BaseNaiveBayesClassifier politicsClassifier;
  private BaseNaiveBayesClassifier musicClassifier;
  private BaseNaiveBayesClassifier sportsClassifier;

  public void prepare() {
    politicsClassifier = new BaseNaiveBayesClassifier();
    musicClassifier = new BaseNaiveBayesClassifier();
    sportsClassifier = new BaseNaiveBayesClassifier();

    try {
      politicsClassifier.createClassifier(politicsTrainingFileName, "tweet", "politics", "politics");
      musicClassifier.createClassifier(musicTrainingFileName, "tweet", "music", "music");
      sportsClassifier.createClassifier(sportsTrainingFileName, "tweet", "sports", "sports");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public ArrayList<String> execute(List<String> tweet_words) {
    String tweet = "";
    ArrayList<String> predictedCategories = new ArrayList<>();

    try {
      if(tweet_words== null || tweet_words.size()==0) {
        return predictedCategories;
      }

      for(String twee:tweet_words) {
        tweet = tweet + twee + " ";
      }

      String politicsPrediction = politicsClassifier.classify(tweet, "tweet", "politics");
      String musicPrediction = musicClassifier.classify(tweet, "tweet", "music");
      //String musicPrediction = "nope";
      String sportsPrediction = sportsClassifier.classify(tweet, "tweet", "sports");
      //String sportsPrediction = "nope";

      if(politicsPrediction.equalsIgnoreCase("yes")) {
        predictedCategories.add("politics");
      }

      if(musicPrediction.equalsIgnoreCase("yes")) {
        predictedCategories.add("music");
      }

      if(sportsPrediction.equalsIgnoreCase("yes")) {
        predictedCategories.add("sports");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return predictedCategories;

  }

  public static void main(String[] args) throws Exception {
    NaiveBayesClassification e = new NaiveBayesClassification();
    e.prepare();

    TextAnalyzer ta = new TextAnalyzer();

    String tweet1 = "increasingly vocal hispanic minority speak favor trump resentment suspicion friend family";
    String tweet2 = "leader speak strip parliamentary immunity one major turkish news channel will broadcast";
    String tweet3 = "professor say three point choose intense study really mean none result make sense";
    String tweet4 = "walk steal base ground wild pitch bring home first st. michael prep run game tie 1-1 go bottom three";
    List<String> deneme1 = ta.extractWordList(tweet1);
    List<String> deneme2 = ta.extractWordList(tweet2);
    List<String> deneme3 = ta.extractWordList(tweet3);
    List<String> deneme4 = ta.extractWordList(tweet4);

    List<String> predictedCategories1 = e.execute(deneme1);
    List<String> predictedCategories2 = e.execute(deneme2);
    List<String> predictedCategories3 = e.execute(deneme3);
    List<String> predictedCategories4 = e.execute(deneme4);
    System.out.println("Predicted Categories: " + predictedCategories1);
    System.out.println("Predicted Categories: " + predictedCategories2);
    System.out.println("Predicted Categories: " + predictedCategories3);
    System.out.println("Predicted Categories: " + predictedCategories4);
  }

}

