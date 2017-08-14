package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

/**
 * 用户信息核对管理
 * @author 吾霞
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result result = userService.checkData(param, type);
		return result;
	}
	
	/**
	 * 用户注册
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = userService.register(user);
		return result;
	}
	
}
