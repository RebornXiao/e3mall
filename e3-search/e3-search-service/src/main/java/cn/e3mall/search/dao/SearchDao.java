package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired 
	private SolrServer solrServer;
	
	
	public SearchResult search(SolrQuery query) throws Exception{
		//根据查询条件查询索引库
		QueryResponse response=solrServer.query(query);
		//取查询结果总记录数
		SolrDocumentList documentList = response.getResults();
		long numFound = documentList.getNumFound();
		//创建一个返回结果对象
		SearchResult result=new SearchResult();
		result.setRecordCount(numFound);
		//创建一个商品列表对象
		List<SearchItem> itemList=new ArrayList<>();
		//取商品列表
		//取高亮后的结果
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument document : documentList) {
			//取商品信息
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) document.get("item_category_name"));
			searchItem.setId((String) document.get("id"));
			searchItem.setImage((String) document.get("item_image"));
			searchItem.setPrice((long) document.get("item_price"));
			searchItem.setSell_point((String) document.get("item_sell_point"));
			//取高亮结果
			List<String> list = highlighting.get(document.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) document.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			//添加到商品列表
			itemList.add(searchItem);

		}
		
		//把列表添加到返回结果对象中
		result.setItemList(itemList);
		return result;

	}
	
}
