package com.xq.vip.spring.framework.beans;

/**
 * @description bean的加载文件
 * @author xq
 *
 */
public class XQBeanDefinition {

	/**
	 * 类所对应的实例名称eg：userController
	 */
	private String beanFactoryName;

	/**
	 * 类的全定径名称eg:com.xq.service
	 */
	private String beanClassName;

	/**
	 * 是否初始化bean
	 */
	private Boolean lazyInit;

	/*
	 * setter/getter
	 */

	public String getBeanFactoryName() {
		return beanFactoryName;
	}

	public void setBeanFactoryName(String beanFactoryName) {
		this.beanFactoryName = beanFactoryName;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public Boolean getLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(Boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

}
