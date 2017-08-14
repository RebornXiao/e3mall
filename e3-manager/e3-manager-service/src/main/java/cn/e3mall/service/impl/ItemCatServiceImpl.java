package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

/**
 * 商品类目
 * @author 吾霞
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	
	@Autowired
	private TbItemCatMapper mapper;
	/**
	 * 获得商品类目数据
	 */
	public List<EasyUITreeNode> getItemCat(long parentId) {
		
		TbItemCatExample example=new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = mapper.selectByExample(example);
		// 转换成EasyUITreeNode列表
		List<EasyUITreeNode> tree=new ArrayList<>();
		for (TbItemCat cat : list) {
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			tree.add(node);
		}
		return tree;
	}

}
