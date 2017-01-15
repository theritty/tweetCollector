package topologyBuilder;

/**
 * Created by ceren on 10.05.2016.
 */
public class Constants {
    public static final String TWITTER_SPOUT_ID = "twitter-spout";
    public static final String FILE_SPOUT_ID = "file-spout";
    public static final String PREPROCESS_SPOUT_ID = "preprocess-spout";
    public static final String DOCUMENT_CREATOR = "document-creator";


    public static final String COUNTRY1_SPLIT_BOLT_ID = "split-bolt1";
    public static final String COUNTRY1_SPLIT_HASHTAG_BOLT_ID = "split-hashtag-bolt1";
    public static final String COUNTRY1_COUNT_BOLT_ID = "count-bolt1";
    public static final String COUNTRY1_COUNT_HASHTAG_BOLT_ID = "count-hashtag-bolt1";
    public static final String COUNTRY1_REPORT_BOLT_ID = "report-bolt1";
    public static final String COUNTRY1_REPORT_HASHTAG_BOLT_ID = "report-hashtag-bolt1";
    public static final String COUNTRY1_EVENT_DETECTOR_BOLT = "event-detector-bolt1";
    public static final String COUNTRY1_EVENT_COMPARE_BOLT = "event-compare-bolt1";
    public static final String COUNTRY1_EVENT_DETECTOR_MANAGER_BOLT = "event-detector-manager-bolt1";
    public static final String CLASSIFIER_BOLT_ID = "classifier-bolt";

    public static final String COUNTRY2_SPLIT_BOLT_ID = "split-bolt2";
    public static final String COUNTRY2_SPLIT_HASHTAG_BOLT_ID = "split-hashtag-bolt2";
    public static final String COUNTRY2_COUNT_BOLT_ID = "count-bolt2";
    public static final String COUNTRY2_COUNT_HASHTAG_BOLT_ID = "count-hashtag-bolt2";
    public static final String COUNTRY2_REPORT_BOLT_ID = "report-bolt2";
    public static final String COUNTRY2_REPORT_HASHTAG_BOLT_ID = "report-hashtag-bolt2";
    public static final String COUNTRY2_EVENT_DETECTOR_BOLT = "event-detector-bolt2";
    public static final String COUNTRY2_EVENT_COMPARE_BOLT = "event-compare-bolt2";
    public static final String COUNTRY2_EVENT_DETECTOR_MANAGER_BOLT = "event-detector-manager-bolt2";

    public static final String WORKHISTORY_FILE = "/home/ceren/Desktop/workhistory.txt";
    public static final String IMAGES_FILE_PATH = "/home/ceren/Desktop/thesis/results/charts/";
    public static final String RESULT_FILE_PATH = "/home/ceren/Desktop/thesis/results/";
    public static final String TIMEBREAKDOWN_FILE_PATH = "/home/ceren/Desktop/thesis/results/timebreakdown/";
    public static final String STREAM_FILE_PATH = "/home/ceren/Desktop/thesis/streamData2/";
    public static final String INPUT_FILE_PATH = "/home/ceren/Desktop/thesis/streamData/";

//    public static final String WORKHISTORY_FILE = "/Users/ozlemcerensahin/Desktop/workhistory.txt";
//    public static final String IMAGES_FILE_PATH = "/Users/ozlemcerensahin/Desktop/thesis/results/charts/";
//    public static final String RESULT_FILE_PATH = "/Users/ozlemcerensahin/Desktop/thesis/results/";
//    public static final String STREAM_FILE_PATH = "/Users/ozlemcerensahin/Desktop/thesis/streamData2/";
//    public static final String INPUT_FILE_PATH  = "/Users/ozlemcerensahin/Desktop/thesis/streamData/";
//    public static final String TIMEBREAKDOWN_FILE_PATH = "/Users/ozlemcerensahin/Desktop/thesis/results/timebreakdown/";


    public static final String CASS_BOLT_ID = "cassandraBolt";
    public static final String CASS_SPOUT_ID = "cassandraSpout";
    public static final String TOPOLOGY_NAME = "word-count-topology";

}
