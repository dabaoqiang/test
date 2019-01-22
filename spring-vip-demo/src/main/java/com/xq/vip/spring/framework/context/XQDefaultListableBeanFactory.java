package com.xq.vip.spring.framework.context;

import java.util.HashMap;
import java.util.Map;

import com.xq.vip.spring.framework.beans.XQBeanDefinition;

/**
 * @description 不同的实例不同的初始化，底层处理是defaultListableBeanFactory
 * @author xq
 *
 */
public class XQDefaultListableBeanFactory extends XQAbstractApplicationContext {
	/**
	 * key为实例名称，value为beanDefinition
	 */
	protected Map<String, XQBeanDefinition> beanDefinitionMap = new HashMap<String, XQBeanDefinition>(16);

	@Override
	protected void onRefresh() {
		// 子类实现
	}

}
