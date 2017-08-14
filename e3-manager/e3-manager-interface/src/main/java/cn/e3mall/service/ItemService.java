package cn.e3mall.service;


import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
	
	public TbItem getItemById(Long id);
	
	public EasyUIDataGridResult findItemList(int page,int rows);
	
	public E3Result addItem(TbItem item,String desc);

	public E3Result updateItem(TbItem item,String desc);
	
	public E3Result deleteItem(List<Long> params);
	
	public E3Result instockItem(List<Long> params);
	
	public E3Result reshelfItem(List<Long> params);
	
	public TbItemDesc getItemDescById(long itemId);
}
