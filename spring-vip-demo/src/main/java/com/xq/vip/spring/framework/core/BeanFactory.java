package com.xq.vip.spring.framework.core;

/**
 * @description bean工厂获取bean方法，专人干专事
 * @author xq
 *
 */
public interface BeanFactory {
	/**
	 * 获取bean方法
	 * 
	 * @param beanName
	 * @return
	 */
	Object getBean(String beanName);

}
