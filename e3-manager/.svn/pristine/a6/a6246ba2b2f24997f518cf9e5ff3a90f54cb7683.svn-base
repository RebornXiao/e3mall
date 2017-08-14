package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	
	/**
	 * 通过id获得商品
	 */
	public TbItem getItemById(Long id){
		
		//查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO_PRE+":"+id+":BASE");
			if(StringUtils.isNotBlank(json)){
				TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//缓存没有数据
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		
		//TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size()>0){
			//添加数据至缓存
			try {
				jedisClient.set(ITEM_INFO_PRE+":"+id+":BASE",JsonUtils.objectToJson(list.get(0)));
				//设置缓存过期时间
				jedisClient.expire(ITEM_INFO_PRE+":"+id+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获得分页数据
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult findItemList(int page,int rows){
		//设置分页信息
		PageHelper.startPage(page, rows);
		
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//取分页信息
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		
		//创建返回结果集
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		
		//设置结果集参数
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	/**
	 * 添加商品
	 */
	public E3Result addItem(TbItem item,String desc) {
		final Long itemId=IDUtils.genItemId();
		item.setId(itemId);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		item.setStatus((byte)1);
		
		itemMapper.insert(item);
		
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(itemDesc);
		
		//添加商品时，同步索引库
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		
		return E3Result.ok();
	}

	/**
	 * 更新商品
	 */
	public E3Result updateItem(TbItem item,String desc) {
		Date created = itemMapper.selectByPrimaryKey(item.getId()).getCreated();
		item.setUpdated(new Date());
		item.setStatus((byte)1);
		item.setCreated(created);
		itemMapper.updateByPrimaryKey(item);
		
		
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(item.getId());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemDesc(desc);
		tbItemDescMapper.updateByPrimaryKeyWithBLOBs(itemDesc);
		return E3Result.ok();
	}

	
	/**
	 *  删除
	 */
	public E3Result deleteItem(List<Long> params) {
		for (Long id : params) {
			TbItem item=new TbItem();
			item.setStatus((byte)3);
			item.setId(id);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	
	/**
	 * 下架
	 */
	public E3Result instockItem(List<Long> params) {
		for (Long id : params) {
			TbItem item=new TbItem();
			item.setStatus((byte)2);
			item.setId(id);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	/**
	 * 上架
	 */
	public E3Result reshelfItem(List<Long> params) {
		for (Long id : params) {
			TbItem item=new TbItem();
			item.setStatus((byte)1);
			item.setId(id);
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return E3Result.ok();
	}

	
	/**
	 * 通过id获得商品信息
	 */
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		//添加数据至缓存
		try {
			jedisClient.set(ITEM_INFO_PRE+":"+itemId+":DESC",JsonUtils.objectToJson(tbItemDesc));
			//设置缓存过期时间
			jedisClient.expire(ITEM_INFO_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}
}
