package cn.hutool.core.lang;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled // FIXME: 这个测试类在 github/actions 上运行会出问题
public class SimpleCacheTest {

	@Test
	public void putTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		ThreadUtil.execute(()->cache.put("key1", "value1"));
		ThreadUtil.execute(()->cache.get("key1"));
		ThreadUtil.execute(()->cache.put("key2", "value2"));
		ThreadUtil.execute(()->cache.get("key2"));
		ThreadUtil.execute(()->cache.put("key3", "value3"));
		ThreadUtil.execute(()->cache.get("key3"));
		ThreadUtil.execute(()->cache.put("key4", "value4"));
		ThreadUtil.execute(()->cache.get("key4"));
		ThreadUtil.execute(()->cache.get("key5", ()->"value5"));

		cache.get("key5", ()->"value5");
	}

	@Test
	public void getTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		cache.put("key1", "value1");
		cache.get("key1");
		cache.put("key2", "value2");
		cache.get("key2");
		cache.put("key3", "value3");
		cache.get("key3");
		cache.put("key4", "value4");
		cache.get("key4");
		cache.get("key5", ()->"value5");

		Assert.assertEquals("value1", cache.get("key1"));
		Assert.assertEquals("value2", cache.get("key2"));
		Assert.assertEquals("value3", cache.get("key3"));
		Assert.assertEquals("value4", cache.get("key4"));
		Assert.assertEquals("value5", cache.get("key5"));
		Assert.assertEquals("value6", cache.get("key6", ()-> "value6"));
	}

	@Test
	public void getConcurrencyTest(){
		final SimpleCache<String, String> cache = new SimpleCache<>();
		final ConcurrencyTester tester = new ConcurrencyTester(9000);
		tester.test(()-> cache.get("aaa", ()-> {
			ThreadUtil.sleep(1000);
			return "aaaValue";
		}));

		Assert.assertTrue(tester.getInterval() > 0);
		Assert.assertEquals("aaaValue", cache.get("aaa"));
	}
}
