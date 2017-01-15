package tweetCollector.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cassandraConnector.CassandraDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CassCategoriesBolt extends BaseRichBolt
{
  List<Object> values = new ArrayList<>();

  private CassandraDao cassandraDao;

  public CassCategoriesBolt( CassandraDao cassandraDao)
  {
      this.cassandraDao = cassandraDao;
  }

  @Override
  public void prepare( final Map map, final TopologyContext topologyContext, final OutputCollector outputCollector )
  {
  }

  @Override
  public void execute( final Tuple tuple )
  {
    values = new ArrayList<>();
    values.add(tuple.getLongByField("id"));
    values.add(tuple.getStringByField("tweet"));
    values.add(tuple.getLongByField("userid"));
    values.add(tuple.getValueByField("tweettime"));
    values.add(tuple.getLongByField("retweetcount"));
    values.add(tuple.getLongByField("round"));
    values.add(tuple.getStringByField("country"));

    ArrayList<String> categories = (ArrayList<String>)tuple.getValueByField("categories");

    boolean set_politics = false;
    boolean set_music = false;
    boolean set_sports = false;
    for(String s:categories){
      if(s.equals("politics"))  set_politics = true;
      if(s.equals("music"))     set_music = true;
      if(s.equals("sports"))    set_sports = true;
    }
    values.add(set_politics);
    values.add(set_music);
    values.add(set_sports);

    try {
//      System.out.println("yey cass");
      cassandraDao.insertIntoTweets(values.toArray());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void declareOutputFields( final OutputFieldsDeclarer outputFieldsDeclarer )
  {
  }
}
