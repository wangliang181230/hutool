package cn.hutool.core.annotation;

/**
 * @author wangliang181230
 */
public @interface UnSupportedJava17 {

	/**
	 * 不兼容的原因
	 *
	 * @return reason 原因
	 */
	String reason() default "";

	/**
	 * 临时修复方案
	 *
	 * @return temporaryRepair 临时修复方案
	 */
	String temporaryRepair() default "";
}
