package cn.e3mall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {

	@Test
	public void publishService() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("服务1已经启动。。。。");
		System.in.read();
		System.out.println("服务2已经关闭");
		
	}
}
