package com.xq.vip.spring.framework.context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xq.vip.spring.demo.service.UserService;
import com.xq.vip.spring.framework.annotation.XQAutowired;
import com.xq.vip.spring.framework.annotation.XQController;
import com.xq.vip.spring.framework.annotation.XQService;
import com.xq.vip.spring.framework.beans.XQBeanDefinition;
import com.xq.vip.spring.framework.beans.XQBeanWrapper;
import com.xq.vip.spring.framework.context.support.XQBeanDefinitionReader;
import com.xq.vip.spring.framework.core.BeanFactory;

/**
 * @description IOC容器上下文
 * @author xq
 *
 */
public class XQApplicationContext extends XQDefaultListableBeanFactory implements BeanFactory {

	/**
	 * 定义加载文件
	 */
	private String[] configLocations;

	/**
	 * 定位加载资源处理专门类
	 */
	private XQBeanDefinitionReader xqBeanDefinitionReader;

	/**
	 * 保存单例beanMap
	 */
	private Map<String, Object> cahceMap = new ConcurrentHashMap<String, Object>(16);

	/**
	 * 被代理的对象存储
	 */
	private Map<String, XQBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, XQBeanWrapper>(16);

	/**
	 * @description 定义一个方法,进行初始化
	 * @param configLocations
	 */
	public XQApplicationContext(String[] configLocations) {
		this.configLocations = configLocations;
		onRefresh();
	}

	@Override
	public void onRefresh() {
		// 初始化IOC，四步，定位，加载，注册，注入 定位，加载可交由专门读取的类干
		this.xqBeanDefinitionReader = new XQBeanDefinitionReader(configLocations);
		// 进行注册bean
		register(xqBeanDefinitionReader.registerClass());
		// 进行注入bean处理
		doAutoWired();
		// 测试
		UserService userService = (UserService) this.getBean("userService");
		userService.sayHello("小强");
	}

	/**
	 * @description 进行注入
	 */
	private void doAutoWired() {
		// 循环所有的beanDefinitionMap,获取真实的bean实例，再在getBean方法里面获取代理bean，用代理bean进行注入
		for (Map.Entry<String, XQBeanDefinition> beanDefinition : this.beanDefinitionMap.entrySet()) {
			// getBean,先获取bean的实例，真正获取bean
			String beanName = beanDefinition.getKey();
			getBean(beanName);
		}
		// 循环所有的代理bean实体
		for (Map.Entry<String, XQBeanWrapper> beanWrapperEntity : beanWrapperMap.entrySet()) {
			// populateBean 真正的去执行DI操作
			populateBean(beanWrapperEntity.getKey(), beanWrapperEntity.getValue().getOriginalInstacne());
		}
	}

	/**
	 * @description 注入bean
	 * @param beanName
	 * @param originalInstacne
	 */
	private void populateBean(String beanName, Object originalInstacne) {
		Class<? extends Object> clazz = originalInstacne.getClass();
		// 注入，注入的是Service,controller
		if (!(clazz.isAnnotationPresent(XQController.class) || clazz.isAnnotationPresent(XQService.class))) {
			return;
		}
		// 取得所有属性
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			// 如果属性有基础于autoWired
			if (!field.isAnnotationPresent(XQAutowired.class)) {
				continue;
			}
			XQAutowired xqAutowired = field.getAnnotation(XQAutowired.class);
			String autoWriedName = xqAutowired.value();
			if ("".equals(autoWriedName)) {
				autoWriedName = field.getType().getName();
				// 取类名
				autoWriedName = loweFirstCase(autoWriedName.substring(autoWriedName.lastIndexOf(".") + 1));
			}

			// 开始赋值
			field.setAccessible(true);
			try {
				field.set(originalInstacne, this.beanWrapperMap.get(autoWriedName).getWrapperInstance());
			} catch (Exception e) {
				e.getStackTrace();
			}

		}

	}

	/**
	 * @description 注册bean，spring对这里又进行了一次封装，即 BeanDefinition，返回的是Bean的封装的类，原始类不做处理
	 * @param registerClass
	 */
	private void register(List<String> registerClass) {
		if (registerClass == null) {
			return;
		}
		for (String beanName : registerClass) {
			// 加载调用BeanDefinition的，register方法，放在一起，好管理，专人干专事
			XQBeanDefinition xQBeanDefinition = xqBeanDefinitionReader.register(beanName);
			// 进行内存存储 ,如果是接口，则不处理
			Class<?> clazz = null;
			try {
				clazz = Class.forName(xQBeanDefinition.getBeanClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (clazz.isInterface()) {
				continue;
			}
			beanDefinitionMap.put(xQBeanDefinition.getBeanFactoryName(), xQBeanDefinition);
			// 这个实例如果是继承了一个接口
			Class<?>[] interfaces = clazz.getInterfaces();
			for (Class<?> i : interfaces) {
				// 给service配置beanDefinition
				String beanAutowiredName = loweFirstCase(i.getName().substring(i.getName().lastIndexOf(".") + 1));
				this.beanDefinitionMap.put(beanAutowiredName, xQBeanDefinition);
			}
		}
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

	/**
	 * getBean方法只有一个对外公开获取bean
	 */
	@Override
	public Object getBean(String beanName) {
		// 先根据baenName获取Map
		XQBeanDefinition xqBeanDefinition = this.beanDefinitionMap.get(beanName);
		try {
			Object object = instantionBean(xqBeanDefinition);
			if (object == null) {
				return null;
			}
			XQBeanWrapper xqBeanWrapper = new XQBeanWrapper(object);
			this.beanWrapperMap.put(beanName, xqBeanWrapper);
			return this.beanWrapperMap.get(beanName).getWrapperInstance();

		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}

	/**
	 * @description 初始化bean
	 * @param xqBeanDefinition
	 */
	private Object instantionBean(XQBeanDefinition xqBeanDefinition) {
		String beanClassName = xqBeanDefinition.getBeanClassName();
		Object instance = null;
		try {
			// spring保证容器只有一个bean
			if (cahceMap.containsKey(beanClassName)) {
				return this.cahceMap.get(beanClassName);
			} else {
				Class<?> clazz = Class.forName(beanClassName);
				instance = clazz.newInstance();
				cahceMap.put(beanClassName, instance);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return instance;
	}

}
