package topologyBuilder;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import cassandraConnector.CassandraDao;
import tweetCollector.spout.CassandraSpout;
import tweetCollector.bolts.*;
import tweetCollector.spout.TwitterSpout;

import java.util.Properties;


public class BoltBuilder {
    public static StormTopology prepareBoltsForTwitter(Properties properties) {
        int COUNT_THRESHOLD = Integer.parseInt(properties.getProperty("topology.count.threshold"));
        double TIME_INTERVAL_IN_HOURS = Double.parseDouble(properties.getProperty("topology.time.interval"));
        String FILENUM = properties.getProperty("topology.file.number");
        String TWEETS_TABLE = properties.getProperty("tweets.table");
        String COUNTS_TABLE = properties.getProperty("counts.table");
        String EVENTS_TABLE = properties.getProperty("events.table");

        String CONSUMER_KEY = properties.getProperty("consumer.key");
        String CONSUMER_SECRET = properties.getProperty("consumer.secret");
        String ACCESS_TOKEN = properties.getProperty("access.token");
        String ACCESS_TOKEN_SECRET = properties.getProperty("access.token.secret");

        TopologyBuilder builder = new TopologyBuilder();

        TwitterSpout spout = new TwitterSpout(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
                ACCESS_TOKEN_SECRET, TIME_INTERVAL_IN_HOURS, Integer.parseInt(properties.getProperty("topology.train.size")),
                Integer.parseInt(properties.getProperty("topology.compare.size")));

        PreprocessTweetBolt preprocessor = new PreprocessTweetBolt();

        System.out.println("time interval " + TIME_INTERVAL_IN_HOURS * 60 * 60 + " & threshold " + COUNT_THRESHOLD);

        TopologyHelper.createFolder(Constants.STREAM_FILE_PATH + FILENUM);
        builder.setSpout(Constants.TWITTER_SPOUT_ID, spout);
        builder.setBolt(Constants.PREPROCESS_SPOUT_ID, preprocessor).shuffleGrouping(Constants.TWITTER_SPOUT_ID);


        builder.setBolt(Constants.CASS_BOLT_ID, new CassBolt(TIME_INTERVAL_IN_HOURS, TWEETS_TABLE, COUNTS_TABLE, EVENTS_TABLE)).
                shuffleGrouping(Constants.PREPROCESS_SPOUT_ID);
        return builder.createTopology();
    }

    public static StormTopology prepareBoltsForPreprocess(Properties properties) throws Exception {
        String FILENUM = properties.getProperty("topology.file.number");
        String TWEETS_TABLE = properties.getProperty("tweets.table");
        String COUNTS_TABLE = properties.getProperty("counts.table");
        String EVENTS_TABLE = properties.getProperty("events.table");

        CassandraDao cassandraDao = new CassandraDao(TWEETS_TABLE, COUNTS_TABLE, EVENTS_TABLE);
        System.out.println("Preparing Bolts...");
        TopologyBuilder builder = new TopologyBuilder();

        CassandraSpout cassandraSpout = new CassandraSpout(cassandraDao, 3,
                Integer.parseInt(properties.getProperty("topology.compare.size")),Integer.MAX_VALUE, FILENUM );

        PreprocessFromCassTweetBolt preprocessor = new PreprocessFromCassTweetBolt();
        TweetCategoryPredictionBolt tweetCategoryPredictionBolt = new TweetCategoryPredictionBolt();
        CassCategoriesBolt cassCategoriesBolt = new CassCategoriesBolt(cassandraDao);

        builder.setBolt(Constants.PREPROCESS_SPOUT_ID, preprocessor).
                shuffleGrouping(Constants.CASS_SPOUT_ID);
        builder.setBolt(Constants.CLASSIFIER_BOLT_ID, tweetCategoryPredictionBolt).
                shuffleGrouping(Constants.PREPROCESS_SPOUT_ID);
        builder.setBolt(Constants.CASS_BOLT_ID, cassCategoriesBolt).
                shuffleGrouping(Constants.CLASSIFIER_BOLT_ID);

        return builder.createTopology();
    }

}

