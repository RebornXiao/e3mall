package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车管理
 * @author 吾霞
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	@Value("${CART_OUTTIME}")
	private int CART_OUTTIME;
	@Autowired
	private CartService cartService;
	
	
	/**
	 * 添加购物车项
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//用户已登录，将cookie中的购物车合并到redis中，并删除本地cookie记录
		if(user!=null){
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		
		
		//用户未登录使用cookie
		//从cookie中取出购物车
		List<TbItem> list = getCookieList(request);
		//判断购物车是否存在该商品
		//存在，将数量相加
		boolean flag=false;
		for (TbItem tbItem : list) {
			//对象比较的是地址，应该是值的比较
			if(tbItem.getId().longValue()==itemId){
				flag=true;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
		
		
		//不存在，将商品添加到cookie中
		if(!flag){
			//根据Id查询商品信息
			TbItem item = itemService.getItemById(itemId);
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)){
				//设置返回一张图片
				item.setImage(image.split(",")[0]);
			}
			//设置数量
			item.setNum(num);
			//添加到购物车
			list.add(item);
		}
		
		//将购物车添加到cookie
		CookieUtils.setCookie(request, response,"cart",JsonUtils.objectToJson(list), CART_OUTTIME, true);
		//返回成功页面
		return "cartSuccess";
	}
	
	
	/**
	 * 获得购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String cartList(HttpServletRequest request,HttpServletResponse response){
		List<TbItem> cookieList = getCookieList(request);
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//用户存在时
		if(user!=null){
			//从cookie中取购物车列表
			//如果不为空，把cookie中的购物车商品和服务端的购物车商品合并。
			cartService.mergeCart(user.getId(), cookieList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cookieList=cartService.getCartList(user.getId());
		}
		
		
		request.setAttribute("cartList", cookieList);
		return "cart";
	}
	
	
	/**
	 * 更新购物车
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartItem(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			//更新redis中的购物车信息
			cartService.updateCartItem(user.getId(),itemId, num);
			return E3Result.ok();
		}
		
		//从cookie中取出购物车信息
		List<TbItem> cookieList = getCookieList(request);
		//遍历商品列表找到对应的商品
		for (TbItem tbItem : cookieList) {
			if(tbItem.getId().longValue()==itemId){
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//将更改后的数据写回cookie
		CookieUtils.setCookie(request, response,"cart",JsonUtils.objectToJson(cookieList), CART_OUTTIME, true);
		//返回成功
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		//判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 更新redis中的购物车信息
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		
		//1、从url中取商品id
		//2、从cookie中取购物车商品列表
		List<TbItem> cookieList = getCookieList(request);
		//3、遍历列表找到对应的商品
		for (TbItem tbItem : cookieList) {
			if(tbItem.getId().longValue()==itemId){
				//4、删除商品。
				cookieList.remove(tbItem);
				break;
			}
		}
		//5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response,"cart",JsonUtils.objectToJson(cookieList), CART_OUTTIME, true);
		//6、返回逻辑视图：在逻辑视图中做redirect跳转。
		return "redirect:/cart/cart.html";
	}
	
	
	/**
	 * 获得cookies数据
	 * @param request
	 * @return
	 */
	private List<TbItem> getCookieList(HttpServletRequest request){
		//去购物车列表
		String json = CookieUtils.getCookieValue(request,"cart", true);
		//判断购物车是否为空
		if(StringUtils.isNotBlank(json)){
			//把json转换成商品列表返回
			return JsonUtils.jsonToList(json,TbItem.class);
		}
		return new ArrayList<>();
	}
}
