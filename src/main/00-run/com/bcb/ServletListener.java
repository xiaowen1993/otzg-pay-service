package com.bcb;

import com.bcb.log.util.LogUtil;
import com.bcb.util.UrlUtil;

import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @author G
 * @date 2018/1/3 10:33.
 */
@WebListener
public class ServletListener implements javax.servlet.ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent servletRequestEvent) {
	}

	@Override
	public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
		String urlLink = "监听器："+ UrlUtil.getRequestURL((HttpServletRequest)servletRequestEvent.getServletRequest());
		//保存访问日志
		LogUtil.addWebAccessLog(urlLink);
	}
}
