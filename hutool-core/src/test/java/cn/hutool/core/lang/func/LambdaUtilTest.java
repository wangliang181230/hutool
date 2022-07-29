package cn.hutool.core.lang.func;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class LambdaUtilTest {

	@Test
	public void getMethodNameTest() {
		final Func1<MyTeacher, String> lambda = MyTeacher::getAge;
		final String methodName = LambdaUtil.getMethodName(lambda);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		final Func1<MyTeacher, String> lambda = MyTeacher::getAge;
		final String fieldName = LambdaUtil.getFieldName(lambda);
		Assert.assertEquals("age", fieldName);
	}

	@Test
	public void resolveTest() {
		Stream.<Runnable>of(() -> {
			// 引用构造函数
			final Func0<MyTeacher> lambda = MyTeacher::new;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(MyTeacher.class, lambdaInfo.getReturnType());
		}, () -> {
			// 数组构造函数引用(此处数组构造参数)
			final Func1<Integer, MyTeacher[]> lambda = MyTeacher[]::new;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(int.class, lambdaInfo.getParameterTypes()[0]);
			Assert.assertEquals(MyTeacher.class, ((Class<Array>) lambdaInfo.getReturnType()).getComponentType());
		}, () -> {
			// 引用静态方法
			final Func0<String> lambda = MyTeacher::takeAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 引用特定对象的实例方法
			final Func0<String> lambda = new MyTeacher()::getAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 引用特定类型的任意对象的实例方法
			final Func1<MyTeacher, String> lambda = MyTeacher::getAge;
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			Assert.assertEquals(0, lambdaInfo.getParameterTypes().length);
			Assert.assertEquals(String.class, lambdaInfo.getReturnType());
		}, () -> {
			// 最最重要的！！！
			final Character character = '0';
			final Integer integer = 0;
			final SerThiCons<Object, Boolean, String> lambda = (obj, bool, str) -> {
				//noinspection ResultOfMethodCallIgnored
				Objects.nonNull(character);
				//noinspection ResultOfMethodCallIgnored
				Objects.nonNull(integer);
			};
			final LambdaInfo lambdaInfo = LambdaUtil.resolve(lambda);
			// 获取闭包使用的参数类型
			Assert.assertEquals(Character.class, lambdaInfo.getParameterTypes()[0]);
			Assert.assertEquals(Integer.class, lambdaInfo.getParameterTypes()[1]);
			// 最后几个是原有lambda的参数类型
			Assert.assertEquals(Object.class, lambdaInfo.getParameterTypes()[2]);
			Assert.assertEquals(Boolean.class, lambdaInfo.getParameterTypes()[3]);
			Assert.assertEquals(String.class, lambdaInfo.getParameterTypes()[4]);

			Assert.assertEquals(void.class, lambdaInfo.getReturnType());
		}).forEach(Runnable::run);

	}

	interface SerThiCons<P1, P2, P3> extends Consumer3<P1, P2, P3>, Serializable {
	}

	@Test
	@Ignore
	public void getRealClassTest() {
		final MyTeacher myTeacher = new MyTeacher();
		Stream.<Runnable>of(() -> {
			// 引用特定类型的任意对象的实例方法
			final Func1<MyTeacher, String> lambda = MyTeacher::getAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 枚举测试，不会导致类型擦除
			final Func1<LambdaKindEnum, Integer> lambda = LambdaKindEnum::ordinal;
			Assert.assertEquals(LambdaKindEnum.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 调用父类方法，能获取到正确的子类类型
			final Func1<MyTeacher, ?> lambda = MyTeacher::getId;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用特定对象的实例方法
			final Func0<String> lambda = myTeacher::getAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 枚举测试，只能获取到枚举类型
			final Func0<Integer> lambda = LambdaKindEnum.REF_NONE::ordinal;
			Assert.assertEquals(Enum.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 调用父类方法，只能获取到父类类型
			//noinspection ResultOfMethodCallIgnored
			final VoidFunc0 lambda = myTeacher::getId;
			Assert.assertEquals(Entity.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用静态带参方法，能够获取到正确的参数类型
			final Func1<MyTeacher, String> lambda = MyTeacher::takeAgeBy;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用父类静态带参方法，只能获取到父类类型
			final Func0<?> lambda = MyTeacher::takeId;
			Assert.assertEquals(Entity.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用静态无参方法，能够获取到正确的类型
			final Func0<String> lambda = MyTeacher::takeAge;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 引用父类静态无参方法，能够获取到正确的参数类型
			final Func1<MyTeacher, ?> lambda = MyTeacher::takeIdBy;
			Assert.assertEquals(MyTeacher.class, LambdaUtil.getRealClass(lambda));
		}, () -> {
			// 数组测试
			final VoidFunc1<String[]> lambda = (String[] stringList) -> {};
			Assert.assertEquals(String[].class, LambdaUtil.getRealClass(lambda));
		}).forEach(Runnable::run);
	}

	@Data
	@AllArgsConstructor
	static class MyStudent {

		private String name;
	}

	@Data
	public static class Entity<T> {

		private T id;

		public static <T> T takeId() {
			return new Entity<T>().getId();
		}

		public static <T> T takeIdBy(final Entity<T> entity) {
			return entity.getId();
		}


	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class MyTeacher extends Entity<MyTeacher> {

		public static String takeAge() {
			return new MyTeacher().getAge();
		}

		public static String takeAgeBy(final MyTeacher myTeacher) {
			return myTeacher.getAge();
		}

		public String age;
	}

	/**
	 * 测试Lambda类型枚举
	 */
	@SuppressWarnings("unused")
	enum LambdaKindEnum {
		REF_NONE,
		REF_getField,
		REF_getStatic,
		REF_putField,
		REF_putStatic,
		REF_invokeVirtual,
		REF_invokeStatic,
		REF_invokeSpecial,
		REF_newInvokeSpecial,
	}
}
