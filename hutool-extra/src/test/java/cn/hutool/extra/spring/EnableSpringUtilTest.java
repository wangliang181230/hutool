package cn.hutool.extra.spring;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author sidian
 */
@SpringBootTest(classes = EnableSpringUtilTest.class)
@EnableSpringUtil
public class EnableSpringUtilTest {

	@Test
	public void test() {
		// 使用@EnableSpringUtil注解后, 能获取上下文
		Assert.assertNotNull(SpringUtil.getApplicationContext());
		// 不使用时, 为null
//        Assert.assertNull(SpringUtil.getApplicationContext());
	}
}
