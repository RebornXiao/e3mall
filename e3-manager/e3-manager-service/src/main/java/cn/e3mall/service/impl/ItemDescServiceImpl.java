package cn.e3mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;

@Service
public class ItemDescServiceImpl implements ItemDescService {
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	public TbItemDesc findItemDesc(Long id) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
		E3Result result=new E3Result();
		if(itemDesc!=null){
			return itemDesc;
		}
		return null;
	}

}
