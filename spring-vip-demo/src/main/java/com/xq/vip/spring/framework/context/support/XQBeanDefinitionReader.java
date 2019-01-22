package com.xq.vip.spring.framework.context.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.xq.vip.spring.framework.beans.XQBeanDefinition;

/**
 * @description 定位，加载资源类
 * @author xq
 *
 */
public class XQBeanDefinitionReader {

	/**
	 * 读取的资源配置存储
	 */
	private Properties config = new Properties();

	/**
	 * 读取资源加载的目录
	 */
	private final String SCAN_PACKAGE = "scanPackge";

	/**
	 * 加载资源的存储
	 */
	private List<String> classNames = new ArrayList<String>();

	/**
	 * @description 初始化
	 * 
	 * @param configLocations
	 */
	public XQBeanDefinitionReader(String[] configLocations) {
		// 定位资源
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(configLocations[0].replaceAll("classpath:", ""));

		try {
			config.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 加载资源
		doScanner(config.getProperty(SCAN_PACKAGE));

	}

	/**
	 * @description 加载资源
	 * @param property
	 */
	private void doScanner(String path) {
		// 将.转为/
		URL url = this.getClass().getClassLoader().getResource(path.replaceAll("\\.", "/"));
		File fileDirctory = new File(url.getFile());
		File[] listFiles = fileDirctory.listFiles();
		for (File file : listFiles) {
			if (file.isDirectory()) {
				doScanner(path + "." + file.getName());
			} else {
				// 定义一个内存中的数组就进行加载资源之后的存储
				classNames.add(path + "." +  file.getName().replaceAll(".class", ""));
			}
		}

	}

	/**
	 * @description 返回加载资源进行注册
	 * 
	 * @return
	 */
	public List<String> registerClass() {
		return this.classNames;
	}

	/**
	 * @description 注册beanDefinition
	 * @return
	 */
	public XQBeanDefinition register(String beanName) {
		if (this.classNames.contains(beanName)) {
			XQBeanDefinition xqBeanDefinition = new XQBeanDefinition();
			xqBeanDefinition.setBeanClassName(beanName);
			xqBeanDefinition.setBeanFactoryName(loweFirstCase(beanName.substring(beanName.lastIndexOf(".") + 1)));
			return xqBeanDefinition;
		}
		return null;
	}

	/**
	 * @description 首字母小写
	 * @param substring
	 * @return
	 */
	private String loweFirstCase(String name) {
		char[] charArray = name.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}

}
