package tweetCollector.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import tweetCollector.nlp.TextAnalyzer;
import twitter4j.GeoLocation;
import twitter4j.Status;

import java.util.List;
import java.util.Map;


public class PreprocessTweetBolt extends BaseRichBolt {

  private TextAnalyzer textAnalyzer;
  private OutputCollector collector;

  @Override
  public void prepare(Map config, TopologyContext context,
                      OutputCollector collector) {
    this.collector = collector;
    textAnalyzer = new TextAnalyzer();
  }

  @Override
  public void execute(Tuple tuple) {
    String tweet;
    GeoLocation[][] loc = null;
    String source = (String) tuple.getValueByField( "source" );
    Status x = null;
    if(source.equals("twitter"))
    {
      x = (Status) tuple.getValueByField( "tweet" );
      tweet = x.getText();
      if(x.getPlace() == null) return;
      loc = x.getPlace().getBoundingBoxCoordinates();
    }
    else
    {
      tweet = (String) tuple.getValueByField( "tweet" );
    }

    List<String> preprocessText = textAnalyzer.extractWordList(tweet);
    this.collector.emit(new Values(preprocessText,
            tuple.getValueByField( "dates" ),
            tuple.getValueByField( "currentDate" ),
            tuple.getValueByField( "blockEnd" ),
            tuple.getLongByField("round"),
            source,
            loc,
            x));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {
    declarer.declare(new Fields("tweet","dates","currentDate","blockEnd", "round",
            "source", "location", "tweetOriginal"));
  }
}
