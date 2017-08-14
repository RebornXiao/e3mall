package cn.e3mall.item.message;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品添加，将商品详情信息网页静态化
 * @author 吾霞
 *
 */

public class HtmlGenListener implements MessageListener{
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private ItemService service;
	@Value("${ITEM_GEN_HTML_PATH}")
	private String ITEM_GEN_HTML_PATH;

	@Override
	public void onMessage(Message message) {
		
		try {
			//创建一个模板，参考jsp
			//从消息中取商品Id
			TextMessage textMessage=(TextMessage) message;
			String text = textMessage.getText();
			Long itemId=new Long(text);
			//等待事务提交
			Thread.sleep(100);
			//根据商品id查询商品信息以及商品详情
			TbItem tbItem = service.getItemById(itemId);
			Item item=new Item(tbItem);
			TbItemDesc tbItemDesc = service.getItemDescById(itemId);
			//创建数据集，将商品数据封装到数据集中
			Map data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//創建一個輸出流
			Writer out=new FileWriter(ITEM_GEN_HTML_PATH+itemId+".html");
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
