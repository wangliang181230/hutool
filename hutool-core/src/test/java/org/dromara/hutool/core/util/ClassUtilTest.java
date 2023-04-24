/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.reflect.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * {@link ClassUtil} 单元测试
 *
 * @author Looly
 *
 */
public class ClassUtilTest {

	@Test
	public void getClassNameTest() {
		final String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assertions.assertEquals("org.dromara.hutool.core.reflect.ClassUtil", className);

		final String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assertions.assertEquals("ClassUtil", simpleClassName);
	}

	@Test
	public void getClassPathTest() {
		final String classPath = ClassUtil.getClassPath();
		Assertions.assertNotNull(classPath);
	}

	@Test
	public void getShortClassNameTest() {
		final String className = "org.dromara.hutool.core.text.StrUtil";
		final String result = ClassUtil.getShortClassName(className);
		Assertions.assertEquals("o.d.h.c.t.StrUtil", result);
	}

	@Test
	public void getLocationPathTest(){
		final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
		Assertions.assertTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
	}

	@Test
	public void isAssignableTest(){
		Assertions.assertTrue(ClassUtil.isAssignable(int.class, int.class));
		Assertions.assertTrue(ClassUtil.isAssignable(int.class, Integer.class));
		Assertions.assertFalse(ClassUtil.isAssignable(int.class, String.class));
	}
}
