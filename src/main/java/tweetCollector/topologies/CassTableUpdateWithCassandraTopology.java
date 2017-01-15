package tweetCollector.topologies;

import topologyBuilder.TopologyCreator;

public class CassTableUpdateWithCassandraTopology {
    public static void main(String[] args) throws Exception {
        TopologyCreator topologyCreator = new TopologyCreator();
        topologyCreator.submitPreprocessTopology();
    }
}