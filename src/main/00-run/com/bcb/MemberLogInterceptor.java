//package com.bcb;
//
//import com.bcb.pay.api.AbstractController;
//import com.bcb.util.RespTips;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 会员登录拦截器
// * 1、验证管理员身份
// * @author G/2018/6/12 16:34
// */
//
//public class MemberLogInterceptor extends AbstractController implements HandlerInterceptor {
//
//	/**
//	 * 预处理
//	 */
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//							 Object handler) throws Exception {
//		//判断登录状态
//		String path = request.getRequestURI();
//		//通过参数传递token的方式确认会员身份
//		String token = (String) getPara("token");
//		//根据token获取登录用户信息
//		MemberDetails md = getCurrentMember(token);
//		if(path.indexOf("/member/login") > -1
//				|| path.indexOf("/member/password/reset")>-1  //重置密码接口
//				|| path.indexOf("/member/findByOpenId")>-1  //
//				|| path.indexOf("/login")>-1
//				|| path.indexOf("/is_login")>-1
//				|| path.indexOf("/check_img")>-1
//
//				|| path.indexOf("/register")>-1
//				|| path.indexOf("/sms/send")>-1
//				|| path.indexOf("/login/smsCode")>-1
//				|| path.indexOf("/is_register")>-1
//				|| path.indexOf("/member/password/reset")>-1  //重置密码接口
//				|| path.indexOf("/bindingPhone")>-1				//绑定账户
//				|| path.indexOf("/findByOpenid")>-1
//				|| path.indexOf("/unit/get")>-1
//				|| path.indexOf("/is_smsCode")>-1
//
//				|| path.indexOf("/pay/code_get")>-1				//根据code获取openId
//
//				|| path.indexOf("/member/category/list")>-1   	//支持游客获取商品分类
//				|| path.indexOf("/member/goods/find")>-1		 //支持游客获取商品列表
//				|| path.indexOf("/member/goods/get")>-1	 		//支持游客获取商品详情
//
//				){
//			return true;
//		}else
//        //判断是否已经登录
//		if(md!=null){
//        	//判断是否可以访问  当前路径
//			return true;
//        }else{
////			如果是移动ajax连接
////			if(MobileUtil.isAjax(request)){
////				sendJson(false,RespTips.NO_LOGIN.code,RespTips.NO_LOGIN.tips);
////			}else{
////				sendRedirect("logon.html");
////			}
//
//			sendJson(false,RespTips.NO_LOGIN.code,RespTips.NO_LOGIN.tips);
//			return false;
//        }
//	}
//
//	/**
//	 * 后处理回调方法，实现处理器的后处理（但在渲染视图之前）
//	 */
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response,
//						   Object handler,ModelAndView modelAndView) throws Exception {
//	}
//
//	/**
//	 * 整个请求处理完毕回调方法，即在视图渲染完毕时回调
//	 */
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
//								Object handler,Exception ex) throws Exception {
//
//	}
//
//}
