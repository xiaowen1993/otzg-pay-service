//package com.bcb;
//
//import com.bcb.admin.controller.AbstractController;
//import com.bcb.admin.controller.UserDetails;
//import com.bcb.log.util.LogUtil;
//import com.bcb.util.RespTips;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author liubei
// */
//public class AdminLogInterceptor extends AbstractController implements HandlerInterceptor {
//
//	/**
//	 * @author 张飞
//	 * 预处理
//	 */
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//							 Object handler) throws Exception {
//
//		//判断登录状态
//		String path = request.getRequestURI();
//
//		//通过参数传递token的方式确认会员身份
//		String token = (String) getPara("token");
//		//根据token获取登录用户信息
//		UserDetails ud = getCurrentUser(token);
//
//		//如果是登录请求 或者 是验证码则不拦截
//        if(path.indexOf("/admin/login") > -1
//			   || path.indexOf("/admin/unit/file/save") > -1
//			   || path.indexOf("/admin/register") > -1
//       		   || path.indexOf("/admin/check_img") > -1
//        ){
//			return true;
//        }else
//		//判断是否已经登录
//        if(ud!=null){
//        	//判断是否可以访问  当前路径
//        	if(!ud.hasRights(path)){
//        		sendJson(false, RespTips.NO_AUTH.code,"没有操作权限");
//        		return false;
//        	}else {
//        		return true;
//        	}
//        }else{
//			LogUtil.print("拦截:游客: "+request.getRequestURI());
//			sendJson(false,RespTips.NO_LOGIN.code,RespTips.NO_LOGIN.tips);
//			return false;
//        }
//	}
//
//	/**
//	 * @author 王志刚
//	 * 后处理回调方法，实现处理器的后处理（但在渲染视图之前）
//	 */
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response,
//						   Object handler,ModelAndView modelAndView) throws Exception {
//	}
//
//	/**
//	 * @author 王志刚
//	 * 整个请求处理完毕回调方法，即在视图渲染完毕时回调
//	 */
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
//								Object handler,Exception ex) throws Exception {
//
//	}
//
//}
