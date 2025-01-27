package cn.hutool.aop.proxy;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.interceptor.CglibInterceptor;
import cn.hutool.core.annotation.UnSupportedJava17;
import net.sf.cglib.proxy.Enhancer;

/**
 * 基于Cglib的切面代理工厂
 *
 * @author looly
 *
 */
public class CglibProxyFactory extends ProxyFactory{
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	@UnSupportedJava17(
			reason = "cglib:cglib:3.3.0 不兼容java17",
			temporaryRepair = "--add-opens java.base/java.lang=ALL-UNNAMED"
	)
	public <T> T proxy(T target, Aspect aspect) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(new CglibInterceptor(target, aspect));
		return (T) enhancer.create();
	}

}
