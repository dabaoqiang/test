package com.xq.vip.spring.framework.webmvc.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.xq.vip.spring.framework.context.XQApplicationContext;

/**
 * @description 处理请求bean
 * @author xq
 *
 */
public class XQDispatcherServlet extends HttpServlet {
	
	private static final long serialVersionUID = -278808529600479691L;
	
	/**
	 * IOC容器
	 */
	private XQApplicationContext XQApplicationContext;
	
	/**
	 * 配置文件
	 */
	private final String configLocations ="contextConfigLocation";

	// 第一，初始化，init 加载配置文件信息
	@Override
	public void init(ServletConfig config) throws ServletException {
		// 初始化ApplicationContext
		this.XQApplicationContext = new XQApplicationContext(new String[] {config.getInitParameter(configLocations)});
		
		
	}
	
	
 
	
	
}
