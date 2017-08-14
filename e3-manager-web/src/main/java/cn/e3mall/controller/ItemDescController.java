package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;

/**
 * 商品描述
 * @author 吾霞
 *
 */
@Controller
public class ItemDescController {
	
	@Autowired
	private ItemDescService service;
	
	@RequestMapping("/rest/item/query/item/desc")
	@ResponseBody
	public E3Result getDesc(long id){
		TbItemDesc itemDesc = service.findItemDesc(id);
		if(itemDesc!=null){
			return E3Result.ok(itemDesc);
		}
		return null;
	}
}
