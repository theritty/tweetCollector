package tweetCollector.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cassandraConnector.CassandraConnection;
import cassandraConnector.CassandraDao;
import com.datastax.driver.core.Session;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CassBolt extends BaseRichBolt
{
  List<Object> values = new ArrayList<>();

  private Session session;
  private CassandraDao cassandraDao;
  private Date currentDate;
  private double blockTimeInterval;
  private long round;

  public CassBolt(double blockTimeInterval, String tweets_table, String counts_table, String events_table)
  {
    this.blockTimeInterval = blockTimeInterval*60*60;
    round = -1;
    currentDate = null;

    try {
      this.cassandraDao = new CassandraDao(tweets_table, counts_table, events_table);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void prepare( final Map map, final TopologyContext topologyContext, final OutputCollector outputCollector )
  {
    try {
      CassandraConnection cassandraConnection = new CassandraConnection();
      session = cassandraConnection.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void execute( final Tuple tuple )
  {

    Status tweet = (Status) tuple.getValueByField( "tweetOriginal" );
    List<String> tweets = (List<String>) tuple.getValueByField( "tweet" );
    if(tweet == null) return;

    User user = tweet.getUser();
    Date date = tweet.getCreatedAt();
    long id = tweet.getId();
    long retweetCount = tweet.getRetweetCount();
    GeoLocation[][] loc = (GeoLocation[][]) tuple.getValueByField("location");

    String country ;
    String sentence = "";
    if(loc == null) return;
    for (String twee : tweets) {
      sentence += twee + " ";
    }

    double latitude = loc[0][0].getLatitude();
    if(latitude>31 && latitude<45)
      country = "USA";
    else
      country = "CAN";

    round = date.getTime() / (12*60*1000);
    //(id, tweet, userid, tweettime, retweetcount, round, country)

    values = new ArrayList<>();
    values.add(id);
    values.add(sentence);
    values.add(user.getId());
    values.add(date);
    values.add(retweetCount);
    values.add(round);
    values.add(country);

    if(currentDate == null)
    {
      currentDate = date;
    }
    long seconds = (date.getTime()-currentDate.getTime())/1000;
    if(seconds>blockTimeInterval ) {
      currentDate = null;
    }

    try {
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
