package cn.e3mall.jedis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class JedisClientTest {
	
	@Test
	public void testJedisClient() throws Exception {
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("test","my jedisClient123456");
		String str = jedisClient.get("test");
		System.out.println(str);
	}
}
