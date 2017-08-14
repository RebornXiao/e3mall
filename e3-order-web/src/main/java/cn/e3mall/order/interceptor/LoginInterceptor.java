package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从cookie中取token,判断用户是否登陆
		String token = CookieUtils.getCookieValue(request,"token");
		//没有token，直接跳转到登陆页面，并携带返回的路径
		if(StringUtils.isBlank(token)){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		
		//token存在,判断是否过期，根据token调用sso获得用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//没有取到用户信息，登陆过期，直接跳转到登陆页面，并携带返回的路径
		if(e3Result.getStatus()!=200){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		
		//取到用户信息，将用户信息放入request域中，放行
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user",user);
		//判段cookie中是否有购物车，如果有，合并到服务端
		String cartList = CookieUtils.getCookieValue(request,"cart",true);
		if(StringUtils.isNotBlank(cartList)){
			cartService.mergeCart(user.getId(),JsonUtils.jsonToList(cartList,TbItem.class));
		}
		//放行
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}



}
