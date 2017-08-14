package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 添加购物车操作
 * @author 吾霞
 *
 */
@Service
public class CartServiceImpl implements CartService {

	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	/**
	 * 添加购物车
	 */
	public E3Result addCart(long userId,long itemId,int num) {
		//向redis中添加购物车
		//数据类型是hash key：用户id field：商品id value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId,itemId+"");
		//存在，更新商品数量
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId,itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"",JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		
		//不存在 ，添加商品信息到redis
		//根据id查询商品信息
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		//取一张图片
		String image = tbItem.getImage();
		if (StringUtils.isNotBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"",JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	
	/**
	 * 合并cookie和redis中的购物车
	 */
	public E3Result mergeCart(long userId, List<TbItem> cookieList) {
		//遍历集合，判断是否有相同的商品
		for (TbItem tbItem : cookieList) {
			addCart(userId,tbItem.getId(),tbItem.getNum());
		}
		return E3Result.ok();
	}
	
	public E3Result deleteCartItem(long userId, long itemId) {
		// 删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	/**
	 * 获得redis中的购物车中的商品列表
	 */
	public List<TbItem> getCartList(long userId) {
		List<String> list = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList=new ArrayList<>();
		for (String string : list) {
			TbItem item= JsonUtils.jsonToPojo(string,TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}


	/**
	 * 更新redis中的购物车信息
	 */
	public E3Result updateCartItem(long userId, long itemId,int num) {
		//从redis中取出商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId,itemId+"");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
		tbItem.setNum(num);
		//将更改后的数据放入redis中
		jedisClient.hset(REDIS_CART_PRE + ":" + userId,itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}


	/**
	 *清空购物车
	 */
	public void clearCart(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
	}

}
