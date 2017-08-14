package cn.e3mall.item.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;


/**
 * 商品详情页面展示
 * @author 吾霞
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService service;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		TbItem tbItem = service.getItemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = service.getItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
