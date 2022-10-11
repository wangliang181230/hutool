package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 3参数Consumer
 *
 * @param <P1> 参数一类型
 * @param <P2> 参数二类型
 * @param <P3> 参数三类型
 * @author TomXin, VampireAchao
 * @since 5.7.22
 */
@FunctionalInterface
public interface SerConsumer3<P1, P2, P3> extends Serializable {

	/**
	 * 接收参数方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 * @throws Exception wrapped checked exceptions
	 */
	void accepting(P1 p1, P2 p2, P3 p3) throws Exception;

	/**
	 * 接收参数方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 */
	default void accept(P1 p1, P2 p2, P3 p3) {
		try {
			accepting(p1, p2, p3);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a composed {@code SerConsumer3} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code SerConsumer3} that performs in sequence this
	 * operation followed by the {@code after} operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default SerConsumer3<P1, P2, P3> andThen(SerConsumer3<P1, P2, P3> after) {
		Objects.requireNonNull(after);
		return (P1 p1, P2 p2, P3 p3) -> {
			accept(p1, p2, p3);
			after.accept(p1, p2, p3);
		};
	}
}
