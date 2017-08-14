package cn.e3mall.jedis;


import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testName() throws Exception {
		//使用连接池
		JedisPool jedisPool=new JedisPool("192.168.25.133",6379);
		//获得一个连接池
		Jedis jedis = jedisPool.getResource();
		jedis.set("test","my jedis");
		
		String str = jedis.get("test");
		System.out.println(str);
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception {
		//连接redis集群
		Set<HostAndPort> nodes =new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.133",7001));
		nodes.add(new HostAndPort("192.168.25.133",7002));
		nodes.add(new HostAndPort("192.168.25.133",7003));
		nodes.add(new HostAndPort("192.168.25.133",7004));
		nodes.add(new HostAndPort("192.168.25.133",7005));
		nodes.add(new HostAndPort("192.168.25.133",7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		
		jedisCluster.set("test","123");
		String str = jedisCluster.get("test");
		System.out.println(str);
		
		jedisCluster.close();
	}
}
