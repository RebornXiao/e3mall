package cn.e3mall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.container.page.Page;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 商品控制层
 * @author 吾霞
 *
 */
@Controller
public class ItemController {
		
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult findItemList(Integer page,Integer rows){
		EasyUIDataGridResult result=itemService.findItemList(page, rows);
		return result;
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	
	@RequestMapping("rest/page/item-edit")
	public String toEditItem(Integer id){
		return "item-edit";
	}
	
	/**
	 * 更新商品
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		E3Result result = itemService.updateItem(item,desc);
		return result;
	}
	
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem( String ids){
		String[] id = ids.split(",");
		List<Long> params=new ArrayList<>();
		for (String s : id) {
			params.add(Long.parseLong(s));
		}
		return itemService.deleteItem(params);
	}
	
	/**
	 * 下架
	 * @param params
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result instockItem(String  ids){
		
		String[] id = ids.split(",");
		List<Long> params=new ArrayList<>();
		for (String s : id) {
			params.add(Long.parseLong(s));
		}
		return itemService.instockItem(params);
	}
	
	/**
	 * 上架
	 * @param params
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(String  ids){
		
		String[] id = ids.split(",");
		List<Long> params=new ArrayList<>();
		for (String s : id) {
			params.add(Long.parseLong(s));
		}
		return itemService.reshelfItem(params);
	}
}
