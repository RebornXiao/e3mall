package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 索引库导入管理
 * @author 吾霞
 *
 */
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService service;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemIndex(){
		E3Result result = service.importItem();
		return result;
	}
	
}
