package com.jasongj.kafka080.Kafka080Demo;

import java.util.concurrent.atomic.AtomicLong;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class RandomPartitioner implements Partitioner {
  
  public RandomPartitioner(VerifiableProperties verifiableProperties) {}

  @Override
  public int partition(Object key, int numPartitions) {
	long randomNumber = System.currentTimeMillis();
	return (int) (randomNumber % numPartitions);
  }
}


