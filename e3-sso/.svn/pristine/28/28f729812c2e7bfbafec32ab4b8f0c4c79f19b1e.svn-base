package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;

/**
 * 用户信息核对
 * @author 吾霞
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper mapper;
	
	/**
	 * 检查信息
	 */
	public E3Result checkData(String param, int type) {
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1.用户名 2.电话号码
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else{
			return E3Result.build(400,"非法的参数");
		}
		List<TbUser> list = mapper.selectByExample(example);
		if(list == null || list.size() == 0){
			return E3Result.ok(true);
		}
		return E3Result.ok(false);
	}

	
	/**
	 * 用户注册
	 */
	public E3Result register(TbUser user) {
		
		//对数据进行判断
		if(StringUtils.isBlank(user.getUsername())){
			return E3Result.build(400,"用户名不为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400,"密码不能为空");
		}
		E3Result result = checkData(user.getUsername(),1);
		if(!(boolean) result.getData()){
			return E3Result.build(400,"此用户名已经被使用");
		}
		if(StringUtils.isNotBlank(user.getPhone())){
			E3Result result2 = checkData(user.getPassword(),2);
			if(!(boolean) result2.getData()){
				return E3Result.build(400,"此手机号码已经在使用");
			}
		}
		
		//补全信息
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码加密
		String asHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(asHex);
		
		//执行插入
		mapper.insert(user);
		
		return E3Result.ok();
	}

}
