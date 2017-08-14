package cn.e3mall.controller;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonFormat.Value;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

/**
 * 商品类目数据
 * @author 吾霞
 *
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService catService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> findItemCat(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> tree = catService.getItemCat(parentId);
		return tree;
	}
}
