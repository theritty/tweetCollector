package tweetCollector.spout;

/**

 /**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class TwitterSpout  extends BaseRichSpout {

    SpoutOutputCollector _collector;
    LinkedBlockingQueue<Status> queue = null;
    TwitterStream _twitterStream;
    String consumerKey;
    String consumerSecret;
    String accessToken;
    String accessTokenSecret;
    ArrayList<Date> dates = new ArrayList<>();
    Date currentDate = null;
    int trainSize;
    int compareSize;
    long round ;

    double blockTimeInterval;

    public TwitterSpout(String consumerKey, String consumerSecret,
                        String accessToken, String accessTokenSecret,
                        double blockTimeIntervalInHours,
                        int trainSize, int compareSize) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.trainSize = trainSize;
        this.compareSize = compareSize;
        blockTimeInterval = blockTimeIntervalInHours*60*60;
        round = 0;
    }

    public TwitterSpout() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        queue = new LinkedBlockingQueue<Status>(1000);
        _collector = collector;

        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                queue.offer(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
            }

            @Override
            public void onTrackLimitationNotice(int i) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onException(Exception ex) {
                System.out.println(ex);
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub

            }

        };

//        double [][]location ={{27,34},{40,38}};
//        double [][]location ={{-124,31},{-66,49}};
        double [][]location ={{-124,31},{-78,70}};


        TwitterStream twitterStream = new TwitterStreamFactory(
                new ConfigurationBuilder().setJSONStoreEnabled(true).build())
                .getInstance();

        twitterStream.addListener(listener);
        twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        AccessToken token = new AccessToken(accessToken, accessTokenSecret);
        twitterStream.setOAuthAccessToken(token);

        FilterQuery query = new FilterQuery().locations(location); //.track(keyWords);
        twitterStream.filter(query);


    }

    @Override
    public void nextTuple() {
        Status ret = null;
        try {
            ret = queue.poll(10, TimeUnit.SECONDS);
            if (ret != null)  {
                Date tweetDate = ret.getCreatedAt();

                if(currentDate == null )
                {
                    currentDate = tweetDate;
                }

                long seconds = (tweetDate.getTime()-currentDate.getTime())/1000;
                if(seconds>blockTimeInterval )
                {
                    dates.add(currentDate);
                    if(dates.size() > trainSize) dates.remove(0);
                    currentDate = tweetDate;

                    System.out.println("Spout::: current " + currentDate + " dates " + dates);
                    if(dates.size() > compareSize) {
                        _collector.emit(new Values(ret, dates, currentDate, true, round++, "twitter"));
                    }
                    else
                    {
                        _collector.emit(new Values(ret, dates, currentDate, false, round, "twitter"));
                    }
                }
                else
                {
                    _collector.emit(new Values(ret, dates, currentDate, false, round, "twitter"));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if(_twitterStream != null) {
            _twitterStream.shutdown();
        }
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet", "dates","currentDate","blockEnd", "round", "source"));
    }



}

