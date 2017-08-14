package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 索引库搜索
 * 
 * @author 吾霞
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao dao;

	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;

	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		// 创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询条件
		solrQuery.setQuery(keyWord);
		// 设置分页条件
		solrQuery.setStart((page - 1) * rows);
		// 设置rows
		solrQuery.setRows(rows);
		// 设置默认搜索域
		solrQuery.set("df", DEFAULT_FIELD);
		// 设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		// 执行查询
		SearchResult searchResult = dao.search(solrQuery);
		// 计算总页数
		long recourdCount = searchResult.getRecordCount();
		int pages = (int) (recourdCount / rows);
		if (recourdCount % rows > 0)
			pages++;
		// 设置到返回结果
		searchResult.setTotalPages(pages);
		return searchResult;

	}

}
