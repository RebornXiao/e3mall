package cn.e3mall.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author 吾霞
 *
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService service;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id", defaultValue="0") Long parentId){
		List<EasyUITreeNode> list = service.getContentCatList(parentId);
		return list;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategory(Long parentId,String name){
		E3Result result = service.addContentCategory(parentId, name);
		return result;
	}
	
	@RequestMapping("/content/category/update")
	public void renameContentCat(Long id,String name){
		service.updateContentCat(id, name);
	}
	
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3Result deleteContentCategory(Long id){
		service.deleteContentCat(id);
		return E3Result.ok();
	}
}
