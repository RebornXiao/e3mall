package cn.e3mall.content.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	/**
	 * 获得内容分类数据
	 */
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据parentid查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = mapper.selectByExample(example);
		//转换成EasyUITreeNode的列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到列表
			nodeList.add(node);
		}
		return nodeList;
	}

	
	/**
	 * 添加内容分类
	 */
	public E3Result addContentCategory(long parentId, String name) {
		//创建一个tb_content_category表对应的pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1(正常),2(删除)
		contentCategory.setStatus(1);
		//默认排序就是1
		contentCategory.setSortOrder(1);
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库
		mapper.insert(contentCategory);
		//判断父节点的isparent属性。如果不是true改为true
		//根据parentid查询父节点
		TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新到数数据库
			mapper.updateByPrimaryKey(parent);
		}
		//返回结果，返回E3Result，包含pojo
		return E3Result.ok(contentCategory);
	}

	
	/**
	 * 重命名分类
	 */
	public void updateContentCat(long id, String name) {
		TbContentCategory category=new TbContentCategory();
		category.setId(id);
		category.setName(name);
		mapper.updateByPrimaryKeySelective(category);
	}


	/**
	 * 删除分类
	 */
	public void deleteContentCat(long id) {
		TbContentCategory contentCategory = mapper.selectByPrimaryKey(id);
		mapper.deleteByPrimaryKey(id);
		Long parentId = contentCategory.getParentId();
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		int count = mapper.countByExample(example);
		if(count==0){
			TbContentCategory category=mapper.selectByPrimaryKey(parentId);
			category.setIsParent(false);
			mapper.updateByPrimaryKeySelective(category);
		}
	}
	
	
}
