package cn.hutool.core.text;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class NamingCaseTest {
	@Test
	public void toUnderlineCaseTest(){
		// https://github.com/dromara/hutool/issues/2070
		Dict.create()
				.set("customerNickV2", "customer_nick_v2")
				.forEach((key, value) -> Assert.assertEquals(value, NamingCase.toUnderlineCase(key)));
	}
}
