package cn.hutool.core.collection;

import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MemorySafeLinkedBlockingQueueTest {

	@Test
	public void offerTest(){
		// 设置初始值达到最大，这样任何时候元素都无法加入队列
		final MemorySafeLinkedBlockingQueue<String> queue = new MemorySafeLinkedBlockingQueue<>(Long.MAX_VALUE);
		Assert.assertFalse(queue.offer(RandomUtil.randomString(RandomUtil.randomInt(100))));

		// 设定一个很小的值，可以成功加入
		queue.setMaxFreeMemory(10);
		Assert.assertTrue(queue.offer(RandomUtil.randomString(RandomUtil.randomInt(100))));
	}
}
