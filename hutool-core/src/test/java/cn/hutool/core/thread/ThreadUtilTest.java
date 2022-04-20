package cn.hutool.core.thread;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("OutOfMemory")
public class ThreadUtilTest {

	@Test
	public void executeTest() {
		final boolean isValid = true;

		ThreadUtil.execute(() -> Assert.assertTrue(isValid));
	}
}
