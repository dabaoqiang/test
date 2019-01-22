package com.xq.vip.spring.demo.service.impl;

import com.xq.vip.spring.demo.service.UserService;
import com.xq.vip.spring.framework.annotation.XQService;

/**
 * @description 被注入的
 * @author xq
 *
 */
@XQService(value = "userService")
public class UserServiceImpl implements UserService {

	@Override
	public void sayHello(String name) {
		System.out.println("打印：" + name);
	}

}
