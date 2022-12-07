package cn.hutool.core.lang.func;

import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * SerBiFunction
 *
 * @param <T> 参数1的类型
 * @param <U> 参数2的类型
 * @param <R> 返回值类型
 * @author VampireAchao
 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBiFunction<T, U, R> extends BiFunction<T, U, R>, Serializable {

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 * @throws Exception wrapped checked exceptions
	 */
	R applying(T t, U u) throws Exception;

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 */
	@Override
	default R apply(final T t, final U u) {
		try {
			return this.applying(t, u);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * Returns a composed function that first applies this function to
	 * its input, and then applies the {@code after} function to the result.
	 * If evaluation of either function throws an exception, it is relayed to
	 * the caller of the composed function.
	 *
	 * @param <V>   the type of output of the {@code after} function, and of the
	 *              composed function
	 * @param after the function to apply after this function is applied
	 * @return a composed function that first applies this function and then
	 * applies the {@code after} function
	 * @throws NullPointerException if after is null
	 */
	default <V> SerBiFunction<T, U, V> andThen(final SerFunction<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return (T t, U u) -> after.apply(this.apply(t, u));
	}
}

