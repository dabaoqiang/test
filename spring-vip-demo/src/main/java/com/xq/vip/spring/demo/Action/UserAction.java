package com.xq.vip.spring.demo.Action;

import com.xq.vip.spring.demo.service.UserService;
import com.xq.vip.spring.framework.annotation.XQAutowired;
import com.xq.vip.spring.framework.annotation.XQController;
import com.xq.vip.spring.framework.annotation.XQRequestMapping;
/**
 * 
 * @author xq
 *
 */
@XQController
@XQRequestMapping(value = "/web")
public class UserAction {

	@XQAutowired
	private UserService userService;

	@XQRequestMapping(value = "/xiaoqiang.get")
	public void sayHello() {
		userService.sayHello("xiaoqiang");
	}

}
