package com.jasongj.kafka080.Kafka080Demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

public class ProducerDemo {

	static private final String TOPIC = "topic1";
	static private final String ZOOKEEPER = "zookeeper0:2181,zookeeper1:2182,zookeeper2:2183/kafka";
	static private final String BROKER_LIST = "kafka0:9092,kafka1:9093,kafka2:9094";
	// static private final int PARTITIONS = TopicAdmin.partitionNum(ZOOKEEPER,
	// TOPIC);
	//static private final int PARTITIONS = 3;

	public static void main(String[] args) {
		Producer<String, String> producer = initProducer();
		try{
			sendOne(producer, TOPIC);
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}

	private static Producer<String, String> initProducer() {
		Properties props = new Properties();
		props.put("metadata.broker.list", BROKER_LIST);
		// props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("serializer.class", StringEncoder.class.getName());
		// props.put("partitioner.class", HashPartitioner.class.getName());
		props.put("partitioner.class", RoundRobinPartitioner.class.getName());
		// props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
		// props.put("compression.codec", "0");
		props.put("producer.type", "async");
		props.put("batch.num.messages", "3");
		props.put("queue.buffer.max.ms", "30000000");
		props.put("queue.buffering.max.messages", "3000000");
		props.put("queue.enqueue.timeout.ms", "20000000");
		props.put("request.required.acks", "-1");

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		return producer;
	}

	public static void sendOne(final Producer<String, String> producer, final String topic)
			throws InterruptedException {
		/*
		for (int i = 0; i <= 1; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
					Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
					String   str   =   formatter.format(curDate);   
					System.out.println("Thread started! " + str);
					for(int j=0;j<10;j++){
						KeyedMessage<String, String> message1 = new KeyedMessage<String, String>(topic, "31", "test " + String.valueOf(30+j)+ ", producer=" + producer);
						producer.send(message1);
					}
				}
			});
			t.start();
		}
		*/
		//KeyedMessage<String, String> message1 = new KeyedMessage<String, String>(topic, "31", "test test test ..." + ", producer=" + producer);
		//producer.send(message1);
		//System.out.println("All threads " + "had started!");
		//Thread.sleep(2000);
		//System.out.println("Main thread " + "ended!");
		KeyedMessage<String, String> message1 = new KeyedMessage<String, String>(topic, "31", "test 31");
	    producer.send(message1);
	    //Thread.sleep(5000);
	    KeyedMessage<String, String> message2 = new KeyedMessage<String, String>(topic, "31", "test 32");
	    producer.send(message2);
	    //Thread.sleep(5000);
	    KeyedMessage<String, String> message3 = new KeyedMessage<String, String>(topic, "31", "test 33");
	    producer.send(message3);
	    //Thread.sleep(5000);
	    KeyedMessage<String, String> message4 = new KeyedMessage<String, String>(topic, "31", "test 34");
	    producer.send(message4);
	    //Thread.sleep(5000);
	    KeyedMessage<String, String> message5 = new KeyedMessage<String, String>(topic, "31", "test 35");
	    producer.send(message5);
	    //Thread.sleep(5000);
		producer.close();
	}
}
