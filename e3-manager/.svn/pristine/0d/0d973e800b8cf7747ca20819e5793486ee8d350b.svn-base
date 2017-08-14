package cn.e3mall.pagehelper;


import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	
	
	@Test
	public void testPageHelper() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//获得Mapper的代理对象
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		//设置分页信息
		PageHelper.startPage(1,10);
		TbItemExample example=new TbItemExample();
		//紧跟着的第一个select方法会被分页
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
		System.out.println(pageInfo.getPrePage());
	}
}
