package cn.hutool.core.clone;


import cn.hutool.core.annotation.UnSupportedJava17;
import cn.hutool.core.util.ReflectUtil;

/**
 * 克隆默认实现接口，用于实现返回指定泛型类型的克隆方法
 *
 * @param <T> 泛型类型
 * @since 5.7.17
 */
public interface DefaultCloneable<T> extends java.lang.Cloneable {

	/**
	 * 浅拷贝，提供默认的泛型返回值的clone方法。
	 *
	 * @return obj
	 */
	@UnSupportedJava17(
			reason = "Method<Object.clone()>.setAccessible(boolean) 抛出 InaccessibleObjectException 异常",
			temporaryRepair = "--add-opens java.base/java.lang=ALL-UNNAMED"
	)
	default T clone0() {
		try {
			return ReflectUtil.invoke(this, "clone");
		} catch (Exception e) {
			throw new CloneRuntimeException(e);
		}
	}
}


