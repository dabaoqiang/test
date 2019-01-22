package com.xq.vip.spring.framework.beans;

import com.xq.vip.spring.framework.core.FactoryBean;

/**
 * @description BeanDefinition是映射到内存中的配置文件BeanWrapper是真正注入的对象
 *              继承FactoryBean是同宗同源，OOP思想
 * @author xq
 *
 */
public class XQBeanWrapper extends FactoryBean {

	/**
	 * 原对象
	 */
	private Object originalInstacne;

	/**
	 * 代理对象
	 */
	private Object wrapperInstance;

	/**
	 * @description 构造方法
	 * @param originalInstacne
	 */
	public XQBeanWrapper(Object originalInstacne) {
		this.originalInstacne = originalInstacne;
		this.wrapperInstance = originalInstacne;
	}

	/**
	 * setter/getter
	 */

	public Object getOriginalInstacne() {
		return originalInstacne;
	}

	public void setOriginalInstacne(Object originalInstacne) {
		this.originalInstacne = originalInstacne;
	}

	public Object getWrapperInstance() {
		return wrapperInstance;
	}

	public void setWrapperInstance(Object wrapperInstance) {
		this.wrapperInstance = wrapperInstance;
	}

}
