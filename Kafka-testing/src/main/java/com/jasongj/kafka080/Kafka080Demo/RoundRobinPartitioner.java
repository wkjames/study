package com.jasongj.kafka080.Kafka080Demo;

import java.util.concurrent.atomic.AtomicLong;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class RoundRobinPartitioner implements Partitioner {
  
  private AtomicLong next = new AtomicLong();

  public RoundRobinPartitioner(VerifiableProperties verifiableProperties) {}

  @Override
  public int partition(Object key, int numPartitions) {
    long nextIndex = next.incrementAndGet();
    return (int)nextIndex % numPartitions;
  }
}


