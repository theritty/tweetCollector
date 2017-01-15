package tweetCollector.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import tweetCollector.nlp.TextAnalyzer;

import java.util.List;
import java.util.Map;


public class PreprocessFromCassTweetBolt extends BaseRichBolt {

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
    String tweet = tuple.getStringByField( "tweet" );

    List<String> preprocessText = textAnalyzer.extractWordList(tweet);
    if(preprocessText == null || preprocessText.size()==0) return;

//    System.out.println("yey prep");
    this.collector.emit(new Values(preprocessText,
            tuple.getLongByField("round"),
            tuple.getValueByField("tweetTime"),
            tuple.getLongByField("id"),
            tuple.getLongByField("retweetcount"),
            tuple.getLongByField("userid"),
            tuple.getStringByField("country")
    ));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {
    declarer.declare(new Fields("tweet", "round", "tweettime", "id", "retweetcount", "userid", "country"));
  }
}
