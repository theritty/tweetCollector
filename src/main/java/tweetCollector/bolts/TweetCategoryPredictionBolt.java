package tweetCollector.bolts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import weka.NaiveBayesClassification;

public class TweetCategoryPredictionBolt extends BaseRichBolt {


	private OutputCollector collector;
	private NaiveBayesClassification naiveBayesClassification;
private long count=0;

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
		naiveBayesClassification = new NaiveBayesClassification();
		naiveBayesClassification.prepare();
		this.collector = outputCollector;
	}



	@Override
	public void execute(Tuple tuple) {
		List<String> tweet_words = (List<String>) tuple.getValueByField( "tweet" );
		Date timestamp = (Date) tuple.getValueByField("tweettime");
		long round = tuple.getLongByField("round");
		long id =	tuple.getLongByField("id");
		long rtCount = tuple.getLongByField("retweetcount");
		long userid =	tuple.getLongByField("userid");
		String country = tuple.getStringByField("country");

		String tweet="";
		for(String twee: tweet_words) tweet= tweet + twee + " ";

		ArrayList<String> predictedCategories =  naiveBayesClassification.execute(tweet_words);
		if((count+1) % 100 == 0) System.out.println("count " + ++count);
//		System.out.println("yey predict");
		this.collector.emit(new Values(
						tweet,
						round,
						timestamp,
						id,
						rtCount,
						userid,
						country,
						predictedCategories));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet", "round", "tweettime", "id", "retweetcount", "userid", "country", "categories"));
	}
}
