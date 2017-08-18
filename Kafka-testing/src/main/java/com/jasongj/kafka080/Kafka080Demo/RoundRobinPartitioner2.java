package com.jasongj.kafka080.Kafka080Demo;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class RoundRobinPartitioner2 implements Partitioner {
	private int i = 0;

	public RoundRobinPartitioner2(VerifiableProperties verifiableProperties) {
	}

	public int partition(Object key, int numPartitions) {
		long nextIndex = i++;
		return (int) nextIndex % numPartitions;
	}
}
