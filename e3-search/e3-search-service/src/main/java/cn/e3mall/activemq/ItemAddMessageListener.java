package cn.e3mall.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.soap.Text;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.xml.internal.rngom.nc.NameClassWalker;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/**
 * 添加商品时，将数据关联到索引库
 * @author 吾霞
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper mapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage) message;
			String text = textMessage.getText();
			Long itemId=new Long(text);
			//等待事务提交
			Thread.sleep(100);
			SearchItem searchItem = mapper.getItemById(itemId);
			SolrInputDocument document=new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			solrServer.add(document);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
